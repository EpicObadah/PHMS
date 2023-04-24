package com.example.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class BloodPressureActivity extends AppCompatActivity {

    Button saveBloodPressureBtn;
    EditText systolicData;
    EditText diastolicData;
    RecyclerView recyclerView;
    BloodPressureAdapter bloodPressureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);

        saveBloodPressureBtn = findViewById(R.id.blood_pressure_done_btn);
        systolicData = findViewById(R.id.blood_systolic_edit_text);
        diastolicData = findViewById(R.id.blood_diastolic_edit_text);
        recyclerView = findViewById(R.id.recycler_view);

        saveBloodPressureBtn.setOnClickListener(v -> saveBloodPressure());
        setUpRecyclerView();
    }

    void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForBloodPressure().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Vitals> options = new FirestoreRecyclerOptions.Builder<Vitals>()
                .setQuery(query, Vitals.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodPressureAdapter = new BloodPressureAdapter(options, this);
        recyclerView.setAdapter(bloodPressureAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bloodPressureAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bloodPressureAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bloodPressureAdapter.notifyDataSetChanged();
    }

    void saveBloodPressure(){
        int systolic = Integer.valueOf(systolicData.getText().toString());
        int diastolic = Integer.valueOf(diastolicData.getText().toString());

        if(diastolic <=0){
            diastolicData.setError("Invalid entry");
            return;
        }

        if(systolic <=0){
            systolicData.setError("Invalid entry");
            return;
        }

        Vitals bloodPressureData = new Vitals();
        bloodPressureData.setSystolic(systolic);
        bloodPressureData.setDiastolic(diastolic);
        bloodPressureData.setTimestamp(Timestamp.now());

        saveBloodPressureToFirebase(bloodPressureData);
    }

    void saveBloodPressureToFirebase(Vitals bloodPressureData){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForBloodPressure().document();

        documentReference.set(bloodPressureData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //data is added
                    Utility.showToast(BloodPressureActivity.this, "Data recorded successfully");
                    //finish(); //close activity and go to previous
                }
                else{
                    Utility.showToast(BloodPressureActivity.this, "Failed while recording data");
                }
            }
        });
    }
}