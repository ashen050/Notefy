package com.example.notesync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;
import java.util.List;

public class SyncService extends Service {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            if (isConnected()) {
                syncNotes();
            } else {
                handler.post(() -> Toast.makeText(this, "No internet connection. Notes will sync when connected.", Toast.LENGTH_SHORT).show());
            }
        }).start();
        return START_NOT_STICKY;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void syncNotes() {
        NoteDatabase db = NoteDatabase.getDatabase(getApplicationContext());
        List<Note> unsyncedNotes = db.noteDao().getUnsyncedNotes();

        for (Note note : unsyncedNotes) {
            boolean success = syncNoteToServer(note);
            if (success) {
                note.setSynced(true); // Mark note as synced
                db.noteDao().update(note);
            }
        }

        // Post Toast on the main thread
        handler.post(() -> Toast.makeText(this, "Notes synchronized!", Toast.LENGTH_SHORT).show());
    }

    private boolean syncNoteToServer(Note note) {
        // Simulate server API call to sync the note
        return true; // Assume successful sync
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
