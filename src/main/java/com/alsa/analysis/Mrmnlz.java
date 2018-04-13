package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

/**
 * Created by alsa on 12.04.2018.
 */
public class Mrmnlz {

    public static void main(String[] args) {
        Mrmnlz ra = new Mrmnlz();
        ra.calculateMove(bits[5] | bits[6] | bits[7] | bits[8] | bits[9] | bits[10] | bits[11] | bits[12] | bits[13] | bits[14] | bits[15] | bits[16] | bits[17] | bits[18] | bits[19] | bits[20] | bits[21] | bits[22] | bits[23] ,
                bits[0] | bits[1] | bits[2] | bits[3] | bits[4], 0, 19, null);
    }

    public int calculateMove(int deck, int hand, int score, int decksize, int [] occurences) {
        int restricted = 0;
        int newhand = 0;
        int newdeck = 0;
        for (ScoreOption option : ScoreOption.allOptions) {
            if ((option.option & hand) == option.option) {
                // check if colored
                if ((option.option & 0xFF | option.option & 0xFF00 | option.option & 0xFF0000) == 0) {
                    restricted |= option.option;
                }
                // select all variants of 3-card from deck
                System.out.println(Integer.toBinaryString(deck) + " ; " + Integer.toBinaryString(hand) + " ; " + Integer.toBinaryString(option.option) + " ; " + score);
                //if (deck == 0) {
                    // this is the end, no more options and scores
                //    return option.option;
               // }
                if (decksize <= 3) {
                    newhand = hand | deck;
                    newdeck = 0;
                    calculateMove(newdeck, newhand, score + option.score, 0, occurences);
                } else {
                    for (int bit1 = 0; bit1 < 24; ++bit1) {
                        if ((deck & bits[bit1]) == 0) continue;
                        int deck1 = deck & inverted_bits[bit1];
                        for (int bit2 = 0; bit2 < 24; ++bit2) {
                            if ((deck1 & bits[bit2]) == 0) continue;
                            int deck2 = deck1 & inverted_bits[bit2];
                            for (int bit3 = 0; bit3 < 24; ++bit3) {
                                if ((deck2 & bits[bit3]) == 0) continue;
                                newdeck = deck2 & inverted_bits[bit3];
                                newhand = (hand & ~option.option) | bits[bit1] | bits[bit2] | bits[bit3];

                            }
                        }
                    }
                    calculateMove(newdeck, newhand, score + option.score, decksize - 3, occurences);
                }
            }
        }
        if (decksize == 0) return 0;
        int canthrow = hand & ~restricted;
        while (canthrow != 0) {
            int thr = Integer.lowestOneBit(canthrow);
            canthrow &= ~thr;
            if (decksize > 0) {
                for (int bit1 = 0; bit1 < 24; ++bit1) {
                    if ((deck & bits[bit1]) == 0) continue;
                    newdeck = deck & inverted_bits[bit1];
                }
                newhand = hand & ~thr;
                calculateMove(newdeck, newhand, score, decksize - 1, occurences);
            }
        }
        return 0;
    }
    public static final int [] bits = {
            1,
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
