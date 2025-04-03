package com.example.androidproject.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.example.androidproject.managers.LutemonManager;

public class HomeActivity extends AppCompatActivity
{
    private LutemonManager lutemonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_storage_activity);

        // Set title of view (Necessary to differentiate between different views)
        TextView textView = findViewById(R.id.storageTitle);
        textView.setText("Home");


        // Get lutemon manager
        lutemonManager = LutemonManager.getInstance(this);

        // Build list of lutemons next.
    }
}
