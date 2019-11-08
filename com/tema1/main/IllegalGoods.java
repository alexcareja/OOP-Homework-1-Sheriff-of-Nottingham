package com.tema1.main;

import java.util.Map;


class IllegalGoods extends Goods {

    private final Map<Goods, Integer> illegalBonus;


    IllegalGoods(final int id, final int profit, final int penalty,
                        final Map<Goods, Integer> illegalBonus) {

        super(id, GoodsType.Illegal, profit, penalty);


        this.illegalBonus = illegalBonus;

    }


    final Map<Goods, Integer> getIllegalBonus() {

        return illegalBonus;

    }

}
