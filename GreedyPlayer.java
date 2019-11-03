package com.tema1.main;

import java.util.ArrayList;

public class GreedyPlayer extends BasePlayer {

    public GreedyPlayer(int id){
        super(id);
    }

    public void createSack(int round){
        /*System.out.println("GREEDY round " + round);
        System.out.println(this.hand);*/
        super.createSack(round);
        if(round % 2 == 0){
            if(sack.getItems().size() >= 8 || this.gold < 4){
                return;
            }
            Goods max_profit_item = new Goods();
            int ok = 0;
            for(Goods good : hand){
                if(good.getType() == GoodsType.Illegal && good.getProfit() > max_profit_item.getProfit()){
                    max_profit_item = good;
                    ok = 1;
                }
            }
            if(ok == 1) {
                this.sack.addToSack(max_profit_item);
            }
        }
    }

    public ArrayList<Goods> verify(BasePlayer player, int initcoins) {
        int bribe = player.getSack().getBribe();
        ArrayList<Goods> items = new ArrayList<>();
        if(bribe > 0){
            this.gold += bribe;
            player.addToStall(player.getSack().getItems());
        }
        else{
            items = super.verify(player, initcoins);
        }
        return items;
    }

    public void printScore(){
        System.out.println(this.id + " GREEDY " + this.score);
    }
}
