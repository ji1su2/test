package com.web.demo.lombok;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.web.demo.test.MyLombok;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// FIXME #REFACT: 롬복 테스트 JUnit 로 변경
@SpringBootTest
public class TestLombok {

	@Test
	void lmbokTest () {
		MyLombok tl = MyLombok.builder()
				  // setter 대용
						  .author("작성자")
						  .title("제목")
						  .content("내용")
						  .build();
		System.out.println(tl.getAuthor());
		System.out.println(tl.toString());
	}
}







