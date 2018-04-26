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
        long[] optionscore = recursiveOptionscore(deck, decksize, hand, score, getDepth(decksize));
        int [] result = new int[2];
        result[0] = (int) optionscore[5];
        result[1] = (int) (optionscore[4] / optionscore[0]);
        return result;
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
        // variations, 0+, 250+, 400+, tatalscore, bestoption
        long[] result = new long[6];
        if (depth <= 0 || deck == 0) {
            int sc = score + Capacity.capacity(hand | deck);
            if (sc < 250) {
                return new long[]{1, 1, 0, 0, sc, Integer.lowestOneBit(hand)};
            } else if (sc < 400) {
                return new long[]{1, 0, 1, 0, sc, Integer.lowestOneBit(hand)};
            } else {
                return new long[]{1, 0, 0, 1, sc, Integer.lowestOneBit(hand)};
            }
        }
        for (ScoreOption option : ScoreOption.allOptions) {
            if ((option.option & hand) == option.option) {
                long[] optionresult = new long[6];
                optionresult[5] = option.option;
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
                result = mergeresult(result, optionresult);
            }
        }
        int canthrow = hand;
        while (canthrow > 0) {
            int option = Integer.lowestOneBit(canthrow);
            canthrow &= ~option;
            long[] throwresult = new long[6];
            throwresult[5] = option;
            int bit1, bit1left = deck;
            while (bit1left > 0) {
                bit1 = Integer.lowestOneBit(bit1left);
                bit1left &= ~bit1;
                long[] traverseResult = recursiveOptionscore(deck & ~bit1, decksize - 1, (hand & ~option) | bit1, score, depth - 1);
                merge(throwresult, traverseResult);
            }
            result = mergeresult(result, throwresult);
        }
        return result;
    }

    /**
     * priority: 400+ percent, average score
     * @param result
     * @param optionresult
     * @return
     */
    private long [] mergeresult(long[] result, long[] optionresult) {
        //return mergeresult1(result, optionresult);
        return mergeresult2(result, optionresult);
    }

    private long[] mergeresult1(long[] result, long[] optionresult) {

        if (result[0] == 0) {
            return optionresult;
        } else {
            if (result[3] * optionresult[0] < optionresult[3] * result[0]) {
                return optionresult;
            } else if (result[3] * optionresult[0] == optionresult[3] * result[0]) {
                if (result[4] * optionresult[0] < optionresult[4] * result[0]) {
                    return optionresult;
                }
            }
            return result;
        }

    }
    private long[] mergeresult2(long[] result, long[] optionresult) {

        if (result[0] == 0) {
            return optionresult;
        } else {
            if (result[4] * optionresult[0] < optionresult[4] * result[0]) {
                return optionresult;
            } else if (result[4] * optionresult[0] == optionresult[4] * result[0]) {
                if (result[3] * optionresult[0] < optionresult[3] * result[0]) {
                    return optionresult;
                }
            }
            return result;
        }

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
