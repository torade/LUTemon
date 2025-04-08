package com.example.androidproject.containers;

public class BattleArea extends  Container
{
    private static BattleArea instance;
    /*
    -----------------------------------------------------------------------------------
    private constructor (singleton):
    -----------------------------------------------------------------------------------
     */
    private BattleArea() {}
    public static BattleArea getInstance()
    {
        if (instance == null)
            instance = new BattleArea();
        return instance;
    }

}
