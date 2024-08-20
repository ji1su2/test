package com.web.demo.entity;

import java.time.LocalDateTime;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	// FIXME #REFACT:오라클 지원형태로 변경(TEXT 제외)
	@Column(length = 512)
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private Post post;
}

