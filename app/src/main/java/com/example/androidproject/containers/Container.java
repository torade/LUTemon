package com.example.androidproject.containers;

import com.example.androidproject.model.Lutemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Container //abstract class, not meant to be instantiated
{
    protected HashMap<Integer, Lutemon> lutemonHashMap;

    /*
    -----------------------------------------------------------------------------------
    constructor:
    -----------------------------------------------------------------------------------
     */
    public Container()
    {
        this.lutemonHashMap = new HashMap<>();
    }


    /*
    -----------------------------------------------------------------------------------
    methods:
    -----------------------------------------------------------------------------------
     */
    public void addLutemon(Lutemon lutemon) { lutemonHashMap.put(lutemon.getId(), lutemon); }
    public void removeLutemon(int id) { lutemonHashMap.remove(id); }
    public Lutemon getLutemon(int id) { return lutemonHashMap.get(id); }
    public List<Lutemon> getAllLutemons() { return new ArrayList<>(lutemonHashMap.values()); }
}
