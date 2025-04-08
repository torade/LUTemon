package com.example.androidproject.battle;

import java.util.ArrayList;
import java.util.List;
import com.example.androidproject.model.Lutemon;

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
        battleLog.clear();
        battleLog.add("Battle begins: " + A.getName() + " vs " + B.getName());

        Lutemon attacker = A;
        Lutemon defender = B;

        while (attacker.isAlive() && defender.isAlive())
        {
            int attackPower = attacker.getPower();
            battleLog.add(attacker.getName() + " attacks " + defender.getName() + " with power " + attackPower);


            int beforeHealth = defender.getHealth();
            defender.defend(attackPower);
            int afterHealth = defender.getHealth();
            int damage = beforeHealth - afterHealth;

            battleLog.add(defender.getName() + " takes " + damage + " damage");

            if (!defender.isAlive()) //when this condition is met, the loop ends (no break necessary)
            {
                battleLog.add(defender.getName() + " has been defeated");
                battleLog.add(attacker.getName() + " wins and gains +3xp");
                attacker.win();
                defender.lose();
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

    private void logStats(Lutemon A, Lutemon B)
    {
        battleLog.add(A.getName() + " (" + A.getColor() + "): " + "\nHP: "+ A.getHealth() + "\n / " + A.getMaxHealth() + "P: " + A.getPower() + "\nDEF: " + A.getDefense() + "\n XP: " + A.getExperience());
        battleLog.add(B.getName() + " (" + B.getColor() + "): " + "\nHP: "+ B.getHealth() + "\n / " + B.getMaxHealth() + "P: " + B.getPower() + "\nDEF: " + B.getDefense() + "\n XP: " + B.getExperience());
    }
}
