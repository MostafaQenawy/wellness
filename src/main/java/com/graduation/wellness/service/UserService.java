package com.graduation.wellness.service;

import com.graduation.wellness.email.EmailService;
import com.graduation.wellness.email.EmailTemplateName;
import com.graduation.wellness.exception.BaseApiExcepetions;
import com.graduation.wellness.mapper.UserMapper;
import com.graduation.wellness.model.dto.UserDto;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.repository.UserRepo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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


    public Map save(User user) {
        if(user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(user.getProvider() == null){
            user.setProvider("LOCAL");
            user.setVerified(false);
        }
        user.setRole(roleService.findByName("ROLE_USER"));
        userRepo.save(user);
        Map<String ,String> map = new HashMap<>();
        map.put("status" , "success");
        map.put("message" ,"User had been added successfully!");
        return map;
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

}
