package com.example.notesync;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity); // Layout with your logo

        // Delay for a few seconds and then start the RegisterActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish(); // Close SplashActivity so it's not in the back stack
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}