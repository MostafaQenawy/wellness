package com.graduation.wellness.service;

import com.graduation.wellness.exception.BaseApiExcepetions;
import com.graduation.wellness.model.entity.User;
import com.graduation.wellness.security.JWTResponseDto;
import com.graduation.wellness.security.JwtRequestDto;
import com.graduation.wellness.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	public JWTResponseDto login(JwtRequestDto jwtRequestDto) {
		User user = userService.loadUserByEmail(jwtRequestDto.getEmail());

		if (!passwordEncoder.matches(jwtRequestDto.getPassword(), user.getPassword())) {
			throw new BaseApiExcepetions(String.format("Wrong password has been invoken"), HttpStatus.BAD_REQUEST);
		}
		String jwtToken = jwtTokenUtils.generateToken(user.getEmail() , user.getId());

		return new JWTResponseDto(jwtToken);

	}

}
