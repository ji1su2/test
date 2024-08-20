package com.web.demo.test;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//FIXME #REFACT: 롬복 테스트 MyLombok 생성
@Getter
@Setter
@ToString
@Builder
public class MyLombok {
	private final String title;
	private final String content;
	private final String author;
}
