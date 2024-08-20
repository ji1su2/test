package com.web.demo.dto;

import groovy.transform.ToString;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ReviewForm {
	@NotEmpty(message="리뷰는 필수 항목입니다.")
	@Size(max=100)
	private String content;
	
	
}
