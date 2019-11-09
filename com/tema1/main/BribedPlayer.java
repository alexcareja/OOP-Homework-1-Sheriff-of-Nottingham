package com.tema1.main;

import java.util.ArrayList;

public class BribedPlayer extends BasePlayer {

    BribedPlayer(final int id) {
        super(id);
    }

    public final void createSack(final int round) {
        this.sack.clearSack();
        ArrayList<Goods> items = new ArrayList<>();
        int ok = 0;
        // Caut daca are macar un ilegal
        for (Goods good : this.hand) {
            if (good.getType() == GoodsType.Illegal) {
                ok = 1;
                break;
            }
        }
        if (ok == 0 || this.gold <= Constants.MIN_GOLD_BRIBED) {
            // Daca nu are niciun ilegal sau daca nu are destui bani-> stategia de baza
            super.createSack(round);
            return;
        }
        ArrayList<Goods> cards = new ArrayList<>(this.hand);
        ArrayList<Goods> sortedCards = new ArrayList<>();
        Goods g;
        Utils utils = Utils.getInstance(0);
        // Sortez cartile din mana
        while (cards.size() > 0) {
            g = utils.getBestCard(cards);
            cards.remove(g);
            sortedCards.add(g);
        }
        int penalty = 0;
        int noIllegals = 0;
        // Adaug cartile cele mai valoroase in sac in limita banilor
        for (Goods good : sortedCards) {
            if (items.size() == Constants.MAX_ITEMS_IN_SACK) {
                break;
            }
            if (penalty + good.getPenalty() < this.gold) {
                penalty += good.getPenalty();
                items.add(good);
                if (good.getType() == GoodsType.Illegal) {
                    noIllegals++;
                }
            }
        }
        int bribe = Constants.SMALL_BRIBE;
        if (noIllegals > 2) {
            bribe = Constants.BIG_BRIBE;
        }
        this.gold -= bribe;
        this.sack.createSack(bribe, 0, items);
    }
    // Returneaza cartile confiscate
    final ArrayList<Goods> verify(final BasePlayer player, final int initcoins) {
        Utils utils = Utils.getInstance(0);
        Sack playerSack = player.getSack();
        // Verifica doar vecinii
        if (utils.neighbour(this, player)) {
            if (initcoins >= Constants.MIN_GOLD_INSPECT) {
                return super.verify(player, initcoins);
            }
            player.updateGold(playerSack.getBribe());
            player.addToStall(playerSack.getItems());  // Playerul isi pune obiectele pe taraba
            return new ArrayList<>();
        }
        // Pe cine nu e vecin nu inspecteaza si ia mita
        this.gold += playerSack.getBribe();
        player.addToStall(player.getSack().getItems());
        return new ArrayList<>();
    }

    final void printScore() {
        System.out.println(this.id + " BRIBED " + this.score);
    }
}
