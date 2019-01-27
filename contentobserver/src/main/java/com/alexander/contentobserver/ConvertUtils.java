package com.alexander.contentobserver;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    public static final String TEXT_SIZE = "textSize";
    public static final String TEXT_COLOR = "textColor";


    public static ContentValues convertNoteToValues(Note note){
        ContentValues values = new ContentValues();
        values.put(ID, note.getId());
        values.put(TITLE, note.getTitle());
        values.put(TEXT, note.getText());
        values.put(TEXT_SIZE, note.getTextSize());
        values.put(TEXT_COLOR, note.getTextColor());
        return values;
    }

    public static List<Note> convertCursorToNotes(Cursor cursor){
        List<Note> notes = new ArrayList<>();
        while (cursor.moveToNext()){
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            note.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
            note.setTextSize(cursor.getInt(cursor.getColumnIndex(TEXT_SIZE)));
            note.setTextColor(cursor.getInt(cursor.getColumnIndex(TEXT_COLOR)));
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    public static Note convertCursorToNote(Cursor cursor, int position){
        Note note = new Note();
        if (cursor.moveToPosition(position))
            note.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            note.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            note.setText(cursor.getString(cursor.getColumnIndex(TEXT)));
            note.setTextSize(cursor.getInt(cursor.getColumnIndex(TEXT_SIZE)));
            note.setTextColor(cursor.getInt(cursor.getColumnIndex(TEXT_COLOR)));
        return note;
    }
}
