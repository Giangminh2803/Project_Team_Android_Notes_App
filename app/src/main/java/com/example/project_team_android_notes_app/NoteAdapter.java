package com.example.project_team_android_notes_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter  extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>  {
    Context context;
    public static boolean flag = false;



    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Note note) {
        noteViewHolder.tv_title.setText(note.getTitle());
        noteViewHolder.tv_content.setText(note.getContent());
        noteViewHolder.tv_date.setText(Utility.timestampToString(note.timestamp));

        noteViewHolder.itemView.setOnClickListener(v->{

            Intent intent = new Intent(context, NoteDetail_Activity.class);
            intent.putExtra("title", note.title);
            intent.putExtra("content", note.content);
            String docId = this.getSnapshots().getSnapshot(i).getId();
            intent.putExtra("docId",docId);
            flag = true;
            context.startActivity(intent);


        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);

        return new NoteViewHolder(view);
    }



    class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title, tv_content, tv_date;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title_nt);
            tv_content = itemView.findViewById(R.id.tv_content_nt);
            tv_date = itemView.findViewById(R.id.tv_date_nt);

        }
    }


}
