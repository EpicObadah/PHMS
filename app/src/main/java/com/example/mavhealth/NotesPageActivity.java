package com.example.mavhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import java.lang.reflect.Method;

public class NotesPageActivity extends AppCompatActivity {

    FloatingActionButton newNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    ImageButton homeBtn;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_page);

        newNoteBtn = findViewById(R.id.new_note_btn);
        recyclerView = findViewById(R.id.recycler_view);
        menuBtn = findViewById(R.id.menu_btn);
        homeBtn = findViewById(R.id.home_btn);

        newNoteBtn.setOnClickListener((v)-> startActivity(new Intent(NotesPageActivity.this, NoteDetailsActivity.class)));
        menuBtn.setOnClickListener((v)-> showMenu());
        homeBtn.setOnClickListener((v)-> goToHome());
        setUpRecyclerView();
    }

    void goToHome(){
        startActivity(new Intent(NotesPageActivity.this, MainActivity.class));
        finish();
    }

    void showMenu(){
        //display menu
        PopupMenu popupMenu = new PopupMenu(NotesPageActivity.this, menuBtn);

        try {
            Method method = popupMenu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(popupMenu.getMenu(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout_item:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(NotesPageActivity.this, LoginActivity.class));
                        finish();
                    default:
                        return false;
                }
            }
        });
    }

    void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);


    }

    @Override
    protected void onStart(){
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume(){
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }

}