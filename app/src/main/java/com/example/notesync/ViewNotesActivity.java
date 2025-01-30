package com.example.notesync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewNotesActivity extends AppCompatActivity implements NetworkChangeReceiver.SyncListener {

    private ListView notesListView;
    private List<String> noteTitles;
    private List<Long> noteTimestamps;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> adapter;
    private Button backToMenuButton;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notes);

        notesListView = findViewById(R.id.notesListView);
        backToMenuButton = findViewById(R.id.backToMenuButton);
        noteTitles = new ArrayList<>();
        noteTimestamps = new ArrayList<>();
        sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);

        loadUnlockedNotes();  // Load only unlocked notes
        setupListView();

        // Back to Menu button functionality
        backToMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewNotesActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });

        // Register network change receiver
        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    private void loadUnlockedNotes() {
        noteTitles.clear();
        noteTimestamps.clear();

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("note_title_")) {
                long timestamp = Long.parseLong(key.replace("note_title_", ""));

                // Check if the note is locked
                boolean isLocked = sharedPreferences.getBoolean("note_locked_" + timestamp, false);
                if (!isLocked) {  // Only add unlocked notes
                    String title = (String) entry.getValue();
                    noteTitles.add(title);
                    noteTimestamps.add(timestamp);
                }
            }
        }
    }

    private void setupListView() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteTitles);
        notesListView.setAdapter(adapter);

        notesListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            long selectedTimestamp = noteTimestamps.get(position);
            viewNoteContent(selectedTimestamp);
        });

        notesListView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedTitle = noteTitles.get(position);
            long selectedTimestamp = noteTimestamps.get(position);
            showDeleteConfirmationDialog(selectedTitle, selectedTimestamp, position);
            return true;
        });
    }

    private void viewNoteContent(long timestamp) {
        String noteTitle = sharedPreferences.getString("note_title_" + timestamp, null);
        String noteContent = sharedPreferences.getString("note_content_" + timestamp, null);

        if (noteTitle != null && noteContent != null) {
            Intent intent = new Intent(ViewNotesActivity.this, NoteDetailActivity.class);
            intent.putExtra("note_timestamp", timestamp);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error loading note", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDeleteConfirmationDialog(String title, long timestamp, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete the note: " + title + "?")
                .setPositiveButton("Yes", (dialog, which) -> deleteNote(timestamp, position))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteNote(long timestamp, int position) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("note_title_" + timestamp);
        editor.remove("note_content_" + timestamp);
        editor.remove("note_locked_" + timestamp);  // Remove lock status if present
        editor.apply();

        Toast.makeText(this, "Note deleted successfully!", Toast.LENGTH_SHORT).show();

        noteTitles.remove(position);
        noteTimestamps.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNetworkConnected() {
        // Sync notes with the server
        syncOfflineNotes();

        // Refresh the notes list
        loadUnlockedNotes();
        adapter.notifyDataSetChanged();
    }

    private void syncOfflineNotes() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();

            // Only process unsynced notes
            if (key.startsWith("note_title_") && !sharedPreferences.getBoolean("note_synced_" + key, false)) {
                long timestamp = Long.parseLong(key.replace("note_title_", ""));
                String title = (String) entry.getValue();
                String content = sharedPreferences.getString("note_content_" + timestamp, "");

                // Assume a method to sync to your backend or database
                boolean success = uploadNoteToServer(timestamp, title, content);

                if (success) {
                    editor.putBoolean("note_synced_" + timestamp, true); // Mark as synced
                }
            }
        }
        editor.apply();
    }

    private boolean uploadNoteToServer(long timestamp, String title, String content) {
        // Implement the server upload logic here
        // Return true if successful; otherwise, return false
        return true; // Placeholder, implement your actual logic here
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver to prevent memory leaks
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}
