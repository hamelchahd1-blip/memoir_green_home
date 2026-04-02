package com.green_home_project.service;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean processPayment(String cardNumber, String cvv) {

        // 🎭 Fake logic
        if (cardNumber.startsWith("4")) {
            return true; // Visa OK
        }

        if (cvv.length() != 3) {
            return false;
        }

        return true;
    }

}