package com.alsa.analysis;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by alsa on 17.04.2018.
 */

public class MCImplTest {


    @Test
    public void name() throws Exception {
        MCImpl mc = new MCImpl();
        int[] optionscore = mc.optionscore(6143743, 10633472, 0);
        /*int[] option = mc.calculateOption(10633472, 6143743, 0,
                new int[]{5,
                        12,
                        10,
                        2,
                        0,
                        7,
                        4,
                        19,
                        16,
                        11,
                        3,
                        15,
                        9,
                        13,
                        18,
                        6,
                        1,
                        22,
                        20},
                0, 500);
        System.out.println(option);
*/
    }
}