package com.web.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 128)
	private String subject;
	
	// FIXME #REFACT: 오라클 지원형태로 변경(TEXT 제외)
	@Column(length = 1024)
	private String content;
	
	private LocalDateTime createDate;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<Review> reviewList;
	
	// @Transient => 클레스 속성으로만 존재
}
