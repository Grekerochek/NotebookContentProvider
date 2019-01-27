package com.alexander.notebook;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class EditNoteActivity extends AppCompatActivity {

    private static final String VALUE = "value";
    private static final String EMPTY = "";
    private static final int [] COLORS = {R.color.colorBlue, R.color.colorGreen, R.color.colorGrey,
                                        R.color.colorLightBlue, R.color.colorYellow, R.color.colorRed};

    private Note note;
    private Note newNote;

    private int id;

    private EditText title;
    private EditText text;
    private FloatingActionButton button;
    private ProgressBar load;

    private NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        initViews();
        initListeners();
        initDB();
        id = getIntent().getIntExtra(VALUE, 0);
        new MyAsyncGet(id).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(SettingsActivity.newIntent(this, id));
        return true;
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

    private void initDB(){
        db = Room.databaseBuilder(getApplicationContext(), NoteDatabase.class, "my_database")
                .build();
    }

    public static final Intent newIntent(Context context, int id){
        Intent intent = new Intent(context, EditNoteActivity.class);
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
            db.getNoteDAO()
                    .insert(newNote);
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
