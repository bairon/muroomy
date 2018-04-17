package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

public class RHImpl implements RoomyHelper {

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
        while (canthrow > 0) {
            int option = lowest(canthrow);
            canthrow &= ~option;
            int possiblescore = score + Capacity.capacity(deck | hand & ~option);
            if (bestscore < possiblescore) {
                bestscore = possiblescore;
                bestoption = option;
            }
        }

        return new int []{bestoption, bestscore};
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
            if (lowest == 0 || lowest < number) {
                lowest = number;
            }
        }
        return lowest;
    }
}
