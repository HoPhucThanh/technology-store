package com.example.technologystore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManageOrders, btnManageProducts, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button btnRevenueReport = findViewById(R.id.btnRevenueReport);
        btnRevenueReport.setOnClickListener(v -> {
            startActivity(new Intent(this, RevenueReportActivity.class));
        });


        btnManageOrders = findViewById(R.id.btnManageOrders);
        btnManageProducts = findViewById(R.id.btnManageProducts);
        btnLogout = findViewById(R.id.btnLogoutAdmin);

        btnManageOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageOrdersActivity.class));
        });

        btnManageProducts.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageProductsActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            // Quay lại màn hình đăng nhập
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}