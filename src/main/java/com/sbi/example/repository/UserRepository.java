package com.sbi.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbi.example.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	UserEntity findByEmail(String email);

	void deleteByEmail(String email);

}
