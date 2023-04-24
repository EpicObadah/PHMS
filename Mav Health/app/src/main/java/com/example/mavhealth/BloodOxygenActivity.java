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

public class BloodOxygenActivity extends AppCompatActivity {
    Button saveBloodOxygenBtn;
    EditText bloodOxygenData;
    RecyclerView recyclerView;
    BloodOxygenAdapter bloodOxygenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_oxygen);

        saveBloodOxygenBtn = findViewById(R.id.blood_oxygen_done_btn);
        bloodOxygenData = findViewById(R.id.blood_oxygen_edit_text);
        recyclerView = findViewById(R.id.recycler_view);

        saveBloodOxygenBtn.setOnClickListener(v -> saveBloodOxygen());
        setUpRecyclerView();
    }

    void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForBloodOxygen().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Vitals> options = new FirestoreRecyclerOptions.Builder<Vitals>()
                .setQuery(query, Vitals.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodOxygenAdapter = new BloodOxygenAdapter(options, this);
        recyclerView.setAdapter(bloodOxygenAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bloodOxygenAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bloodOxygenAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bloodOxygenAdapter.notifyDataSetChanged();
    }

    void saveBloodOxygen(){
        String data_s = bloodOxygenData.getText().toString();
        float data = Float.valueOf(bloodOxygenData.getText().toString());

        if(data_s == null || data_s.isEmpty() || data <=0 || data > 100){
            bloodOxygenData.setError("Invalid percentage");
            return;
        }

        Vitals bloodOxygenData = new Vitals();
        bloodOxygenData.setData(data);
        bloodOxygenData.setTimestamp(Timestamp.now());

        saveBloodOxygenToFirebase(bloodOxygenData);
    }

    void saveBloodOxygenToFirebase(Vitals bloodOxygenData){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForBloodOxygen().document();

        documentReference.set(bloodOxygenData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //data is added
                    Utility.showToast(BloodOxygenActivity.this, "Data recorded successfully");
                    //finish(); //close activity and go to previous
                }
                else{
                    Utility.showToast(BloodOxygenActivity.this, "Failed while recording data");
                }
            }
        });
    }
}