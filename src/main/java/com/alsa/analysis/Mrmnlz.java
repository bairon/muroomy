package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

/**
 * Created by alsa on 12.04.2018.
 */
public class Mrmnlz {


    public int calculateMove(int deck, int hand, int score, int [] occurences) {
        for (ScoreOption option : ScoreOption.allOptions) {
            if ((option.option & hand) == option.option) {
                // select all variants of 3-card from deck
                if (deck == 0) {
                    // this is the end, no more options and scores
                    return option.option;
                }
                for (int bit1 = 0; bit1 < 24; ++bit1) {
                    if ((deck & bits[bit1]) == 0) continue;
                    int deck1 = deck & inverted_bits[bit1];
                    for (int bit2 = 0; bit2 < 24; ++bit2) {
                        if ((deck1 & bits[bit2]) == 0) continue;
                        int deck2 = deck1 & inverted_bits[bit2];
                        for (int bit3 = 0; bit3 < 24; ++bit3) {
                            if ((deck2 & bits[bit3]) == 0) continue;
                            int deck3 = deck2 & inverted_bits[bit3];
                            int newhand = (hand & ~option.option) | bit1 | bit2 | bit3;


                        }
                    }
                }
            }
        }
        return 0;
    }
    public static final int [] bits = {
            1 << 0,
            1 << 1,
            1 << 2,
            1 << 3,
            1 << 4,
            1 << 5,
            1 << 6,
            1 << 7,
            1 << 8,
            1 << 9,
            1 << 10,
            1 << 11,
            1 << 12,
            1 << 13,
            1 << 14,
            1 << 15,
            1 << 16,
            1 << 17,
            1 << 18,
            1 << 19,
            1 << 20,
            1 << 21,
            1 << 22,
            1 << 23
    };
    public static final int [] inverted_bits = {
            ~1,
            ~(1 << 1),
            ~(1 << 2),
            ~(1 << 3),
            ~(1 << 4),
            ~(1 << 5),
            ~(1 << 6),
            ~(1 << 7),
            ~(1 << 8),
            ~(1 << 9),
            ~(1 << 10),
            ~(1 << 11),
            ~(1 << 12),
            ~(1 << 13),
            ~(1 << 14),
            ~(1 << 15),
            ~(1 << 16),
            ~(1 << 17),
            ~(1 << 18),
            ~(1 << 19),
            ~(1 << 20),
            ~(1 << 21),
            ~(1 << 22),
            ~(1 << 23)
    };
}
