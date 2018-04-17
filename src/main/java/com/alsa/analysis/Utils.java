package com.alsa.analysis;

import java.util.Random;

/**
 * Created by alsa on 17.04.2018.
 */
public class Utils {
    private static Random random = new Random();

    public static void shuffleArray(int[] array)
    {
        int index;
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

}
