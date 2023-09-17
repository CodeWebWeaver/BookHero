package com.parkhomovsky.bookstore.model;

import com.parkhomovsky.bookstore.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    @Column(name = "status")
    private Status status;
    @NotNull
    @Column(name = "total")
    private BigDecimal total;
    @NotNull
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @NotNull
    @Column(name = "shipping_address")
    private String shippingAddress;
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;
}
