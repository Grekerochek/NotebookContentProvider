package com.alexander.notebook;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

@Dao
public interface NoteDAO {

    @Query("select * from notes where id = :id")
    Note getNote(long id);

    @Query("select * from notes")
    List<Note> getNotes();

    @Query("select * from notes where id = :id")
    Cursor getCursorNote(long id);

    @Query("select * from notes")
    Cursor getCursorNotes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Note note);

    @Update
    int update(Note note);

    @Delete
    void delete(Note note);
}
