package com.tema1.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasePlayer {
    protected final int id;
    protected static int s = 0;
    protected int gold;
    protected ArrayList<Goods> hand = new ArrayList<>();
    protected Sack sack = new Sack();
    protected ArrayList<Goods> stall = new ArrayList<>();
    protected int score;

    public BasePlayer() {
        this.id = 0;
        this.gold = Constants.INIT_GOLD;
        this.score = 0;
    }

    public BasePlayer(final int id) {
        this.id = id;
        this.gold = Constants.INIT_GOLD;
        this.score = 0;
    }

    final int getId() {
        return this.id;
    }

    final int getScore() {
        return this.score;
    }

    final int getGold() {
        return this.gold;
    }

    final Sack getSack() {
        return this.sack;
    }

    final void drawHand(final ArrayList<Goods> deck) {
        this.hand.clear();  // Arde cartile vechi
        this.hand.addAll(deck.subList(s, s + Constants.NO_CARDS_IN_HAND)); // ia 10 carti
        s += Constants.NO_CARDS_IN_HAND;
    }

    /**
     * Crearea sacului.
     * @param round useful pentru greedy
     */
    void createSack(final int round) {
        //System.out.println("HAND" + this.hand);
        this.sack.clearSack();
        ArrayList<Goods> items = new ArrayList<>();
        int ok = 0;
        for (Goods good : hand) {
            if (good.getType() == GoodsType.Legal) {
                ok = 1;
                break;
            }
        }
        if (ok == 0) {    // Cazul in care nu are niciun obiect legal
            if (this.gold < Constants.MIN_GOLD_BAG) {
                // Daca nu are suficient gold pt a plati,penalty face un sac gol
                return;
            }
            int maxProfit = 0;
            Goods maxProfitItem = new Goods();
            for (Goods good : hand) {
                if (good.getProfit() > maxProfit) {
                    maxProfit = good.getProfit();
                    maxProfitItem = good;
                }
            }
            Goods good;
            for (int i = 0; i < this.hand.size(); i++) {
                good = this.hand.get(i);
                if (maxProfitItem.getId() == good.getId()) {
                    this.hand.remove(i);
                    items.add(good);
                    break;
                }
            }
            this.sack.createSack(0, 0, items);
            return;
        }

        // Cazul in care exista cel putin un obiect legal
        int maxFr = 0;
        Goods maxFrItem = new Goods();
        Map<Goods, Integer> frequency = new HashMap<>();
        // Calculez frecventa fiecarui bun
        for (Goods good : hand) {
            frequency.put(good, frequency.getOrDefault(good, 0) + 1);
        }
        // Aleg bunurle cu cea mai mare frecventa, profit, id
        for (Goods key : frequency.keySet()) {
            if (frequency.get(key) > maxFr && key.getType() == GoodsType.Legal) {
                maxFr = frequency.get(key);
                maxFrItem = key;
                continue;
            }
            if (key.getType() == GoodsType.Legal
                    && ((frequency.get(key) == maxFr && key.getProfit() > maxFrItem.getProfit())
                            || (frequency.get(key) == maxFr
                                && key.getProfit() == maxFrItem.getProfit()
                                    && key.getId() > maxFrItem.getId()))) {
                maxFrItem = key;
            }
        }
        for (Goods good : this.hand) {
            if (good.getId() == maxFrItem.getId()) {
                items.add(good);
            }
            if (items.size() == Constants.MAX_ITEMS_IN_SACK) {
                break;
            }
        }
        this.sack.createSack(0, maxFrItem.getId(), items);
    }

    final void updateGold(final int value) {
        this.gold += value;
    }

    final void addToStall(final ArrayList<Goods> passedGoods) {
        this.stall.addAll(passedGoods);
    }

    /**
     * Crearea sacului.
     * @param player playerul al carui sac e inspectat
     * @param initcoins useful pentru bribed sa nu inspecteze inainte de a lua mita
     * @return lista de carti confiscate
     */
    ArrayList<Goods> verify(final BasePlayer player, final int initcoins) {
        Sack playerSack = player.getSack();
        ArrayList<Goods> confiscated = new ArrayList<>();
        ArrayList<Goods> passedGoods = new ArrayList<>();
        // Returneaza mita daca exista
        player.updateGold(playerSack.getBribe());

        // Seriful isi termina turul de serif daca nu are bani destui
        if (this.gold < Constants.MIN_GOLD_INSPECT) {
            player.addToStall(playerSack.getItems());  // Playerul isi pune obiectele pe taraba
            return confiscated;
        }

        int penalty = 0;
        Sack sackCopy = new Sack();
        sackCopy.createSack(playerSack.getBribe(), playerSack.getitemDeclared(),
                playerSack.getItems());
        for (Goods good : sackCopy.getItems()) {
            if (good.getId() != playerSack.getitemDeclared()
                    || good.getType() == GoodsType.Illegal) {
                // Item ilegal sau nedeclarat, se confisca
                // Calculeaza penalty (cat primeste seriful = cat pierde comerciantul)
                penalty += good.getPenalty();
                confiscated.add(good);
                playerSack.removeFromSack(good);
            }
        }
        if (penalty == 0) {   // Daca nu a mintit comerciantul, calculez cat datoreaza seriful
            for (Goods good : playerSack.getItems()) {
                penalty -= good.getPenalty();
            }
        }
        for (Goods good : playerSack.getItems()) {
            if (good.getId() == playerSack.getitemDeclared()) {
                passedGoods.add(good);
            }
        }
        player.addToStall(passedGoods);    // Playerul pune pe stall obiectele cu care a trecut
        // Se achita penalty
        this.gold += penalty;
        player.updateGold(-penalty);
        return confiscated;
    }

    final void sellItems() {
        for (Goods good : this.stall) {   // Vinde toate itemele de pe taraba
            this.gold += good.getProfit();
        }
        ArrayList<Goods> bonusItems = new ArrayList<>();
        for (Goods good : this.stall) {   // Adauga bonusul adus de cartile ilegale la scor
            if (good.getType() == GoodsType.Illegal) {
                Map<Goods, Integer> bonusMap = ((IllegalGoods) good).getIllegalBonus();
                for (Goods goodie : bonusMap.keySet()) {
                    for (int i = bonusMap.get(goodie); i > 0; i--) {
                        bonusItems.add(goodie);
                        this.score += goodie.getProfit();
                    }
                }
            }
        }
        this.stall.addAll(bonusItems);
        this.score += this.gold;
    }

    final int getCardFrequency(final int goodId) {  // returneaza frecventa cartii specificate
        int counter = 0;
        for (Goods goodie : this.stall) {
            if (goodie.getId() == goodId) {
                counter++;
            }
        }
        return counter;
    }

    final void updateScore(final int newScore) {
        this.score += newScore;
    }

    /**
     * Printarea scorului.
     */
    void printScore() {
        System.out.println(this.id + " BASIC " + this.score);
    }
}
