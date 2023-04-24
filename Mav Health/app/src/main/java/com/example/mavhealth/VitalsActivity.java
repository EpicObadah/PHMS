package com.example.mavhealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class VitalsActivity extends AppCompatActivity {

    Button bloodGlucoseBtn;
    Button bloodOxygenBtn;
    Button bloodPressureBtn;
    Button bodyTempBtn;
    Button menstruationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitals);

        bloodGlucoseBtn = findViewById(R.id.blood_glucose_btn);
        bloodOxygenBtn = findViewById(R.id.blood_oxygen_btn);
        bloodPressureBtn = findViewById(R.id.blood_pressure_btn);
        bodyTempBtn = findViewById(R.id.body_temp_btn);
        menstruationBtn = findViewById(R.id.menstruation_btn);

        bloodGlucoseBtn.setOnClickListener(v -> startActivity(new Intent(VitalsActivity.this, BloodGlucoseActivity.class)));
        bloodOxygenBtn.setOnClickListener(v -> startActivity(new Intent(VitalsActivity.this, BloodOxygenActivity.class)));
        bloodPressureBtn.setOnClickListener(v -> startActivity(new Intent(VitalsActivity.this, BloodPressureActivity.class)));
        bodyTempBtn.setOnClickListener(v -> startActivity(new Intent(VitalsActivity.this, BodyTemperatureActivity.class)));
        menstruationBtn.setOnClickListener(v -> startActivity(new Intent(VitalsActivity.this, MenstruationActivity.class)));
    }
}