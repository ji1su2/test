package com.web.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.SnsUser;

public interface UserRepository extends JpaRepository<SnsUser, Long> {
	Optional<SnsUser> findByUsername(String username);
}
