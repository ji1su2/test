package com.web.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.demo.entity.Post;
import com.web.demo.entity.SnsUser;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SnsUser create(String username, String password, String email) {
		SnsUser user = new SnsUser();
		user.setUsername(username);
		user.setPassword( this.passwordEncoder.encode(password) );
		user.setEmail(email);
		// FIXME #REFACT: 회원 가입 시간 추가
		user.setRegDate(LocalDateTime.now());
		System.out.println("디비저장");
		try {
			this.userRepository.save(user);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
		System.out.println("반환");
		return user;
	}
	public SnsUser findById(Long userId) {
		Optional<SnsUser> oUser = this.userRepository.findById(userId);
		if(oUser.isPresent()) {
			return oUser.get();
		}
		throw new DataNotFoundException("user not found");
		// OR
        //return userRepository.findById(userId)
        //        .orElseThrow(() -> new DataNotFoundException("user not found"));
    }
}









