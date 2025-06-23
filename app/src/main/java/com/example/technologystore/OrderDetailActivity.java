package com.example.technologystore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technologystore.adapters.AdapterOrderDetail;
import com.example.technologystore.models.OrderDetail;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterOrderDetail adapter;
    private List<OrderDetail> detailList = new ArrayList<>();
    private DatabaseReference orderDetailsRef;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


        orderId = getIntent().getStringExtra("order_id");

        recyclerView = findViewById(R.id.recyclerOrderDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterOrderDetail(this, detailList);
        recyclerView.setAdapter(adapter);

        orderDetailsRef = FirebaseDatabase.getInstance().getReference("orderDetails");
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        orderDetailsRef.orderByChild("order_id").equalTo(orderId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        detailList.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            OrderDetail detail = item.getValue(OrderDetail.class);
                            if (detail != null) detailList.add(detail);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(OrderDetailActivity.this, "Lỗi tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
