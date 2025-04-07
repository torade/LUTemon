package com.example.androidproject.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.containers.Container;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

public class LutemonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener { // Interface for handling item clicks
        void onItemClick(int position);

    }
    private List<Lutemon> lutemons; // List of lutemons to display
    private final Context context;
    private final OnItemClickListener listener;
    private final String currentContainer;
    private String trainStatUpdated = "";

    public LutemonAdapter(List<Lutemon> lutemons, Context context, OnItemClickListener listener, String currentContainer){
        this.lutemons = lutemons;
        this.context = context;
        this.listener = listener;
        this.currentContainer = currentContainer;

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

            lutemonHolder.getMoveButton().setOnClickListener(v ->  {
                showMoveDialog(context, lutemon, currentContainer);
            });

            Button actionButton = lutemonHolder.getActionButton();
            if (currentContainer.equals("Training Area")) {
                actionButton.setText("Train");
                actionButton.setVisibility(View.VISIBLE);
                actionButton.setOnClickListener(v -> {
                    if (lutemon.getExperience() > 0) {
                        trainStatUpdated = lutemon.train();
                        showTrainingDialog(context, lutemon);
                        notifyItemChanged(position);
                    } else {
                        Toast.makeText(context, lutemon.getName() + " has no experience to train!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (currentContainer.equals("Battle Area")) {
                    actionButton.setText("Select for Battle");
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setOnClickListener(v -> {
                        Toast.makeText(context, lutemon.getName() + " selected for battle!", Toast.LENGTH_SHORT).show();
                        // BATTLE LOGIC ALSO HERE
                    });
                } else {
                    actionButton.setVisibility(View.GONE);
                }

        } catch (Exception e) {
            Log.e("LutemonAdapter", "Error binding lutemon data: " + e.getMessage());
        }
    }

    private void showTrainingDialog(Context context, Lutemon lutemon) {
        // Make training shaky. Used some inspiration from AI & the internet for this.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_training_animation, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        ImageView lutemonImage = dialogView.findViewById(R.id.trainingLutemonImage);
        TextView statsText = dialogView.findViewById(R.id.trainingStatsText);
        View energyGlow = dialogView.findViewById(R.id.energyGlowView);

        // Set Lutemon image based on color
        switch (lutemon.getColor()) {
            case "White":
                lutemonImage.setImageResource(R.drawable.lutemon_white);
                break;
            case "Green":
                lutemonImage.setImageResource(R.drawable.lutemon_green);
                break;
            case "Pink":
                lutemonImage.setImageResource(R.drawable.lutemon_pink);
                break;
            case "Orange":
                lutemonImage.setImageResource(R.drawable.lutemon_orange);
                break;
            case "Black":
                lutemonImage.setImageResource(R.drawable.lutemon_black);
                break;
            default:
                lutemonImage.setImageResource(R.drawable.lutemon_placeholder_sprite);
        }

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Start shaking animation
        Animation shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_animation);
        lutemonImage.startAnimation(shakeAnimation);

        if (energyGlow != null) {
            Animation glowAnimation = AnimationUtils.loadAnimation(context, R.anim.glow_animation);
            energyGlow.startAnimation(glowAnimation);
        }

        // Update stats text with training info
        statsText.setText(trainStatUpdated);

        // Auto-disappear after training completes (3 seconds)
        new Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                // Show training results
                Toast.makeText(context,
                        lutemon.getName() + " completed training!",
                        Toast.LENGTH_SHORT).show();
            }
        }, 3000); // 3 seconds
    }

    private void showMoveDialog(Context context, Lutemon lutemon, String currentContainer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Move " + lutemon.getName() + " to:");

        List<String> destinations = new ArrayList<>();
        destinations.add("Home");
        destinations.add("Training Area");
        destinations.add("Battle Area");
        destinations.remove(currentContainer); // Cant move to the same container

        String[] options = destinations.toArray(new String[0]);

        builder.setItems(options, (dialog, which) -> {
            String destination = options[which];
            moveLutemon(context, lutemon, currentContainer, destination);
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void moveLutemon(Context context, Lutemon lutemon, String fromContainerName, String toContainerName) {
        LutemonManager manager = LutemonManager.getInstance(context);

        Container fromContainer = null;
        switch (fromContainerName) {
            case "Home":
                fromContainer = manager.getHome();
                break;
            case "Training Area":
                fromContainer = manager.getTrainingArea();
                break;
            case "Battle Area":
                fromContainer = manager.getBattleArea();
                break;
        }

        Container toContainer = null;
        switch (toContainerName) {
            case "Home":
                toContainer = manager.getHome();
                break;
            case "Training Area":
                toContainer = manager.getTrainingArea();
                break;
            case "Battle Area":
                toContainer = manager.getBattleArea();
                break;
        }

        if (fromContainer != null && toContainer != null) {
            manager.moveLutemon(lutemon.getId(), fromContainer, toContainer);
            Toast.makeText(context, lutemon.getName() + " moved to " + toContainerName, Toast.LENGTH_SHORT).show();

            lutemons.remove(lutemon);
            notifyDataSetChanged();
        }


    }



    public int getItemCount() {
        return lutemons != null ? lutemons.size() : 0;
    }
}
