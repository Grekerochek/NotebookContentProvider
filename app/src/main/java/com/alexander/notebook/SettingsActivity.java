package com.alexander.notebook;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static final String VALUE = "value";
    private static final String EMPTY = "";

    private EditText textColor;
    private EditText textSize;
    private FloatingActionButton button;
    private ProgressBar load;

    private Note note;
    private Note newNote;

    private NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
        initDB();
        initListeners();
        new MyAsyncGet(getIntent().getIntExtra(VALUE, 0)).execute();
    }

    private void initViews(){
        textColor = findViewById(R.id.textColor);
        textSize = findViewById(R.id.textSize);
        button = findViewById(R.id.button);
        load = findViewById(R.id.load);
    }

    private void initDB(){
        db = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "my_database")
                .build();
    }

    private void initListeners(){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textColor.getText().toString().equals(EMPTY) || textSize.getText().toString().equals(EMPTY))
                    showMessage();
                else {
                    newNote = new Note();
                    newNote.setId(note.getId());
                    newNote.setTextColor(Integer.parseInt(textColor.getText().toString()));
                    newNote.setTextSize(Integer.parseInt(textSize.getText().toString()));
                    newNote.setTitle(note.getTitle());
                    newNote.setText(note.getText());
                    new MyAsyncWrite().execute();
                }
            }
        });
    }

    private void showMessage(){
        Toast.makeText(this, R.string.message, Toast.LENGTH_SHORT).show();
    }

    public static final Intent newIntent(Context context, int id){
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(VALUE, id);
        return intent;
    }

    private class MyAsyncGet extends AsyncTask<Void, Void, Void> {

        private int id;

        MyAsyncGet (int id){
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            note = db.getNoteDAO()
                    .getNote(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textColor.setText(String.valueOf(note.getTextColor()));
            textColor.setTransformationMethod(null);
            textSize.setText(String.valueOf(note.getTextSize()));
            textSize.setTransformationMethod(null);
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
            db.getNoteDAO()
                    .insert(newNote);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            load.setVisibility(View.INVISIBLE);
            startActivity(EditNoteActivity.newIntent(SettingsActivity.this, newNote.getId()));
        }
    }
}
