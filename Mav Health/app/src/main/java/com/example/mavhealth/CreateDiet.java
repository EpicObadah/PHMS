package com.example.mavhealth;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.Calendar;
import java.util.Locale;

public class CreateDiet extends AppCompatActivity
{
    //Save button
    ImageButton saveDietBtn;
    //EditText Fields
    EditText totalCalories, Sugar, Proteins, Fats;
    //Date
    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    //Time
    private Button timeButton;
    int hour, min;

    //Edit Diet Functionality
    TextView pageTitleTextView;
    String docId, time;
    int fat, protein, totalCal, sug;
    boolean isEditMode = false;

    //Delete TextView
    TextView deleteDietTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diet);

        //Date
        initDatePicker();
        dateButton = findViewById(R.id.datePicker);
        dateButton.setText(getTodaysDate());

        //time
        timeButton = findViewById(R.id.timePicker);

        //All edit text fields
        totalCalories = findViewById(R.id.TotalCalories);
        Sugar = findViewById(R.id.Sugar);
        Proteins = findViewById(R.id.Protein);
        Fats = findViewById(R.id.Fats);

        //Save com.example.notesapp.Diet Button (Floating button)
        saveDietBtn = findViewById(R.id.save_diet_btn);

        //Delete Button id
        deleteDietTextViewBtn = findViewById(R.id.delete_diet_text_view_btn);

        //For Edit Diet
        pageTitleTextView = findViewById(R.id.text_Add_Text);
        docId = getIntent().getStringExtra("docId");
        time = getIntent().getStringExtra("time");
        totalCal = getIntent().getIntExtra("TotalCalories", 0);
        fat = getIntent().getIntExtra("Fats",0);
        protein = getIntent().getIntExtra("Protein",0);
        sug = getIntent().getIntExtra("Sugar",0);

        if(docId!=null && !docId.isEmpty())
        {
            isEditMode = true;
        }
        if(isEditMode)
        {
            pageTitleTextView.setText("Edit Diet");
            timeButton.setText(time);
            Sugar.setText(String.valueOf(sug));
            Proteins.setText(String.valueOf(protein));
            Fats.setText(String.valueOf(fat));
            totalCalories.setText(String.valueOf(totalCal));
            deleteDietTextViewBtn.setVisibility(View.VISIBLE);
        }
        //Setting OnClickListener for Save button
        saveDietBtn.setOnClickListener((v)->saveDiet());

        //Setting OnClickListener for delete Button
        deleteDietTextViewBtn.setOnClickListener((v)-> deleteDietFromFireBase());

    }

    //Save com.example.notesapp.Diet method for listener
    void saveDiet()
    {
        String dietTime = timeButton.getText().toString();
        String dietDate = dateButton.getText().toString();
        int totCal = Integer.valueOf(totalCalories.getText().toString());
        int Protein = Integer.valueOf(Proteins.getText().toString());
        int Fat = Integer.valueOf(Fats.getText().toString());
        int sugar = Integer.valueOf(Sugar.getText().toString());

        Diet diet = new Diet();

        diet.setTime(dietTime);
        diet.setDate(dietDate);
        diet.setTotalCalories(totCal);
        diet.setProteins(Protein);
        diet.setFats(Fat);
        diet.setSugar(sugar);
        diet.setTimestamp(Timestamp.now());

        saveDietToFireBase(diet);
    }

    //SaveMethod for FireBase
    void saveDietToFireBase(Diet diet)
    {
        DocumentReference documentReference;

        //if Edit Diet
        if(isEditMode)
        {
            //update diet
            documentReference = Utility.getCollectionReferenceForDiet().document(docId);
        }
        else
        {
            //make new diet
            documentReference = Utility.getCollectionReferenceForDiet().document();
        }

        documentReference.set(diet).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(CreateDiet.this, "Diet added successfully");
                    finish();
                }else {
                    Utility.showToast(CreateDiet.this, "Failed while adding diet");
                }
            }
        });
    }

    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute)
            {
                String AM_PM;
                hour = selectedHour;
                if(hour > 12)
                {
                    hour = hour - 12;
                    AM_PM = "PM";
                }
                else
                {
                    AM_PM = "AM";
                }
                min = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, min) + " " + AM_PM);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, min, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) 
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        return  "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    //Delete Diet Method
    void deleteDietFromFireBase()
    {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForDiet().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(CreateDiet.this, "Diet deleted successfully");
                    finish();
                }else {
                    Utility.showToast(CreateDiet.this, "Failed while deleting diet");
                }
            }
        });
    }
}