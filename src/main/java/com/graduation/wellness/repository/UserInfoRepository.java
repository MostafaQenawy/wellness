package com.graduation.wellness.repository;

import com.graduation.wellness.model.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findUserInfoById(Long id);
}
