package com.example.technologystore.models;

public class OrderDetail {
    private String order_detail_id;
    private String order_id;
    private String product_id;
    private String name;
    private String image_url;
    private int quantity;
    private long price;  // ✅ Sửa từ double → long

    public OrderDetail() {}

    public OrderDetail(String order_detail_id, String order_id, String product_id, String name, String image_url, int quantity, long price) {
        this.order_detail_id = order_detail_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.name = name;
        this.image_url = image_url;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(String order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {  // ✅ sửa kiểu dữ liệu getter
        return price;
    }

    public void setPrice(long price) {  // ✅ sửa setter
        this.price = price;
    }
}
