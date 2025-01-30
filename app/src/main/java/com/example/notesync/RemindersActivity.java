package com.example.notesync;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RemindersActivity extends AppCompatActivity {

    private ListView remindersListView;
    private Button addReminderButton;
    private ArrayList<String> reminderTitles; // Stores reminder titles
    private ArrayList<Reminder> reminders;    // Stores Reminder objects with title, description, and date/time
    private ArrayAdapter<String> remindersAdapter;

    private static final int ADD_REMINDER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        remindersListView = findViewById(R.id.remindersListView);
        addReminderButton = findViewById(R.id.addReminderButton);

        // Initialize the list of reminder titles and Reminder objects
        reminderTitles = new ArrayList<>();
        reminders = new ArrayList<>();
        remindersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminderTitles);
        remindersListView.setAdapter(remindersAdapter);

        // Set listener for the Add Reminder button
        addReminderButton.setOnClickListener(v -> {
            Intent intent = new Intent(RemindersActivity.this, AddReminderActivity.class);
            startActivityForResult(intent, ADD_REMINDER_REQUEST_CODE);
        });

        // Set listener for clicking on reminder titles in the ListView
        remindersListView.setOnItemClickListener((parent, view, position, id) -> {
            // Open ReminderDetailActivity and pass the reminder details
            Reminder clickedReminder = reminders.get(position);
            Intent intent = new Intent(RemindersActivity.this, ReminderDetailActivity.class);
            intent.putExtra("title", clickedReminder.getTitle());
            intent.putExtra("description", clickedReminder.getDescription());
            intent.putExtra("dateTime", clickedReminder.getDateTime());
            startActivity(intent);
        });
    }

    // This method will be called when the user returns from AddReminderActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REMINDER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve the new reminder details from the result
            String newReminderTitle = data.getStringExtra("reminderTitle");
            String newReminderDescription = data.getStringExtra("reminderDescription");
            String newReminderDateTime = data.getStringExtra("reminderDateTime");

            if (newReminderTitle != null && newReminderDescription != null && newReminderDateTime != null) {
                // Add the new reminder to the list
                reminderTitles.add(newReminderTitle); // Display title only in the ListView
                reminders.add(new Reminder(newReminderTitle, newReminderDescription, newReminderDateTime)); // Store full details

                remindersAdapter.notifyDataSetChanged(); // Refresh the list to show the new reminder
                Toast.makeText(this, "Reminder added!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
