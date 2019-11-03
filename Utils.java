import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    private int no_players;
    static Utils instance = null;

    private Utils(int no_players){
        this.no_players = no_players;
    }

    static Utils getInstance(int no_players){
        if(instance == null){
            instance = new Utils(no_players);
        }
        return instance;
    }

    Goods getBestCard(ArrayList<Goods> hand){
        int max_profit = 0;
        Goods best_item = new Goods();
        Map<Goods, Integer> profit = new HashMap<>();

        for (Goods good : hand) {
            profit.put(good, good.getProfit());
        }

        for (Goods key : profit.keySet()) {
            if(profit.get(key) > max_profit){
                max_profit = profit.get(key);
                best_item = key;
                continue;
            }
            if(profit.get(key) == max_profit && key.getProfit() == best_item.getProfit()
                    && key.getId() > best_item.getId()){
                best_item = key;
            }
        }
        return best_item;
    }

    boolean neighbour(BasePlayer p1, BasePlayer p2){
        return p1.getId() == p2.getId() + 1 || p1.getId() == p2.getId() - 1 ||
                (p1.getId() == no_players - 1 && p2.getId() == 0) ||
                (p1.getId() == 0 && p2.getId() == no_players - 1);
    }
}
