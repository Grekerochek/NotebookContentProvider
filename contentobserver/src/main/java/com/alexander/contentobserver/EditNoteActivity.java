package com.alexander.contentobserver;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;



public class EditNoteActivity extends AppCompatActivity {


    private static final String VALUE = "value";
    private static final String EMPTY = "";
    private static final int [] COLORS = {R.color.colorBlue, R.color.colorGreen, R.color.colorGrey,
                                        R.color.colorLightBlue, R.color.colorYellow, R.color.colorRed};

    private static String ID = "id";
    private static String POSITION = "position";

    private Note note;
    private Note newNote;

    private int id;
    private int position;

    private EditText title;
    private EditText text;
    private FloatingActionButton button;
    private ProgressBar load;


    private ContentObserver contentObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        initViews();
        initListeners();
        initContentObserver();
        id = getIntent().getIntExtra(VALUE, 0);
        position = getIntent().getIntExtra(POSITION, 0);
        new MyAsyncGet(id, position).execute();
    }

    private void initViews(){

        title = findViewById(R.id.title);
        text = findViewById(R.id.note);
        button = findViewById(R.id.button);
        load = findViewById(R.id.load);
    }

    private void initListeners(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText().toString().equals(EMPTY) || text.getText().toString().equals(EMPTY))
                    showMessage();
                else {
                    newNote = new Note();
                    newNote.setId(note.getId());
                    newNote.setTextColor(note.getTextColor());
                    newNote.setTextSize(note.getTextSize());
                    newNote.setTitle(title.getText().toString());
                    newNote.setText(text.getText().toString());
                    new MyAsyncWrite().execute();
                }
            }
        });
    }

    private void showMessage(){
        Toast.makeText(this, R.string.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(ListNotesActivity.CONTENT_URI, true, contentObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    private void initContentObserver(){
        contentObserver = new MyObserver(new Handler(Looper.getMainLooper()));
    }

    public static final Intent newIntent(Context context, int id, int position){
        Intent intent = new Intent(context, EditNoteActivity.class);
        intent.putExtra(VALUE, id);
        intent.putExtra(POSITION, position);
        return intent;
    }

    private class MyAsyncGet extends AsyncTask<Void, Void, Void> {

        private int id;
        private int position;

        MyAsyncGet (int id, int position){
            this.id = id;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Cursor cursor = getContentResolver().query(ListNotesActivity.CONTENT_URI, null, ID + "=\"" + id + "\"", null, null);
                if (cursor != null) {
                    note = ConvertUtils.convertCursorToNote(cursor, position);
                } else return null;
            } catch (IllegalArgumentException e) {
                Log.v("IllegalArgumentExcept", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            title.setText(note.getTitle());
            text.setText(note.getText());
            text.setTextSize(note.getTextSize());
            text.setTextColor(getResources().getColor(COLORS[note.getTextColor()%COLORS.length]));
            load.setVisibility(View.INVISIBLE);
        }
    }

    private class MyAsyncWrite extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                getContentResolver().update(ListNotesActivity.CONTENT_URI, ConvertUtils.convertNoteToValues(newNote), ID, new String[]{String.valueOf(note.getId())});
            } catch (IllegalArgumentException e){
                Log.v("IllegalArgumentExcept", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            load.setVisibility(View.INVISIBLE);
            startActivity(ListNotesActivity.newIntent(EditNoteActivity.this));
        }
    }
}
