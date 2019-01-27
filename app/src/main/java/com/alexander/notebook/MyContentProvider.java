package com.alexander.notebook;

import android.arch.persistence.room.Room;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.alexander.notebook";
    private static final String NOTES_TABLE = "notes";

    public static final int NOTES = 1;
    public static final int NOTE_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {
        uriMatcher.addURI(AUTHORITY, NOTES_TABLE, NOTES);
        uriMatcher.addURI(AUTHORITY, NOTES_TABLE + "/#", NOTE_ID);
    }


    private NoteDAO dao;
    private NoteDatabase noteDatabase;

    @Override
    public boolean onCreate() {
        noteDatabase = Room.databaseBuilder(getContext(), NoteDatabase.class, "my_database")
                .build();
        dao = new DAOImplementation(noteDatabase);
        return noteDatabase != null;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        int uriType = uriMatcher.match(uri);

        switch (uriType){
            case NOTE_ID:
                cursor = dao.getCursorNote(ContentUris.parseId(uri));
                break;
            case NOTES:
                cursor = dao.getCursorNotes();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        int uriType = uriMatcher.match(uri);
        long id = 0;

        switch (uriType){
            case NOTES:
                id = dao.insert(ConvertUtils.convertValuesToNote(values));
            break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(NOTES_TABLE + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int id = Integer.parseInt(uri.getLastPathSegment());
        dao.delete(dao.getNote(id));

        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        int id;
        Log.d("PROVERKAN", String.valueOf(uriType));
        switch (uriType){
            case NOTES:
                id = dao.update(ConvertUtils.convertValuesToNote(values));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }
}
