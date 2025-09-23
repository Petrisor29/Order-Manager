package com.ordermanager.api.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private OrderStatus status = OrderStatus.NEW;
    private List<OrderItem> items = new ArrayList<>();

    public Order(int id, int customerId){
        this.id = id; this.customerId = customerId;
    }
    public int getId(){ return id; }
    public int getCustomerId(){ return customerId; }
    public OrderStatus getStatus(){ return status; }
    public void setStatus(OrderStatus s){ this.status = s; }
    public List<OrderItem> getItems(){ return items; }
}
