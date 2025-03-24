package com.example.order_serivce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_line_items")
public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "orderNumber")
    @NotBlank
    private String orderNumber;

    @Column(name = "productName")
    @NotBlank
    private String productName;

    @Column(name = "price")
    @NotBlank
    private BigDecimal price;

    @Column(name = "quantity")
    @NotBlank
    private Integer quantity;

}
