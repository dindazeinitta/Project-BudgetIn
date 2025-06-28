package com.budgetin.model;

import lombok.Data;

@Data
public class Transaction {
    private Long id;
    private String name;
    private String category;
    private String paymentMethod;
    private String amount; // Contoh: "+Rp. 2.500.000" atau "-Rp. 79.000"
    private String date;   // Contoh: "13 June 2024"
    private String time;   // Contoh: "09:30"
    private String notes;  // Optional
}
