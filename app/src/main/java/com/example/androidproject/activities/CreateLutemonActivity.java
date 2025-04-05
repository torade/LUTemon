package com.example.androidproject.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.R;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

public class CreateLutemonActivity extends AppCompatActivity {
    private EditText nameInput;
    private RadioGroup colorGroup;
    private Button createButton;
    private LutemonManager lutemonManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lutemon_creator);

        // Initialize views
        nameInput = findViewById(R.id.nameInput);
        colorGroup = findViewById(R.id.colorGroup);
        createButton = findViewById(R.id.createLutemonButton);

        // Initialize LutemonManager
        lutemonManager = LutemonManager.getInstance(this);

        // Set up create button click listener
        createButton.setOnClickListener(v -> createLutemon());
    }

    private void createLutemon() {
        String name = nameInput.getText().toString().trim();
        if(name.isEmpty() || name.length() > 15 || name.equals("null")) {
            Toast.makeText(this, "Invalid lutemon name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if name already exists
        for (Lutemon lutemon : lutemonManager.getHome().getAllLutemons()) {
            if (lutemon.getName().equals(name)) {
                Toast.makeText(this, "Lutemon name already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        int colorId = colorGroup.getCheckedRadioButtonId();
        if(colorId == -1) {
            Toast.makeText(this, "Please select a color", Toast.LENGTH_SHORT).show();
            return;
        }

        String color;
        if (colorId == R.id.whiteRadio) {
            color = "White";
        }
        else if (colorId == R.id.greenRadio) {
            color = "Green";
        }
        else if (colorId == R.id.pinkRadio) {
            color = "Pink";
        }
        else if (colorId == R.id.orangeRadio) {
            color = "Orange";
        }
        else {
            color = "Black";
    }

        try {
            lutemonManager.createLutemon(name, color);

            Toast.makeText(this, "Created " + color + " Lutemon: " + name, Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error creating lutemon", Toast.LENGTH_SHORT).show();
        }

        // Clear text
        nameInput.setText("");
        colorGroup.clearCheck();
    }
}
