package com.example.technologystore;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technologystore.models.Order;
import com.google.firebase.database.*;

import java.text.DecimalFormat;

public class RevenueReportActivity extends AppCompatActivity {

    private TextView txtTotalRevenue;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_report);

        txtTotalRevenue = findViewById(R.id.txtTotalRevenue);
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        calculateRevenue();
    }

    private void calculateRevenue() {
        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            long total = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Order order = item.getValue(Order.class);
                    if (order != null && "đã giao".equalsIgnoreCase(order.getStatus())) {
                        total += order.getTotal_amount();
                    }
                }

                txtTotalRevenue.setText("Tổng doanh thu: " + formatMoney(total) + " đ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RevenueReportActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatMoney(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount).replace(",", ".");
    }
}
