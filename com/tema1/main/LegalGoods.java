package com.tema1.main;

public class LegalGoods extends Goods {

    private final int kingBonus;

    private final int queenBonus;


    public LegalGoods(final int id, final int profit, final int penalty, final int kingBonus,
                      final int queenBonus) {

        super(id, GoodsType.Legal, profit, penalty);


        this.kingBonus = kingBonus;

        this.queenBonus = queenBonus;

    }


    final int getKingBonus() {

        return kingBonus;

    }


    final int getQueenBonus() {

        return queenBonus;

    }

}
