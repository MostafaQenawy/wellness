package com.graduation.wellness.service;

import com.graduation.wellness.exception.BaseApiExcepetions;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.model.entity.UserInfo;
import com.graduation.wellness.repository.UserInfoRepository;
import com.graduation.wellness.security.JWTResponseDto;
import com.graduation.wellness.security.JwtRequestDto;
import com.graduation.wellness.security.JwtTokenUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@AllArgsConstructor
public class AuthService {


	private PasswordEncoder passwordEncoder;
	private UserService userService;
	private JwtTokenUtils jwtTokenUtils;
	private UserInfoRepository userInfoRepository;

	public JWTResponseDto login(JwtRequestDto jwtRequestDto) {
		User user = userService.loadUserByEmail(jwtRequestDto.getEmail());


		if (!passwordEncoder.matches(jwtRequestDto.getPassword(), user.getPassword())) {
			throw new BaseApiExcepetions(String.format("Wrong password has been invoken"), HttpStatus.BAD_REQUEST);
		}

		UserInfo userInfo = userInfoRepository.findUserInfoById(user.getId());
		if (userInfo == null) {
			return new JWTResponseDto();
		}
		String jwtToken = jwtTokenUtils.generateToken(user.getEmail() , user.getId() , user.getUsername());

		return new JWTResponseDto(jwtToken);

	}

}
