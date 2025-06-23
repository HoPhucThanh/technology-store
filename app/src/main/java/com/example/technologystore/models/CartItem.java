package com.example.technologystore.models;

public class CartItem {
    private String cartItemId;
    private String user_id;
    private String product_id;
    private int quantity;
    private long price;

    private String name;         // tên sản phẩm (đổi từ productName)
    private String image_url;    // ảnh sản phẩm (đổi từ productImage)

    public CartItem() {}

    public CartItem(String cartItemId, String user_id, String product_id, int quantity, long price, String name, String image_url) {
        this.cartItemId = cartItemId;
        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.image_url = image_url;
    }

    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
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
}
