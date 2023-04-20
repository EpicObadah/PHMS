package com.example.mavhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button goToNotesPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToNotesPage = findViewById(R.id.go_to_notes_page);
        goToNotesPage.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this, NotesPageActivity.class)));
    }
}