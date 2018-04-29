package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

public class TestSuite {
    static RoomyHelper rh = new RHImpl();
    static RoomyHelper mc = new MCImpl();
    static RoomyHelper rec = new RecursiveCapHelperImpl();
    public static void main(String[] args) {
        int plus0 = 0;
        int plus250 = 0;
        int plus400 = 0;
        int N = 1000000;
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
        RoomyHelper helper = rh;
        do {
            int decksize = Integer.bitCount(input.deck);
            //System.out.print(String.format("Deck: %s Hand: %s ", decksize, Utils.format(input.hand)));
            long start = System.currentTimeMillis();
            //if (decksize > 0) helper = rh;
            //else helper = rec;
            int[] optionscore = helper.optionscore(input.deck, input.hand, score);
            long end = System.currentTimeMillis();
            if (optionscore[0] == 0) {
                optionscore = rh.optionscore(input.deck, input.hand, score);
            }
            ScoreOption scoreOption = ScoreOption.fromOption(optionscore[0]);
            if (scoreOption != null) {
                score += scoreOption.score;
            }
            if (optionscore[0] == 0) return 0;
            //System.out.println(String.format(" Move %s Score %s Time %s", Utils.format(optionscore[0]), score, ((end - start) / 1000)));
            input.hand &= ~optionscore[0];
            while (position < input.order.length && Integer.bitCount(input.hand) < 5) {
                int fromdeck = input.deck & (1 << input.order[position++]);
                input.hand |= fromdeck;
                input.deck &= ~fromdeck;
            }
        } while (!(input.deck == 0 && Integer.bitCount(input.hand) < 3));
        //System.out.println("Solved with score " + score);
        return score;
    }

}
