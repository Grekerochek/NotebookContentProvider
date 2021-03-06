package com.alexander.contentobserver;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements MyTouchHelper.ItemTouchHelperAdapter {

    private static final String AUTHORITY = "com.alexander.notebook";
    private static final String NOTES_TABLE = "notes";

    private List<Note> data;
    private Context context;

    private ContentObserver contentObserver;

    public Adapter(Context context, List<Note> data){
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.title.setText(data.get(position).getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                context.startActivity(EditNoteActivity.newIntent(context, data.get(position).getId(), position));
            }
        });
    }

    public void setData(List<Note> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public void onViewSwiped(int position) {

        contentObserver = new MyObserver(new Handler(Looper.getMainLooper()));
        new MyAsyncRemove(data.get(position), position).execute();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyAsyncRemove extends AsyncTask<Void, Void, Void> {

        private Note note;
        private int position;

        MyAsyncRemove(Note note, int position){

            this.note = note;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Uri uri = Uri.parse("content://" + AUTHORITY + "/" + NOTES_TABLE + "/" + note.getId());
            context.getContentResolver().delete(uri, null, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            data.remove(position);
            notifyItemRemoved(position);
        }
    }
}


