package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

import java.util.HashMap;
import java.util.Map;

public class MCImpl implements RoomyHelper {
    Map<Integer, Integer> occur0 = new HashMap<>();
    Map<Integer, Integer> occur250 = new HashMap<>();
    Map<Integer, Integer> occur400 = new HashMap<>();
    Map<Integer, Integer> optOccur = new HashMap<>();
    Map<Integer, Integer> optScore = new HashMap<>();
    public MCImpl() {
        Capacity.capacity(0);
    }

    int iteration = 0;
    @Override
    public int[] optionscore(int deck, int hand, int score) {
        occur0.clear();
        occur250.clear();
        occur400.clear();
        optOccur.clear();
        optScore.clear();
        int [] order = initialOrder(deck);

        for (int i = 0; i < 50; ++i) {
            Utils.shuffleArray(order);
            int[] option;
            int threshold = 510;
            do {
                threshold-=10;
                option = calculateOption(hand, deck, 0, order, score, threshold);
                //System.out.println(String.format("Option %s, threshold %s", option[1], threshold));
            } while (option[1] < threshold && threshold >= 0);

            merge(option);
            //System.out.println(String.format("input processed: option: %s, score: %s", option[0], option[1]));
        }
        return resolveBestOption();
    }

    private int[] resolveBestOption() {
        int bestoccur = 0;
        int bestopt = 0;
        for (Integer key : optOccur.keySet()) {
            int percent0 = occur0.get(key) == null ? 0 : occur0.get(key) * 100 / optOccur.get(key);
            int percent250 = occur250.get(key) == null ? 0 : occur250.get(key) * 100 / optOccur.get(key);
            int percent400 = occur400.get(key) == null ? 0 : occur400.get(key) * 100 / optOccur.get(key);
            System.out.println(String.format("option: %s, occur0: %s, occur250: %s, occur400: %s", key, percent0, percent250, percent400));
            if (bestoccur < percent250 + percent400) {
                bestoccur = percent250 + percent400;
                bestopt = key;
            }
        }
        System.out.println();
        return new int []{bestopt, bestoccur};
    }

    private void merge(int[] option) {
        if (option[1] < 250) {
            Integer occurences = occur0.get(option[0]);
            if (occurences == null) {
                occurences = 1;
            } else {
                occurences = occurences + 1;
            }
            occur0.put(option[0], occurences);
        } else if (option[1] < 400) {
            Integer occurences = occur250.get(option[0]);
            if (occurences == null) {
                occurences = 1;
            } else {
                occurences = occurences + 1;
            }
            occur250.put(option[0], occurences);
        } else {
            Integer occurences = occur400.get(option[0]);
            if (occurences == null) {
                occurences = 1;
            } else {
                occurences = occurences + 1;
            }
            occur400.put(option[0], occurences);
        }

        Integer score = optScore.get(option[0]);
        if (score == null) {
            score = option[1];
        } else {
            score = score + option[1];
        }
        optScore.put(option[0], score);

        Integer occur = optOccur.get(option[0]);
        if (occur == null) {
            occur = 1;
        } else {
            occur = occur + 1;
        }
        optOccur.put(option[0], occur);
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

    public int[] calculateOption(int hand, int deck, int position, int[] order, int score, int threshold) {
        /*iteration ++;
        if (iteration % 10000000 == 0) {
            System.out.println("Processed: " + iteration);
            //iteration = 0;
        }*/
        if (score >= threshold) {
            return new int[]{0, score};
        }
        int [] bestoption = new int[]{0, 0};
        if (score + Capacity.capacity(hand | deck) >= threshold) {
            bestoption[1] = score;
            if (deck > 0 || Integer.bitCount(hand) > 2) {
                for (ScoreOption option : ScoreOption.allOptions) {
                    if ((option.option & hand) == option.option) {
                        int fromdeck = 0;
                        int newposition = order.length;
                        if (deck > 0) {
                            if (position < order.length - 2) {
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
                        int[] optionresult = calculateOption(newhand, newdeck, newposition, order, score + option.score, threshold);
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
                    int[] optionresult = calculateOption((hand & ~option) | fromdeck, deck & ~fromdeck, newposition, order, score, threshold);
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
