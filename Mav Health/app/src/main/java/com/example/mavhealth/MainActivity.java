package com.example.mavhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity{

    MaterialButton notesBtn, medBtn, vitalsBtn;
    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuBtn = (ImageButton) findViewById(R.id.dashboard_menu_btn);
        menuBtn.setOnClickListener((v) -> showMenu());

        notesBtn = findViewById(R.id.notes_dashboard_btn);
        notesBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this, NotesPageActivity.class)));

        medBtn = findViewById(R.id.meds_dashboard_btn);
        medBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this, MedicationActivity.class)));

        vitalsBtn = findViewById(R.id.vitals_dashboard_btn);
        vitalsBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this, VitalsActivity.class)));
    }

    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuBtn);

        try{
            Method method = popupMenu.getMenu().getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            method.setAccessible(true);
            method.invoke(popupMenu.getMenu(), true);
        }catch (Exception e) {
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
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    default:
                        return false;
                }
            }
        });

    }
}