// TODO #1-3-1 : ReviewService 생성
package com.web.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.demo.entity.Post;
import com.web.demo.entity.Review;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.ReviewRepository;

// TODO #1-3-2 : 서비스 적용
@Service
public class ReviewService {
	// TODO #1-3-3 : 리뷰레파지토리 DI(의존성 주입) -> @Autowired, Setter, constructor
	@Autowired
	private ReviewRepository reviewRepository;
	
	/**
	 * 리뷰 등록 메소드
	 * @param post    : 리뷰의 부모에 해당되는(N:1) 부모 객체
	 * @param content : 리뷰 내용
	 */
	// TODO #1-3-4 : 리뷰 데이터를 리뷰 엔티티(하나 생성되야함)에 세팅해서, save() 사용하여 입력	
	public void create(Post post, String content) {
		// TODO #1-3-5 : 리뷰 엔티티 생성 및 데이터 세팅
		Review review = new Review();
		review.setContent(content);
		review.setCreateDate(LocalDateTime.now());	// 서버측의 시간
		review.setPost(post);
		// TODO #1-3-6 : 리뷰 엔티티 저장 -> insert SQL 작동
		this.reviewRepository.save(review);
	}

	public Review selectOneReview(Integer id) {
		// 실습 2분 => getOnePost와 패턴 동일함
		Optional<Review> oReview = this.reviewRepository.findById(id);
		if(oReview.isPresent()) {
			return oReview.get();
		}
		// 커스텀 예외 상황
		throw new DataNotFoundException("post not found");
	}

	public void delete(Review review) {
		this.reviewRepository.delete(review);
	}

	public void modify(Review review) {
		this.reviewRepository.save(review);
	}
}











