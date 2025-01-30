package com.example.notesync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {
    private final Context context;
    private List<Note> notes;
    private List<String> titles;
    private List<String> contents;

    // Constructor for Note objects
    public NoteAdapter(Context context, List<Note> notes) {
        super(context, 0, notes);
        this.context = context;
        this.notes = notes;
    }

    // Constructor for separate title and content lists
    public NoteAdapter(Context context, List<String> titles, List<String> contents) {
        super(context, 0);
        this.context = context;
        this.titles = titles;
        this.contents = contents;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.noteTitleTextView);
        TextView contentTextView = convertView.findViewById(R.id.noteContentTextView);

        if (notes != null && !notes.isEmpty()) {
            // Handling Note objects
            Note note = notes.get(position);
            if (note != null) {
                titleTextView.setText(note.getTitle());
                contentTextView.setText(note.getContent());
            }
        } else if (titles != null && contents != null) {
            // Handling separate title and content lists
            titleTextView.setText(titles.get(position));
            contentTextView.setText(contents.get(position));
        }

        return convertView;
    }
}
