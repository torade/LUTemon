package com.example.androidproject.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidproject.R;
import com.example.androidproject.containers.Home;
import com.example.androidproject.model.*;

public class CreateLutemonActivity extends AppCompatActivity {

    private EditText nameInput;
    private Spinner typeSpinner;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lutemon_creator);

        nameInput = findViewById(R.id.nameInput);
        typeSpinner = findViewById(R.id.typeSpinner);
        createButton = findViewById(R.id.confirmLutemonCreationButton);

        // Setup spinner options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.lutemon_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        createButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
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
                Home.getInstance().addLutemon(lutemon);
                finish(); // Go back to Home fragment
            }
        });
    }
}
