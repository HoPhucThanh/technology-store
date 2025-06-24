package com.example.technologystore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.technologystore.models.CartItem;
import com.example.technologystore.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView txtName, txtPrice, txtDescription, txtQuantity;
    private ImageView imgProduct;
    private Button btnAddToCart;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String currentUserId = prefs.getString("current_user_id", "");

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.txtProductNameDetail);
        txtPrice = findViewById(R.id.txtProductPriceDetail);
        txtDescription = findViewById(R.id.txtProductDescription);
        imgProduct = findViewById(R.id.imgProductDetail);
        txtQuantity = findViewById(R.id.txtQuantity);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        product = (Product) getIntent().getSerializableExtra("product"); // nhận product khi click vào sản phẩm( xử lý trong adapter)

        if (product != null) {
            getSupportActionBar().setTitle(product.getName());
            txtName.setText(product.getName());
            txtPrice.setText(product.getPrice() + " đ");
            txtDescription.setText(product.getDescription());
            Glide.with(this).load(product.getImage_url()).into(imgProduct);
            txtQuantity.setText("1");

            txtQuantity.setOnClickListener(v -> {
                if (product.getQuantity() == 0) {
                    Toast.makeText(this, "Sản phẩm đã hết hàng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final NumberPicker picker = new NumberPicker(this);
                picker.setMinValue(1);
                picker.setMaxValue(product.getQuantity());
                picker.setValue(Integer.parseInt(txtQuantity.getText().toString()));

                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Chọn số lượng")
                        .setView(picker)
                        .setPositiveButton("Chọn", (dialog, which) -> {
                            txtQuantity.setText(String.valueOf(picker.getValue()));
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });

            btnAddToCart.setOnClickListener(v -> {
                int quantity = Integer.parseInt(txtQuantity.getText().toString());
                long priceLong = product.getPrice();

                if (currentUserId.isEmpty()) {
                    Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference cartRef = FirebaseDatabase.getInstance()
                        .getReference("cart")
                        .child(currentUserId);

                String cartItemId = cartRef.push().getKey();

                CartItem cartItem = new CartItem(
                        cartItemId,
                        currentUserId,
                        String.valueOf(product.getProduct_id()),
                        quantity,
                        priceLong,
                        product.getName(),
                        product.getImage_url()
                );

                cartRef.child(cartItemId).setValue(cartItem)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Lỗi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
                        );
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (item.getItemId() == R.id.action_cart) {
            // ✅ Mở giỏ hàng khi click icon giỏ hàng
            startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
