package com.green_home_project.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;
}
