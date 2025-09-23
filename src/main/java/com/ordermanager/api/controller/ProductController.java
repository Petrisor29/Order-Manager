package com.ordermanager.api.controller;

import com.ordermanager.api.model.Product;
import com.ordermanager.api.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> byId(@PathVariable int id) {
        Product p = service.findById(id);
        return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }

    record CreateReq(String name, double price) {}
    record UpdateReq(String name, double price) {}

    @PostMapping
    public Product create(@RequestBody CreateReq req) {
        return service.addProduct(req.name(), req.price());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable int id, @RequestBody UpdateReq req) {
        Product p = service.updateProduct(id, req.name(), req.price());
        return p == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return service.deleteProduct(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
