package com.alexander.notebook;

import android.database.Cursor;

import java.util.List;

public class DAOImplementation implements NoteDAO {

    private final NoteDatabase noteDatabase;

    public DAOImplementation(NoteDatabase noteDatabase){
        this.noteDatabase = noteDatabase;
    }

    @Override
    public Note getNote(long id) {
        return noteDatabase.getNoteDAO().getNote(id);
    }

    @Override
    public List<Note> getNotes() {
        return noteDatabase.getNoteDAO().getNotes();
    }

    @Override
    public Cursor getCursorNote(long id){
        return noteDatabase.getNoteDAO().getCursorNote(id);
    }

    @Override
    public Cursor getCursorNotes() {
        return noteDatabase.getNoteDAO().getCursorNotes();
    }

    @Override
    public long insert(Note note) {
        return noteDatabase.getNoteDAO().insert(note);
    }

    @Override
    public void delete(Note note) {
        noteDatabase.getNoteDAO().delete(note);
    }

    @Override
    public int update(Note note) {
        return noteDatabase.getNoteDAO().update(note);
    }
}
