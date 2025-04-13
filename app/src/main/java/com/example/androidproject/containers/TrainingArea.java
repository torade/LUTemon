package com.example.androidproject.containers;


public class TrainingArea extends Container
{
    private static TrainingArea instance;

    /*
    -----------------------------------------------------------------------------------
    private constructor (singleton):
    -----------------------------------------------------------------------------------
     */
    private TrainingArea() {}
    public static TrainingArea getInstance()
    {
        if (instance == null)
            instance = new TrainingArea();
        return instance;
    }

}
