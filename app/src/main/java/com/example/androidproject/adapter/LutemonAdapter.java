package com.example.androidproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.model.Lutemon;

import java.util.List;

public class LutemonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener { // Interface for handling item clicks
        void onItemClick(int position);
    }
    private List<Lutemon> lutemons; // List of lutemons to display
    private final Context context;
    private final OnItemClickListener listener;

    public LutemonAdapter(List<Lutemon> lutemons, Context context, OnItemClickListener listener){
        this.lutemons = lutemons;
        this.context = context;
        this.listener = listener;

    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lutemon_item, parent, false);
        return new LutemonItemActivity.LutemonViewHolder(view, listener);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateLutemons(List<Lutemon> newLutemons) {
        this.lutemons = newLutemons;
        notifyDataSetChanged();
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lutemon lutemon = lutemons.get(position); // Get the lutemon at the current position
        LutemonItemActivity.LutemonViewHolder lutemonHolder = (LutemonItemActivity.LutemonViewHolder) holder;
        try {
            try {
                String color = lutemon.getColor(); // Get the color of the lutemon to set the image accordingly
                switch (color) {
                    case "Pink":
                        lutemonHolder.getLutemonImageView().setImageResource(R.drawable.lutemon_pink);
                        break;
                    case "White":
                        lutemonHolder.getLutemonImageView().setImageResource(R.drawable.lutemon_white);
                        break;
                    case "Green":
                        lutemonHolder.getLutemonImageView().setImageResource(R.drawable.lutemon_green);
                        break;
                    case "Orange":
                        lutemonHolder.getLutemonImageView().setImageResource(R.drawable.lutemon_orange);
                        break;
                    case "Black":
                        lutemonHolder.getLutemonImageView().setImageResource(R.drawable.lutemon_black);
                        break;
                }
            } catch (Exception e) {
                lutemonHolder.getLutemonImageView().setImageResource(R.drawable.lutemon_placeholder_sprite); // Set a default image if an error occurs
                Log.e("LutemonAdapter", "Error setting lutemon image: " + e.getMessage());
            }

            lutemonHolder.getNameTextView().setText(lutemon.getName());
            lutemonHolder.getColorTextView().setText(lutemon.getColor());
            String stats = "ATK: " + lutemon.getPower() + " DEF: " + lutemon.getDefense() + " HP: " + lutemon.getHealth() + "/" + lutemon.getMaxHealth() + " XP: " + lutemon.getExperience(); // Format the stats
            lutemonHolder.getStatsTextView().setText(stats);

            // TO DO - Move and action buttons & win-counter setText.

        } catch (Exception e) {
            Log.e("LutemonAdapter", "Error binding lutemon data: " + e.getMessage());
        }
    }


    public int getItemCount() {
        return lutemons != null ? lutemons.size() : 0;
    }
}
