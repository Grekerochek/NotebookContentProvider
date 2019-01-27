package com.alexander.contentobserver;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;


public class ListNotesActivity extends AppCompatActivity {

    private static final String AUTHORITY = "com.alexander.notebook";
    private static final String NOTES_TABLE = "notes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTES_TABLE);

    private Adapter adapter;
    private List<Note> notes;

    private FloatingActionButton button;
    private ProgressBar load;

    private ContentObserver contentObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initRecyclerView();
        initListeners();
        initContentObserver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(CONTENT_URI, true, contentObserver);
        new MyAsync().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    private void initViews(){
        button = findViewById(R.id.button);
        load = findViewById(R.id.load);
    }

    private void initContentObserver(){
        contentObserver = new MyObserver(new Handler(Looper.getMainLooper()));
    }

    private void initListeners(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(CreateNoteActivity.newIntent(ListNotesActivity.this));
            }
        });
    }

    private void initRecyclerView(){

        notes = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        MyTouchHelper helper = new MyTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(helper);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

    }

    public static final Intent newIntent(Context context){
        Intent intent = new Intent(context, ListNotesActivity.class);
        return intent;
    }

    private class MyAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
           Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
           if (cursor != null)
               notes = ConvertUtils.convertCursorToNotes(cursor);
           return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setData(notes);
            load.setVisibility(View.INVISIBLE);
        }
    }
}


