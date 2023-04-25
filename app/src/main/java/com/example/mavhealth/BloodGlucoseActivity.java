package com.example.mavhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class BloodGlucoseActivity extends AppCompatActivity {

    Spinner mealtimeSpinner;
    Button saveBloodGlucoseBtn;
    EditText bloodGlucoseData;
    RecyclerView recyclerView;
    BloodGlucoseAdapter bloodGlucoseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_glucose);

        mealtimeSpinner = findViewById(R.id.mealtime_spinner);
        saveBloodGlucoseBtn = findViewById(R.id.blood_glucose_done_btn);
        bloodGlucoseData = findViewById(R.id.blood_glucose_edit_text);
        recyclerView = findViewById(R.id.recycler_view);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mealtime_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mealtimeSpinner.setAdapter(adapter);

        saveBloodGlucoseBtn.setOnClickListener(v -> saveBloodGlucose());
        setUpRecyclerView();
    }

    void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForBloodGlucose().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Vitals> options = new FirestoreRecyclerOptions.Builder<Vitals>()
                .setQuery(query, Vitals.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodGlucoseAdapter = new BloodGlucoseAdapter(options, this);
        recyclerView.setAdapter(bloodGlucoseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bloodGlucoseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bloodGlucoseAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bloodGlucoseAdapter.notifyDataSetChanged();
    }

    void saveBloodGlucose(){
        float data = Float.valueOf(bloodGlucoseData.getText().toString());
        String mealtime = mealtimeSpinner.getSelectedItem().toString();

        if(data <= 0){
            bloodGlucoseData.setError("Blood glucose cannot be 0 or less");
            return;
        }

        Vitals bloodGlucoseData = new Vitals();
        bloodGlucoseData.setData(data);
        bloodGlucoseData.setMealtime(mealtime);
        bloodGlucoseData.setTimestamp(Timestamp.now());

        saveBloodGlucoseToFirebase(bloodGlucoseData);
    }

    void saveBloodGlucoseToFirebase(Vitals bloodGlucoseData){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForBloodGlucose().document();

        documentReference.set(bloodGlucoseData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //data is added
                    Utility.showToast(BloodGlucoseActivity.this, "Data recorded successfully");
                    //finish(); //close activity and go to previous
                }
                else{
                    Utility.showToast(BloodGlucoseActivity.this, "Failed while recording data");
                }
            }
        });
    }
}