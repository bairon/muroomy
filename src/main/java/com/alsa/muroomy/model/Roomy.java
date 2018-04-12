package com.alsa.muroomy.model;

import java.util.List;

/**
 * Created by alsa on 03.03.2018.
 */
public class Roomy {
    public static void main(String[] args) {
        Roomy roomy = new Roomy();
        while (true) {
            roomy.play();
        }
    }

    public void play() {
        Room room = new Room(5);

        room.reset();

        //System.out.println(room);

        do {
            List<ScoreOption> scoreOptions = room.getScoreOptions();
            ScoreOption bestOption = findBestOption(scoreOptions);
            if (bestOption != null) {
                room.applyOption(bestOption);
            } else {
                int worstCard = findWorstCard(room.getTable());
                if (worstCard > 0) {
                    room.skipCard(worstCard);
                } else {
                    System.out.println("NO CARDS!");
                }
            }
        } while (room.hasCards());
        System.out.println(room);

    }
    private  int findWorstCard(int[] table) {
        int worst = 0;
        for (int i = 0; i < table.length; ++i) {
            if (table[i] > 0 && (table[i] < worst || worst == 0)) {
                worst = table[i];
            }
        }
        return worst;
    }

    private  ScoreOption findBestOption(List<ScoreOption> scoreOptions) {
        if (scoreOptions == null) {
            return null;
        }
        ScoreOption bestOption = null;
        for (ScoreOption option : scoreOptions) {
            if (bestOption == null || option.score > bestOption.score) {
                bestOption = option;
            }
        }
        return bestOption;
    }
}
