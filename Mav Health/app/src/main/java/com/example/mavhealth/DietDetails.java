package com.example.mavhealth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class DietDetails extends AppCompatActivity
{
    //Add Diet button
    FloatingActionButton addDietBtn;
    //Recycler View
    RecyclerView recyclerView;

    DietAdapter dietAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_details);

        //Add Diet Button
        addDietBtn = findViewById(R.id.add_diet_button);
        addDietBtn.setOnClickListener((v)-> startActivity(new Intent(DietDetails.this, CreateDiet.class)));

        //Recycler View and Menu Button
        recyclerView = findViewById(R.id.recycler_view);

        setupRecyclerView();
    }

    void setupRecyclerView()
    {
        Query query = Utility.getCollectionReferenceForDiet().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Diet> options = new FirestoreRecyclerOptions.Builder<Diet>().setQuery(query, Diet.class).build();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dietAdapter = new DietAdapter(options, this);
        recyclerView.setAdapter(dietAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        dietAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dietAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dietAdapter.notifyDataSetChanged();
    }
}