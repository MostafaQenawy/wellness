package com.graduation.wellness.service;

import com.graduation.wellness.exception.BaseApiExcepetions;
import com.graduation.wellness.mapper.UserMapper;
import com.graduation.wellness.model.dto.UserDto;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;

    private final RoleService roleService;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map save(User user) {
        if(user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(user.getProvider() == null){
            user.setProvider("LOCAL");
        }
        user.setRole(roleService.findByName("ROLE_USER"));
        userRepo.save(user);
        Map<String ,String> map = new HashMap<>();
        map.put("status" , "success");
        map.put("message" ,"User had been added successfully!");
        return map;
    }

    public User getGoogleUser(String idToken) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());

            User user = new User();
            user.setEmail(json.getString("email"));
            user.setFirstName(json.getString("given_name"));
            user.setLastName(json.getString("family_name"));
            user.setProvider("GOOGLE");
            user.setProviderUserId(json.getString("sub"));
            return user;

        } catch (Exception e) {
            return null;
        }
    }

    public User getUserFromFacebook(String accessToken) {
        String url = "https://graph.facebook.com/me?fields=id,name,email&access_token=" + accessToken;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject json = new JSONObject(response.getBody());

            String id = json.getString("id");
            String email = json.getString("email");
            String name = json.getString("name");

            String[] names = name.split(" ", 2);
            String firstName = names[0];
            String lastName = names.length > 1 ? names[1] : "";

            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setProvider("FACEBOOK");
            user.setProviderUserId(id);

            return user;

        } catch (Exception e) {
            return null;
        }
    }

    public void changePassword(String email, String password) {
        User user = loadUserByEmail(email);
        if(password != null) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepo.save(user);
    }


    public UserDto findById(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new BaseApiExcepetions(String.format("No Record with user_id [%d] found in data base " , id) , HttpStatus.NOT_FOUND));
        UserDto userDto = userMapper.Map(user);
        return userDto;
    }

    public List<User> findAll() {

        return userRepo.findAll();
    }

    public User loadUserByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if(user == null)
            throw new BaseApiExcepetions(String.format("This Email not registered yet " , email), HttpStatus.NOT_FOUND);
        return user;
    }


    public boolean isExist(String email) {
        return userRepo.findByEmail(email) != null;
    }

    private static List<GrantedAuthority> getAuthorities(User user) {

        return (List<GrantedAuthority>) user.getAuthorities();
    }

    public boolean isOAuthUser(String email) {
        User user = loadUserByEmail(email);
        return user != null && "GOOGLE".equals(user.getProvider());
    }

    public void deleteAccount(String email) {
        User user = loadUserByEmail(email);
        userRepo.delete(user);
    }

}
