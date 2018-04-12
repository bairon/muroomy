package com.alsa.muroomy.model;

import java.util.*;

/**
 * Created by alsa on 26.02.2018.
 */
public class Room implements Cloneable{

    public int [] deck;
    public int position;
    public int [] table;
    public int tablesize;
    public int score;
    public List<ScoreOption> scoreOptions;

    public Room(int tablesize)
    {
        this.tablesize = tablesize;
        this.scoreOptions = new ArrayList<>(3);
    }

    public Room(Room copy) {
        this.deck = Arrays.copyOf(copy.deck, copy.deck.length);
        this.position = copy.position;
        this.table = Arrays.copyOf(copy.table, copy.table.length);
        this.tablesize = copy.tablesize;
        this.score = copy.score;
        this.scoreOptions = new ArrayList<>(copy.scoreOptions.size());
        Collections.copy(this.scoreOptions, copy.scoreOptions);
    }

    public void reset() {

        initDeck();
        initTable();
        position = deck.length;
        score = 0;
        layoutCards();
    }

    private void initTable() {
        table = new int[tablesize];
    }

    private void initDeck() {
        deck = new int [] {
                11, 12, 13,
                21, 22, 23,
                31, 32, 33,
                41, 42, 43,
                51, 52, 53,
                61, 62, 63,
                71, 72, 73,
                81, 82, 83
        };
        shuffle(deck);
    }

    public void layoutCards() {
        boolean updated = false;
        for (int i = 0; i < table.length && position > 0; ++i) {
            if (table[i] == 0) {
                updated = true;
                position--;
                table[i] = deck[position];
                deck[position] = 0;
            }
        }
        Arrays.sort(table);
        if (updated) {
            recalculateScoreOptions();
        }
    }

    private void recalculateScoreOptions() {
        scoreOptions.clear();
        for (ScoreOption option : ScoreOption.allOptions) {
            if (hasOption(option)) {
                scoreOptions.add(option);
            }
        }
    }

    private boolean hasOption(ScoreOption option) {
        int match = 0;
        for (int t : table) {
            if (t == option.a || t == option.b || t == option.c) {
                match++;
            }
        }
        return match == 3;
    }

    public static void shuffle(int[] array) {
        Random random = new Random();
        int count = array.length;
        for (int i = count; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public List<ScoreOption> getScoreOptions() {
        return scoreOptions;
    }

    public void applyOption(ScoreOption option) {
        if (scoreOptions.contains(option)) {
            score += option.score;
            for (int i = 0; i < table.length; ++i) {
                if (table[i] == option.a || table[i] == option.b || table[i] == option.c) {
                    table[i] = 0;
                }
            }
        }
        layoutCards();
    }
    public void skipCard(int card) {
        for (int i = 0; i < table.length; ++i) {
            if (table[i] == card) {
                table[i] = 0;
                break;
            }
        }
        layoutCards();
    }

    @Override
    public String toString() {
        return "Room{" +
                "deck=" + Arrays.toString(deck) +
                ", position=" + position +
                ", table=" + Arrays.toString(table) +
                ", tablesize=" + tablesize +
                ", score=" + score +
                ", scoreOptions=" + scoreOptions +
                '}';
    }

    public boolean hasCards() {
        return position > 0;
    }

    public int[] getTable() {
        return table;
    }

}
