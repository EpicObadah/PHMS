package com.example.mavhealth;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionReferenceForNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");
    }

    static CollectionReference getCollectionReferenceForDiet()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("diet")
                .document(currentUser.getUid()).collection("my_diet");

    }

    static CollectionReference getCollectionReferenceForBloodOxygen(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vitals")
                .document(currentUser.getUid()).collection("blood_oxygen");
    }

    static CollectionReference getCollectionReferenceForBloodPressure(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vitals")
                .document(currentUser.getUid()).collection("blood_pressure");
    }

    static CollectionReference getCollectionReferenceForBodyTemperature(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vitals")
                .document(currentUser.getUid()).collection("body_temperature");
    }

    static CollectionReference getCollectionReferenceForMenstruation(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vitals")
                .document(currentUser.getUid()).collection("menstruation");
    }

    static CollectionReference getCollectionReferenceForBloodGlucose(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("vitals")
                .document(currentUser.getUid()).collection("blood_glucose");
    }

    static CollectionReference getCollectionReferenceForMedication(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("medications")
                .document(currentUser.getUid()).collection("my_meds");
    }

    static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
    }

}
