package com.sbi.example.repository;

import com.sbi.example.model.BalanceEnquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceEnquiryRepository extends JpaRepository<BalanceEnquiryEntity, Long> {
}
