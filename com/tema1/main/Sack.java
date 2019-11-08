package com.tema1.main;

import java.util.ArrayList;

public class Sack {
    private int bribe;
    private int itemDeclared;
    private ArrayList<Goods> items = new ArrayList<>();

    public Sack() {
        this.bribe = 0;
        this.itemDeclared = 0;
    }

    final int getitemDeclared() {
        return this.itemDeclared;
    }

    final int getBribe() {
        return this.bribe;
    }

    final ArrayList<Goods> getItems() {
        return this.items;
    }

    final void createSack(final int newBribe, final int newItemDeclared,
                                 final ArrayList<Goods> newItems) {
        this.bribe = newBribe;
        this.itemDeclared = newItemDeclared;
        this.items.addAll(newItems);
    }

    final void clearSack() {
        this.bribe = 0;
        this.itemDeclared = 0;
        this.items.clear();
    }

    final void addToSack(final Goods good) {
        this.items.add(good);
    }

    final void removeFromSack(final Goods good) {
        this.items.remove(good);
    }

}
