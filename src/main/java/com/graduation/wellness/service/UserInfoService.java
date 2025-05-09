package com.graduation.wellness.service;

import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.UserInfoRepository;
import com.graduation.wellness.repository.UserRepo;
import com.graduation.wellness.security.JwtTokenUtils;
import com.graduation.wellness.mapper.UserInfoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.graduation.wellness.model.dto.UserInfoDTO;

@Service
@AllArgsConstructor
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepo userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public void saveUserData(UserInfo userInfo, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userInfo.setUser(user);           // Important: this sets the id via @MapsId
        userInfoRepository.save(userInfo);
    }

    public UserInfoDTO getUserInfo() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

         UserInfo userInfo = userInfoRepository.findById(userID)
                 .orElseThrow(() -> new RuntimeException("User not found"));
         User user = userRepository.findById(userID)
                 .orElseThrow(() -> new RuntimeException("User not found"));
         return UserInfoMapper.toDTO(user, userInfo);
    }
}