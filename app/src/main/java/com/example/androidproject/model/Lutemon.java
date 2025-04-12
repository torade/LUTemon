package com.example.androidproject.model;

public abstract class Lutemon // abstract class not meant to be instantiated
{
    protected int id, power, defense, maxHealth, health, experience, trainingCount = 0, battleCount = 0, winCount = 0; // protected fields -> allows inheritance
    private static int ID = 0;
    protected String color, name, location;
    /*
    -----------------------------------------------------------------------------------
    constructor:
    -----------------------------------------------------------------------------------
     */
    public Lutemon(String name, String color, int power, int defense, int maxHealth) { // FOR subclasses
        this.id = ID++; // Auto-generate ID
        this.name = name;
        this.color = color;
        this.power = power;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.experience = 0;
        this.location = "home";
        this.trainingCount = 0;
        this.battleCount = 0;
        this.winCount = 0;
    }


    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    public void defend(int attackPower) // method that applies damage to the lutemon
    {
        int damage = (int) (attackPower - (defense / 2.5));

        // Add a small chance for better defense
        if (Math.random() < 0.2) { // 20% chance for a better block
            damage = Math.max(1, damage / 2); // Cut damage in half but ensure at least 1 damage
        }

        // Ensure minimum damage is at least 12% of attack power
        int minimumDamage = Math.max(1, attackPower / 8);
        if (damage < minimumDamage) {
            damage = minimumDamage;
        }

        if (health - damage >= 0) {
            health -= damage;
        } else {
            health = 0;
        }
    }
    public boolean isAlive() { return health > 0; }

    public String train()
    {
        int choice = getRandomNumber(1, 3); // random attribute gets trained
        int increasedBy = 0;
        if (experience > 0)
        {
            this.experience--;
            if (choice == 1) {
                increasedBy = getRandomNumber(3, 7);
                this.power += increasedBy;
                return "Power increasing by " + increasedBy + "...";
            }
            else if (choice == 2) {
                increasedBy = getRandomNumber(2, 4);
                this.defense += increasedBy;
                return "Defense increasing by " + increasedBy + "...";
            }
            else if (choice == 3) {
                increasedBy = getRandomNumber(2, 4);
                this.maxHealth += increasedBy;
                return "Max Health increasing by " + increasedBy + "...";
            }
            this.trainingCount++;
        }
        return "No experience to train.";
    }
    public void win() { experience += getRandomNumber(1, 5); winCount++; battleCount++; }
    public void lose() { battleCount++;}
    public void resetHealth() { health = maxHealth; }
    /*
    -----------------------------------------------------------------------------------
    getters:
    -----------------------------------------------------------------------------------
     */
    public int getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public int getHealth() { return health; }
    public int getExperience() { return experience; }
    public String getLocation() { return location; }
    public int getMaxHealth() { return maxHealth; }
    public int getPower() { return power; }
    public int getDefense() { return defense; }
    public int getTrainingCount() { return trainingCount; }
    public int getBattleCount() { return battleCount; }
    public int getWinCount() { return winCount; }

    /*
    -----------------------------------------------------------------------------------
    setters
    -----------------------------------------------------------------------------------
     */
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void setExperience(int experience) { this.experience = experience; }
    public void setHealth(int health) { this.health = health; }
    public void setPower(int power) { this.power = power; }
    public void setDefense(int defense) { this.defense = defense; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }
    public void setTrainingCount(int trainingCount) { this.trainingCount = trainingCount; }
    public void setBattleCount(int battleCount) { this.battleCount = battleCount; }
    public void setWinCount(int winCount) { this.winCount = winCount; }
}
