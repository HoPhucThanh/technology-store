package com.example.technologystore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technologystore.adapters.AdapterAdminOrder;
import com.example.technologystore.models.Order;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterAdminOrder adapterAdminOrder;
    private List<Order> orderList = new ArrayList<>();
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


        recyclerView = findViewById(R.id.recyclerManageOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Sử dụng adapter dành riêng cho admin
        adapterAdminOrder = new AdapterAdminOrder(this, orderList);
        recyclerView.setAdapter(adapterAdminOrder);

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        loadAllOrders();
    }

    private void loadAllOrders() {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Order order = item.getValue(Order.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }
                adapterAdminOrder.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageOrdersActivity.this, "Lỗi tải đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
