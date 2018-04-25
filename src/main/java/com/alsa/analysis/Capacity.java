package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

import java.io.*;

/**
 * Created by alsa on 16.04.2018.
 */
public class Capacity {
    static Capacity instance = null;

    public int [] capacities = null;

    public Capacity() {
        try {
            InputStream capacitiesStream = getClass().getClassLoader().getResourceAsStream("capacities.properties");
            capacities = (int[]) (new ObjectInputStream(capacitiesStream).readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static int capacity(int hand) {
        return instance.capacities[hand];
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int [] capacities = new int [0xFFFFFF];
        for (int i = 0; i < 0xFFFFFF; ++i) {
            if (i % 1000000 == 0) {
                System.out.println("Processed " + i);
            }
            capacities[i] = cpct(i);
        }
        ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream("capacities.properties"));
        file.writeObject(capacities);
        file.flush();
        file.close();
        System.out.println("DONE!");

        /*ObjectInputStream file = new ObjectInputStream(new FileInputStream("capacities.properties"));
        int []  capacities = (int[]) file.readObject();
        for (int i = 15777216; i < 16777216; ++i) {
            System.out.println(capacities[i]);
        }*/
        //int cpct = cpct(16760311);
        //System.out.println(cpct);
    }

    /**
     * Calculate maximum score can be achieved with the deck
     *
     * @param deck
     */

    private static int cpct(int deck) {
        // should be possible max 8 combinations ( 24 cards)
        int maxscore = 0;
        if (Integer.bitCount(deck) < 3) return 0;
        for (int i1 = 0; i1 < ScoreOption.options.length; ++i1) {
            if ((deck & ScoreOption.options[i1].option) == ScoreOption.options[i1].option) {
                int score1 = ScoreOption.options[i1].score;
                int deck1 = deck & ~ScoreOption.options[i1].option;
                if (maxscore < score1) {
                    maxscore = score1;
                }
                if (Integer.bitCount(deck1) < 3) continue;
                for (int i2 = i1 + 1; i2 < ScoreOption.options.length; ++i2) {
                    if ((deck1 & ScoreOption.options[i2].option) == ScoreOption.options[i2].option) {
                        int score2 = score1 + ScoreOption.options[i2].score;
                        int deck2 = deck1 & ~ScoreOption.options[i2].option;
                        // compare score with max
                        if (maxscore < score2) {
                            maxscore = score2;
                        }
                        if (Integer.bitCount(deck2) < 3) continue;
                        for (int i3 = i2 + 1; i3 < ScoreOption.options.length; ++i3) {
                            if ((deck2 & ScoreOption.options[i3].option) == ScoreOption.options[i3].option) {
                                int score3 = score2 + ScoreOption.options[i3].score;
                                int deck3 = deck2 & ~ScoreOption.options[i3].option;
                                // compare score with max
                                if (maxscore < score3) {
                                    maxscore = score3;
                                }
                                if (Integer.bitCount(deck3) < 3) continue;
                                for (int i4 = i3 + 1; i4 < ScoreOption.options.length; ++i4) {
                                    if ((deck3 & ScoreOption.options[i4].option) == ScoreOption.options[i4].option) {
                                        int score4 = score3 + ScoreOption.options[i4].score;
                                        int deck4 = deck3 & ~ScoreOption.options[i4].option;
                                        // compare score with max
                                        if (maxscore < score4) {
                                            maxscore = score4;
                                        }
                                        if (Integer.bitCount(deck4) < 3) continue;
                                        for (int i5 = i4 + 1; i5 < ScoreOption.options.length; ++i5) {
                                            if ((deck4 & ScoreOption.options[i5].option) == ScoreOption.options[i5].option) {
                                                int score5 = score4 + ScoreOption.options[i5].score;
                                                int deck5 = deck4 & ~ScoreOption.options[i5].option;
                                                // compare score with max
                                                if (maxscore < score5) {
                                                    maxscore = score5;
                                                }
                                                if (Integer.bitCount(deck5) < 3) continue;
                                                for (int i6 = i5 + 1; i6 < ScoreOption.options.length; ++i6) {
                                                    if ((deck5 & ScoreOption.options[i6].option) == ScoreOption.options[i6].option) {
                                                        int score6 = score5 + ScoreOption.options[i6].score;
                                                        int deck6 = deck5 & ~ScoreOption.options[i6].option;
                                                        // compare score with max
                                                        if (maxscore < score6) {
                                                            maxscore = score6;
                                                        }
                                                        if (Integer.bitCount(deck6) < 3) continue;
                                                        for (int i7 = i6 + 1; i7 < ScoreOption.options.length; ++i7) {
                                                            if ((deck6 & ScoreOption.options[i7].option) == ScoreOption.options[i7].option) {
                                                                int score7 = score6 + ScoreOption.options[i7].score;
                                                                int deck7 = deck6 & ~ScoreOption.options[i7].option;
                                                                // compare score with max
                                                                if (maxscore < score7) {
                                                                    maxscore = score7;
                                                                }
                                                                if (Integer.bitCount(deck7) < 3) continue;
                                                                for (int i8 = i7 + 1; i8 < ScoreOption.options.length; ++i8) {
                                                                    if ((deck7 & ScoreOption.options[i8].option) == ScoreOption.options[i8].option) {
                                                                        int score8 = score7 + ScoreOption.options[i8].score;
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


    public static void init() {
        if (instance == null) instance = new Capacity();
    }
}
