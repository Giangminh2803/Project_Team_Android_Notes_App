package com.example.project_team_android_notes_app;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
    static CollectionReference getColletionReferenceForNotes()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
       return FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");
    }
    static String timestampToString(Timestamp timestamp)
    {
      return   new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }
}
