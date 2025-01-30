package com.example.notesync;

import android.content.Context;

import java.util.List;

public class NoteRepository {
    private final NoteDao noteDao;

    public NoteRepository(Context context) {
        NoteDatabase db = NoteDatabase.getDatabase(context);
        noteDao = db.noteDao();
    }

    public void insert(Note note) {
        // Run database operations in a background thread
        new Thread(() -> noteDao.insert(note)).start();
    }

    public void syncNotesWithServer() {
        // Get unsynced notes and send them to the server
        List<Note> unsyncedNotes = noteDao.getUnsyncedNotes();

        // Perform network request here to sync notes with the server

        // Mark notes as synced after successful sync
        for (Note note : unsyncedNotes) {
            note.setSynced(true);
            noteDao.update(note);
        }
    }
}
