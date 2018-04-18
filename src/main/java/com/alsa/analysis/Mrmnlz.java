package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by alsa on 12.04.2018.
 */
public class Mrmnlz {
    public int iteration;
    public int heavy = 5;
    public int scoregoal1 = 250;
    public int scoregoal2 = 400;
    Random random = new Random();
    public static int [] capacities;
    {
        try {
            capacities = (int[]) (new ObjectInputStream(new FileInputStream("C:/etc/capacities.properties")).readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        Mrmnlz ra = new Mrmnlz();
//        ra.calculateMove(bits[5] | bits[6] | bits[7] | bits[8] | bits[9] | bits[10] | bits[11] | bits[12] | bits[13] | bits[14] | bits[15] | bits[16] | bits[17] | bits[18] | bits[19] | bits[20] | bits[21] | bits[22] | bits[23] ,
//                bits[0] | bits[1] | bits[2] | bits[3] | bits[4], 0, 19, null);
        //int deck = bits[0] | bits[5] | bits[7] | bits[8] | bits[9] | bits[10] | bits[11] | bits[12] | bits[13] | bits[14] | bits[15] | bits[16] | bits[17] | bits[18] | bits[19] | bits[20] | bits[21] | bits[22] | bits[23];
        int hand = bits[0] | bits[2] | bits[3] | bits[5] | bits[6];
        int deck = 0x0000FF - hand;
        int[] options = ra.calculateMove(deck, hand, 0, Integer.bitCount(deck), 0);
        System.out.println(Arrays.toString(options));
        System.out.println(ra.iteration);
    }


    public int [] calculateMove(int deck, int hand, int score, int decksize, int depth) {
        iteration++;
        if (iteration % 1000000 == 0) System.out.println(iteration);
        System.out.println(Integer.toBinaryString(deck) + " ; " + Integer.toBinaryString(hand) + " ; " + score + " ; " + depth);
        int restricted = 0;
        int [] bestoption = new int[5]; // option, 0+, 250+, 400+, score
        bestoption[4] = score;
        if (deck > 0 || Integer.bitCount(hand) > 2) {
            for (ScoreOption option : ScoreOption.allOptions) {
                if ((option.option & hand) == option.option) {
                    // check if colored
                    if (((option.option & 0xFFFF00) == option.option
                            || (option.option & 0xFF00FF) == option.option
                            || (option.option & ~0x00FFFF) == option.option)) {
                        restricted |= option.option;
                    }
                    int [] optionresult = new int[5];
                    optionresult[4] = score + option.score;
                    optionresult[0] = option.option;
                    // select all variants of 3-card from deck


                    if (decksize <= 3) {
                        addResult(calculateMove(0, hand & ~option.option | deck, score + option.score, 0, depth + 1), optionresult);
                    } else {
                        // get getRandom bit
                        for (int counter = 0; counter < heavy; ++counter) {
/*
                        for (int bit1 = 0; bit1 < 24; ++bit1) {
                            if (counter >= heavy) break;
                            if ((deck & bits[bit1]) == 0) continue;
                            int deck1 = deck & inverted_bits[bit1];
                            for (int bit2 = 0; bit2 < 24; ++bit2) {
                                if (counter >= heavy) break;
                                if ((deck1 & bits[bit2]) == 0) continue;
                                int deck2 = deck1 & inverted_bits[bit2];
                                for (int bit3 = 0; bit3 < 24; ++bit3) {
                                    if (counter >= heavy) break;
                                    if ((deck2 & bits[bit3]) == 0) continue;

                                    newdeck = deck2 & inverted_bits[bit3];
                                   newhand = (hand & ~option.option) | bits[bit1] | bits[bit2] | bits[bit3];

*/                                      int random3bits = randomBit3(deck);
                                        int[] subresult = calculateMove(deck & ~random3bits, (hand & ~option.option) | random3bits, score + option.score, decksize - 3, depth + 1);
                                        addResult(subresult, optionresult);
                                    }
//                                }
//                            }
//                        }
                    }
                    if (bestoption[3] < optionresult[3] || (bestoption[3] == optionresult[3] && bestoption[2] < optionresult[2])) {
//                  if (bestoption[2] < optionresult[2]) {
                        System.arraycopy(optionresult, 0, bestoption, 0, optionresult.length);
                        bestoption[0] = option.option;

                    }

                }
            }
        }
        if (decksize != 0) {
            int canthrow = hand & ~restricted;
            while (canthrow != 0) {
                int thr = Integer.lowestOneBit(canthrow);
                canthrow &= ~thr;
                int [] optionresult = new int[5];
                optionresult[4] = score;
                optionresult[0] = thr;
                if (decksize > 0) {
                    int subdeck = deck;
                    for (int counter = 0; counter < heavy && subdeck > 0; ++counter) {
                        int bit = randomBit(subdeck);
                        subdeck &= ~bit;
                        addResult(calculateMove(deck & ~bit, (hand & ~thr) | bit, score, decksize - 1, depth + 1), optionresult);
                    }
                }
                if (bestoption[3] < optionresult[3] || (bestoption[3] == optionresult[3] && bestoption[2] < optionresult[2])) {
//                  if (bestoption[2] < optionresult[2]) {
                    System.arraycopy(optionresult, 0, bestoption, 0, optionresult.length);
                    bestoption[0] = thr;
                }
            }
        }
        if ((bestoption[1] | bestoption[2] | bestoption[3]) == 0) {
            // nosubcases
            if (bestoption[4] < scoregoal1) {
                bestoption[1]++;
            } else if (bestoption[4] < scoregoal2) {
                bestoption[2]++;
            } else {
                bestoption[3]++;
            }
        }
        if ( bestoption[0] > 0) System.out.println("Return option " + Integer.toBinaryString(bestoption[0]) + " ; " + Arrays.toString(bestoption));
        return bestoption;
    }

    private int randomBit3(int deck) {
        int bit1, bit2, bit3;
        do {
            int random = this.random.nextInt(24);
            bit1 = Integer.lowestOneBit(deck >> random) << random;
        } while (bit1 == 0);
        deck &= ~bit1;

        do {
            int random = this.random.nextInt(24);
            bit2 = Integer.lowestOneBit(deck >> random) << random;
        } while (bit2 == 0);
        deck &= ~bit2;

        do {
            int random = this.random.nextInt(24);
            bit3 = Integer.lowestOneBit(deck >> random) << random;
        } while (bit3 == 0);
        return bit1 | bit2 | bit3;
    }
    private int randomBit(int deck) {
        int bit;
        do {
            int random = this.random.nextInt(24);
            bit = Integer.lowestOneBit(deck >> random) << random;
        } while (bit == 0);
        return bit;
    }
    private void addResult(int[] from, int[] to) {
        to[1] += from[1];
        to[2] += from[2];
        to[3] += from[3];
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
