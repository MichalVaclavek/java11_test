package com.example.reactive_demo.controller;

import com.example.reactive_demo.repository.Customer;
import com.example.reactive_demo.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@Controller
@ResponseBody
public class CustomerController {

    private CustomerRepository repository;

    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    public Flux<Customer> getAllCustomers() {
        return repository.findAll();
    }
}
