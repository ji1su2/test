package com.web.demo.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.web.demo.entity.Post;
import com.web.demo.entity.Review;
import com.web.demo.repository.PostRepository;
import com.web.demo.repository.ReviewRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class JpaTests {
	// DI 사용
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private PostRepository postRepository;

	/**
	 * 포스트 데이터 획득후 관련 리뷰 모두 가져오기
	 * n개 이상 레포지토리를 이용하여 데이터 획득, 상호 연관을 맺는 경우 
	 * - Junit 단위테스트에서는 세션 종료가 발생될수 있다, 실 서비스에서는 상관없음
	 * - 해결방안 @Transactional 추가 => 세션유지
	 */
	@Transactional
	@Test
	void selectAllReviewByPost() {
		// 1. 아이디가 2인 post 데이터 획득 및 데이터 유무에 따른 테스트 진행 여부 판단
		Optional<Post> oPost = this.postRepository.findById(2);	
		assertTrue(oPost.isPresent()); // 해당 데이터가 없으면 중단됨	
		Post p = oPost.get();	
		// 실습 1분
		// 2. post로부터 review 데이터 획득
		List<Review> reviews = p.getReviewList();		
		// 실습 1분
		for(Review review : reviews ) {
			System.out.println( review.getContent() );
		}		
		// 3. review 데이터에 대한 테스트, 내용 체크 "좋은 프로그램 언어네요"과 일치하는지 특정 리뷰만 비교
		assertEquals("좋은 프로그램 언어네요", reviews.get(0).getContent());
	}
	
	/**
	 * 리뷰 등록 단독 테스트
	 * SQL 수행이 내부적으로 2개 이상 존재함 
	 * - JUnit 에러 발생(연결유지 설정 필요함), 실 서비스는 정상적
	 */
	@Test
	void insertReview() {
		// 1. post 아이디가 2인 데이터 획득(sql : select ~ )	
		Optional<Post> oPost = this.postRepository.findById(2);		
		// 2. 위 데이터가 존재하지 않으면 테스트 중단
		assertTrue(oPost.isPresent());		
		// 3. 실제 post 데이터(엔티티) 획득
		Post p = oPost.get();		
		// 4. Review 엔티티 객체 생성및 리뷰값 등록
		// 4-1 Review 엔티티 객체 생성
		Review r = new Review();
		// 4-2 리뷰값 등록
		r.setContent("좋은 프로그램 언어네요");
		r.setCreateDate(LocalDateTime.now());
		r.setPost(p); // 해당 리뷰에 대한 부모 post 설정
		// 5. 리뷰 저장(sql : insert ~ )
		this.reviewRepository.save( r );
	}
	/**
	 * @Test를 특정 메소드에 부여하면 -> 단위 테스트 함수가 된다
	 * 실행 : Run > Run as > JUnit Test
	 */
	@Test
	void testJpaDbTest() {
		// 데이터 입력 테스트
		//insertTest();
		
		// 데이터 모두 조회
		//selectAllTest();
		
		// 데이터 한개 조회
		//selectOneTest();
		
		// 특정 컬럼 조회
		//selectAllByColumn();
		
		// 특정 컬럼 N개 조회
		//selectSubjectContent();
		
		// like를 이용한 조회
		//selectLike();
		
		// 데이터 수정 테스트
		//updateData();
		
		// 데이터 삭제 테스트
		//deleteData();
	}
	void deleteData() {
		// 1. 데이터 획득( 특정 조건의 Entity 객체 획득 )
		Optional<Post> oPost = this.postRepository.findById(1);
		assertTrue(oPost.isPresent());
		// 2. 엔티티 삭제( 레포지토리.delete(엔티티)    )
		Post p = oPost.get();
		/*
		 delete 
		    from
		        post 
		    where
		        id=?
		 */
		this.postRepository.delete(p);
		// 3. 검사 -> 데이터의 개수 체크
		// select count(*) from post;
		assertEquals(1, this.postRepository.count());
	}
	
	// 데이터 수정 테스트
	void updateData() {
		// 1. 데이터 획득(특정 조건의 Entity 객체 획득)
		Optional<Post> oPost = this.postRepository.findById(1);
		// 성공이면 아이다 1인 post 데이터를 잘 획득했다
		assertTrue(oPost.isPresent());
		// 2. 엔티티값 수정
		Post p = oPost.get();
		p.setSubject("수정 : " + p.getSubject());
		// 3. 엔티티를 저장
		/*
		 	update
		        post 
		    set
		        content=?,
		        create_date=?,
		        subject=? 
		    where
		        id=?
		 */
		this.postRepository.save(p);
	}	
	
	
	
	void insertTest() {
		System.out.println("데이터 입력 단위 테스트");
		// Post 작성 -> Entity 객체 생성 and 데이터 입력, 편의상 setter 사용
		Post p = new Post();
		p.setSubject("자바는?");
		p.setContent("어떤 언어인가요?");
		p.setCreateDate(LocalDateTime.now());
		// Post 엔티티의 내용을 디비 테이블에 반영 => CRUD 업무 => 담당 :  PostRepository
		// PostRepository 객체가 필요
		// 스트링부트가 뭔가 작업해서 객체를 만들어서 사용할수 있게 제공해준다면?
		// => 스프링의 의존성 주입(DI : Dependency Injection) <- 중요한 특성/기능
		// 스프링부트가 알아서 객체를 대신 생성 => 주입해주는 기법 or Setter 방식 or 생성자 사용 방식
		this.postRepository.save(p); // 첫번째 포스트 입력
		
		Post p1 = new Post();
		p1.setSubject("스프링부트는?");
		p1.setContent("어떤 프레임웍인가요?");
		p1.setCreateDate(LocalDateTime.now());
		this.postRepository.save(p1); // 두번째 포스트 입력
		
	}
	void selectAllTest() {
		// 조회계열 => 레퍼지토리를 통해서 제공 혹응 확장
		// 특정 테이블의 모든 데이터 가져오기 자동으로 제공됨
		List<Post> posts = this.postRepository.findAll();
		System.out.println( posts.size() );
		// 개별 데이터확인은 for문 가능등 toString()도 가능,..
		
		// 값체크 함수를 통해서 정상 여부 점검
		//assertEquals(3, posts.size()); // 최종 결과의 개수가 3하고 다르면 오류
		//assertEquals(2, posts.size()); // 최종 결과의 개수가 2하고 같으면 통과
		
		// 결과값에서 한개 추출
		//Post p = posts.get(0);
		//assertEquals("자바는?", p.getSubject()); // 첫번째 게시물의 제목이 동일하면 통과
		
		// 값을 모두 확인하고 싶다
		// lombok의 @ToString 적용 혹은 for문 처리후 확인 => 리뷰시간에 실행!!
		// 체크
		for(Post p1 : posts) {
			System.out.println( p1.getSubject() );
		}
		
	}
	void selectOneTest() {
		// PK 값인 id를 이용하여 데이터 조회
		// 디비 테이블상에 아이디값 1이 존재함을 확인후 테스트한 것임
		// Optional => null일수도 있어서
		Optional<Post> optPost = this.postRepository.findById(1);
		if( optPost.isPresent() ) { // 데이터가 존재하면
			Post p = optPost.get();
			assertEquals("자바는?", p.getSubject());
		} else {
			System.out.println("데이터 없음");
		}
		
	}
	void selectAllByColumn() {
		// findBy컬럼명 => 대소문자 주의, 오타 주의
		Post p = this.postRepository.findBySubject("자바는?");
		// p 엔티티의 id값을 비교 => 1번 Post 체크
		assertEquals(1, p.getId());
	}
	void selectSubjectContent() {
		Post p = this.postRepository.findBySubjectAndContent("자바는?", "어떤 언어인가요?");
		assertEquals(1, p.getId());
	}
	void selectLike() {
		List<Post> ps = this.postRepository.findBySubjectLike("%는?%");
		// 첫번재 데이터 추출
		//Post p = ps.get(0);
		// 데이터가 2개이면 통과 (개수가 다르면 수치를 변경)
		assertEquals(2, ps.size());
	}
}
