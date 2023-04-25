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

public class BodyTemperatureActivity extends AppCompatActivity {

    Button saveBodyTempBtn;
    EditText bodyTempData;
    RecyclerView recyclerView;
    BodyTemperatureAdapter bodyTemperatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_temperature);

        saveBodyTempBtn = findViewById(R.id.body_temp_done_btn);
        bodyTempData = findViewById(R.id.body_temp_edit_text);
        recyclerView = findViewById(R.id.recycler_view);

        saveBodyTempBtn.setOnClickListener(v -> saveBodyTemperature());
        setUpRecyclerView();
    }

    void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForBodyTemperature().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Vitals> options = new FirestoreRecyclerOptions.Builder<Vitals>()
                .setQuery(query, Vitals.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bodyTemperatureAdapter = new BodyTemperatureAdapter(options, this);
        recyclerView.setAdapter(bodyTemperatureAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bodyTemperatureAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bodyTemperatureAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bodyTemperatureAdapter.notifyDataSetChanged();
    }

    void saveBodyTemperature(){
        float data = Float.valueOf(bodyTempData.getText().toString());

        if(data <=0){
            bodyTempData.setError("Invalid entry");
            return;
        }

        Vitals bodyTemperature = new Vitals();
        bodyTemperature.setData(data);
        bodyTemperature.setTimestamp(Timestamp.now());

        saveBodyTemperatureToFirebase(bodyTemperature);
    }

    void saveBodyTemperatureToFirebase(Vitals bodyTemperature){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForBodyTemperature().document();

        documentReference.set(bodyTemperature).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //data is added
                    Utility.showToast(BodyTemperatureActivity.this, "Data recorded successfully");
                    //finish(); //close activity and go to previous
                }
                else{
                    Utility.showToast(BodyTemperatureActivity.this, "Failed while recording data");
                }
            }
        });
    }
}