package com.tema1.main;

import java.util.ArrayList;

public class Sack {
    private int bribe;
    private int item_declared;
    private ArrayList<Goods> items = new ArrayList<>();

    public Sack(){
        this.bribe = 0;
        this.item_declared = 0;
    }

    public int getItem_declared(){
        return this.item_declared;
    }

    public int getBribe(){
        return this.bribe;
    }

    public ArrayList<Goods> getItems(){
        return this.items;
    }

    public void createSack(int bribe, int item_declared, ArrayList<Goods> items){
        this.bribe = bribe;
        this.item_declared = item_declared;
        this.items.addAll(items);
    }

    public void clearSack(){
        this.bribe = 0;
        this.item_declared = 0;
        this.items.clear();
    }

    public void addToSack(Goods good){
        this.items.add(good);
    }

    public void removeFromSack(Goods good){
        this.items.remove(good);
}

    @Override
    public String toString() {
        return "com.tema1.main.Sack{" +
                "bribe=" + bribe +
                ", item_declared=" + item_declared +
                ", items=" + items +
                '}';
    }
}
