package com.web.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.web.demo.dto.PostForm;
import com.web.demo.dto.ReviewForm;
import com.web.demo.entity.Post;
import com.web.demo.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/post")
@RequiredArgsConstructor
@Controller
public class PostController {
	private final PostService postService;
	
	@GetMapping("/list")
	public String list(Model model) {
		List<Post> posts = this.postService.getAllPost();
		/*
		for(Post post : posts){
			System.out.println( post.getSubject() );
		}
		*/
		model.addAttribute("posts", posts);
		model.addAttribute("dumy_year", 2024);
		return "post_list";
	}
	@GetMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, 
			ReviewForm reviewForm) {
		Post p = this.postService.getOnePost(id);
		model.addAttribute("post", p);
		return "post_detail";
	}
	@GetMapping("/create")
	public String create(Model model, PostForm postForm) {
		model.addAttribute("method", "post");
		return "post_form";
	}
	@PostMapping("/create")
	public String create(@Valid PostForm postForm, BindingResult bindingResult) {
		if( bindingResult.hasErrors() ) {
			return "post_form";
		}
		this.postService.create(postForm.getSubject(), postForm.getContent());
		return "redirect:/post/list";
	}
	@GetMapping("/modify/{id}")
	public String modify(Model model, PostForm postForm, @PathVariable("id") Integer id) {
		Post post = this.postService.getOnePost(id);
		postForm.setSubject( post.getSubject() );
		postForm.setContent( post.getContent() );
		model.addAttribute("method", "put");
		return "post_form";
	}
	@PutMapping("/modify/{id}")
	public String modify(@Valid PostForm postForm, BindingResult bindingResult,
			 			 @PathVariable("id") Integer id) {
		if( bindingResult.hasErrors() ) {
			return "post_form";
		}
		Post post = this.postService.getOnePost(id);
		post.setSubject(postForm.getSubject());
		post.setContent(postForm.getContent());
		this.postService.modify(post );
		return "redirect:/post/detail/" + id;
	}
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id) {
		Post post = this.postService.getOnePost(id);
		this.postService.delete( post );
		return "redirect:/post/list";
	}
}























