package com.sbi.example.model;

import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private long accountNumber;
    private Double lastCreditedAmount;
    private Double availableBalance;
}
