package com.example.project_team_android_notes_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.util.Timer;

public class NoteDetail_Activity extends AppCompatActivity {

    EditText edt_title, edt_content;
    ImageButton btn_saveNote;
    TextView tv_page;
    String strTitle, strContent, StrId;
    Button btn_delete, btn_back;

    boolean isEditMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_detail);

        edt_content = findViewById(R.id.edt_content_nd);
        edt_title = findViewById(R.id.edt_title_nd);
        btn_saveNote = findViewById(R.id.save_note_nd);
        tv_page = findViewById(R.id.page_title);
        btn_delete = findViewById(R.id.delete_nd);
        btn_back = findViewById(R.id.back_nd);



        //Nhận dữ liêụ
     if (NoteAdapter.flag==true) {
            Intent intent = getIntent();
            if (intent!=null)
            {
                strTitle = intent.getStringExtra("title");
                strContent = intent.getStringExtra("content");
                StrId = intent.getStringExtra("docId");
                if (!StrId.isEmpty()){
                    isEditMode = true;
                }
                edt_title.setText(strTitle);
                edt_content.setText(strContent);
                if (isEditMode){
                    tv_page.setText("Edit your note");
                    btn_delete.setVisibility(View.VISIBLE);
                }
                NoteAdapter.flag = false;
            }
        }






        btn_back.setOnClickListener(v-> finish());
        btn_saveNote.setOnClickListener(v-> saveNote());
        btn_delete.setOnClickListener(v-> showDeleteConfirmationDialog());

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNoteFromFireBase();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Người dùng đã nhấn "Không", không làm gì cả
                        dialog.dismiss(); // Đóng hộp thoại
                    }
                });

        // Tạo hộp thoại và hiển thị nó
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteNoteFromFireBase() {
        DocumentReference documentReference;

        documentReference = Utility.getColletionReferenceForNotes().document(StrId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(NoteDetail_Activity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(NoteDetail_Activity.this, "Note not deleted successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void saveNote() {

        String strTitle = edt_title.getText().toString().trim();
        String strContent = edt_content.getText().toString().trim();

            if (!strTitle.isEmpty()){
                Note note = new Note();
            note.setTitle(strTitle);
            note.setContent(strContent);
            note.setTimestamp(Timestamp.now());

            saveNoteToFireBase(note);
        }else {
                edt_title.setError("Enter the Title first");
            }

    }

    private void saveNoteToFireBase(Note note) {
        DocumentReference documentReference;

        if (isEditMode){
            documentReference = Utility.getColletionReferenceForNotes().document(StrId);
        }else {
            documentReference = Utility.getColletionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(NoteDetail_Activity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(NoteDetail_Activity.this, "Note not added successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}