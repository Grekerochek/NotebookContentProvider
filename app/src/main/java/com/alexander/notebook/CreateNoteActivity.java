package com.alexander.notebook;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CreateNoteActivity extends AppCompatActivity {

    private static final String EMPTY = "";

    private EditText title;
    private EditText text;
    private Note note;
    private FloatingActionButton button;
    private ProgressBar load;

    private NoteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        initViews();
        initDB();
        initListeners();
    }

    private void initViews(){
        title = findViewById(R.id.titleCreate);
        text = findViewById(R.id.noteCreate);
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

                if (title.getText().toString().equals(EMPTY) || text.getText().toString().equals(EMPTY))
                    showMessage();
                else {
                    note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setText(text.getText().toString());
                    new MyAsync().execute();
                }
            }
        });
    }

    private void showMessage(){
        Toast.makeText(this, R.string.message, Toast.LENGTH_SHORT).show();
    }

    public static final Intent newIntent(Context context){
        Intent intent = new Intent(context, CreateNoteActivity.class);
        return intent;
    }

    private class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.getNoteDAO()
                    .insert(note);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            load.setVisibility(View.INVISIBLE);
            startActivity(ListNotesActivity.newIntent(CreateNoteActivity.this));
        }
    }
}
