package com.alsa.analysis;

import com.alsa.muroomy.model.ScoreOption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MuHelper {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    int hand;
    int deck;
    int score;
    int [] optionscore;
    int move;
    int fromdeck;
    RoomyHelper rh = new RHImpl();
    public static void main(String[] args) throws IOException {
        MuHelper muHelper = new MuHelper();
        while(true) {
            muHelper.newgame();
        }
    }

    private void newgame() throws IOException {
        System.out.println("============= New Game =============");
        score = 0;
        hand = input("Enter hand:");
        deck = 0xFFFFFF & ~hand;
        while(Integer.bitCount(hand | deck) > 2) {
            optionscore = rh.optionscore(deck, hand, score);
            printstatus();
            if (optionscore[0] == 0) break;
            move = input("Enter move:");
            ScoreOption scoreOption = ScoreOption.fromOption(move);
            if (scoreOption != null) {
                score += scoreOption.score;
            }
            if (Integer.bitCount(deck) > 0) {
                fromdeck = input("Got from deck: ");
            } else {
                fromdeck = 0;
            }
            hand &= ~move;
            hand |= fromdeck;
            deck &= ~fromdeck;
        }
    }

    private int input(String message) {
        while (true) {
            String move = null;
            try {
                System.out.println(message);
                move = input.readLine();
                return parseInput(move);
            } catch (Exception e) {
                System.out.println("Wrong input: " + move);
                e.getMessage();
            }
        }
    }

    private void printstatus() {
        System.out.println("Deck:");
        printout(deck);
        System.out.println("Hand:");
        printout(hand);
        System.out.println("Score: " + score);
        System.out.println("Best option: " + format(optionscore[0]) + " with " + optionscore[1] + " max score");
    }

    private String format(int hand) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatCards(hand & 0xFF, "r"));
        sb.append(formatCards(hand >> 8 & 0xFF, "y"));
        sb.append(formatCards(hand >> 16 & 0xFF, "b"));
        return sb.toString();

    }

    private String formatCards(int hand, String suffix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            if ((hand & (1 << i)) > 0) {
                sb.append(i + 1).append(suffix).append(' ');
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private void printout(int hand) {
        System.out.println("r: " + indentCards(hand & 0xFF));
        System.out.println("y: " + indentCards(hand >> 8 & 0xFF));
        System.out.println("b: " + indentCards(hand >> 16 & 0xFF));
    }

    private String indentCards(int hand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; ++i) {
            if ((hand & (1 << i)) > 0) {
                sb.append(i + 1);
            } else {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private int parseInput(String input) {
        int result = 0;
        String[] cards = input.split(" ");
        for (String card : cards) {
            result |= parseCard(card);
        }
        return result;
    }

    private int parseCard(String card) {
        if (card.length() != 2) throw new IllegalArgumentException("Card input not valid: " + card);
        int number;
        try {
            number = Integer.parseInt(card.substring(0, 1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Card input not valid: " + card);
        }
        String suffix = card.substring(1);
        int suit;
        if ("r".equalsIgnoreCase(suffix) || "k".equalsIgnoreCase(suffix)) {
            suit = 0;
        } else if ("y".equalsIgnoreCase(suffix) || "j".equalsIgnoreCase(suffix)) {
            suit = 1;
        } else if ("b".equalsIgnoreCase(suffix) || "s".equalsIgnoreCase(suffix)) {
            suit = 2;
        } else {
            throw new IllegalArgumentException("Card input not valid: " + card);
        }
        return 1 << (number - 1) + (suit * 8);

    }
}
