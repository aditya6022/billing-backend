package com.vivek.billing.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;

    private LocalDateTime dateTime;

    private String customerName;

    private String customerAddress;

    private String customerEmail;

    private String customerMobile;

    private Double totalAmount;

    private String formattedDateTime;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<Item> items;
}