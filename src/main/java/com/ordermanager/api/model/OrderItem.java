package com.ordermanager.api.model;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // linia aparține unei comenzi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // linia se referă la un produs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;
    private Double unitPrice;

    public OrderItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
}
