package com.example.notesync;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {

    private Button addNoteButton, lockedNotesButton, remindersButton, profileButton,
            collaborationButton, settingsButton, viewNotesButton;
    private TextView clockTextView;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize views
        clockTextView = findViewById(R.id.clockTextView); // Clock text view
        addNoteButton = findViewById(R.id.addNoteButton);
        lockedNotesButton = findViewById(R.id.lockedNotesButton);
        remindersButton = findViewById(R.id.remindersButton);
        profileButton = findViewById(R.id.profileButton);
        collaborationButton = findViewById(R.id.collaborationButton);
        settingsButton = findViewById(R.id.settingsButton);
        viewNotesButton = findViewById(R.id.viewNotesButton); // New View Notes button

        // Set onClickListeners for each button
        addNoteButton.setOnClickListener(v -> openAddNote());
        lockedNotesButton.setOnClickListener(v -> openLockedNotes());
        remindersButton.setOnClickListener(v -> openReminders());
        profileButton.setOnClickListener(v -> openProfile());
        collaborationButton.setOnClickListener(v -> openCollaboration());
        settingsButton.setOnClickListener(v -> openSettings());
        viewNotesButton.setOnClickListener(v -> openViewNotes());

        // Start the clock display
        startClock();
    }

    private void startClock() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    String currentTime = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(new Date());
                    clockTextView.setText(currentTime);
                });
            }
        }, 0, 1000); // Update every second
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void openAddNote() {
        Intent intent = new Intent(MenuActivity.this, AddNoteActivity.class);
        startActivity(intent);
    }

    private void openLockedNotes() {
        Intent intent = new Intent(MenuActivity.this, LockedNotesActivity.class);
        startActivity(intent);
    }

    private void openReminders() {
        Intent intent = new Intent(MenuActivity.this, RemindersActivity.class);
        startActivity(intent);
    }

    private void openProfile() {
        Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void openCollaboration() {
        Intent intent = new Intent(MenuActivity.this, CollaborationActivity.class);
        startActivity(intent);
    }

    private void openSettings() {
        Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openViewNotes() {
        Intent intent = new Intent(MenuActivity.this, ViewNotesActivity.class);
        startActivity(intent);
    }
}
