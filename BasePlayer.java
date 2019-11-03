package com.tema1.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasePlayer {
    int id;
    private static int s = 0;
    int gold;
    ArrayList<Goods> hand = new ArrayList<>();
    Sack sack = new Sack();
    private ArrayList<Goods> stall = new ArrayList<>();
    int score;

    public BasePlayer(){
        this.gold = 80;
        this.score = 0;
    }

    public BasePlayer(int id){
        this.id = id;
        this.gold = 80;
        this.score = 0;
    }

    public ArrayList<Goods> getStall(){
        return this.stall;
    }

    public int getId(){
        return this.id;
    }

    public int getScore(){
        return this.score;
    }

    public int getGold(){
        return this.gold;
    }

    public Sack getSack(){
        return this.sack;
    }

    public void drawHand(ArrayList<Goods> deck){
        this.hand.clear();
        this.hand.addAll(deck.subList(s, s+10));
        s += 10;
    }

    public void printHand(){
        System.out.println(hand);
    }

    public void createSack(int round){
        //System.out.println("HAND" + this.hand);
        this.sack.clearSack();
        ArrayList<Goods> items = new ArrayList<>();
        int ok = 0;
        for(Goods good : hand){
            if(good.getType() == GoodsType.Legal){
                ok = 1;
                break;
            }
        }
        if(ok == 0){    // Cazul in care nu are niciun obiect legal
            if(this.gold < 4){  // Daca nu are suficiente monede pt a plati penalty creeaza un sac gol
                return ;
            }
            int max_profit = 0;
            Goods max_profit_item = new Goods();
            for(Goods good : hand){
                if(good.getProfit() > max_profit){
                    max_profit = good.getProfit();
                    max_profit_item = good;
                }
            }
            Goods good = new Goods();
            for(int i = 0; i < this.hand.size(); i++){
                good = this.hand.get(i);
                if(max_profit_item.getId() == good.getId()){
                    this.hand.remove(i);
                    items.add(good);
                    break;
                }
            }
            this.sack.createSack(0, 0, items);
            return;
        }

        // Cazul in care exista cel putin un obiect legal
        int max_fr = 0;
        Goods max_fr_item = new Goods();
        Map<Goods, Integer> frequency = new HashMap<>();

        for (Goods good : hand) {
            frequency.put(good, frequency.getOrDefault(good, 0) + 1);
        }

        for (Goods key : frequency.keySet()) {
            if(frequency.get(key) > max_fr && key.getType() == GoodsType.Legal){
                max_fr = frequency.get(key);
                max_fr_item = key;
                continue;
            }
            if(key.getType() == GoodsType.Legal &&
                    ((frequency.get(key) == max_fr && key.getProfit() > max_fr_item.getProfit()) ||
                    (frequency.get(key) == max_fr && key.getProfit() == max_fr_item.getProfit()
                            && key.getId() > max_fr_item.getId()))){
                max_fr_item = key;
            }
        }
        for(Goods good : this.hand){
            if(good.getId() == max_fr_item.getId()){
                items.add(good);
            }
            if(items.size() == 8){
                break;
            }
        }
        this.sack.createSack(0, max_fr_item.getId(), items);
        // System.out.println(this.sack);
    }

    public void updateGold(int value){
        this.gold += value;
    }

    public void addToStall(ArrayList<Goods> passed_goods){
        this.stall.addAll(passed_goods);
    }

    public ArrayList<Goods> verify(BasePlayer player, int initcoins){ // Returneaza cartile confiscate
        Sack player_sack = player.getSack();
        ArrayList<Goods> confiscated = new ArrayList<>();
        ArrayList<Goods> passed_goods = new ArrayList<>();
        // Returneaza mita daca exista
        player.updateGold(player_sack.getBribe());

        // Seriful isi termina turul de serif daca nu are bani destui
        if(this.gold < 16){
            player.addToStall(player_sack.getItems());  // Playerul isi pune obiectele pe taraba
            return confiscated;
        }

        int penalty = 0;
        Sack sack_copy = new Sack();
        sack_copy.createSack(player_sack.getBribe(), player_sack.getItem_declared(), player_sack.getItems());
        for(Goods good : sack_copy.getItems()) {
            if (good.getId() != player_sack.getItem_declared() || good.getType() == GoodsType.Illegal) {
                // Item ilegal sau nedeclarat, se confisca
                // Calculeaza penalty (cat primeste seriful = cat pierde comerciantul)
                penalty += good.getPenalty();
                confiscated.add(good);
                player_sack.removeFromSack(good);
            }
        }
        if(penalty == 0){   // Daca nu a mintit comerciantul, calculez cat datoreaza seriful
            for(Goods good : player_sack.getItems()){
                penalty -= good.getPenalty();
            }
        }
        for(Goods good : player_sack.getItems()){
            if(good.getId() == player_sack.getItem_declared()){
                passed_goods.add(good);
            }
        }
        player.addToStall(passed_goods);    // Playerul isi pune pe taraba obiectele cu care a trecut
        // Se achita penalty
        this.gold += penalty;
        player.updateGold(-penalty);
        /*System.out.println("Penalty = " + penalty);
        System.out.println("sheriff gold = " + this.gold);
        System.out.println("comerciant gold = " + player.getGold());*/
        return confiscated;
    }

    public void sellItems(){
        for(Goods good : this.stall){   // Vinde toate itemele de pe taraba
            this.gold += good.getProfit();
        }
        ArrayList<Goods> bonus_items = new ArrayList<>();
        for(Goods good : this.stall) {   // Adauga bonusul adus de cartile ilegale la scor
            if (good.getType() == GoodsType.Illegal) {
                Map<Goods, Integer> bonus_map = ((IllegalGoods) good).getIllegalBonus();
                for (Goods goodie : bonus_map.keySet()) {
                    for (int i = bonus_map.get(goodie); i > 0; i--) {
                        bonus_items.add(goodie);
                        //this.stall.add(goodie);
                        this.score += goodie.getProfit();
                    }
                }
            }
        }
        this.stall.addAll(bonus_items);
        //System.out.println(this.gold);
        this.score += this.gold;
    }

    public int getCardFrequency(int id){
        int counter = 0;
        for(Goods goodie : this.stall){
            if(goodie.getId() == id){
                counter++;
            }
        }
        return counter;
    }

    public void updateScore(int score){
        this.score += score;
    }

    public void printScore(){
        System.out.println(this.id + " BASIC " + this.score);
    }
}
