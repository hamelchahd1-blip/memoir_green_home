package com.green_home_project.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // اسم النبات
    private String type;        // نوع النبات (زهري، داخلي، خارجي..)
    private String description; // وصف أو معلومات إضافية
    private double price;       // السعر (اختياري لو تحب البيع)

    // الربط مع صاحب النبات (Seller)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    private boolean available = true; // إذا النبات متوفر للبيع أو لا
}