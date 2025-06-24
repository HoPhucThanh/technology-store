package com.example.technologystore;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;

import com.example.technologystore.adapters.AdapterOrder; // ✅ import đúng tên mới
import com.example.technologystore.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerOrders;
    private List<Order> orderList = new ArrayList<>();
    private AdapterOrder orderAdapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("current_user_id", "");

        recyclerOrders = findViewById(R.id.recyclerOrders);
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));


        orderAdapter = new AdapterOrder(this, orderList);
        recyclerOrders.setAdapter(orderAdapter);

        loadUserOrders();
    }

    private void loadUserOrders() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("orders");
        ref.orderByChild("user_id").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            Order order = item.getValue(Order.class);
                            if (order != null) orderList.add(order);
                        }
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyOrdersActivity.this, "Lỗi tải đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
