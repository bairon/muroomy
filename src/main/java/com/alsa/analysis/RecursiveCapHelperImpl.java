package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

/**
 * Created by alsa on 24.04.2018.
 */
public class RecursiveCapHelperImpl implements RoomyHelper {
    static long N = 10000000;
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
        result[1] = optionscore[0] > 0 ? (int) (optionscore[4] / optionscore[0]) : 0;
        return result;
    }

    private static int getDepth(int decksize) {
        switch(decksize) {
            case 19: return 2;
            case 18: return 2;
            case 17: return 2;
            case 16: return 2;
            case 15: return 2;
            case 14: return 2;
            case 13: return 3;
            case 12: return 3;
            case 11: return 3;
            case 10: return 3;
            case 9: return 4;
            case 8: return 4;
            case 7: return 5;
            default: return 6;
        }
        /*long variations = 1;
        int depth = 0;
        while (variations <= N && depth < decksize) {
            variations *= wide * (decksize - depth);
            if (variations <= N) {
                depth++;
            }
        }
        return Math.min(decksize, depth);*/
    }

    private long[] recursiveOptionscore(int deck, int decksize, int hand, int score, int depth) {
        if (score + Capacity.capacity(hand | deck) < 250) {
            return new long[]{1, 1, 0, 0, 0, 0};
        }
        // variations, 0+, 250+, 400+, tatalscore, bestoption
        long[] result = null;
        long [] bestscoreresult = null;
        long [] bestthrowresult = null;
            if (depth >= 0 && (decksize > 0 || Integer.bitCount(hand) + decksize > 2)) {
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
                    bestthrowresult = mergeresult(bestthrowresult, throwresult);
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
                        bestscoreresult = mergeresult(bestscoreresult, optionresult);
                    }
                }
            }
            result = bestscore(bestscoreresult, bestthrowresult);
            if (result == null) {
                int sc = score + Capacity.capacity(hand | deck);
                if (sc < 250) {
                    result =  new long[]{1, 1, 0, 0, sc, 0}; //ToDo WRONG
                } else if (sc < 400) {
                    result =  new long[]{1, 0, 1, 0, sc, 0}; //ToDo WRONG
                } else {
                    result =  new long[]{1, 0, 0, 1, sc, 0}; //ToDo WRONG
                }

            }
        return result;
    }

    private long[] bestscore(long[] bestscoreresult, long[] bestthrowresult) {
        if (bestscoreresult == null) return bestthrowresult;
        if (bestthrowresult == null) return bestscoreresult;
        long avbestscoreresult = bestscoreresult[0] == 0 ? 0 : bestscoreresult[5] / bestscoreresult[0];
        long avbestthrowresult = bestthrowresult[0] == 0 ? 0 : bestthrowresult[5] / bestthrowresult[0];
        if (avbestscoreresult < avbestthrowresult) {
            return bestthrowresult;
        } else {
            return bestscoreresult;
        }
    }

    /**
     * priority: 400+ percent, average score
     * @param result
     * @param optionresult
     * @return
     */
    private long [] mergeresult(long[] result, long[] optionresult) {
        return mergeresult1(result, optionresult);
        //return mergeresult2(result, optionresult);
    }

    private long[] mergeresult1(long[] result, long[] optionresult) {

        if (result == null) {
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

        if (result == null) {
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
