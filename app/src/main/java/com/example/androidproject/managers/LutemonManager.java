package com.example.androidproject.managers;

import android.content.Context;
import android.util.Log;

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
        this.home.getInstance();
        this.trainingArea.getInstance();
        this.battleArea.getInstance();
        battle = new Battle();
        //loadLutemons(); //JSON FILE!! //maybe loading should be done more than just once (do it in getInstance() ???)
    }
    public static LutemonManager getInstance(Context context)
    {
        if (instance == null)
            instance = new LutemonManager(context.getApplicationContext());
        return instance;
    }

    public void initializeContainers() {
        if (home == null) {
            home = new Home();
            Log.d("LutemonManager", "Created new Home container");
        }
    }
    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    public void createLutemon(String color, String name)
    {
        Lutemon lutemon;
        switch (color)
        {
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
                throw new IllegalArgumentException("Invalid color: " + color);
        }
        home.addLutemon(lutemon);
        //saveLutemons(); //JSON FILE!!
    }
    public void moveLutemon(int id, Container from, Container to)
    {
        Lutemon lutemon = from.getLutemon(id);
        if (lutemon != null)
        {
            from.removeLutemon(id);
            to.addLutemon(lutemon);
        }
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
