package com.example.mavhealth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MedicationAdapter extends FirestoreRecyclerAdapter<Medication, MedicationAdapter.MedicationViewHolder> {
    Context context;

    public MedicationAdapter(@NonNull FirestoreRecyclerOptions<Medication> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicationViewHolder holder, int position, @NonNull Medication med) {
        //set in view
        holder.nameTextView.setText(med.name);
        holder.strengthTextView.setText(String.valueOf(med.strength + " mg"));
        holder.dosageTextView.setText(String.valueOf(med.dosage) + " dose(s)");
        holder.timeTextView.setText(med.hour + ":" + med.minutes);
        holder.rxTextView.setText("RX: " + String.valueOf(med.rxNumber));

        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, MedDetailsActivity.class);
            intent.putExtra("dosage", med.dosage);
            intent.putExtra("hour", med.hour);
            intent.putExtra("minutes", med.minutes);
            intent.putExtra("name", med.name);
            intent.putExtra("rxNumber", med.rxNumber);
            intent.putExtra("strength", med.strength);

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_med_item, parent, false);
        return new MedicationViewHolder(view);
    }

    class MedicationViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView, strengthTextView, dosageTextView, timeTextView, rxTextView;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.med_name_text_view);
            strengthTextView = itemView.findViewById(R.id.med_strength_text_view);
            dosageTextView = itemView.findViewById(R.id.med_dosage_text_view);
            timeTextView = itemView.findViewById(R.id.med_timestamp_text_view);
            rxTextView = itemView.findViewById(R.id.med_rx_text_view);

        }
    }
}