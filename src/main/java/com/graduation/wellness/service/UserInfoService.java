package com.graduation.wellness.service;

import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.UserInfoRepository;
import com.graduation.wellness.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserRepo userRepository;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, UserRepo userRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    public void saveUserData(UserInfo userInfo, long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        userInfo.setUser(user);           // Important: this sets the id via @MapsId
        userInfoRepository.save(userInfo);
    }
}