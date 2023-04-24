package com.example.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class MedDetailsActivity extends AppCompatActivity {

    EditText medName, medRxNumber, medStrength, medDosage;
    TimePicker timeToTake;
    ImageButton saveMedsBtn;
    String name, rxNumber, dosage, hour, minutes, strength, docId;
    TextView deleteMedTextViewBtn, medPageTitle;
    boolean isEditMode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_details);

        medName = findViewById(R.id.med_name_edit_text);
        medRxNumber = findViewById(R.id.med_rx_edit_text);
        medStrength = findViewById(R.id.med_strength_edit_text);
        medDosage = findViewById(R.id.med_dosage_edit_text);
        timeToTake = findViewById(R.id.med_timepicker);
        saveMedsBtn = findViewById(R.id.save_med_btn);
        deleteMedTextViewBtn = findViewById(R.id.delete_med_text_view_btn);
        medPageTitle = findViewById(R.id.page_title);

        //receive data
        docId = getIntent().getStringExtra("docId");

        //receive data
        name = getIntent().getStringExtra("name");
        rxNumber = getIntent().getStringExtra("rxNumber");
        dosage = getIntent().getStringExtra("dosage");
        hour = getIntent().getStringExtra("hour");
        minutes = getIntent().getStringExtra("minutes");
        strength = getIntent().getStringExtra("strength");

        docId = getIntent().getStringExtra("docId");

        if(docId != null && !docId.isEmpty()){
            isEditMode = true;
        }

        medName.setText(name);
        medRxNumber.setText(rxNumber);
        medStrength.setText(strength);
        medDosage.setText(dosage);
        //timeToTake.setHour(Integer.parseInt(hour));
        //timeToTake.setMinute(Integer.parseInt(minutes));

        if(isEditMode){
            medPageTitle.setText("Edit Your Medication");
            deleteMedTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveMedsBtn.setOnClickListener(v -> saveMed() );
        deleteMedTextViewBtn.setOnClickListener(v -> deleteMedFromFirebase());
    }

    void deleteMedFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForMedication().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //note is deleted
                    Utility.showToast(MedDetailsActivity.this, "Medication deleted successfully");
                    finish();
                }
                else{
                    Utility.showToast(MedDetailsActivity.this, "Failed while deleting medication");
                }
            }
        });
    }
    void saveMed(){
        String name = medName.getText().toString();
        int rxNumber = Integer.valueOf(medRxNumber.getText().toString());
        float strength = Float.valueOf(medStrength.getText().toString());
        int dosage = Integer.valueOf(medDosage.getText().toString());
        int hour = timeToTake.getHour();
        int minutes = timeToTake.getMinute();

        if(name == null || name.isEmpty()){
            medName.setError("Medication name is required");
            return;
        }

        Medication med = new Medication();
        med.setName(name);
        med.setRxNumber(rxNumber);
        med.setStrength(strength);
        med.setDosage(dosage);
        med.setHour(hour);
        med.setMinutes(minutes);

        saveMedToFirebase(med);
    }

    void saveMedToFirebase(Medication med){
        DocumentReference documentReference;

        if(isEditMode){
            //update the med
            documentReference = Utility.getCollectionReferenceForMedication().document(docId);
        }
        else{
            //create new med
            documentReference = Utility.getCollectionReferenceForMedication().document();
        }
        documentReference.set(med).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //med is added
                    Utility.showToast(MedDetailsActivity.this, "Medication added successfully");
                    finish();
                }
                else{
                    Utility.showToast(MedDetailsActivity.this, "Failed while adding medication");
                }
            }
        });
    }
}