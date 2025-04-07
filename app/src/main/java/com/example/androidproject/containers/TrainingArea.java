package com.example.androidproject.containers;

import com.example.androidproject.model.Lutemon;

public class TrainingArea extends Container
{
    private static TrainingArea instance;

    /*
    -----------------------------------------------------------------------------------
    private constructor (singleton):
    -----------------------------------------------------------------------------------
     */
    public TrainingArea() {}
    public static TrainingArea getInstance()
    {
        if (instance == null)
            instance = new TrainingArea();
        return instance;
    }
    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
//    public void trainLutemon(Lutemon lutemon) { lutemon.train(); }

}
