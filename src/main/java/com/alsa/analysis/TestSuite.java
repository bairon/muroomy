package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

public class TestSuite {
    static RoomyHelper rh = new MCImpl();
    public static void main(String[] args) {
        int plus0 = 0;
        int plus250 = 0;
        int plus400 = 0;
        int N = 1000;
        for (int i = 0; i < N; ++i) {
            Input input = Input.getRandom();
            int score = solve(input);
            if (score < 250) {
                plus0++;
            } else if (score < 400) {
                plus250++;
            } else {
                plus400++;
            }
        }
        System.out.println(String.format("From %s tests: 0-249 = %s, 250 - 399 = %s, 400+ = %s", N, plus0, plus250, plus400));
    }
    public static int solve(Input input) {
        int score = 0;
        int position = 0;
        do {
            int[] optionscore = rh.optionscore(input.deck, input.hand, score);
            if (optionscore[0] == 0) {
                System.out.println("NO options - error");
                break;
            }
            ScoreOption scoreOption = ScoreOption.fromOption(optionscore[0]);
            if (scoreOption != null) {
                score += scoreOption.score;
            }
            input.hand &= ~optionscore[0];
            while (position < input.order.length && Integer.bitCount(input.hand) < 5) {
                int fromdeck = input.deck & (1 << input.order[position++]);
                input.hand |= fromdeck;
                input.deck &= ~fromdeck;
            }
        } while (!(input.deck == 0 && Integer.bitCount(input.hand) < 3));
        return score;
    }
}
