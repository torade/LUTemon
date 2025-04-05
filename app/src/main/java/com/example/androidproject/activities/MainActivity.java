package com.example.androidproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set buttons to do something
        Button createButton = findViewById(R.id.createButton);
        Button homeButton = findViewById(R.id.homeButton);
        Button trainingButton = findViewById(R.id.trainingButton);
        Button battleButton = findViewById(R.id.battleButton);


        createButton.setOnClickListener(v -> {
            // Create new lutemon
            Intent intent = new Intent(MainActivity.this, CreateLutemonActivity.class);
            startActivity(intent);
        });
        homeButton.setOnClickListener(v -> {
            // Go to home view
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}