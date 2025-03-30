package com.example.product_service.domain;

import java.io.Serial;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = { "category" })
@Data
@Builder
@Entity
@Table(name = "products")
public final class Product extends AbstractMappedEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true, nullable = false, updatable = false)
    private Integer productId;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(unique = true)
    private String sku;

    @Column(name = "price_unit", columnDefinition = "decimal")
    private Double priceUnit;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

}
