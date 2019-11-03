import java.util.ArrayList;

public class BribedPlayer extends BasePlayer {

    public BribedPlayer(int id){
        super(id);
    }

    public void createSack(int round){
        this.sack.clearSack();
        ArrayList<Goods> items = new ArrayList<>();
        int ok = 0;
        for(Goods good : this.hand){
            if(good.getType() == GoodsType.Illegal){
                ok = 1;
                break;
            }
        }
        if(ok == 0 || this.gold <= 5){    // Daca nu are niciun ilegal -> stategia de baza
            super.createSack(round);
            return;
        }
        ArrayList<Goods> cards = new ArrayList<>();
        cards.addAll(this.hand);
        ArrayList<Goods> sorted_cards = new ArrayList<>();
        Goods g = new Goods();
        Utils utils = Utils.getInstance(0);
        while(cards.size() > 0){
            g = utils.getBestCard(cards);
            cards.remove(g);
            sorted_cards.add(g);
        }
        /*cards.sort((Goods g1, Goods g2) -> g2.getProfit() - g1.getProfit());*/
        int penalty = 0;
        int no_illegals = 0;
        for(Goods good : sorted_cards){
            if(items.size() == 8){
                break;
            }
            if(penalty + good.getPenalty() < this.gold){
                penalty += good.getPenalty();
                items.add(good);
                if(good.getType() == GoodsType.Illegal){
                    no_illegals++;
                }
            }
        }
        int bribe = 5;
        if(no_illegals > 2){
            bribe = 10;
        }
        this.gold -= bribe;
        this.sack.createSack(bribe, 0, items);
    }

    public ArrayList<Goods> verify(BasePlayer player, int initcoins){ // Returneaza cartile confiscate
        Utils utils = Utils.getInstance(0);
        Sack player_sack = player.getSack();
        // Verifica doar vecinii
        if(utils.neighbour(this, player)){
            return super.verify(player, initcoins);
//            if(initcoins >= 16) {
//                return super.verify(player, initcoins);
//            }
//            player.updateGold(player_sack.getBribe());
//            player.addToStall(player_sack.getItems());  // Playerul isi pune obiectele pe taraba
//            return new ArrayList<>();
        }
        this.gold += player_sack.getBribe();
        player.addToStall(player.getSack().getItems());
        return new ArrayList<>();
    }

    public void printScore(){
        System.out.println(this.id + " BRIBED " + this.score);
    }
}
