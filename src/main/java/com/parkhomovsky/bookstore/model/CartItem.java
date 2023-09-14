package com.parkhomovsky.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_items")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "shopping_cart", nullable = false)
  @ManyToOne
  @JoinColumn(name = "shopping_cart_id")
  private ShoppingCart shoppingCart;
  @Column(name = "book", nullable = false)
  @OneToOne
  @JoinColumn(name = "book_id")
  private Book book;
  @Column(name = "quantity", nullable = false)
  private int quantity;
}
