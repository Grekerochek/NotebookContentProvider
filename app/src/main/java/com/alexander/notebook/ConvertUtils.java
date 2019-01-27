package com.alexander.notebook;

import android.content.ContentValues;

public class ConvertUtils {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String TEXT = "text";
    public static final String TEXT_SIZE = "textSize";
    public static final String TEXT_COLOR = "textColor";

    public static Note convertValuesToNote(ContentValues values){
        Note note = new Note();
        note.setId(values.getAsInteger(ID));
        note.setTitle(values.getAsString(TITLE));
        note.setText(values.getAsString(TEXT));
        note.setTextSize(values.getAsInteger(TEXT_SIZE));
        note.setTextColor(values.getAsInteger(TEXT_COLOR));
        return note;
    }
}
