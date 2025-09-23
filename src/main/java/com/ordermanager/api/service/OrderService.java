package com.ordermanager.api.service;

import com.ordermanager.api.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    private final CustomerService customers;
    private final ProductService products;

    private final Map<Integer, Order> orders = new LinkedHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public OrderService(CustomerService customers, ProductService products){
        this.customers = customers; this.products = products;
    }

    public Order create(int customerId){
        if (customers.byId(customerId) == null) throw new NoSuchElementException("Customer not found: "+customerId);
        int id = nextId.getAndIncrement();
        Order o = new Order(id, customerId);
        orders.put(id, o);
        return o;
    }

    public Order addItem(int orderId, int productId, int qty){
        if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        Order o = orders.get(orderId); if (o==null) throw new NoSuchElementException("Order not found: "+orderId);
        if (o.getStatus()!=OrderStatus.NEW) throw new IllegalStateException("Order not editable in status: "+o.getStatus());
        var p = products.findById(productId); if (p==null) throw new NoSuchElementException("Product not found: "+productId);

        // dacă există deja, creștem cantitatea
        for (var it : o.getItems()){
            if (it.getProductId()==productId){ it.setQuantity(it.getQuantity()+qty); return o; }
        }
        o.getItems().add(new OrderItem(p.getId(), p.getName(), p.getPrice(), qty));
        return o;
    }

    public double total(int orderId){
        Order o = orders.get(orderId); if (o==null) throw new NoSuchElementException("Order not found: "+orderId);
        return o.getItems().stream().mapToDouble(it -> it.getUnitPrice()*it.getQuantity()).sum();
    }

    public Order changeStatus(int orderId, OrderStatus newStatus){
        Order o = orders.get(orderId); if (o==null) throw new NoSuchElementException("Order not found: "+orderId);
        OrderStatus cur = o.getStatus();
        boolean valid =
            (cur==OrderStatus.NEW  && (newStatus==OrderStatus.PAID || newStatus==OrderStatus.CANCELLED)) ||
            (cur==OrderStatus.PAID && (newStatus==OrderStatus.SHIPPED || newStatus==OrderStatus.CANCELLED));
        if (!valid) throw new IllegalStateException("Invalid transition: "+cur+" -> "+newStatus);
        o.setStatus(newStatus);
        return o;
    }

    public List<Order> all(){ return new ArrayList<>(orders.values()); }
    public Order byId(int id){ return orders.get(id); }

    public List<Order> byCustomer(int customerId){
        List<Order> out = new ArrayList<>();
        for (var o : orders.values()) if (o.getCustomerId()==customerId) out.add(o);
        return out;
    }

    public List<Order> byStatus(OrderStatus st){
        List<Order> out = new ArrayList<>();
        for (var o : orders.values()) if (o.getStatus()==st) out.add(o);
        return out;
    }

    public Order updateItemQty(int orderId, int productId, int newQty){
        Order o = orders.get(orderId); if (o==null) throw new NoSuchElementException("Order not found: "+orderId);
        if (o.getStatus()!=OrderStatus.NEW) throw new IllegalStateException("Order not editable in status: "+o.getStatus());
        if (newQty<0) throw new IllegalArgumentException("Quantity must be >= 0");
        var it = o.getItems().stream().filter(x -> x.getProductId()==productId).findFirst().orElse(null);
        if (it==null) throw new NoSuchElementException("Product not in order: "+productId);
        if (newQty==0) { o.getItems().remove(it); } else { it.setQuantity(newQty); }
        return o;
    }
}
