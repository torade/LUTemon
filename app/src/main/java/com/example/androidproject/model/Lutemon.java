package com.example.androidproject.model;

public abstract class Lutemon // abstract class not meant to be instantiated
{
    protected int id, power, defense, maxHealth, health, experience, trainingCount = 0, battleCount = 0, winCount = 0; // protected fields -> allows inheritance
    private static int ID = 0;
    protected String color, name;
    /*
    -----------------------------------------------------------------------------------
    constructor:
    -----------------------------------------------------------------------------------
     */
    public Lutemon(String name, String color, int power, int defense, int maxHealth)
    {
        this.id = ID++; // lutemons' ids start from 0
        this.name = name;
        this.color = color;
        this.power = power;
        this.defense = defense;
        this.health = maxHealth; // Start at max health
        this.maxHealth = maxHealth;
        this.experience = 0;
    }
    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    public void defend(int attackPower) // method that applies damage to the lutemon
    {
        int damage = attackPower - defense;
        if (damage > 0)
        {
            if (health - damage >= 0)
                health -= damage;
            else health = 0;
        }
    }
    public boolean isAlive() { return health > 0; }

    //training (CHANGE VALUES AFTER TESTING)
    public void train()
    {
        int choice = getRandomNumber(1, 3); // random attribute gets trained
        if (experience > 0)
        {
            experience--;
            if (choice == 1) {
                power += getRandomNumber(3, 7);
                System.out.println("Power increased by " + power);
            }
            else if (choice == 2) {
                defense += getRandomNumber(2, 4);
                System.out.println("Defense increased by " + defense);
            }
            else if (choice == 3) {
                maxHealth += getRandomNumber(2, 4);
                System.out.println("Max Health increased by " + maxHealth);
            }
            trainingCount++;
        }
    }
    public void win() { experience += getRandomNumber(1, 5); winCount++; battleCount++; }
    public void lose() { battleCount++;}
    public void resetHealth() { health = maxHealth; }
    /*
    -----------------------------------------------------------------------------------
    getters:
    -----------------------------------------------------------------------------------
     */
    public int getPower() { return power; }
    public String getName() { return name; }
    public int getId() { return id; }
    public String getColor() { return color; }
    public int getDefense() { return defense; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getExperience() { return experience; }
    public int getTrainingCount() { return trainingCount; }
    public int getBattleCount() { return battleCount; }
    public int getWinCount() { return winCount; }

    /*
    -----------------------------------------------------------------------------------
    RANDOM NUMBER BETWEEN MAX AND MIN:
    -----------------------------------------------------------------------------------
     */
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
