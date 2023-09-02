package com.parkhomovsky.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;
    @Column(name = "Title", nullable = false)
    private String title;
    @Column(name = "Author", nullable = false)
    private String author;
    @Column(name = "ISBN",nullable = false, unique = true)
    private String isbn;
    @Column(name = "Price", nullable = false)
    private BigDecimal price;
    @Column(name = "Description")
    private String description;
    @Column(name = "Image")
    private String coverImage;
}
