package com.example.model;

import com.example.interfaces.Identifiable;

import java.util.UUID;

public class Product implements Identifiable {
    private UUID id;
    private String name;
    private double price;

    public Product() {}

    public Product(UUID id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, double price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product " + id + " [ name=" + name + ", price=" + price + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Product product = (Product) obj;
        return id.equals(product.id) && name.equals(product.name) && price == product.price;
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + Double.hashCode(price);
    }
}
