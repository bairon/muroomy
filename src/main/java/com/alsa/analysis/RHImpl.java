package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class RHImpl implements RoomyHelper {
    public static int [] capacities;
    {
        try {
            capacities = (int[]) (new ObjectInputStream(new FileInputStream("capacities.txt")).readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int [] optionscore(int deck, int hand, int score) {
        int bestoption = 0;
        int bestscore = 0;

        for (ScoreOption option : ScoreOption.allOptions) {
            if ((hand & option.option) == option.option) {
                int possiblescore = score + option.score + capacities[deck | hand & ~option.option];
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
            int possiblescore = score + capacities[deck | hand & ~option];
            if (bestscore < possiblescore) {
                bestscore = possiblescore;
                bestoption = option;
            }
        }

        return new int []{bestoption, bestscore};
    }
}
