package com.graduation.wellness.service;

import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public void saveUserData(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}