// TODO #1 : ReviewController 생성, /review/create/{id} 라우팅 
package com.web.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.demo.dto.PostForm;
import com.web.demo.dto.ReviewForm;
import com.web.demo.entity.Post;
import com.web.demo.entity.Review;
import com.web.demo.service.PostService;
import com.web.demo.service.ReviewService;

import jakarta.validation.Valid;

// URL 프리픽스 추가
@RequestMapping("/review")
// TODO #1-1 : 컨트롤러 기능 부여
@Controller
public class ReviewController {
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private PostService postService;
	/**
	 * 리뷰 생성 함수
	 * @param id
	 * @param content
	 * @return
	 */
	// TODO #1-2 : /review/create/{id}를 매핑하는 메소드구현
	//@PostMapping("/review/create/{id}")
	@PostMapping("/create/{id}")
	//public String create(@PathVariable("id") Integer id, 
	//		             @RequestParam(value="content") String content) {
	
	public String create(Model model, @PathVariable("id") Integer id,
			@Valid ReviewForm reviewForm, BindingResult bindingResult) {
		Post post = this.postService.getOnePost(id);
		// 오류 검사 추가
		if( bindingResult.hasErrors() ) {
			// 원래 detatil에 접속하던 방식으로 접근(재현)
			model.addAttribute("post", post);
			return "post_detail";
		}
		// TODO #1-3 : 서비스를 통해서 Review 엔티티 생성하여 디비에 저장 (서비스 생성, 레퍼지토리사용, 엔티티사용)
		// 1-3 구현, 실습 2분
		// 1. ReviewService 의존성 주입 -> 맴버 변수 자리에서 진행
		// 2. Post 엔티티 객체 획득
		// 3. ReviewService.create(Post객체, content) 함수 호출
		// 유효성 폼사용 => content은 폼객체로 부처 추출
		this.reviewService.create(post , reviewForm.getContent());
		
		// TODO #1-4 : ~/post/detail/{id} 페이지를 요청한다 -> 서버에서 요청 : 리다이렉트
		return "redirect:/post/detail/" + id;
	}
	
	/**
	 * 
	 * @param id : 리뷰 ID
	 * @return
	 */
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		// 리뷰 ID => 리뷰 엔티티 획득
		Review review = this.reviewService.selectOneReview( id );
		this.reviewService.delete( review );
		// Post ID를 획득해서 해당 Post의 상세페이지로 이동
		return "redirect:/post/detail/" + review.getPost().getId();
	}

	@GetMapping("/modify/{id}")
	public String modify(ReviewForm reviewForm, @PathVariable("id") Integer id) {
		// 실습 1분
		// Review 내용 획득
		Review review = this.reviewService.selectOneReview(id);
		// reviewForm에 내용 설정
		reviewForm.setContent(review.getContent());
		return "review_form";
	}
	@PostMapping("/modify/{id}")
	public String modify(@Valid ReviewForm reviewForm, BindingResult bindingResult,
			 			 @PathVariable("id") Integer id) {
		if( bindingResult.hasErrors() ) {
			return "review_form";
		}
		Review review = this.reviewService.selectOneReview(id);		
		review.setContent(reviewForm.getContent());
		this.reviewService.modify( review );
		return "redirect:/post/detail/" + review.getPost().getId();
	}
}







