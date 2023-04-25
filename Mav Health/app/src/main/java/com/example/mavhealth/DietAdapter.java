package com.example.mavhealth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import io.reactivex.rxjava3.annotations.NonNull;

public class DietAdapter extends FirestoreRecyclerAdapter<Diet, DietAdapter.DietViewHolder>
{
    Context context;

    public DietAdapter(@NonNull FirestoreRecyclerOptions<Diet> options, Context context)
    {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DietViewHolder holder, int position, @NonNull Diet diet)
    {
        holder.titleTextView.setText(String.valueOf(diet.totalCalories) + " calories");
        holder.contentTextView.setText(diet.time);
        holder.timestampTextView.setText(diet.date);

        //Changes to add edit and delete functionality
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, CreateDiet.class);
            intent.putExtra("TotalCalories", diet.totalCalories);
            intent.putExtra("Fats", diet.fats);
            intent.putExtra("Protein", diet.proteins);
            intent.putExtra("Sugar", diet.sugar);
            intent.putExtra("time", diet.time);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });
    }

    @androidx.annotation.NonNull
    @Override
    public DietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new DietViewHolder(view);
    }

    class DietViewHolder extends RecyclerView.ViewHolder
    {
        TextView titleTextView, contentTextView, timestampTextView;
        public DietViewHolder(@NonNull View itemView)
        {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.note_title_text_view);
            contentTextView = itemView.findViewById(R.id.note_content_text_view);
            timestampTextView = itemView.findViewById(R.id.note_timestap_text_view);
        }
    }
}
