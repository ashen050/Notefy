package com.example.notesync;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Query("SELECT * FROM notes WHERE isSynced = 0")
    List<Note> getUnsyncedNotes();  // Get notes that haven't been synced yet

    @Query("SELECT * FROM notes")
    List<Note> getAllNotes();

    @Query("DELETE FROM notes WHERE id = :noteId")
    void delete(int noteId);
}
