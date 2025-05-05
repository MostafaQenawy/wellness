package com.graduation.wellness.config;

import com.graduation.wellness.model.dto.UserDto;
import com.graduation.wellness.model.entity.Role;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.service.RoleService;
import com.graduation.wellness.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StartUpApp implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(String... args)throws Exception{

        if (roleService.findAll().isEmpty()){
            roleService.save(new Role( "ROLE_ADMIN"));
            roleService.save(new Role( "ROLE_USER"));
            roleService.save(new Role( "ROLE_COACH"));
        }

        if (userService.findAll().isEmpty()){


            userService.save( new User(
                    "Mostafa",    // firstName
                    "Qenawy",              // lastName
                    "Mostafa@example.com", // email
                    "Developer@gmail.com", // syncAccount
                    "GOOGLE",              // provider
                    "123456789"            // providerUserId (Google ID, for example)
            ));
            userService.save( new User(
                    "Mohanad",    // firstName
                    "Khaled",              // lastName
                    "Honda@example.com",   // email
                    "SecurePass12" ,       // password
                    "LOCAL"                //provider
            ));

            userService.save( new User(
                    "Mohamed",    // firstName
                    "Magdy",               // lastName
                    "magdy@example.com",   // email
                    "SecurePass1" ,        // password
                    "LOCAL"                //provider
            ));
        }
    }
}
