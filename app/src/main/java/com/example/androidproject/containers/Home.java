package com.example.androidproject.containers;

import com.example.androidproject.model.Lutemon;

public class Home extends Container
{
    private static Home instance;
    /*
    -----------------------------------------------------------------------------------
    private constructor (singleton):
    -----------------------------------------------------------------------------------
     */
    public Home() {}

    public static Home getInstance()
    {
        if (instance == null)
            instance = new Home();
        return instance;
    }
    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    public void healLutemon(Lutemon lutemon) { lutemon.resetHealth(); } // heal when returning home, might not be used
    @Override
    public void addLutemon(Lutemon lutemon)
    {
        lutemon.resetHealth(); // heal when returning home
        super.addLutemon(lutemon);
    }
}
