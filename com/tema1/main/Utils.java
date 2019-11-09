package com.tema1.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class Utils {
    private int noPlayers;
    private static Utils instance = null;

    private Utils(final int noPlayers) {
        this.noPlayers = noPlayers;
    }

    static Utils getInstance(final int noPlayers) { // Singleton method
        if (instance == null) {
            instance = new Utils(noPlayers);
        }
        return instance;
    }

    // Returneaza cea mai buna carte din hand
    Goods getBestCard(final ArrayList<Goods> hand) {
        int maxProfit = 0;
        Goods bestItem = new Goods();
        Map<Goods, Integer> profit = new HashMap<>();

        for (Goods good : hand) {
            profit.put(good, good.getProfit());
        }

        for (Goods key : profit.keySet()) {
            if (profit.get(key) > maxProfit) {
                maxProfit = profit.get(key);
                bestItem = key;
                continue;
            }
            if (profit.get(key) == maxProfit && key.getProfit() == bestItem.getProfit()
                    && key.getId() > bestItem.getId()) {
                bestItem = key;
            }
        }
        return bestItem;
    }

    // Returneaza true daca sunt vecini p1 si p2
    boolean neighbour(final BasePlayer p1, final BasePlayer p2) {
        return p1.getId() == p2.getId() + 1 || p1.getId() == p2.getId() - 1
                || (p1.getId() == noPlayers - 1 && p2.getId() == 0)
                || (p1.getId() == 0 && p2.getId() == noPlayers - 1);
    }
}
