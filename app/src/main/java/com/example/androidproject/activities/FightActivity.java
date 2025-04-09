package com.example.androidproject.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.R;
import com.example.androidproject.battle.Battle;
import com.example.androidproject.managers.LutemonManager;
import com.example.androidproject.model.Lutemon;

import java.util.List;

public class FightActivity extends AppCompatActivity {

    private Lutemon a, b;
    private TextView battleLogView, aName, bName, aStats, bStats;
    private ImageView aImage, bImage;
    private Button startBattleButton, backButton;
    private LutemonManager lutemonManager;
    private Battle battle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        battle = new Battle();

        //initialize views
        initializeViews();

        //get lutemons from intent (LutemonAdapter)
        int firstLutemonId = getIntent().getIntExtra("firstLutemon", -1);
        int secondLutemonId = getIntent().getIntExtra("secondLutemon", -1);
        if (firstLutemonId == -1 || secondLutemonId == -1) {
            Toast.makeText(this, "Invalid LUTemon selection", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        lutemonManager = LutemonManager.getInstance(this);
        a = lutemonManager.getBattleArea().getLutemon(firstLutemonId);
        b = lutemonManager.getBattleArea().getLutemon(secondLutemonId);

        if (a == null || b == null) {
            Toast.makeText(this, "Couldn't find selected LUTemons", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //set up lutemon displays
        setupLutemonDisplays();

        //listeners
        setupButtonListeners();

    }

    private void initializeViews() {
        battleLogView = findViewById(R.id.battleLogView);
        aName = findViewById(R.id.firstLutemonName);
        bName = findViewById(R.id.secondLutemonName);
        aStats = findViewById(R.id.firstLutemonStats);
        bStats = findViewById(R.id.secondLutemonStats);
        aImage = findViewById(R.id.firstLutemonImage);
        bImage = findViewById(R.id.secondLutemonImage);
        startBattleButton = findViewById(R.id.startBattleButton);
        backButton = findViewById(R.id.backButton);
    }

    private void setupLutemonDisplays() {
        //names
        aName.setText(a.getName());
        bName.setText(b.getName());

        //stats
        updateStats();

        //images
        setLutemonImage(a, aImage);
        setLutemonImage(b, bImage);
    }

    private void setLutemonImage(Lutemon lutemon, ImageView imageView) {
        switch (lutemon.getColor()) {
            case "White":
                imageView.setImageResource(R.drawable.lutemon_white);
                break;
            case "Green":
                imageView.setImageResource(R.drawable.lutemon_green);
                break;
            case "Pink":
                imageView.setImageResource(R.drawable.lutemon_pink);
                break;
            case "Orange":
                imageView.setImageResource(R.drawable.lutemon_orange);
                break;
            case "Black":
                imageView.setImageResource(R.drawable.lutemon_black);
                break;
            default:
                imageView.setImageResource(R.drawable.lutemon_placeholder_sprite);
        }
    }

    private void updateStats() {
        aStats.setText(String.format("POW:   %d \nDEF:   %d \nHP:   %d / %d \nXP:   %d", a.getPower(), a.getDefense(), a.getHealth(), a.getMaxHealth(), a.getExperience()));
        bStats.setText(String.format("POW:   %d \nDEF:   %d \nHP:   %d / %d \nXP:   %d", b.getPower(), b.getDefense(), b.getHealth(), b.getMaxHealth(), b.getExperience()));
    }

    private void setupButtonListeners() {
        startBattleButton.setOnClickListener(v -> {
            startBattle();
        });
        backButton.setOnClickListener(v -> {
            finish(); //return to previous activity
        });
    }

    private void startBattle() {
        // Disable buttons during battle
        startBattleButton.setEnabled(false);
        backButton.setEnabled(false);

        // Clear previous battle log and reset Lutemons to full health if needed
        battleLogView.setText("");

        //save initial xp values before the battle
        final int originalXpA =a.getExperience();
        final int originalXpB = b.getExperience();

        // Start fight and get log
        List<String> battleLog = battle.fight(a, b); // the fight is already done by this point, but we need to update stats after each hit using the log

        final int finalXpA = a.getExperience();
        final int finalXpB = b.getExperience();

        //reset xp to originals (for visual display during animation)
        a.setExperience(originalXpA);
        b.setExperience(originalXpB);
        updateStats();

        // Display log entries one by one with delay
        final int[] logIndex = {0};
        Handler handler = new Handler();

        Runnable battleSequence = new Runnable() {
            @Override
            public void run() {
                if (logIndex[0] < battleLog.size()) {

                    String logEntry = battleLog.get(logIndex[0]);
                    //check if stats need to be updated:
                    if (logEntry.startsWith("STATS:"))
                    {
                        //handle stats
                        String[] stats = logEntry.split(":");
                        if (stats.length == 3 || stats.length ==5 ) {
                            a.setHealth(Integer.parseInt(stats[1]));
                            b.setHealth(Integer.parseInt(stats[2]));
                            updateStats();
                        }
                    }else {
                            // Append new log entry
                            String currentText = battleLogView.getText().toString();
                            if (!currentText.isEmpty()) {
                                currentText += "\n";
                            }
                            battleLogView.setText(currentText + battleLog.get(logIndex[0]));

                            // Auto-scroll to bottom
                            final ScrollView scrollView = (ScrollView) battleLogView.getParent();
                            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));

                            // Apply animations based on the log entry
                            // logEntry = battleLog.get(logIndex[0]);
                            if (logEntry.contains("attacks")) {
                                String[] parts = logEntry.split(" ");
                                String attackerName = parts[0];
                                if (attackerName.equalsIgnoreCase(a.getName()))
                                    animateAttack(aImage);
                                else
                                    animateAttack(bImage);
                            }
                            else if (logEntry.contains("takes")) {
                                if (logEntry.contains(a.getName()))
                                    animateDamage(aImage);
                                else
                                    animateDamage(bImage);

                                // Update stats after damage is taken
                                updateStats();
                            } else if (logEntry.contains("defeated")) {
                                if (logEntry.contains(a.getName())) {
                                    animateDefeat(aImage);
                                } else {
                                    animateDefeat(bImage);
                                }
                                // Update stats after defeat
                                updateStats();
                            } else if (logEntry.contains("wins") && logEntry.contains("gains +")) {
                                // Update stats after battle ends
                                a.setExperience(finalXpA);
                                b.setExperience(finalXpB);
                                updateStats();
                            }
                        }
                    // Move to next log entry
                    logIndex[0]++;

                    // Schedule next entry after delay
                    handler.postDelayed(this, 1500); // 1.5 second delay between entries
                } else {
                    // Battle complete, enable back button
                    backButton.setEnabled(true);
                    Toast.makeText(FightActivity.this, "Battle complete!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Start the battle sequence
        handler.post(battleSequence);
    }

    // Animation methods
    private void animateAttack(ImageView attackerImage) {
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        attackerImage.startAnimation(pulse);
    }

    private void animateDamage(ImageView defenderImage) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake2_animation);
        defenderImage.startAnimation(shake);
    }

    private void animateDefeat(ImageView defeatedImage) {
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_animation);
        defeatedImage.startAnimation(fadeOut);
    }
}


//        //display log
//        StringBuilder logText = new StringBuilder();
//        for (String entry: battleLog)
//            logText.append(entry).append("\n");
//        battleLogView.setText(logText.toString());
//
//        //update stats after fight
//        updateStats();
//
//        //enable backButton
//        backButton.setEnabled(true);


//    }
//}