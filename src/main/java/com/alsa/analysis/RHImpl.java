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
            int option = Integer.lowestOneBit(canthrow);
            canthrow &= ~option;
            int possiblescore = score + Capacity.capacity(deck | hand & ~option);
            if (bestscore < possiblescore) {
                bestscore = possiblescore;
                bestoption = option;
            }
        }

        return new int []{bestoption, bestscore};
    }
}
