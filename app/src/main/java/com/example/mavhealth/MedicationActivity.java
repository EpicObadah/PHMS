package com.example.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.logging.type.HttpRequest;

import java.io.IOException;
import java.net.URI;

public class MedicationActivity extends AppCompatActivity {

    FloatingActionButton addMedBtn;
    ImageButton menuBtn;

    RecyclerView recyclerView;
    MedicationAdapter medAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication);

        addMedBtn = findViewById(R.id.add_med_btn);
        menuBtn = findViewById(R.id.menu_btn);
        recyclerView = findViewById(R.id.recycler_view);

        addMedBtn.setOnClickListener(v -> startActivity(new Intent(MedicationActivity.this, MedDetailsActivity.class)));
        menuBtn.setOnClickListener(v -> showMenu());
        setupRecyclerView();
    }

    void showMenu(){
        PopupMenu popupMenu = new PopupMenu(MedicationActivity.this, menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle() == "Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MedicationActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForMedication().orderBy("hour", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Medication> options = new FirestoreRecyclerOptions.Builder<Medication>()
                .setQuery(query, Medication.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medAdapter = new MedicationAdapter(options, this);
        recyclerView.setAdapter(medAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        medAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        medAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        medAdapter.notifyDataSetChanged();
    }
}