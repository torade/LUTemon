package com.example.androidproject.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.activities.FightActivity;
import com.example.androidproject.activities.LutemonItemActivity;
import com.example.androidproject.containers.Container;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

public class LutemonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private List<Lutemon> lutemons;
    private final Context context;
    private final OnItemClickListener listener;
    private final String currentContainer;

    private Lutemon firstSelectedLutemon = null;
    private Lutemon secondSelectedLutemon = null;
    private String trainStatUpdated = "";

    /*
    -----------------------------------------------------------------------------------
    constructor:
    -----------------------------------------------------------------------------------
     */
    public LutemonAdapter(List<Lutemon> lutemons, Context context, OnItemClickListener listener, String currentContainer) {
        this.lutemons = lutemons;
        this.context = context;
        this.listener = listener;
        this.currentContainer = currentContainer;
    }

    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lutemon_item, parent, false);
        return new LutemonItemActivity.LutemonViewHolder(view, listener);
    }

    /**
     * Updates the list of lutemons and notifies the adapter that the data has changed.
     *
     * @param newLutemons List of updated lutemons
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateLutemons(List<Lutemon> newLutemons) {
        this.lutemons = newLutemons;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lutemon lutemon = lutemons.get(position);
        LutemonItemActivity.LutemonViewHolder lutemonHolder = (LutemonItemActivity.LutemonViewHolder) holder;

        try {
            // Set Lutemon image based on color
            switch (lutemon.getColor()) {
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
                default:
                    lutemonHolder.getLutemonImageView().setImageResource(R.drawable.lutemon_placeholder_sprite);
                    break;
            }

            // Populate text fields
            lutemonHolder.getNameTextView().setText(lutemon.getName());
            lutemonHolder.getColorTextView().setText(lutemon.getColor());
            lutemonHolder.getWinsText().setText("Wins: " + lutemon.getWinCount());
            lutemonHolder.getBattleCountText().setText("Battles: " + lutemon.getBattleCount());
            lutemonHolder.getTrainingCountText().setText("Trainings: " + lutemon.getTrainingCount());


            String stats = "POW: " + lutemon.getPower() + " DEF: " + lutemon.getDefense() + " HP: " + lutemon.getHealth() + "/" + lutemon.getMaxHealth() + " XP: " + lutemon.getExperience();
            lutemonHolder.getStatsTextView().setText(stats);

            // Move button
            lutemonHolder.getMoveButton().setOnClickListener(v -> showMoveDialog(context, lutemon, currentContainer));

            Button actionButton = lutemonHolder.getActionButton();

            // Behavior depends on current container
            switch (currentContainer) {
                case "Training Area":
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
                    break;

                case "Battle Area":
                    actionButton.setText("Select for Battle");
                    actionButton.setVisibility(View.VISIBLE);
                    actionButton.setEnabled(firstSelectedLutemon != lutemon && secondSelectedLutemon != lutemon);

                    actionButton.setOnClickListener(v -> {
                        if (firstSelectedLutemon == null) {
                            firstSelectedLutemon = lutemon;
                            Toast.makeText(context, lutemon.getName() + " selected as first fighter!", Toast.LENGTH_SHORT).show();
                        } else if (secondSelectedLutemon == null && lutemon != firstSelectedLutemon) {
                            secondSelectedLutemon = lutemon;
                            Toast.makeText(context, lutemon.getName() + " selected as second fighter!", Toast.LENGTH_SHORT).show();

                            // Start fight activity
                            Intent intent = new Intent(context, FightActivity.class);
                            intent.putExtra("firstLutemon", firstSelectedLutemon.getId());
                            intent.putExtra("secondLutemon", secondSelectedLutemon.getId());
                            context.startActivity(intent);

                            // Reset for next fight
                            firstSelectedLutemon = null;
                            secondSelectedLutemon = null;
                        }
                        notifyDataSetChanged();
                    });
                    break;

                default:
                    actionButton.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.e("LutemonAdapter", "Error binding lutemon data: " + e.getMessage());
        }
    }

    private void showTrainingDialog(Context context, Lutemon lutemon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_training_animation, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        ImageView lutemonImage = dialogView.findViewById(R.id.trainingLutemonImage);
        TextView statsText = dialogView.findViewById(R.id.trainingStatsText);
        View energyGlow = dialogView.findViewById(R.id.energyGlowView);

        // Set image based on color
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

        AlertDialog dialog = builder.create();
        dialog.show();

        // Animate image and glow
        lutemonImage.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_animation));
        if (energyGlow != null) {
            energyGlow.startAnimation(AnimationUtils.loadAnimation(context, R.anim.glow_animation));
        }

        statsText.setText(trainStatUpdated);

        // Save the updated lutemon state
        LutemonManager.getInstance(context).saveLutemons();

        // Auto-close dialog after delay
        new Handler().postDelayed(() -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                Toast.makeText(context, lutemon.getName() + " completed training!", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged(); // Refresh the entire list to update all stats
            }
        }, 3000);
    }

    private void showMoveDialog(Context context, Lutemon lutemon, String currentContainer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Move " + lutemon.getName() + " to:");

        List<String> destinations = new ArrayList<>();
        destinations.add("Home");
        destinations.add("Training Area");
        destinations.add("Battle Area");
        destinations.add("Trash");
        destinations.remove(currentContainer);

        String[] options = destinations.toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.dialog_move_item) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                String item = getItem(position);
                textView.setText(item);

                if ("Trash".equals(item)) {
                    textView.setTextColor(Color.RED);
                    textView.setTypeface(null, Typeface.BOLD);
                } else {
                    textView.setTextColor(Color.BLACK);
                    textView.setTypeface(null, Typeface.NORMAL);
                }

                return textView;
            }
        };
        adapter.addAll(destinations);

        builder.setAdapter(adapter, (dialog, which) -> {
            String destination = options[which];
            if (destination.equals("Trash")) {
                showDeleteConfirmation(context, lutemon);
            } else {
                moveLutemon(context, lutemon, currentContainer, destination);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmation(Context context, Lutemon lutemon) {
        new AlertDialog.Builder(context).setTitle("Delete " + lutemon.getName()).setMessage("This action cannot be undone.").setPositiveButton("Delete", (dialog, which) -> {
            LutemonManager manager = LutemonManager.getInstance(context);
            if (manager.deleteLutemon(lutemon.getId())) {
                lutemons.remove(lutemon);
                notifyDataSetChanged();
                Toast.makeText(context, lutemon.getName() + " has been deleted", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", null).show();
    }

    /**
     * Moves a lutemon from one container to another.
     *
     * @param context           Context
     * @param lutemon           Lutemon to move
     * @param fromContainerName Name of the container to move from
     * @param toContainerName   Name of the container to move to
     */
    private void moveLutemon(Context context, Lutemon lutemon, String fromContainerName, String toContainerName) {
        LutemonManager manager = LutemonManager.getInstance(context);

        Container fromContainer = getContainerByName(manager, fromContainerName);
        Container toContainer = getContainerByName(manager, toContainerName);

        if (fromContainer != null && toContainer != null) {
            manager.moveLutemon(lutemon.getId(), fromContainer, toContainer);
            Toast.makeText(context, lutemon.getName() + " moved to " + toContainerName, Toast.LENGTH_SHORT).show();

            lutemons.remove(lutemon);
            notifyDataSetChanged();
        }
    }

    /**
     * Gets the container by its name.
     *
     * @param manager LutemonManager
     * @param name    Name of the container
     * @return Container
     */
    private Container getContainerByName(LutemonManager manager, String name) {
        switch (name) {
            case "Home":
                return manager.getHome();
            case "Training Area":
                return manager.getTrainingArea();
            case "Battle Area":
                return manager.getBattleArea();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return lutemons != null ? lutemons.size() : 0;
    }
}
