package com.example.notesync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class LockedNotesActivity extends AppCompatActivity {

    private ListView lockedNotesListView;
    private List<String> lockedNotes;
    private List<Long> lockedNoteTimestamps;
    private SharedPreferences sharedPreferences;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private long currentTimestamp; // To keep track of the current note being unlocked

    //code attribution
    //(No date) YouTube. Available at: https://m.youtube.com/watch?v=_dCRQ9wta-I&pp=ygUoSG93IHRvIGZvIGEgYmlvZW50cmljcyBvbiBhbmRyaW9kIHN0dWlkbw%3D%3D (Accessed: 04 November 2024).
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_notes);

        lockedNotesListView = findViewById(R.id.lockedNotesListView);
        sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);

        loadLockedNotes();
        setupBiometricAuthentication();

        // Back to Menu Button
        Button backToMenuButton = findViewById(R.id.backToMenuButton);
        backToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(LockedNotesActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });

        // Set up the ListView item click listener
        lockedNotesListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            currentTimestamp = lockedNoteTimestamps.get(position);
            String lockType = sharedPreferences.getString("note_lock_type_" + currentTimestamp, null);

            if ("fingerprint".equals(lockType)) {
                if (isBiometricSetup()) {
                    showBiometricPrompt();
                } else {
                    Toast.makeText(this, "Please set up biometrics in settings", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    startActivity(intent);
                }
            } else if ("password".equals(lockType)) {
                showPasswordPrompt();
            }
        });
    }

    private void loadLockedNotes() {
        lockedNotes = new ArrayList<>();
        lockedNoteTimestamps = new ArrayList<>();
        for (String key : sharedPreferences.getAll().keySet()) {
            if (key.startsWith("note_locked_") && sharedPreferences.getBoolean(key, false)) {
                String noteTitleKey = key.replace("note_locked_", "note_title_");
                String noteTitle = sharedPreferences.getString(noteTitleKey, "Locked Note");
                long timestamp = Long.parseLong(key.replace("note_locked_", ""));
                lockedNotes.add(noteTitle);
                lockedNoteTimestamps.add(timestamp);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lockedNotes);
        lockedNotesListView.setAdapter(adapter);
    }

    private void setupBiometricAuthentication() {
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                unlockNoteContent(currentTimestamp); // Pass the timestamp to unlockNoteContent
            }

            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Note")
                .setSubtitle("Use your fingerprint to unlock this note")
                .setNegativeButtonText("Cancel")
                .build();
    }

    private boolean isBiometricSetup() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG);
        return canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS;
    }

    private void showBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo);
    }

    private void showPasswordPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Password");

        final EditText input = new EditText(this);
        input.setHint("Password");
        builder.setView(input);

        builder.setPositiveButton("Unlock", (dialog, which) -> {
            String enteredPassword = input.getText().toString().trim();
            String savedPassword = sharedPreferences.getString("note_password_" + currentTimestamp, null);

            if (enteredPassword.equals(savedPassword)) {
                unlockNoteContent(currentTimestamp);
            } else {
                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void unlockNoteContent(long timestamp) {
        // Navigate to NoteDetailActivity with the unlocked note timestamp
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra("note_timestamp", timestamp);
        startActivity(intent);



    }
}
