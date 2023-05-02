package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;

    ImageButton saveNoteBtn;

    private TextView pageTitleTextView;
    String title;
    String content;
    String docId;

    boolean isEditMode = false;

    TextView deleteNoteTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);

        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);

        //Receiving data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEditMode){
            pageTitleTextView.setText("Edit your note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        deleteNoteTextViewBtn.setOnClickListener((v)-> deleteNoteFromFirebase() );

        saveNoteBtn = findViewById(R.id.save_note_btn);

        saveNoteBtn.setOnClickListener( (v)-> saveNote());
    }

    private void saveNote(){
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }


        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    private void saveNoteToFirebase(Note note){

        //saving a note in that document
        DocumentReference documentReference;
        if(isEditMode){
            //This will update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            //This will create a new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
        //documentReference = Utility.getCollectionReferenceForNotes().document();

        //adding note to the database
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    //note is added
                    Utility.showToast(NoteDetailsActivity.this, "Note added successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this, "Failed while adding note");
                }
            }
        });
    }

    private void deleteNoteFromFirebase(){


        //saving a note in that document
        DocumentReference documentReference;
            //This will update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);


        //documentReference = Utility.getCollectionReferenceForNotes().document();

        //adding note to the database
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");
                }
            }
        });







    }
}