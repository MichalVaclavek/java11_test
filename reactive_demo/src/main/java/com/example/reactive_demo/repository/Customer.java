package com.example.reactive_demo.repository;

public class Customer {

    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
