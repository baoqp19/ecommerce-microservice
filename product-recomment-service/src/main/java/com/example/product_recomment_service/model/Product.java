package com.example.product_recomment_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "products")
public class Product {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (name = "product_name")
    private String productName;

    @OneToMany (mappedBy = "product")
    @JsonIgnore
    private List<Recomment> recomendations;

}
