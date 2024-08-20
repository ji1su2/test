package com.web.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 예외 발생시, 스프링부트에 설정된 http 상태코드, 사유(이유)를 포함한
 * 응답을 클라이언트에서 전달 구성 필요
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String msg) {
		super(msg);
	}
}
