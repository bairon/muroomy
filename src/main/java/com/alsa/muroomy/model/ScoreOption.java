package com.alsa.muroomy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alsa on 02.03.2018.
 */
public class ScoreOption {
    public static final List<ScoreOption> allOptions = new ArrayList();
    public static final ScoreOption[] options = ScoreOption.allOptions.toArray(new ScoreOption[0]);

    public int a;
    public int b;
    public int c;
    public int option;
    public int score;

    /**
     * 11 12 13
     * 21 12 33
     * 11 21 13
     */
    static {
        for (int i = 0; i < 6; ++i) {
            for (int k = 0; k < 3; ++k) {
                for (int l = 0; l < 3; ++l) {
                    for (int m = 0; m < 3; ++m) {
                        allOptions.add(
                                new ScoreOption(
                                        (1 + i) * 10 + k + 1,
                                        (2 + i) * 10 + l + 1,
                                        (3 + i) * 10 + m + 1,
                                        (k == l && l == m ? 40 : 0) + (i+1) * 10
                                ));
                    }
                }
            }
        }
        for (int j = 0; j < 8; ++j) {
            allOptions.add(new ScoreOption(
                    10 * (j + 1) + 1, 10 * (j + 1) + 2, 10 * (j + 1) + 3,
                    (j + 2) * 10
            ));

        }
    }

    public ScoreOption(int a, int b, int c, int score) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.option |= toOption(a) | toOption(b) | toOption(c);
        this.score = score;
    }

    private int toOption(int a) {
        int number = (a - 1) / 10 - 1;
        int suit = a % 10 - 1;
        return 1 << number + (suit * 8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreOption that = (ScoreOption) o;

        if (a + b + c != that.a + that.b + that.c) return false;
        if (a * b *c != that.a * that.b * that.c) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = a + b + c;
        result = 31 * result + a * b * c;
        return result;
    }

    @Override
    public String toString() {
        return a + "," + b + "," + c + ",=" + score;
    }

    public static ScoreOption fromOption(int option) {
        for (ScoreOption allOption : allOptions) {
            if (allOption.option == option) return allOption;
        }
        return null;
    }
}
