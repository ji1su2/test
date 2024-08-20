package com.web.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.demo.config.UserRole;
import com.web.demo.entity.SnsUser;
import com.web.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 스프링 시큐리티가 로그인 시 사용할
 * UserDetailsService 인터페이스를 구현(implements)
 * loadUserByUsername 메서드를 구현하도록 강제
 * 사용자명(username)으로 스프링 시큐리티의 사용자(User) 객체를 조회하여 리턴하는 메서드
 */
//@RequiredArgsConstructor
@Service
public class SecurityService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		// 사용자 검색
		Optional<SnsUser> _user = this.userRepository.findByUsername(username);
        if (_user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }
        // 유저 획득
        SnsUser user = _user.get();
        // 권한 목
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 사용자명이 ‘admin’인 경우에는 ADMIN 권한(ROLE_ADMIN)을 부여
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        System.out.println(username + " 회원임");
        // 유저 정보 전달
        // 이 객체는 스프링 시큐리티에서 사용하며 User 생성자에는 사용자명, 비밀번호, 권한 리스트가 전달
        // 스프링 시큐리티 => User 객체의 비밀번호가 사용자로부터 입력받은 비밀번호와 일치하는지를 검사하는 기능을 내부
        return new User(user.getUsername(), user.getPassword(), authorities);
	}

}
