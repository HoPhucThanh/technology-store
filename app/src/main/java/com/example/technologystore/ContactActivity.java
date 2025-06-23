package com.example.technologystore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    private Button btnFacebook, btnPhone, btnEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Button btnBack = findViewById(R.id.btnBackContact);
        btnBack.setOnClickListener(v -> finish());


        btnFacebook = findViewById(R.id.btnFacebook);
        btnPhone = findViewById(R.id.btnPhone);
        btnEmail = findViewById(R.id.btnEmail);

        // Mở Facebook
        btnFacebook.setOnClickListener(v -> {
            String facebookUrl = "https://www.facebook.com/ho.thanh.792189";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            startActivity(intent);
        });

        // Gọi điện
        btnPhone.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0932467058"));
            startActivity(intent);
        });

        // Gửi email
        btnEmail.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:hophucthanh8a@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Phản hồi từ ứng dụng");
            startActivity(Intent.createChooser(intent, "Chọn ứng dụng gửi email"));
        });
    }
}
