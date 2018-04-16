package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

import java.io.*;

/**
 * Created by alsa on 16.04.2018.
 */
public class Capacity {
    public static final ScoreOption[] options = ScoreOption.allOptions.toArray(new ScoreOption[0]);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        /*ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream("capacities.txt"));
        int [] capacities = new int [16777216];
        for (int i = 0; i < 16777216; ++i) {
            if (i % 1000000 == 0) {
                System.out.println("Processed " + i);
            }
            capacities[i] = capacity(i);
        }
        System.out.println("DONE!");
        file.writeObject(capacities);
        file.flush();
        file.close();*/

        /*ObjectInputStream file = new ObjectInputStream(new FileInputStream("capacities.txt"));
        int []  capacities = (int[]) file.readObject();
        for (int i = 15777216; i < 16777216; ++i) {
            System.out.println(capacities[i]);
        }*/
        int capacity = capacity(16760311);
        System.out.println(capacity);
    }

    /**
     * Calculate maximum score can be achieved with the deck
     *
     * @param deck
     */
    private static int capacity(int deck) {
        // should be possible max 8 combinations ( 24 cards)
        int maxscore = 0;
        if (Integer.bitCount(deck) < 3) return 0;
        for (int i1 = 0; i1 < options.length; ++i1) {
            if ((deck & options[i1].option) == options[i1].option) {
                int score1 = options[i1].score;
                int deck1 = deck & ~options[i1].option;
                if (maxscore < score1) {
                    maxscore = score1;
                }
                for (int i2 = i1 + 1; i2 < options.length; ++i2) {
                    if ((deck1 & options[i2].option) == options[i2].option) {
                        int score2 = score1 + options[i2].score;
                        int deck2 = deck1 & ~options[i2].option;
                        // compare score with max
                        if (maxscore < score2) {
                            maxscore = score2;
                        }
                        for (int i3 = i2 + 1; i3 < options.length; ++i3) {
                            if ((deck2 & options[i3].option) == options[i3].option) {
                                int score3 = score2 + options[i3].score;
                                int deck3 = deck2 & ~options[i3].option;
                                // compare score with max
                                if (maxscore < score3) {
                                    maxscore = score3;
                                }
                                for (int i4 = i3 + 1; i4 < options.length; ++i4) {
                                    if ((deck3 & options[i4].option) == options[i4].option) {
                                        int score4 = score3 + options[i4].score;
                                        int deck4 = deck3 & ~options[i4].option;
                                        // compare score with max
                                        if (maxscore < score4) {
                                            maxscore = score4;
                                        }
                                        for (int i5 = i4 + 1; i5 < options.length; ++i5) {
                                            if ((deck4 & options[i5].option) == options[i5].option) {
                                                int score5 = score4 + options[i5].score;
                                                int deck5 = deck4 & ~options[i5].option;
                                                // compare score with max
                                                if (maxscore < score5) {
                                                    maxscore = score5;
                                                }
                                                for (int i6 = i5 + 1; i6 < options.length; ++i6) {
                                                    if ((deck5 & options[i6].option) == options[i6].option) {
                                                        int score6 = score5 + options[i6].score;
                                                        int deck6 = deck5 & ~options[i6].option;
                                                        // compare score with max
                                                        if (maxscore < score6) {
                                                            maxscore = score6;
                                                        }
                                                        for (int i7 = i6 + 1; i7 < options.length; ++i7) {
                                                            if ((deck6 & options[i7].option) == options[i7].option) {
                                                                int score7 = score6 + options[i7].score;
                                                                int deck7 = deck6 & ~options[i7].option;
                                                                // compare score with max
                                                                if (maxscore < score7) {
                                                                    maxscore = score7;
                                                                }
                                                                for (int i8 = i7 + 1; i8 < options.length; ++i8) {
                                                                    if ((deck7 & options[i8].option) == options[i8].option) {
                                                                        int score8 = score7 + options[i8].score;
                                                                        // compare score with max
                                                                        if (maxscore < score8) {
                                                                            maxscore = score8;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return maxscore;
    }
}
