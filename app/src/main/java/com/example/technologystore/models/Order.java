package com.example.technologystore.models;

public class Order {
    private String order_id;
    private String user_id;
    private long total_amount;
    private String payment_method;
    private String qr_code_url;
    private String payment_code;
    private String status;
    private String created_at;

    public Order() {}

    public Order(String order_id, String user_id, long total_amount, String payment_method,
                 String qr_code_url, String payment_code, String status, String created_at) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.total_amount = total_amount;
        this.payment_method = payment_method;
        this.qr_code_url = qr_code_url;
        this.payment_code = payment_code;
        this.status = status;
        this.created_at = created_at;
    }

    public String getOrder_id() { return order_id; }
    public String getUser_id() { return user_id; }
    public long getTotal_amount() { return total_amount; }
    public String getPayment_method() { return payment_method; }
    public String getQr_code_url() { return qr_code_url; }
    public String getPayment_code() { return payment_code; }
    public String getStatus() { return status; }
    public String getCreated_at() { return created_at; }

    // Nếu cần setter có thể thêm sau
}
