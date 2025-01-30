package com.example.notesync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText noteTitleEditText, noteContentEditText;
    private Button saveNoteButton, imageButton, backToMenuButton;
    private ImageView imageView;
    private Switch lockWithPasswordSwitch, lockWithFingerprintSwitch;
    private Uri imageUri;
    private String notePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);

        // Initialize UI elements
        noteTitleEditText = findViewById(R.id.editTextNoteTitle);
        noteContentEditText = findViewById(R.id.editTextNoteContent);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        lockWithPasswordSwitch = findViewById(R.id.lockWithPasswordSwitch);
        lockWithFingerprintSwitch = findViewById(R.id.lockWithFingerprintSwitch);
        imageButton = findViewById(R.id.imageButton);
        imageView = findViewById(R.id.imageView);
        backToMenuButton = findViewById(R.id.backToMenuButton);

        // Set up switches to allow only one lock option at a time
        lockWithPasswordSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) lockWithFingerprintSwitch.setChecked(false);
        });

        lockWithFingerprintSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) lockWithPasswordSwitch.setChecked(false);
        });

        // Set click listener for the save button
        saveNoteButton.setOnClickListener(v -> saveNote());

        // Set click listener for the image button
        imageButton.setOnClickListener(v -> addImage());

        // Set click listener for back to menu button
        backToMenuButton.setOnClickListener(v -> finish());
    }

    private void saveNote() {
        String noteTitle = noteTitleEditText.getText().toString().trim();
        String noteContent = noteContentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(noteTitle) || TextUtils.isEmpty(noteContent)) {
            Toast.makeText(this, "Please fill in both title and content", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long timestamp = System.currentTimeMillis();

        editor.putString("note_title_" + timestamp, noteTitle);
        editor.putString("note_content_" + timestamp, noteContent);

        // Check for lock type and save accordingly
        if (lockWithPasswordSwitch.isChecked()) {
            // Prompt to set a password if "Lock with Password" is selected
            showPasswordPrompt(editor, timestamp);
        } else if (lockWithFingerprintSwitch.isChecked()) {
            // Set fingerprint lock if selected
            editor.putBoolean("note_locked_" + timestamp, true);
            editor.putString("note_lock_type_" + timestamp, "fingerprint");
            editor.apply();
            Toast.makeText(this, "Note locked with fingerprint", Toast.LENGTH_SHORT).show();
            navigateToNoteDetail(timestamp);
        } else {
            // Save without lock
            editor.apply();
            Toast.makeText(this, "Note saved without lock", Toast.LENGTH_SHORT).show();
            navigateToNoteDetail(timestamp);
        }
    }

    private void showPasswordPrompt(SharedPreferences.Editor editor, long timestamp) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set a Password to Lock Note");

        final EditText input = new EditText(this);
        input.setHint("Enter password");
        builder.setView(input);

        builder.setPositiveButton("Set Password", (dialog, which) -> {
            notePassword = input.getText().toString().trim();
            if (TextUtils.isEmpty(notePassword)) {
                Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                // Save the password in SharedPreferences with lock status
                editor.putString("note_password_" + timestamp, notePassword);
                editor.putBoolean("note_locked_" + timestamp, true);
                editor.putString("note_lock_type_" + timestamp, "password");
                editor.apply();
                Toast.makeText(this, "Note locked with password", Toast.LENGTH_SHORT).show();
                navigateToNoteDetail(timestamp);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void addImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            if (imageUri != null) {
                imageView.setImageURI(imageUri);
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void navigateToNoteDetail(long timestamp) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra("note_timestamp", timestamp);
        startActivity(intent);
        finish();
}
}
