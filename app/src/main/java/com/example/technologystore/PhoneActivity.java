package com.example.technologystore;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technologystore.adapters.AdapterProduct;
import com.example.technologystore.models.Product;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class PhoneActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterProduct adapterProduct;
    private List<Product> productList = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Toolbar toolbar = findViewById(R.id.toolbarPhone);
        setSupportActionBar(toolbar);

        // Enable nút back trên ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Điện thoại");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerPhone);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapterProduct = new AdapterProduct(this, productList);
        recyclerView.setAdapter(adapterProduct);

        loadPhoneProducts();
    }

    private void loadPhoneProducts() {
        dbRef = FirebaseDatabase.getInstance().getReference("products");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String categoryId = item.child("category_id").getValue(String.class);
                    if ("1".equals(categoryId)) { // category_id 1 là Điện thoại
                        Product product = item.getValue(Product.class);
                        if (product != null) {
                            productList.add(product);
                        }
                    }
                }
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PhoneActivity.this, "Lỗi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xử lý nút back trên Toolbar


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        // ✅ Xử lý bấm giỏ hàng
        if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(PhoneActivity.this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
