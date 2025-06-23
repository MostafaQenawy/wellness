package com.graduation.wellness.service;

import com.graduation.wellness.exception.BaseApiExceptions;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.UserInfoRepository;
import com.graduation.wellness.repository.UserRepo;
import com.graduation.wellness.security.JwtTokenUtils;
import com.graduation.wellness.mapper.UserInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.graduation.wellness.model.dto.UserInfoDTO;

@Service
@AllArgsConstructor
public class UserInfoService {
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private final UserRepo userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public void saveUserData(UserInfo userInfo, String userEmail) {
        User user = userService.loadUserByEmail(userEmail);
        userInfo.setUser(user);
        userInfo.setId(user.getId()); // ensures it's treated as update
        userInfoRepository.save(userInfo);

    }

    public UserInfoDTO getUserInfo() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        User user = userRepository.findUserById(userID);
        if( user == null)
            new RuntimeException("User not found");

        UserInfo userInfo = userInfoRepository.findUserInfoById(userID);
        if(userInfo == null)
            new RuntimeException("UserInfo not found");

        return UserInfoMapper.toDTO(user, userInfo);
    }

    public UserInfo loadUserInfoById(Long id) {
        return userInfoRepository.findById(id).orElse(null);
    }
}