package com.example.androidproject.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;

public class LutemonItemActivity {

    public static class LutemonViewHolder extends RecyclerView.ViewHolder {
        private ImageView lutemonImage;
        private TextView nameText;
        private TextView colorText;
        private TextView statsText;
        private Button moveButton;
        private Button actionButton;

        public LutemonViewHolder(View lutView, LutemonAdapter.OnItemClickListener listener) {
            super(lutView);
            lutemonImage = lutView.findViewById(R.id.lutemonImage);
            nameText = lutView.findViewById(R.id.nameText);
            colorText = lutView.findViewById(R.id.colorText);
            statsText = lutView.findViewById(R.id.statsText);
            moveButton = lutView.findViewById(R.id.moveButton);
            actionButton = lutView.findViewById(R.id.actionButton);

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
}
