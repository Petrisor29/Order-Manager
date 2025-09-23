package com.ordermanager.api.controller;

import com.ordermanager.api.model.Customer;
import com.ordermanager.api.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service){ this.service = service; }

    record CreateReq(String name, String email){}
    record UpdateReq(String name, String email){}

    @GetMapping public List<Customer> all(){ return service.all(); }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> byId(@PathVariable int id){
        var c = service.byId(id);
        return c==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(c);
    }

    @PostMapping
    public Customer create(@RequestBody CreateReq req){ return service.add(req.name(), req.email()); }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> update(@PathVariable int id, @RequestBody UpdateReq req){
        var c = service.update(id, req.name(), req.email());
        return c==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id){
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
