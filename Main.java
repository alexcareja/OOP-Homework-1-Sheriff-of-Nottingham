package com.tema1.main;

import java.util.*;
import java.util.stream.Collectors;


public final class Main {



    public static void main(final String[] args) {

        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);

        GameInput gameInput = gameInputLoader.load();
        List<String> player_names = gameInput.getPlayerNames();
        /*System.out.println(gameInput.getAssetIds());
        System.out.println(player_names);
        System.out.println(gameInput.getRounds());*/

        // Crearea deck-ului cu bunuri
        ArrayList<Goods> deck = new ArrayList<>();
        GoodsFactory factory = GoodsFactory.getInstance();
        for(int item : gameInput.getAssetIds()){
            deck.add(factory.getGoodsById(item));
        }

        // Declararea jucatorilor
        ArrayList<BasePlayer> players = new ArrayList<BasePlayer>();

        int id = 0;
        for(String s: gameInput.getPlayerNames()){
            switch (s){
                case "basic":
                    players.add(new BasePlayer(id));
                    break;
                case "bribed":
                    players.add(new BribedPlayer(id));
                    break;
                case "greedy":
                    players.add(new GreedyPlayer(id));
                    break;
            }
            id++;
        }

        int no_players = player_names.size();
        Utils.getInstance(no_players);  // Setez no_players in Utils
        int no_rounds = gameInput.getRounds();
        if(no_rounds > 7){
            no_rounds = 7;
        }
        BasePlayer p = new BasePlayer();
        BasePlayer sheriff = new BasePlayer();
        int initgold;
        for(int round = 1; round <= no_rounds; round++){
            for(int sub_round = 1; sub_round <= no_players; sub_round++){
                sheriff = players.get(sub_round - 1);
                initgold = sheriff.getGold();
                for(int i = 1; i <= no_players; i++){
                    if(i == sub_round){
                        continue;
                    }
                    p = players.get(i - 1);
                    p.drawHand(deck);   // Ia 10 carti
                    p.createSack(round);     // Creeaza sacul
                    // Verificarea sacului si adaugarea cartilor confiscate in deck
                    deck.addAll(sheriff.verify(p, initgold));
                    /*System.out.println("Dupa inspectie sheriful are "+ sheriff.getGold());
                    System.out.println("Dupa inspectie comerciantul are " +p.getGold());*/
                }
            }
        }
        for(BasePlayer plr : players){
            plr.sellItems();
        }
        Goods good = new Goods();
        int max_freq, second_max, freq, bonus;
        for(int i = 0; i <= 9; i++){    // Crearea map-ului
            max_freq = second_max = 0;
            good = factory.getGoodsById(i);
            bonus = ((LegalGoods)good).getKingBonus();
            Map<BasePlayer, Integer> bonus_map = new HashMap<>();
            for(int j = 0; j < no_players; j++){
                p = players.get(j);
                freq = p.getCardFrequency(good.getId());
                bonus_map.put(p, freq);
                if(freq > max_freq){
                    second_max = max_freq;
                    max_freq = freq;
                    continue;
                }
                if(freq > second_max){
                    second_max = freq;
                }

            }
            for(BasePlayer plr : players){
                if(bonus_map.get(plr) == max_freq && max_freq != 0){
                    plr.updateScore(bonus);
                    if(bonus == ((LegalGoods)good).getQueenBonus()){
                        bonus = 0;
                        break;
                    }
                    bonus = ((LegalGoods)good).getQueenBonus();    // king bonus -> queen bonus -> break
                }
            }
            if(bonus != 0){
                for(BasePlayer plr : players){
                    if(bonus_map.get(plr) == second_max && second_max != 0){
                        plr.updateScore(bonus);
                        break;
                    }
                }
            }
        }
        players.sort((BasePlayer p1, BasePlayer p2) -> p2.getScore() - p1.getScore());
        for(BasePlayer plr: players){
            plr.printScore();
        }
    }
}