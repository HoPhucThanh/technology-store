package com.example.technologystore;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.technologystore.models.Product;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddProductActivity extends AppCompatActivity {

    private EditText edtName, edtBrand, edtPrice, edtQuantity, edtCategoryId, edtImageUrl, edtDescription;
    private Button btnAddProduct;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        edtName = findViewById(R.id.edtName);
        edtBrand = findViewById(R.id.edtBrand);
        edtPrice = findViewById(R.id.edtPrice);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtCategoryId = findViewById(R.id.edtCategoryId);
        edtImageUrl = findViewById(R.id.edtImageUrl);
        edtDescription = findViewById(R.id.edtDescription);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        productsRef = FirebaseDatabase.getInstance().getReference("products");

        btnAddProduct.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        String name = edtName.getText().toString().trim();
        String brand = edtBrand.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String quantityStr = edtQuantity.getText().toString().trim();
        String categoryId = edtCategoryId.getText().toString().trim();
        String imageUrl = edtImageUrl.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(brand) || TextUtils.isEmpty(priceStr) ||
                TextUtils.isEmpty(quantityStr) || TextUtils.isEmpty(categoryId) ||
                TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        long price = Long.parseLong(priceStr);
        int quantity = Integer.parseInt(quantityStr);
        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Tạo ID mới tự tăng
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                long maxId = 0;
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    try {
                        long id = Long.parseLong(snapshot.getKey());
                        if (id > maxId) maxId = id;
                    } catch (NumberFormatException ignored) {}
                }

                String newId = String.valueOf(maxId + 1);

                Product newProduct = new Product(
                        newId, name, description, price, imageUrl,
                        categoryId, quantity, brand, createdAt
                );

                productsRef.child(newId).setValue(newProduct)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            finish(); // Quay lại trang trước
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Lỗi thêm sản phẩm", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Không thể tạo ID mới", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
