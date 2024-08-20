/**
 * Post 대상 다양한 쿼리 기능 제공을 위한 인터페이스
 * <Post, Integer> => <대상 엔티티 타입, 엔티티 값을 구분하는 PK타입>
 */
package com.web.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
	// 커스텀 조회 함수 추가 -> 요구사항에 맞게 구성
	// 제목을 통해서 post 검색
	Post findBySubject (String subject);
	Post findBySubjectAndContent (String subject, String content);
	
	// Like 적용
	List<Post> findBySubjectLike(String subject);
}
