package com.example.notesync;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;  // Unique ID for each note

    private String title;
    private String content;
    private boolean isSynced;  // Indicates if the note has been synced with the server

    // Constructor
    public Note(String title, String content, boolean isSynced) {
        this.title = title;
        this.content = content;
        this.isSynced = isSynced;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
