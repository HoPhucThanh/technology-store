package com.example.technologystore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.technologystore.adapters.AdapterBanner;
import com.example.technologystore.adapters.AdapterProduct;
import com.example.technologystore.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private AdapterProduct adapterProduct;
    private List<Product> productList = new ArrayList<>();
    private DatabaseReference dbRef;

    private ViewPager viewPagerBanner;
    private List<Integer> bannerList = new ArrayList<>();
    private AdapterBanner adapterBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                drawerLayout.closeDrawers();
            } else if (id == R.id.nav_phone) {
                startActivity(new Intent(HomeActivity.this, PhoneActivity.class));
            } else if (id == R.id.nav_laptop) {
                startActivity(new Intent(HomeActivity.this, LaptopActivity.class));
            } else if (id == R.id.nav_contact) {
                startActivity(new Intent(HomeActivity.this, ContactActivity.class));
            } else if (id == R.id.nav_info) {
                startActivity(new Intent(HomeActivity.this, UserInfoActivity.class));
            } else if (id == R.id.nav_orders) {
                // 👉 Mở trang đơn hàng
                startActivity(new Intent(HomeActivity.this, MyOrdersActivity.class));
            }

            return true;
        });



        // Banner ViewPager
        viewPagerBanner = findViewById(R.id.viewPagerBanner);
        bannerList.add(R.drawable.quangcao1);
        bannerList.add(R.drawable.quangcao3);
        bannerList.add(R.drawable.quangcao4);
        adapterBanner = new AdapterBanner(this, bannerList);
        viewPagerBanner.setAdapter(adapterBanner);

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == bannerList.size()) {
                    currentPage = 0;
                }
                viewPagerBanner.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.post(update);

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapterProduct = new AdapterProduct(this, productList);
        recyclerView.setAdapter(adapterProduct);

        // Load sản phẩm
        loadProducts();
    }

    private void loadProducts() {
        dbRef = FirebaseDatabase.getInstance().getReference("products");

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();

                List<Product> phoneList = new ArrayList<>();
                List<Product> laptopList = new ArrayList<>();

                for (DataSnapshot item : snapshot.getChildren()) {
                    Object rawCat = item.child("category_id").getValue();
                    String cat = null;

                    if (rawCat instanceof Long) {
                        cat = String.valueOf(rawCat);
                    } else if (rawCat instanceof String) {
                        cat = (String) rawCat;
                    }

                    Product product = item.getValue(Product.class);
                    if (product != null && cat != null) {
                        if (cat.equals("1")) {
                            phoneList.add(product);
                        } else if (cat.equals("2")) {
                            laptopList.add(product);
                        }
                    }
                }

                // Log kiểm tra
                Log.d("FirebaseDebug", "Phone: " + phoneList.size() + ", Laptop: " + laptopList.size());

                // Lấy tối đa 6 mỗi loại
                int maxCount = 6;
                int phoneCount = Math.min(maxCount, phoneList.size());
                int laptopCount = Math.min(maxCount, laptopList.size());

                // Gộp 2 loại xen kẽ
                for (int i = 0; i < Math.max(phoneCount, laptopCount); i++) {
                    if (i < phoneCount) productList.add(phoneList.get(i));
                    if (i < laptopCount) productList.add(laptopList.get(i));
                }

                Log.d("FirebaseDebug", "Tổng sản phẩm sau gộp: " + productList.size());
                adapterProduct.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDebug", "Load sản phẩm lỗi: " + error.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_cart) {
            // Mở CartActivity
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
