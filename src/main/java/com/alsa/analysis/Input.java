package com.alsa.analysis;

import java.util.Random;

/**
 * Created by alsa on 16.04.2018.
 */
public class Input {
    public static int [] initial = new int []{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    public Random random = new Random();
    public int hand;
    public int deck;
    public int [] order = new int[19];

    public static Input getRandom() {
        Input input = new Input();
        input.randomize();
        return input;
    }

    private void randomize() {
        Utils.shuffleArray(initial);
        for (int i = 0; i < 5; ++i) {
            hand |= 1 << initial[i];
        }
        deck = 0xFFFFFF & ~hand;
        System.arraycopy(initial, 5, order, 0, initial.length - 5);
    }

}
