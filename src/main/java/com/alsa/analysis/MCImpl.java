package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

import java.util.HashMap;
import java.util.Map;

public class MCImpl implements RoomyHelper {
    public MCImpl() {
        Capacity.capacity(0);
    }

    int iteration = 0;
    @Override
    public int[] optionscore(int deck, int hand, int score) {
        int bestoption = 0;
        int avgscore = 0;
        int occurences = 0;
        int [] order = initialOrder(deck);
        Map<Integer, Integer> optionsOccur = new HashMap<>();
        Map<Integer, Integer> optionsScore = new HashMap<>();
        for (int i = 0; i < 10; ++i) {
            Utils.shuffleArray(order);
            int [] option = calculateOption(hand, deck, 0, order, score);
            merge(option, optionsOccur, optionsScore);
            System.out.println(String.format("input processed: option: %s, score: %s", option[0], option[1]));
        }
        for (Map.Entry<Integer, Integer> entry : optionsOccur.entrySet()) {
            System.out.println(String.format("option: %s, avgscore: %s, occurences: %s", entry.getKey(), optionsScore.get(entry.getKey()), entry.getValue()));
            if (occurences < entry.getValue()) {
                bestoption = entry.getKey();
                occurences = entry.getValue();
                avgscore = Math.round(1f * optionsScore.get(entry.getKey()) / occurences);
            }
        }
        return new int[]{bestoption, avgscore};
    }

    private void merge(int[] option, Map<Integer, Integer> optionsOccur, Map<Integer, Integer> optionsScore) {
        Integer occurences = optionsOccur.get(option[0]);
        if (occurences == null) {
            occurences = 1;
        } else {
            occurences = occurences + 1;
        }
        optionsOccur.put(option[0], occurences);

        Integer score = optionsScore.get(option[0]);
        if (score == null) {
            score = 1;
        } else {
            score = score + 1;
        }
        optionsScore.put(option[0], score);
    }

    private int[] initialOrder(int deck) {
        int[] order = new int[Integer.bitCount(deck)];
        int position = 0;
        for (int i = 0; i < 24; ++i) {
            if ((deck & (1 << i)) > 0) {
                order[position++] = i;
            }
        }
        return order;
    }

    private int[] calculateOption(int hand, int deck, int position, int[] order, int score) {
        iteration ++;
        if (iteration % 10000000 == 0) {
            System.out.println("Processed: " + iteration);
            //iteration = 0;
        }
        int [] bestoption = new int[]{0, score};
        if (score + Capacity.capacity(hand | deck) >= 300) {
            if (deck > 0 || Integer.bitCount(hand) > 2) {
                for (ScoreOption option : ScoreOption.allOptions) {
                    if ((option.option & hand) == option.option) {
                        int fromdeck = 0;
                        int newposition = order.length;
                        if (deck > 0) {
                            if (position >= order.length - 3) {
                                // at least 3 cards in the deck
                                fromdeck = deck & (1 << order[position]);
                                fromdeck |= deck & (1 << order[position + 1]);
                                fromdeck |= deck & (1 << order[position + 2]);
                                newposition = position + 3;
                            } else {
                                // 2 or less cards, take whole deck
                                fromdeck = deck;
                            }
                        }
                        int newhand = (hand & ~option.option) | fromdeck;
                        int newdeck = deck & ~fromdeck;
                        int[] optionresult = calculateOption(newhand, newdeck, newposition, order, score + option.score);
                        if (bestoption[1] < optionresult[1]) {
                            bestoption[0] = option.option;
                            bestoption[1] = optionresult[1];
                        }
                    }
                }
                int canthrow = hand;
                while (deck > 0 && canthrow > 0) {
                    int option = Integer.lowestOneBit(canthrow);
                    canthrow &= ~option;
                    int fromdeck = 0;
                    int newposition = order.length;
                    if (deck > 0) {
                        fromdeck = deck & (1 << order[position]);
                        newposition = position + 1;
                    }
                    int[] optionresult = calculateOption((hand & ~option) | fromdeck, deck & ~fromdeck, newposition, order, score);
                    if (bestoption[1] < optionresult[1]) {
                        bestoption[0] = option;
                        bestoption[1] = optionresult[1];
                    }
                }

            }
        }
        return bestoption;
    }

}
