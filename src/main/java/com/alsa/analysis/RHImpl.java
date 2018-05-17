package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

public class RHImpl implements RoomyHelper {
    public RHImpl() {
        Capacity.init();
    }

    @Override
    public int [] optionscore(int deck, int hand, int score) {
        int bestoption = 0;
        int bestscore = 0;

        for (ScoreOption option : ScoreOption.allOptions) {
            if ((hand & option.option) == option.option) {
                int possiblescore = score + option.score + Capacity.capacity(deck | hand & ~option.option);
                if (bestscore < possiblescore) {
                    bestscore = possiblescore;
                    bestoption = option.option;
                }
            }
        }
        //deck = 0b11110100_01001110_10101110;
        //hand = 0b11_10110000_00000000;
        int canthrow = hand;
        int bestthrow = 0;
        int bestthrowscore = 0;
        while (canthrow > 0) {
            int option = lowest(canthrow);
            canthrow &= ~option;
            int possiblescoreNew = score + Capacity.capacity(deck | hand & ~option);
            int possiblescoreOld = score + Capacity.capacity(deck | hand);
            if (bestthrowscore < possiblescoreNew) {
                if ((possiblescoreNew == possiblescoreOld) || (notInSequence(hand, option) && bestthrowscore + 30 < possiblescoreNew) || bestthrowscore == 0) {
                    bestthrowscore = possiblescoreNew;
                    bestthrow = option;
                }
            }
        }
        if (bestthrowscore == 0 || bestscore >= 400 && bestthrowscore < 400) {
            return new int[]{bestoption, bestscore};
        }
        if (bestscore == 0 || bestthrowscore >= 400 && bestscore < 400) {
            return new int[]{bestthrow, bestthrowscore};
        }
        if (bestscore + 20 < bestthrowscore || bestscore < bestthrowscore && Integer.bitCount(deck) < 8) {
            return new int[]{bestthrow, bestthrowscore};
        } else {
            return new int[]{bestoption, bestscore};
        }
    }

    private boolean notInSequence(int hand, int option) {
        if ((option & 0xFF) > 0) {
            return (hand & 0xFF & (option << 1 | option >> 1)) == 0;
        } else if ((option & 0xFF00) > 0) {
            return (hand & 0xFF00 & (option << 1 | option >> 1)) == 0;
        } else if ((option & 0xFF0000) > 0) {
            return (hand & 0xFF0000 & (option << 1 | option >> 1)) == 0;
        }
        return true;
    }

    private int lowest(int hand) {
        int lowestred =  Integer.lowestOneBit(hand & 0xFF);
        int lowestyellow = Integer.lowestOneBit(hand >> 8 & 0xFF);
        int lowestblue = Integer.lowestOneBit(hand >> 16 & 0xFF);
        int lowest = lowestnonzero(lowestred, lowestyellow, lowestblue);
        if (lowest == 0) return 0;
        if (lowestred == lowest) return lowestred;
        if (lowestyellow == lowest) return lowestyellow << 8;
        if (lowestblue == lowest) return lowestblue << 16;
        return 0;
    }
    private int lowestnonzero(int ... numbers) {
        int lowest = 0;
        for (int number : numbers) {
            if (lowest == 0 || lowest > number && number > 0) {
                lowest = number;
            }
        }
        return lowest;
    }
}
