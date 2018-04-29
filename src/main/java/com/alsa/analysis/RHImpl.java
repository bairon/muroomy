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
        int canthrow = hand;
        int bestthrow = 0;
        int bestthrowscore = 0;
        while (canthrow > 0) {
            int option = lowest(canthrow);
            canthrow &= ~option;
            int possiblescore = score + Capacity.capacity(deck | hand & ~option);
            if (bestthrowscore < possiblescore) {
                if ((notInSequence(hand, option) && bestthrowscore + 20 < possiblescore) || bestthrowscore == 0) {
                    bestthrowscore = possiblescore;
                    bestthrow = option;
                }
            } else if (bestthrowscore == possiblescore && possiblescore > 0 && bestthrowscore == score) {
                // compare bestthrow and option if they breaking almost ready combination
                /*int optionBreakScore = 0;
                int bestOptionBreakScore = 0;
                for (ScoreOption possible : ScoreOption.allOptions) {
                    if (Integer.bitCount(possible.option & hand) == 2 && (possible.option & option) > 0) {
                        optionBreakScore += possible.score;
                    }
                    if (Integer.bitCount(possible.option & hand) == 2 && (possible.option & bestthrow) > 0) {
                        bestOptionBreakScore += possible.score;
                    }
                }
                if (optionBreakScore < bestOptionBreakScore) {
                    bestthrowscore = possiblescore;
                    bestthrow = option;
                }*/
            }
        }
        if (bestscore + 20 < bestthrowscore) {
            return new int[]{bestthrow, bestthrowscore};
        } else {
            return new int[]{bestoption, bestscore};
        }
    }

    private boolean notInSequence(int hand, int option) {

        return (((hand & 0xFF) & (option << 1 | option >> 1)) |
        ((hand & 0xFF00) & (option << 1 | option >> 1)) |
        ((hand & 0xFF0000) & (option << 1 | option >> 1)))
                == 0;
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
