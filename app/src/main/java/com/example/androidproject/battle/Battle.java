package com.example.androidproject.battle;

import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.List;

public class Battle
{
    private List<String> battleLog;

    /*
    -----------------------------------------------------------------------------------
    constructor:
    -----------------------------------------------------------------------------------
     */
    public Battle() { this.battleLog = new ArrayList<>(); }
    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    public List<String> fight(Lutemon A, Lutemon B)
    {
        Lutemon attacker = A;
        Lutemon defender = B;

        battleLog.clear();
        battleLog.add("Battle begins: " + A.getName() + " vs " + B.getName());
        battleLog.add("STATS:" + A.getHealth() + ":" + B.getHealth()); //initial stats

        while (attacker.isAlive() && defender.isAlive())
        {
            int attackPower = Lutemon.getRandomNumber(2, attacker.getPower()); // Randomize attack power
            battleLog.add(attacker.getName() + " attacks " + defender.getName() + " with power " + attackPower);


            int beforeHealth = defender.getHealth();
            defender.defend(attackPower);
            int afterHealth = defender.getHealth();
            int damage = beforeHealth - afterHealth;

            battleLog.add(defender.getName() + " takes " + damage + " damage");
            battleLog.add("STATS:" + A.getHealth() + ":" + B.getHealth()); //update stats immediately after each hit

            if (!defender.isAlive()) //when this condition is met, the loop ends (no break necessary)
            {
                battleLog.add(defender.getName() + " has been defeated");
                int beforeXP = attacker.getExperience();
                attacker.win();
                int gainedXP = attacker.getExperience() - beforeXP;
                battleLog.add(attacker.getName() + " wins and gains +" + gainedXP + " xp");
                defender.lose();
                battleLog.add("STATS:" + A.getHealth() + ":" + B.getHealth() + ":" + A.getExperience() + ":" + B.getExperience()); //final stats
            }
            else {
                //swap roles
                Lutemon aux = attacker;
                attacker = defender;
                defender = aux;
            }
        }
        return battleLog;
    }

//    private void logStats(Lutemon A, Lutemon B)
//    {
//        battleLog.add(A.getName() + " (" + A.getColor() + "): " + "\nHP: "+ A.getHealth() + "\n / " + A.getMaxHealth() + "P: " + A.getPower() + "\nDEF: " + A.getDefense() + "\n XP: " + A.getExperience());
//        battleLog.add(B.getName() + " (" + B.getColor() + "): " + "\nHP: "+ B.getHealth() + "\n / " + B.getMaxHealth() + "P: " + B.getPower() + "\nDEF: " + B.getDefense() + "\n XP: " + B.getExperience());
//    }
}
