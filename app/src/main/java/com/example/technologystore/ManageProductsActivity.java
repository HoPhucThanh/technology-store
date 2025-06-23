package com.example.technologystore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technologystore.adapters.AdapterAdminProduct;
import com.example.technologystore.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterAdminProduct adapter;
    private List<Product> productList = new ArrayList<>();
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // quay lại màn hình trước

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        btnAddProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ManageProductsActivity.this, AddProductActivity.class);
            startActivity(intent);
        });



        recyclerView = findViewById(R.id.recyclerManageProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdapterAdminProduct(this, productList);
        recyclerView.setAdapter(adapter);

        productsRef = FirebaseDatabase.getInstance().getReference("products");
        loadProducts();
    }

    private void loadProducts() {
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    Product product = item.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageProductsActivity.this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
