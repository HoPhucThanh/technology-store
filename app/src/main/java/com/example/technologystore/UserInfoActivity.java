// UserInfoActivity.java
package com.example.technologystore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPhone, edtAddress, edtPassword;
    private Button btnSave, btnLogout;
    private DatabaseReference userRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("current_user_id", "");

        if (currentUserId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy người dùng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);

        loadUserInfo();

        btnSave.setOnClickListener(v -> saveChanges());

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("current_user_id");
            editor.apply();
            startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
            finish();
        });
    }

    private void loadUserInfo() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edtName.setText(snapshot.child("name").getValue(String.class));
                edtEmail.setText(snapshot.child("email").getValue(String.class));
                edtPhone.setText(snapshot.child("phone").getValue(String.class));
                edtAddress.setText(snapshot.child("address").getValue(String.class));
                edtPassword.setText(snapshot.child("password").getValue(String.class));

                edtName.setEnabled(false);
                edtEmail.setEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserInfoActivity.this, "Lỗi tải thông tin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChanges() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("phone", edtPhone.getText().toString().trim());
        updates.put("address", edtAddress.getText().toString().trim());
        updates.put("password", edtPassword.getText().toString().trim());

        userRef.updateChildren(updates).addOnSuccessListener(aVoid ->
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e ->
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show()
        );
    }
}
