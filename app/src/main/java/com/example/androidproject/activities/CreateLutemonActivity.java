package com.example.androidproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidproject.R;
import com.example.androidproject.containers.Home;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.*;

import java.util.List;

public class CreateLutemonActivity extends AppCompatActivity {

    private EditText nameInput;
    private Spinner typeSpinner;
    private Button createButton;
    private ImageView lutemonImage;
    private TextView statsText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lutemon);

        nameInput = findViewById(R.id.nameInput);
        typeSpinner = findViewById(R.id.typeSpinner);
        createButton = findViewById(R.id.confirmLutemonCreationButton);
        lutemonImage = findViewById(R.id.lutemonImageCreate);
        statsText = findViewById(R.id.statsTextCreate);
        backButton = findViewById(R.id.cancelLutemonCreationButton);

        // Setup spinner options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.lutemon_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Show lutemon image and stats when selected
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePreview((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        backButton.setOnClickListener(v -> {
            finish();
        });



        createButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if(name.isEmpty() || name.length() > 15 || name.equals("null")) {
                Toast.makeText(this, "Invalid lutemon name", Toast.LENGTH_SHORT).show();
                return;
            }

            LutemonManager manager = LutemonManager.getInstance(this);
//            if (manager.getHome() == null) {
//                manager.initializeContainers();
//            }

            // Check if name already exists
            List<Lutemon> existingLutemons = manager.getHome().getAllLutemons();
            if (existingLutemons != null) {
                for (Lutemon lutemon : existingLutemons) {
                    if (lutemon.getName().equals(name)) {
                        Toast.makeText(this, "Lutemon name already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            String type = typeSpinner.getSelectedItem().toString();

            Lutemon lutemon = null;
            switch (type) {
                case "White":
                    lutemon = new WhiteLutemon(name);
                    break;
                case "Green":
                    lutemon = new GreenLutemon(name);
                    break;
                case "Pink":
                    lutemon = new PinkLutemon(name);
                    break;
                case "Orange":
                    lutemon = new OrangeLutemon(name);
                    break;
                case "Black":
                    lutemon = new BlackLutemon(name);
                    break;
            }

            if (lutemon != null) {
//                if (manager.getHome() == null) {
//                    manager.initializeContainers(); // THIS IS WHY IT'S WORKING. Home needs to be initialized.
//                }
                manager.getHome().addLutemon(lutemon); // Add lutemon to Home container
                lutemon.setExperience(10);               // !! FOR TESTING PURPOSES
                finish(); // Go back to Home fragment
            }
        });
    }

    private void updatePreview(String type) {
        // Set image based on type
        int imageResId;
        String stats;

        switch (type) {
            case "White":
                imageResId = R.drawable.lutemon_white;
                stats = "Attack: 5\nDefense: 4\nHealth: 20";
                break;
            case "Green":
                imageResId = R.drawable.lutemon_green;
                stats = "Attack: 6\nDefense: 3\nHealth: 19";
                break;
            case "Pink":
                imageResId = R.drawable.lutemon_pink;
                stats = "Attack: 7\nDefense: 2\nHealth: 18";
                break;
            case "Orange":
                imageResId = R.drawable.lutemon_orange;
                stats = "Attack: 8\nDefense: 1\nHealth: 17";
                break;
            case "Black":
                imageResId = R.drawable.lutemon_black;
                stats = "Attack: 9\nDefense: 0\nHealth: 16";
                break;
            default:
                imageResId = R.drawable.ic_launcher_foreground; // Default image
                stats = "";
        }

        lutemonImage.setImageResource(imageResId);
        statsText.setText(stats);
    }
}
