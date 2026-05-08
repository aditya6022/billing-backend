package com.vivek.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private Integer quantity;
    private Double price;
    private Double total;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    @JsonIgnore
    private Invoice invoice;
}