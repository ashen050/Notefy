package com.example.notesync;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NoteDetailActivity extends AppCompatActivity {

    private EditText noteTitleEditText, noteContentEditText;
    private Button deleteNoteButton, saveNoteButton, backToMenuButton;
    private SharedPreferences sharedPreferences;
    private long timestamp;
    private boolean isModified = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        noteTitleEditText = findViewById(R.id.noteTitleEditText);
        noteContentEditText = findViewById(R.id.noteContentEditText);
        deleteNoteButton = findViewById(R.id.deleteNoteButton);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        backToMenuButton = findViewById(R.id.backToMenuButton);

        sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);
        timestamp = getIntent().getLongExtra("note_timestamp", -1);

        if (timestamp != -1) {
            String noteTitle = sharedPreferences.getString("note_title_" + timestamp, "");
            String noteContent = sharedPreferences.getString("note_content_" + timestamp, "");

            noteTitleEditText.setText(noteTitle);
            noteContentEditText.setText(noteContent);
        } else {
            Toast.makeText(this, "Error loading note", Toast.LENGTH_SHORT).show();
            finish();
        }

        addTextWatchers();

        saveNoteButton.setOnClickListener(v -> saveNote());
        deleteNoteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
        backToMenuButton.setOnClickListener(v -> navigateBackToMenu());
    }

    private void addTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isModified = true;
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        noteTitleEditText.addTextChangedListener(textWatcher);
        noteContentEditText.addTextChangedListener(textWatcher);
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> deleteNote())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteNote() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("note_title_" + timestamp);
        editor.remove("note_content_" + timestamp);
        editor.apply();

        Toast.makeText(this, "Note deleted successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveNote() {
        String updatedTitle = noteTitleEditText.getText().toString().trim();
        String updatedContent = noteContentEditText.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("note_title_" + timestamp, updatedTitle);
        editor.putString("note_content_" + timestamp, updatedContent);
        editor.apply();

        isModified = false;
        Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isModified) {
            new AlertDialog.Builder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("You have unsaved changes. Do you want to save before exiting?")
                    .setPositiveButton("Save", (dialog, which) -> saveNote())
                    .setNegativeButton("Discard", (dialog, which) -> finish())
                    .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private void navigateBackToMenu() {
        if (isModified) {
            new AlertDialog.Builder(this)
                    .setTitle("Unsaved Changes")
                    .setMessage("You have unsaved changes. Do you want to save before exiting?")
                    .setPositiveButton("Save", (dialog, which) -> saveNote())
                    .setNegativeButton("Discard", (dialog, which) -> {
                        startActivity(new Intent(NoteDetailActivity.this, MenuActivity.class));
                        finish();
                    })
                    .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            startActivity(new Intent(NoteDetailActivity.this, MenuActivity.class));
            finish();
        }
    }
}
