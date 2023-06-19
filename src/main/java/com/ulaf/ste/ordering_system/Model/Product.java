package com.ulaf.ste.ordering_system.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private String description;
    private Boolean customizable;
    @JsonManagedReference
    @ManyToOne
    private Category category;
    @ManyToMany
    private List<Ingredient> ingredients;

    @Lob
    private byte[] image;

    public Product(String name, double price,String description,Boolean customizable, List<Ingredient> ingredients, byte[] image, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.ingredients = ingredients;
        this.image = image;
        this.description = description;
        this.customizable = customizable;
    }

//    public Product(Long id, String name, double price, List<Ingredient> ingredients, byte[] image, Category category) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.category = category;
//        this.ingredients = ingredients;
//        this.image = image;
//    }
//
//    public Product(String name, double price) {
//        this.name = name;
//        this.price = price;
//    }
}
