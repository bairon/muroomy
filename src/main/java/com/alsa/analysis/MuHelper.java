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
            move = optionscore[0];//input("Enter move:");
            ScoreOption scoreOption = ScoreOption.fromOption(move);
            if (scoreOption != null) {
                score += scoreOption.score;
            }
            if (Integer.bitCount(deck) > 0) {
                if (Integer.bitCount(deck) <= Integer.bitCount(move)) {
                    fromdeck = deck;
                } else {
                    fromdeck = input("Got from deck: ");
                }
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
            result |= Utils.parseCard(card);
        }
        return result;
    }

}
