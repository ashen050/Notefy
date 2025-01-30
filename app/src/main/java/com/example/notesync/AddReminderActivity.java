package com.example.notesync;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity {

    private EditText reminderTitleEditText, reminderDescriptionEditText;
    private Button dateTimePickerButton, saveReminderButton, backToMenuButton; // Declare the new button
    private Calendar reminderCalendar;
    private String selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        reminderTitleEditText = findViewById(R.id.reminderTitleEditText);
        reminderDescriptionEditText = findViewById(R.id.reminderDescriptionEditText);
        dateTimePickerButton = findViewById(R.id.dateTimePickerButton);
        saveReminderButton = findViewById(R.id.saveReminderButton);
        backToMenuButton = findViewById(R.id.backToMenuButton); // Initialize the new button

        reminderCalendar = Calendar.getInstance();

        // Set listener for Date and Time Picker
        dateTimePickerButton.setOnClickListener(v -> promptDatePicker());

        // Set listener for Save Reminder Button
        saveReminderButton.setOnClickListener(v -> saveReminder());

        // Set listener for Back to Menu Button

        backToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddReminderActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });// Close the activity to go back
    }



    private void promptDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    reminderCalendar.set(Calendar.YEAR, year);
                    reminderCalendar.set(Calendar.MONTH, month);
                    reminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    promptTimePicker();
                },
                reminderCalendar.get(Calendar.YEAR),
                reminderCalendar.get(Calendar.MONTH),
                reminderCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void promptTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    reminderCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    reminderCalendar.set(Calendar.MINUTE, minute);
                    reminderCalendar.set(Calendar.SECOND, 0);

                    selectedDateTime = String.format("Date: %02d/%02d/%d Time: %02d:%02d",
                            reminderCalendar.get(Calendar.DAY_OF_MONTH),
                            reminderCalendar.get(Calendar.MONTH) + 1,
                            reminderCalendar.get(Calendar.YEAR),
                            reminderCalendar.get(Calendar.HOUR_OF_DAY),
                            reminderCalendar.get(Calendar.MINUTE));

                    Toast.makeText(this, "Selected: " + selectedDateTime, Toast.LENGTH_SHORT).show();
                },
                reminderCalendar.get(Calendar.HOUR_OF_DAY),
                reminderCalendar.get(Calendar.MINUTE),
                true);

        timePickerDialog.show();
    }

    private void saveReminder() {
        String reminderTitle = reminderTitleEditText.getText().toString().trim();
        String reminderDescription = reminderDescriptionEditText.getText().toString().trim();

        if (reminderTitle.isEmpty() || reminderDescription.isEmpty() || selectedDateTime == null) {
            Toast.makeText(this, "Please fill all fields and select a date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Send the reminder details back to RemindersActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("reminderTitle", reminderTitle);
        resultIntent.putExtra("reminderDescription", reminderDescription);
        resultIntent.putExtra("reminderDateTime", selectedDateTime);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
