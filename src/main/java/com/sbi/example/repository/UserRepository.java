package com.sbi.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sbi.example.model.UserEnitity;

@Repository
public interface UserRepository extends JpaRepository<UserEnitity, Integer>{
	
	UserEnitity findByEmail(String email);

}
