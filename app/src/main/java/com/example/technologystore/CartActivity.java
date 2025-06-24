package com.example.technologystore;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.technologystore.adapters.AdapterCartItem;
import com.example.technologystore.models.CartItem;
import com.example.technologystore.models.Order;
import com.example.technologystore.models.OrderDetail;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private AdapterCartItem cartAdapter;
    private List<CartItem> cartItemList = new ArrayList<>();
    private TextView txtTotal;
    private Button btnCheckout, btnContinue;
    private DatabaseReference cartRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("current_user_id", "");
        if (currentUserId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy người dùng, vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnContinue = findViewById(R.id.btnContinue);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new AdapterCartItem(this, cartItemList, txtTotal);
        recyclerViewCart.setAdapter(cartAdapter); // gắn adapter vào recylerview

        loadCartItems();

        btnContinue.setOnClickListener(v ->
                startActivity(new Intent(CartActivity.this, HomeActivity.class)));

        btnCheckout.setOnClickListener(v -> showPaymentDialog());
    }

    private void loadCartItems() {
        cartRef = FirebaseDatabase.getInstance().getReference("cart").child(currentUserId);
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItemList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    CartItem cartItem = item.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItemList.add(cartItem);
                    }
                }
                cartAdapter.notifyDataSetChanged();
                cartAdapter.updateTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Lỗi tải giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPaymentDialog() {
        String[] options = {"Thanh toán khi nhận hàng", "Chuyển khoản QR"};
        new AlertDialog.Builder(this)
                .setTitle("Chọn phương thức thanh toán")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        placeOrder("cod");
                    } else {
                        placeOrder("qr");
                    }
                }).show();
    }

    private void placeOrder(String method) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = ordersRef.push().getKey();  // ✅ order_id là String( ngẫu nhiên)
        long total = (long) cartAdapter.calculateTotal();
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        final String finalQrUrl = method.equals("qr")
                ? "https://png.pngtree.com/png-clipart/20190924/original/pngtree-qr-code-free-png-png-image_4863862.jpg"
                : "";
        final String finalPaymentCode = method.equals("qr") ? orderId : "";



        Order order = new Order(
                orderId,
                currentUserId,
                total,
                method,
                finalQrUrl,
                finalPaymentCode,
                "chờ xác nhận",
                createdAt
        );
        ordersRef.child(orderId).setValue(order);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference orderDetailsRef = FirebaseDatabase.getInstance().getReference("orderDetails");

                for (DataSnapshot item : snapshot.getChildren()) {
                    CartItem cartItem = item.getValue(CartItem.class);
                    if (cartItem == null) continue;

                    String detailId = orderDetailsRef.push().getKey();

                    OrderDetail detail = new OrderDetail(
                            detailId,
                            orderId,
                            cartItem.getProduct_id(),
                            cartItem.getName(),
                            cartItem.getImage_url(),
                            cartItem.getQuantity(),
                            cartItem.getPrice()
                    );

                    orderDetailsRef.child(detailId).setValue(detail);
                }

                cartRef.removeValue();

                if (method.equals("qr")) {
                    showQrDialog(finalQrUrl, finalPaymentCode);
                } else {
                    Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Lỗi khi đặt hàng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ Dialog hiển thị QR và nội dung chuyển khoản
    private void showQrDialog(String qrUrl, String paymentCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thông tin thanh toán");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        ImageView qrImage = new ImageView(this);
        Glide.with(this).load(qrUrl).into(qrImage);
        layout.addView(qrImage);

        TextView txtCode = new TextView(this);
        txtCode.setText("Nội dung chuyển khoản:\n" + paymentCode);
        txtCode.setTextSize(16f);
        txtCode.setPadding(0, 20, 0, 0);
        txtCode.setTextColor(getResources().getColor(android.R.color.black));
        layout.addView(txtCode);

        builder.setView(layout);
        builder.setPositiveButton("Xong", (dialog, which) -> {
            startActivity(new Intent(CartActivity.this, HomeActivity.class));
            finish();
        });
        builder.setCancelable(false);
        builder.show();
    }
}
