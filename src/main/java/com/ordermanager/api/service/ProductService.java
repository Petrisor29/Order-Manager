package com.ordermanager.api.service;

import com.ordermanager.api.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductService {
    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public ProductService() {
        // demo data
        addProduct("Laptop", 3500);
        addProduct("Mouse", 80);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Product findById(int id) {
        return products.get(id);
    }

    public Product addProduct(String name, double price) {
        int id = nextId.getAndIncrement();
        Product p = new Product(id, name, price);
        products.put(id, p);
        return p;
    }

    public Product updateProduct(int id, String name, double price) {
        Product p = products.get(id);
        if (p == null) return null;
        p.setName(name);
        p.setPrice(price);
        return p;
    }

    public boolean deleteProduct(int id) {
        return products.remove(id) != null;
    }
}
