package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

/**
 * Created by alsa on 24.04.2018.
 */
public class RecursiveCapHelperImpl implements RoomyHelper {
    static long N = 100000000;
    static int wide = 7;

    public RecursiveCapHelperImpl() {
        Capacity.init();
    }

    @Override
    public int[] optionscore(int deck, int hand, int score) {
        int decksize = Integer.bitCount(deck);
        return null;//recursiveOptionscore(deck, decksize, hand, score, getDepth(decksize));
    }

    private static int getDepth(int decksize) {
        long variations = 1;
        int depth = 0;
        while (variations <= N && depth < decksize) {
            variations *= wide * (decksize - depth);
            if (variations <= N) {
                depth++;
            }
        }
        return Math.min(decksize, depth);
    }

    private long[] recursiveOptionscore(int deck, int decksize, int hand, int score, int depth) {
        long[] result = new long[5];
        if (depth == 0 || deck == 0) {
            int sc = score + Capacity.capacity(hand | deck);
            if (sc < 250) {
                return new long[]{1, 1, 0, 0, sc};
            } else if (sc < 400) {
                return new long[]{1, 0, 1, 0, sc};
            } else {
                return new long[]{1, 0, 0, 1, sc};
            }
        }
        for (ScoreOption option : ScoreOption.allOptions) {
            if ((option.option & hand) == option.option) {
                long[] optionresult = new long[5];
                int fromdeck = 0;
                if (decksize > 2) {
                    int bit1, bit2, bit3, bit2left, bit3left, bit1left = deck;
                    while (bit1left > 0) {
                        bit1 = Integer.lowestOneBit(bit1left);
                        bit2left = (bit1left &= ~bit1);
                        while (bit2left > 0) {
                            bit2 = Integer.lowestOneBit(bit2left);
                            bit3left = (bit2left &= ~bit2);
                            while (bit3left > 0) {
                                bit3 = Integer.lowestOneBit(bit3left);
                                bit3left &= ~bit3;
                                fromdeck = bit1 | bit2 | bit3;
                                long[] traverseResult = recursiveOptionscore(deck & ~fromdeck, decksize - 3, (hand & ~option.option) | fromdeck, score + option.score, depth - 3);
                                merge(optionresult, traverseResult);
                            }
                        }
                    }
                } else {
                    fromdeck = deck;
                    long[] traverseResult = recursiveOptionscore(0, 0, (hand & ~option.option) | fromdeck, score + option.score, depth - decksize);
                    merge(optionresult, traverseResult);
                }
                mergeresult(result, optionresult);
            }
        }
        int canthrow = hand;
        while (canthrow > 0) {
            int option = Integer.lowestOneBit(canthrow);
            canthrow &= ~option;
            long[] throwresult = new long[5];
            int bit1, bit1left = deck;
            while (bit1left > 0) {
                bit1 = Integer.lowestOneBit(bit1left);
                long[] traverseResult = recursiveOptionscore(deck & ~bit1, decksize - 1, (hand & ~option) | bit1, score, depth - 1);
                merge(throwresult, traverseResult);
            }
            mergeresult(result, throwresult);
        }
        return result;
    }

    private void mergeresult(long[] result, long[] optionresult) {

    }


    private void merge(long[] optionresult, long[] traverseResult) {
        optionresult[0] += traverseResult[0];
        optionresult[1] += traverseResult[1];
        optionresult[2] += traverseResult[2];
        optionresult[3] += traverseResult[3];
        optionresult[4] += traverseResult[4];

    }

    public static void main(String[] args) {
        for (int i = 1; i <= 19; ++i) {
            System.out.println(i + " : " + getDepth(i));
        }
    }


}
