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

public class MenstruationActivity extends AppCompatActivity {

    Spinner startOfCycle;
    Spinner flowLevel;
    Button saveMenstruationBtn;
    RecyclerView recyclerView;
    MenstruationAdapter menstruationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menstruation);

        startOfCycle = findViewById(R.id.start_spinner);
        flowLevel = findViewById(R.id.flow_spinner);
        saveMenstruationBtn = findViewById(R.id.menstruation_done_btn);
        recyclerView = findViewById(R.id.recycler_view);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterYesNo = ArrayAdapter.createFromResource(this,
                R.array.yesno_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterYesNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        startOfCycle.setAdapter(adapterYesNo);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterFlow = ArrayAdapter.createFromResource(this,
                R.array.flow_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterFlow.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        flowLevel.setAdapter(adapterFlow);

        saveMenstruationBtn.setOnClickListener(v -> saveMenstruation());
        setUpRecyclerView();
    }

    void setUpRecyclerView(){
        Query query = Utility.getCollectionReferenceForMenstruation().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Vitals> options = new FirestoreRecyclerOptions.Builder<Vitals>()
                .setQuery(query, Vitals.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menstruationAdapter = new MenstruationAdapter(options, this);
        recyclerView.setAdapter(menstruationAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        menstruationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        menstruationAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        menstruationAdapter.notifyDataSetChanged();
    }

    void saveMenstruation(){
        String flow = flowLevel.getSelectedItem().toString();
        String start = startOfCycle.getSelectedItem().toString();

        Vitals menstruationData = new Vitals();
        menstruationData.setYesno(start);
        menstruationData.setFlow(flow);
        menstruationData.setTimestamp(Timestamp.now());

        saveMenstruationToFirebase(menstruationData);
    }

    void saveMenstruationToFirebase(Vitals menstruationData){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForMenstruation().document();

        documentReference.set(menstruationData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //data is added
                    Utility.showToast(MenstruationActivity.this, "Data recorded successfully");
                    //finish(); //close activity and go to previous
                }
                else{
                    Utility.showToast(MenstruationActivity.this, "Failed while recording data");
                }
            }
        });
    }
}