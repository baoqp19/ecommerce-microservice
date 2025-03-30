package com.example.product_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"subCategories", "parentCategory", "products"})
@Entity
@Table(name = "categories")
public final class Category extends AbstractMappedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", unique = true, nullable = false, updatable = false)
    private Integer categoryId;

    @Column(name = "category_title")
    private String categoryTitle;

    @Column(name = "image_url")
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Category> subCategories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> products;

}
