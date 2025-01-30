package com.example.notesync;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private TextView emailTextView;
    private EditText usernameEditText, passwordEditText;
    private Button updateUsernameButton, updatePasswordButton, logoutButton, backToMenuButton; // Add backToMenuButton

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailTextView = findViewById(R.id.textViewEmail);
        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        updateUsernameButton = findViewById(R.id.buttonUpdateUsername);
        updatePasswordButton = findViewById(R.id.buttonUpdatePassword);
        logoutButton = findViewById(R.id.buttonLogout);
        backToMenuButton = findViewById(R.id.backToMenuButton); // Initialize backToMenuButton

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Display the current user's email
        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail());

            // Display current username if available
            if (currentUser.getDisplayName() != null) {
                usernameEditText.setText(currentUser.getDisplayName());
            }
        }

        // Update username
        updateUsernameButton.setOnClickListener(v -> {
            String newUsername = usernameEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(newUsername)) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newUsername)
                        .build();

                if (currentUser != null) {
                    currentUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ProfileActivity.this, "Username updated!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        // Update password
        updatePasswordButton.setOnClickListener(v -> {
            String newPassword = passwordEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(newPassword) && newPassword.length() >= 6) {
                if (currentUser != null) {
                    currentUser.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    passwordEditText.setText(""); // Clear password field
                                    Toast.makeText(ProfileActivity.this, "Password updated!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else {
                Toast.makeText(ProfileActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            }
        });

        // Logout button
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        // Back to Menu button
        backToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MenuActivity.class); // Adjust this line to your main menu activity
            startActivity(intent);
            finish(); // Optional: close the ProfileActivity
        });
    }
}
