package com.sbi.example.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "BALANCE_ENQUIRY")
public class BalanceEnquiryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private long accountNumber;
    private Double lastCreditedAmount;
    private Double availableBalance;
}
