package org.example.wellness_hub.Service;

import org.example.wellness_hub.model.entity.UserInfo;
import org.example.wellness_hub.model.enums.ActivityLevel;
import org.example.wellness_hub.model.enums.ExperienceLevel;
import org.example.wellness_hub.model.enums.Gender;
import org.example.wellness_hub.model.enums.Goal;
import org.example.wellness_hub.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public UserService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public void saveUserData(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }
}