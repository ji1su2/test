package com.web.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.Review;

public interface ReviewRepository  extends JpaRepository<Review, Integer>{
	// 필요한 메소드(디비에 연동되어서 쿼리 수행) 선언 => {} 없음
}
