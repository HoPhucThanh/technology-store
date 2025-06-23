package com.example.technologystore.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String product_id;
    private String name;
    private String description;
    private long price; // ✅ Đã đổi từ String → long
    private String image_url;
    private String category_id;
    private int quantity;
    private String brand;
    private String created_at;

    public Product() {
        // Bắt buộc để Firebase đọc dữ liệu
    }

    public Product(String product_id, String name, String description, long price, String image_url, String category_id, int quantity, String brand, String created_at) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image_url = image_url;
        this.category_id = category_id;
        this.quantity = quantity;
        this.brand = brand;
        this.created_at = created_at;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
