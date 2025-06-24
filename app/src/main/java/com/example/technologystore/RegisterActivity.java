package com.example.technologystore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPassword, edtPhone, edtAddress;
    private Button btnRegister, btnBack;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack); // Nút quay lại



        // Sự kiện nút đăng ký
        btnRegister.setOnClickListener(v -> registerUser());

        // Sự kiện nút quay lại → MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }



        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean emailExists = false;
                int maxId = 0;

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String existingEmail = userSnap.child("email").getValue(String.class);
                    //String existingPassword = userSnap.child("password").getValue(String.class);
                    String uidStr = userSnap.child("user_id").getValue(String.class);

                    if (email.equals(existingEmail)) {
                        emailExists = true;
                        break;
                    }

                    try {
                        int uid = Integer.parseInt(uidStr); // Chuyển từ String về int để tìm max ID
                        if (uid > maxId) maxId = uid;
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu không parse được
                    }
                }

                if (emailExists) {
                    Toast.makeText(RegisterActivity.this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                } else {
                    String newUserId = String.valueOf(maxId + 1);
                    String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                    Map<String, Object> newUser = new HashMap<>();
                    newUser.put("user_id", newUserId);
                    newUser.put("name", name);
                    newUser.put("email", email);
                    newUser.put("password", password);
                    newUser.put("phone", phone);
                    newUser.put("address", address);
                    newUser.put("role", "user"); // mặc định khi đki là user
                    newUser.put("created_at", createdAt);

                    databaseRef.child(newUserId).setValue(newUser) // đẩy dữ liệu lên firebase
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(RegisterActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
