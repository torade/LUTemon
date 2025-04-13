package com.example.androidproject.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.adapter.LutemonAdapter;
import com.example.androidproject.containers.Container;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

public class LutemonItemActivity {

    public static class LutemonViewHolder extends RecyclerView.ViewHolder {
        private ImageView lutemonImage;
        private TextView nameText;
        private TextView colorText;
        private TextView statsText;
        private Button moveButton;
        private Button actionButton;
        private TextView winsText;
        private TextView battleCountText;
        private TextView trainingCountText;

        public LutemonViewHolder(View lutView, LutemonAdapter.OnItemClickListener listener) {
            super(lutView);
            lutemonImage = lutView.findViewById(R.id.lutemonImage);
            nameText = lutView.findViewById(R.id.nameText);
            colorText = lutView.findViewById(R.id.colorText);
            statsText = lutView.findViewById(R.id.statsText);
            moveButton = lutView.findViewById(R.id.createLutemonButton);
            actionButton = lutView.findViewById(R.id.actionButton);
            winsText = lutView.findViewById(R.id.winsText);
            battleCountText = lutView.findViewById(R.id.battleCountText);
            trainingCountText = lutView.findViewById(R.id.trainingCountText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });

        }

        public Button getMoveButton() {
            return moveButton;
        }

        public Button getActionButton() {
            return actionButton;
        }
        public TextView getWinsText() { return winsText; }
        public TextView getBattleCountText() { return battleCountText; }
        public TextView getTrainingCountText() { return trainingCountText; }

        public ImageView getLutemonImageView() {
            return lutemonImage;
        }

        public TextView getNameTextView() {
            return nameText;
        }

        public TextView getColorTextView() {
            return colorText;
        }

        public TextView getStatsTextView() {
            return statsText;
        }

    }

        private void showMoveDialog(Context context, Lutemon lutemon, String currentContainer) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Move " + lutemon.getName() + " to:");

            // Create list of destinations (exclude current container)
            List<String> destinations = new ArrayList<>();
            destinations.add("Home");
            destinations.add("Training Area");
            destinations.add("Battle Area");
            destinations.remove(currentContainer); // Remove current location

            // Convert to array for dialog
            final String[] options = destinations.toArray(new String[0]);

            builder.setItems(options, (dialog, which) -> {
                String destination = options[which];
                moveLutemon(context, lutemon, currentContainer, destination);
            });

            builder.setNegativeButton("Cancel", null);
            builder.show();
        }

        private void moveLutemon(Context context, Lutemon lutemon, String fromContainerName, String toContainerName) {
            LutemonManager manager = LutemonManager.getInstance(context);

            // Get source container
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

            // Get destination container
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

            // Move the lutemon if both containers are valid
            if (fromContainer != null && toContainer != null) {
                manager.moveLutemon(lutemon.getId(), fromContainer, toContainer);
                Toast.makeText(context, lutemon.getName() + " moved to " + toContainerName, Toast.LENGTH_SHORT).show();

//                // Notify adapter to refresh (you'll need to call this from your Activity)
//                notifyDataSetChanged();
            }
        }





}
