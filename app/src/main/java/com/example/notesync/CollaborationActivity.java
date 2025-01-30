package com.example.notesync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CollaborationActivity extends AppCompatActivity {

    private EditText addCollaboratorEditText;
    private Button addCollaboratorButton, addNoteButton, removeSelectedNoteButton, backToMenuButton; // Added backToMenuButton
    private ListView collaboratorsListView;
    private TextView selectedNoteTextView;

    private ArrayList<String> collaborators;
    private ArrayAdapter<String> collaboratorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaboration);

        addCollaboratorEditText = findViewById(R.id.addCollaboratorEditText);
        addCollaboratorButton = findViewById(R.id.addCollaboratorButton);
        addNoteButton = findViewById(R.id.addNoteButton);
        removeSelectedNoteButton = findViewById(R.id.removeSelectedNoteButton);
        backToMenuButton = findViewById(R.id.backToMenuButton); // Initialize the back to menu button
        collaboratorsListView = findViewById(R.id.collaboratorsListView);
        selectedNoteTextView = findViewById(R.id.selectedNoteTextView);

        // Initialize the list of collaborators
        collaborators = new ArrayList<>();

        // Set up the adapter to display collaborators
        collaboratorsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, collaborators);
        collaboratorsListView.setAdapter(collaboratorsAdapter);

        // Set onClickListener to add collaborators
        addCollaboratorButton.setOnClickListener(v -> addCollaborator());

        // Set onClickListener to add a note for collaboration
        addNoteButton.setOnClickListener(v -> showNoteSelectionDialog());

        // Set long-click listener to delete a collaborator
        collaboratorsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            confirmCollaboratorDeletion(position);
            return true;
        });

        // Set onClickListener to remove the selected note
        removeSelectedNoteButton.setOnClickListener(v -> removeSelectedNote());

        // Set onClickListener for Back to Menu button
        backToMenuButton.setOnClickListener(v -> finish()); // Close the activity to go back
    }

    private void addCollaborator() {
        String collaborator = addCollaboratorEditText.getText().toString().trim();

        if (TextUtils.isEmpty(collaborator)) {
            Toast.makeText(this, "Please enter a username or email", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add collaborator to the list and notify the adapter
        collaborators.add(collaborator);
        collaboratorsAdapter.notifyDataSetChanged();
        addCollaboratorEditText.setText("");

        // In a real app, you would also want to handle sending a request to the server/database
        // to update the collaborators of the specific note.
        Toast.makeText(this, "Collaborator added!", Toast.LENGTH_SHORT).show();
    }

    private void showNoteSelectionDialog() {
        // Retrieve the list of saved notes from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Notes", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        List<String> noteTitles = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("note_title_")) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    String title = (String) value;
                    noteTitles.add(title);
                } else {
                    Toast.makeText(this, "Invalid note format in storage", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (noteTitles.isEmpty()) {
            Toast.makeText(this, "No notes available for collaboration", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an AlertDialog to show the notes
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Note for Collaboration");

        // Convert the noteTitles list to an array
        String[] noteTitlesArray = noteTitles.toArray(new String[0]);

        builder.setItems(noteTitlesArray, (dialog, which) -> {
            String selectedNoteTitle = noteTitlesArray[which];
            // Update the TextView with the selected note
            selectedNoteTextView.setText("Selected Note: " + selectedNoteTitle);
            removeSelectedNoteButton.setVisibility(View.VISIBLE); // Show the "Remove Selected Note" button
            Toast.makeText(CollaborationActivity.this, "Selected Note: " + selectedNoteTitle, Toast.LENGTH_SHORT).show();

            // In a real app, you would now link this note with the collaborators or database
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void confirmCollaboratorDeletion(int position) {
        String collaborator = collaborators.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Delete Collaborator")
                .setMessage("Are you sure you want to delete collaborator: " + collaborator + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Remove the collaborator and update the list
                    collaborators.remove(position);
                    collaboratorsAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Collaborator removed!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void removeSelectedNote() {
        // Reset the selected note display and hide the "Remove Selected Note" button
        selectedNoteTextView.setText("Selected Note: None");
        removeSelectedNoteButton.setVisibility(View.GONE);
        Toast.makeText(this, "Selected note removed!", Toast.LENGTH_SHORT).show();
    }
}
