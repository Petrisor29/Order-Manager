package com.ordermanager.api.controller;

import com.ordermanager.api.model.Order;
import com.ordermanager.api.model.OrderStatus;
import com.ordermanager.api.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service){ this.service = service; }

    record CreateReq(int customerId){}
    record AddItemReq(int productId, int quantity){}
    record ChangeStatusReq(OrderStatus status){}

    @PostMapping public Order create(@RequestBody CreateReq req){ return service.create(req.customerId()); }

    @PostMapping("/{id}/items")
    public ResponseEntity<Order> addItem(@PathVariable int id, @RequestBody AddItemReq req){
        try { return ResponseEntity.ok(service.addItem(id, req.productId(), req.quantity())); }
        catch (RuntimeException ex){ return ResponseEntity.badRequest().build(); }
    }

    @GetMapping public List<Order> all(){ return service.all(); }

    @GetMapping("/{id}")
    public ResponseEntity<Order> byId(@PathVariable int id){
        var o = service.byId(id);
        return o==null ? ResponseEntity.notFound().build() : ResponseEntity.ok(o);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<Double> total(@PathVariable int id){
        try { return ResponseEntity.ok(service.total(id)); }
        catch (RuntimeException ex){ return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Order> change(@PathVariable int id, @RequestBody ChangeStatusReq req){
        try { return ResponseEntity.ok(service.changeStatus(id, req.status())); }
        catch (RuntimeException ex){ return ResponseEntity.badRequest().build(); }
    }

    @GetMapping("/by-customer/{customerId}")
    public List<Order> byCustomer(@PathVariable int customerId){ return service.byCustomer(customerId); }

    @GetMapping("/by-status/{status}")
    public List<Order> byStatus(@PathVariable OrderStatus status){ return service.byStatus(status); }

    @PostMapping("/{id}/items/{productId}")
    public ResponseEntity<Order> updateQty(@PathVariable int id, @PathVariable int productId, @RequestParam int qty){
        try { return ResponseEntity.ok(service.updateItemQty(id, productId, qty)); }
        catch (RuntimeException ex){ return ResponseEntity.badRequest().build(); }
    }
}
