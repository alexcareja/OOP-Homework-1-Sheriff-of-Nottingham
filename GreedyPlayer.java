package com.tema1.main;

import java.util.ArrayList;

public class GreedyPlayer extends BasePlayer {

    public GreedyPlayer(final int id) {
        super(id);
    }

    final void createSack(final int round) {
        /*System.out.println("GREEDY round " + round);
        System.out.println(this.hand);*/
        super.createSack(round);
        if (round % 2 == 0) {
            if (sack.getItems().size() >= Constants.MAX_ITEMS_IN_SACK
                    || this.gold < Constants.MIN_GOLD_BAG) {
                return;
            }
            Goods maxProfitItem = new Goods();
            int ok = 0;
            for (Goods good : hand) {
                if (good.getType() == GoodsType.Illegal
                        && good.getProfit() > maxProfitItem.getProfit()) {
                    maxProfitItem = good;
                    ok = 1;
                }
            }
            if (ok == 1) {
                this.sack.addToSack(maxProfitItem);
            }
        }
    }

    final ArrayList<Goods> verify(final BasePlayer player, final int initcoins) {
        int bribe = player.getSack().getBribe();
        ArrayList<Goods> items = new ArrayList<>();
        if (bribe > 0) {
            this.gold += bribe;
            player.addToStall(player.getSack().getItems());
        } else {
            items = super.verify(player, initcoins);
        }
        return items;
    }

    final void printScore() {
        System.out.println(this.id + " GREEDY " + this.score);
    }
}
