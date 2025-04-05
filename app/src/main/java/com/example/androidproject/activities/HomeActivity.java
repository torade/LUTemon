package com.example.androidproject.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.R;
import com.example.androidproject.adapter.LutemonAdapter;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private LutemonManager lutemonManager;
    private RecyclerView recyclerView;
    private LutemonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_storage_activity);

        // Set title of view (Necessary to differentiate between different views)
        TextView textView = findViewById(R.id.storageTitle);
        textView.setText("Home");

        // Set up recycler view
        recyclerView = findViewById(R.id.lutemonRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get lutemon manager
        lutemonManager = LutemonManager.getInstance(this);

        setupRecyclerView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list when returning to this activity
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<Lutemon> lutemonListHome = new ArrayList<>();
        try {
            lutemonListHome = lutemonManager.getHome().getAllLutemons();
            if (lutemonListHome == null || lutemonListHome.isEmpty()) {
                Toast.makeText(this, "No lutemons in home", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("HomeActivity", "Error getting lutemons", e);
            lutemonListHome = new ArrayList<>();
        }

        List<Lutemon> finalLutemonListHome = lutemonListHome;
        adapter = new LutemonAdapter(lutemonListHome, this, position -> {
            // Handle item click if needed
            Lutemon selectedLutemon = finalLutemonListHome.get(position);
            Toast.makeText(HomeActivity.this,
                    "Selected: " + selectedLutemon.getName(),
                    Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);
    }
}

    // Set up Back to Main Menu
//    Button backButton = findViewById(R.id.backButton);
//        backButton.setOnClickListener(v -> finish());
//}

