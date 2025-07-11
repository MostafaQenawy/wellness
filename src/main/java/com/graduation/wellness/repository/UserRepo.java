package com.graduation.wellness.repository;

import com.graduation.wellness.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findUserById(long id);
    User findByEmail(String email);
}