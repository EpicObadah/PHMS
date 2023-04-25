package com.example.mavhealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MenstruationAdapter extends FirestoreRecyclerAdapter<Vitals, MenstruationAdapter.VitalsViewHolder> {
    Context context;

    public MenstruationAdapter(@NonNull FirestoreRecyclerOptions<Vitals> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull VitalsViewHolder holder, int position, @NonNull Vitals vital) {
        holder.dataTextView.setText(vital.flow);
        holder.mealtimeTextView.setText("Start of cycle: " + vital.yesno);
        holder.timestampTextView.setText(Utility.timestampToString(vital.timestamp));
    }

    @NonNull
    @Override
    public VitalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new VitalsViewHolder(view);
    }

    class VitalsViewHolder extends RecyclerView.ViewHolder{

        TextView dataTextView, mealtimeTextView, timestampTextView;

        public VitalsViewHolder(@NonNull View itemView) {
            super(itemView);

            dataTextView = itemView.findViewById(R.id.note_title_text_view);
            mealtimeTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestap_text_view);
        }
    }
}
