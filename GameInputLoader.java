package com.tema1.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameInputLoader {

    // DO NOT MODIFY

    private final String mInputPath;

    private final String mOutputPath;


    public GameInputLoader(final String inputPath, final String outputPath) {

        mInputPath = inputPath;

        mOutputPath = outputPath;

    }


    final GameInput load() {

        ArrayList<Integer> assetsIds = new ArrayList<>();

        List<String> playerOrder = new ArrayList<>();

        int rounds = 0;

        int noPlayers = 0;

        int noGoods = 0;


        try {

            File file = new File(mInputPath);

            Scanner input = new Scanner(file);

            rounds = input.nextInt();

            noPlayers = input.nextInt();

            String s = input.nextLine();
            s = input.nextLine();
            String[] words = s.split("\\s+");

            for (int i = 0; i < noPlayers; ++i) {
                playerOrder.add(words[i]);
            }

            noGoods = input.nextInt();

            for (int i = 0; i < noGoods; ++i) {
                assetsIds.add(input.nextInt());
            }

            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new GameInput(rounds, assetsIds, playerOrder);

    }

}
