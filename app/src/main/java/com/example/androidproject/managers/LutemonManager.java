package com.example.androidproject.managers;

import android.content.Context;
import android.util.Log;

import com.example.androidproject.R;
import com.example.androidproject.battle.Battle;
import com.example.androidproject.containers.BattleArea;
import com.example.androidproject.containers.Container;
import com.example.androidproject.containers.Home;
import com.example.androidproject.containers.TrainingArea;
import com.example.androidproject.model.BlackLutemon;
import com.example.androidproject.model.GreenLutemon;
import com.example.androidproject.model.Lutemon;
import com.example.androidproject.model.OrangeLutemon;
import com.example.androidproject.model.PinkLutemon;
import com.example.androidproject.model.WhiteLutemon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LutemonManager
{
    private static LutemonManager instance;
    private Home home;
    private TrainingArea trainingArea;
    private BattleArea battleArea;
    private Battle battle;
    private Context context;
    /*
    -----------------------------------------------------------------------------------
    private constructor (singleton):
    -----------------------------------------------------------------------------------
     */
    private LutemonManager(Context context)
    {
        this.context = context;
        battle = new Battle();
    }
    public static LutemonManager getInstance(Context context) {
        if (instance == null) {
            instance = new LutemonManager(context.getApplicationContext());
            instance.initializeContainers();
            instance.loadLutemons();
            return instance;
        }
        return instance;
    }

    private void loadLutemons() {
        try {
            // Try to read from internal storage
            String jsonString;
            try {
                FileInputStream fis = context.openFileInput("lutemons.json");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                jsonString = jsonBuilder.toString();
                fis.close();
            } catch (IOException e) {
                // If file doesn't exist in internal storage, load default from raw resources
                Log.d("LutemonManager", "No saved file found, loading default from resources");
                InputStream is = context.getResources().openRawResource(R.raw.lutemons);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                jsonString = jsonBuilder.toString();
                is.close();

                // Save the default data to internal storage for future use
                FileOutputStream fos = context.openFileOutput("lutemons.json", Context.MODE_PRIVATE);
                fos.write(jsonString.getBytes());
                fos.close();
            }

            // Parse and process the JSON data
            JSONArray jsonArray = new JSONArray(jsonString);
            int loadedCount = 0;

            // Process each Lutemon in the array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extract fields from the JSON object
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                String color = jsonObject.getString("color");
                int health = jsonObject.getInt("health");
                int experience = jsonObject.getInt("experience");
                String location = jsonObject.getString("location");
                int maxHealth = jsonObject.getInt("maxHealth");
                int power = jsonObject.getInt("power");
                int defense = jsonObject.getInt("defense");
                int trainingCount = jsonObject.getInt("trainingCount");
                int battleCount = jsonObject.getInt("battleCount");
                int winCount = jsonObject.getInt("winCount");

                // Create appropriate Lutemon type based on color
                Lutemon lutemon;
                switch (color) {
                    case "Green":
                        lutemon = new GreenLutemon(name);
                        break;
                    case "Orange":
                        lutemon = new OrangeLutemon(name);
                        break;
                    case "Black":
                        lutemon = new BlackLutemon(name);
                        break;
                    case "Pink":
                        lutemon = new PinkLutemon(name);
                        break;
                    case "White":
                        lutemon = new WhiteLutemon(name);
                        break;
                    default:
                        Log.e("LutemonManager", "Unknown color: " + color + " for Lutemon " + name);
                        continue; // Skip this Lutemon
                }

                // Set properties from JSON
                lutemon.setHealth(health);
                lutemon.setExperience(experience);
                lutemon.setPower(power);
                lutemon.setDefense(defense);
                lutemon.setMaxHealth(maxHealth);
                lutemon.setTrainingCount(trainingCount);
                lutemon.setBattleCount(battleCount);
                lutemon.setWinCount(winCount);

                // Place in appropriate container based on location
                switch (location) {
                    case "home":
                        home.addLutemon(lutemon);
                        break;
                    case "training":
                        trainingArea.addLutemon(lutemon);
                        break;
                    case "battle":
                        battleArea.addLutemon(lutemon);
                        break;
                    default:
                        // Default to home if location is unknown
                        home.addLutemon(lutemon);
                }

                loadedCount++;
            }

            Log.d("LutemonManager", "Loaded " + loadedCount + " Lutemons from JSON");
        } catch (IOException e) {
            Log.e("LutemonManager", "Error reading JSON file", e);
        } catch (JSONException e) {
            Log.e("LutemonManager", "Error parsing JSON", e);
        }
    }

    public void saveLutemons() {
        try {
            // Create a new JSONArray to hold all Lutemons
            JSONArray jsonArray = new JSONArray();

            // Add Lutemons from each container
            addLutemonsToJsonArray(home.getAllLutemons(), "home", jsonArray);
            addLutemonsToJsonArray(trainingArea.getAllLutemons(), "training", jsonArray);
            addLutemonsToJsonArray(battleArea.getAllLutemons(), "battle", jsonArray);

            // Convert to a formatted JSON string
            String jsonString = jsonArray.toString(2); // 2 spaces for indentation

            // Write to internal storage
            FileOutputStream fos = context.openFileOutput("lutemons.json", Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();

            Log.d("LutemonManager", "Saved " + jsonArray.length() + " Lutemons to JSON");
        } catch (Exception e) {
            Log.e("LutemonManager", "Error saving Lutemons to JSON", e);
        }
    }

    private void addLutemonsToJsonArray(List<Lutemon> lutemons, String location, JSONArray jsonArray) throws JSONException {
        if (lutemons == null) return;

        for (Lutemon lutemon : lutemons) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", lutemon.getId());
            jsonObject.put("name", lutemon.getName());
            jsonObject.put("color", lutemon.getColor());
            jsonObject.put("health", lutemon.getHealth());
            jsonObject.put("experience", lutemon.getExperience());
            jsonObject.put("location", location);
            jsonObject.put("maxHealth", lutemon.getMaxHealth());
            jsonObject.put("power", lutemon.getPower());
            jsonObject.put("defense", lutemon.getDefense());
            jsonObject.put("trainingCount", lutemon.getTrainingCount());
            jsonObject.put("battleCount", lutemon.getBattleCount());
            jsonObject.put("winCount", lutemon.getWinCount());

            jsonArray.put(jsonObject);

            Log.d("LutemonManager", "Added lutemon to JSON: " + lutemon.getName() + " in " + location);
        }
    }

    public void initializeContainers() {
            home = Home.getInstance();
            Log.d("LutemonManager", "Setting Home container");

            trainingArea = TrainingArea.getInstance();
            Log.d("LutemonManager", "Created new TrainingArea container");

            battleArea = BattleArea.getInstance();
            Log.d("LutemonManager", "Created new BattleArea container");
    }
    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    public void moveLutemon(int id, Container from, Container to)
    {
        Lutemon lutemon = from.getLutemon(id);
        if (lutemon != null)
        {
            from.removeLutemon(id);
            to.addLutemon(lutemon);
            saveLutemons();
        }
    }

    public boolean deleteLutemon(int id) {
        // Try to find and remove the Lutemon from each container
        Lutemon lutemon = home.getLutemon(id);
        if (lutemon != null) {
            home.removeLutemon(id);
            saveLutemons();
            return true;
        }

        lutemon = trainingArea.getLutemon(id);
        if (lutemon != null) {
            trainingArea.removeLutemon(id);
            saveLutemons();
            return true;
        }

        lutemon = battleArea.getLutemon(id);
        if (lutemon != null) {
            battleArea.removeLutemon(id);
            saveLutemons();
            return true;
        }

        return false; // Lutemon not found in any container
    }

    /*
    -----------------------------------------------------------------------------------
    getters:
    -----------------------------------------------------------------------------------
     */
    public Home getHome() { return home; }
    public TrainingArea getTrainingArea() { return trainingArea; }
    public BattleArea getBattleArea() { return battleArea; }
    public Battle getBattle() { return battle; }

}
