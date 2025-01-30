package com.example.notesync;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReminderDetailActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, dateTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dateTimeTextView = findViewById(R.id.dateTimeTextView);

        // Get the reminder details passed from RemindersActivity
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String dateTime = getIntent().getStringExtra("dateTime");

        // Display the reminder details
        titleTextView.setText("Title: " + title);
        descriptionTextView.setText("Description: " + description);
        dateTimeTextView.setText("Date & Time: " + dateTime);
    }
}
