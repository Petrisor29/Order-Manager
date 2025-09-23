package com.ordermanager.api.service;

import com.ordermanager.api.model.Customer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CustomerService {
    private final Map<Integer, Customer> customers = new LinkedHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public CustomerService() {
        add("Alice","alice@example.com");
        add("Bob","bob@example.com");
    }

    public List<Customer> all() { return new ArrayList<>(customers.values()); }
    public Customer byId(int id) { return customers.get(id); }

    public Customer add(String name, String email) {
        int id = nextId.getAndIncrement();
        Customer c = new Customer(id, name, email);
        customers.put(id, c);
        return c;
    }

    public Customer update(int id, String name, String email) {
        Customer c = customers.get(id);
        if (c == null) return null;
        c.setName(name); c.setEmail(email);
        return c;
    }

    public boolean delete(int id){ return customers.remove(id) != null; }
}
