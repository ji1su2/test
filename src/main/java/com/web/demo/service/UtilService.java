package com.web.demo.service;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

//FIXME #REFACT: 유틸리티
@Service
public class UtilService {
	public void setCookie(String key, String value, int expiration, HttpServletResponse response) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(expiration);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	public int toSecoundOfDay(int day) {
		return 60*60*24*day;
	}
}
