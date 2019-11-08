package com.tema1.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {

        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);

        GameInput gameInput = gameInputLoader.load();
        List<String> playerNames = gameInput.getPlayerNames();


        // Crearea deck-ului cu bunuri
        ArrayList<Goods> deck = new ArrayList<>();
        GoodsFactory factory = GoodsFactory.getInstance();
        for (int item : gameInput.getAssetIds()) {
            deck.add(factory.getGoodsById(item));
        }

        // Declararea jucatorilor
        ArrayList<BasePlayer> players = new ArrayList<>();

        int id = 0;
        for (String s : gameInput.getPlayerNames()) {
            switch (s) {
                case "basic":
                    players.add(new BasePlayer(id));
                    break;
                case "bribed":
                    players.add(new BribedPlayer(id));
                    break;
                case "greedy":
                    players.add(new GreedyPlayer(id));
                    break;
                default:
                    break;
            }
            id++;
        }

        int noPlayerss = playerNames.size();
        Utils.getInstance(noPlayerss);  // Setez noPlayerss in Utils
        int noRounds = gameInput.getRounds();
        if (noRounds > Constants.MAX_ROUNDS) {
            noRounds = Constants.MAX_ROUNDS;
        }
        BasePlayer p;
        BasePlayer sheriff;
        int initgold;
        for (int round = 1; round <= noRounds; round++) {
            for (int subRound = 1; subRound <= noPlayerss; subRound++) {
                sheriff = players.get(subRound - 1);
                initgold = sheriff.getGold();
                for (int i = 1; i <= noPlayerss; i++) {
                    if (i == subRound) {
                        continue;
                    }
                    p = players.get(i - 1);
                    p.drawHand(deck);   // Ia 10 carti
                    p.createSack(round);     // Creeaza sacul
                    // Verificarea sacului si adaugarea cartilor confiscate in deck
                    deck.addAll(sheriff.verify(p, initgold));
                }
            }
        }
        for (BasePlayer plr : players) {
            plr.sellItems();
        }
        Goods good;
        int maxFreq, secondMax, freq, bonus;
        for (int i = 0; i <= Constants.HIGHEST_LEGAL_ID; i++) {    // Crearea map-ului
            maxFreq = 0;
            secondMax = 0;
            good = factory.getGoodsById(i);
            bonus = ((LegalGoods) good).getKingBonus();
            Map<BasePlayer, Integer> bonusMap = new HashMap<>();
            for (int j = 0; j < noPlayerss; j++) {
                p = players.get(j);
                freq = p.getCardFrequency(good.getId());
                bonusMap.put(p, freq);
                if (freq > maxFreq) {
                    secondMax = maxFreq;
                    maxFreq = freq;
                    continue;
                }
                if (freq > secondMax) {
                    secondMax = freq;
                }

            }
            for (BasePlayer plr : players) {
                if (bonusMap.get(plr) == maxFreq && maxFreq != 0) {
                    // king bonus -> queen bonus -> break
                    plr.updateScore(bonus);
                    if (bonus == ((LegalGoods) good).getQueenBonus()) {
                        bonus = 0;
                        break;
                    }
                    bonus = ((LegalGoods) good).getQueenBonus();
                }
            }
            if (bonus != 0) {
                for (BasePlayer plr : players) {
                    if (bonusMap.get(plr) == secondMax && secondMax != 0) {
                        plr.updateScore(bonus);
                        break;
                    }
                }
            }
        }
        players.sort((BasePlayer p1, BasePlayer p2) -> p2.getScore() - p1.getScore());
        for (BasePlayer plr : players) {
            plr.printScore();
        }
    }
}
