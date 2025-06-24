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
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private final UserRepo userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public UserInfo saveUserData(UserInfo userInfoInput, String userEmail) {
        User user = userService.loadUserByEmail(userEmail);

        userInfoInput.setUser(user);
        return userInfoRepository.save(userInfoInput); // 💡 return the saved managed entity
    }

    public UserInfoDTO getUserInfo() {
        String jwtToken = jwtTokenUtils.getJwtToken();
        Long userID = jwtTokenUtils.getIdFromToken(jwtToken);

        User user = userRepository.findUserById(userID);
        if (user == null)
            throw new RuntimeException("User not found");

        UserInfo userInfo = userInfoRepository.findUserInfoById(userID);
        if (userInfo == null)
            throw new RuntimeException("UserInfo not found");

        return UserInfoMapper.toDTO(user, userInfo);
    }
}