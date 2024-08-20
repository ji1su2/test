package com.web.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.web.demo.entity.Post;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// 서비스로 인식
@Service
public class PostService {
	// 여러개의 레파지토리를 의존성 주입, 
	// 생성자 방식(@RequiredArgsConstructor) -> final
	private final PostRepository postRepository;
	
	// 필요한 복잡한 기능들을 메소드 단위로 구현
	// 요구사항: 현재 존재하는 모든 post 데이터 획득 (개수제한 x, 정렬 x, 모든컬럼)
	public List<Post> getAllPost() {
		return this.postRepository.findAll();
	}
	
	// id를 넣어서 일치하는 Post 엔티티 객체 리턴
	public Post getOnePost(Integer id) {
		Optional<Post> oPost = this.postRepository.findById(id);
		if(oPost.isPresent()) {
			return oPost.get();
		}
		// 커스텀 예외 상황
		throw new DataNotFoundException("post not found");
	}
	// Post 1개 디비에 등록 처리
	public void create(String subject, String content) {
		// Post 엔티티 생성
		Post p = new Post();
		// Post 엔티티에 데이터 세팅
		p.setSubject(subject);
		p.setContent(content);
		p.setCreateDate(LocalDateTime.now());
		// PostRepository에 save() => 디비에 Post 테이블에 row데이터 1개 저장
		this.postRepository.save(p);
		// 실습 2분		
	}

	// Post 수정하기 기능
	public void modify(Post post) {
		this.postRepository.save( post );
	}

	public void delete(Post post) {
		this.postRepository.delete(post);
	}
}







