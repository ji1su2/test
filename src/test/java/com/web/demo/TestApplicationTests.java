package com.web.demo;

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
class TestApplicationTests {
	@Test
	void contextLoads() {		
	}
}








