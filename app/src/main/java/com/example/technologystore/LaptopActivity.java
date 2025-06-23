package com.example.technologystore;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.technologystore.adapters.AdapterProduct;
import com.example.technologystore.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LaptopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterProduct adapterProduct;
    private List<Product> productList = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);

        Toolbar toolbar = findViewById(R.id.toolbarLaptop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Laptop");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerLaptop);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapterProduct = new AdapterProduct(this, productList);
        recyclerView.setAdapter(adapterProduct);

        loadLaptopProducts();
    }

    private void loadLaptopProducts() {
        dbRef = FirebaseDatabase.getInstance().getReference("products");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    String cat = String.valueOf(item.child("category_id").getValue());
                    Product product = item.getValue(Product.class);
                    if (product != null && cat.equals("2")) {
                        productList.add(product);
                    }
                }
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LaptopActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Xử lý nút quay lại
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        // ✅ Xử lý nút giỏ hàng
        if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(LaptopActivity.this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
