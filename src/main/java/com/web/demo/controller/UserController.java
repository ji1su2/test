package com.web.demo.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.demo.config.JwtProperties;
import com.web.demo.dto.UserForm;
import com.web.demo.entity.SnsUser;
import com.web.demo.service.PostService;
import com.web.demo.service.TokenProvider;
import com.web.demo.service.TokenService;
import com.web.demo.service.UserService;
import com.web.demo.service.UtilService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {
	private final UserService userService;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;
    private final UtilService utilService;
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@GetMapping("/signup")
	public String signup(UserForm userForm) {
		return "signup_form";
	}
	@PostMapping("/signup")
	public String signup(@Valid UserForm userForm, BindingResult bindingResult, 
			HttpServletResponse response) {
		System.out.println("회원 가입 요청 왔다");
		if( bindingResult.hasErrors() ) { // 입력 오류시 입력화면으로 리턴
			return "signup_form";
		}
		System.out.println("비번체크");
		if( !userForm.getPassword1().equals(userForm.getPassword2()) ) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "패스워드가 서로 일치하지 않습니다.");
			return "signup_form";
		}
		SnsUser snsUser;
		try {
			System.out.println("회원가입 진행");
			snsUser = this.userService.create(userForm.getUsername(), 
					userForm.getPassword1(), userForm.getEmail());
			System.out.println("회원 가입 완료");
			
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("signupError", "이미 사용중인 아이디입니다.");
			return "signup_form";
		} catch (Exception e) {
			bindingResult.reject("signupError", e.getMessage());
			return "signup_form";
		}
		//String accessToken  = this.tokenProvider.generateToken(snsUser, Duration.ofDays(7));
		String accessToken  = this.tokenProvider.reverseGenerateToken(snsUser, Duration.ofDays(7));
		String refreshToekn = this.tokenProvider.generateToken(snsUser, Duration.ofDays(30));
		this.tokenService.saveRefreshToken(snsUser.getId(), refreshToekn);
		
		utilService.setCookie("access_token", accessToken, utilService.toSecoundOfDay(7), response);
		utilService.setCookie("refresh_token", refreshToekn, utilService.toSecoundOfDay(30), response);
		
		return "redirect:/user/login"; // 현재는 홈페이지로 이동
	}
	
	@GetMapping("/login")
    public String login(HttpServletRequest request) {
		
		Cookie[] list = request.getCookies();
		//System.out.println("쿠키 획득" + list.length);
		for(Cookie cookie:list) {
			if(cookie.getName().equals("access_token")) {
				//System.out.println("access_token=" +  cookie.getValue() );			
			}
		}	
        return "login_form";
    }
}








