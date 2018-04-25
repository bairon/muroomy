package com.alsa.control;

import com.alsa.analysis.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by alsa on 17.04.2018.
 */
public class MRControllerImpl implements MRController {
    public static final int [] xcards = new int []{352, 388, 425, 461, 498};
    public static final int [] ycards = new int []{119, 119, 119, 119, 119};

    public static final int [] xplace = new int []{405, 435, 465};
    public static final int [] yplace = new int []{200, 200, 200};

    public static final int xnormal = 429;
    public static final int ynormal = 259;

    public static final int xstart = 410;
    public static final int ystart = 310;

    public static final int xok = 360;
    public static final int yok = 400;

    public static final int xdeck = 330;
    public static final int ydeck = 265;

    public static final int xoffset = 11;
    public static final int yoffset = 13;

    public static final int width = 10;
    public static final int height = 14;

    public static final Rectangle frame = new Rectangle(0, 0, 550, 330);


    BufferedImage screen;
    BufferedImage [][] templates = new BufferedImage[5][24];
    Robot robot = new Robot();
    int [] cards = new int[5];
    int hand;


    @Override
    public void startGame() {
        clickNormal();
        clickStart();
        clickDeck();
    }

    @Override
    public int getHand() {
        refresh();
        return hand;
    }


    @Override
    public void makeMove(int move) {
        if (move != 0) {
            if (Integer.bitCount(move) == 1) {
                throwCard(move);
            } else {
                placeCards(move);
            }
        }
        clickDeck();
    }
    @Override
    public void endGame() {
        clickStart();
        clickOk();
        Utils.sleep(5000);
    }

    private void placeCards(int move) {
        int [] cardsBeforePlace = new int[5];
        int handBeforePlace = hand;
        System.arraycopy(cards, 0, cardsBeforePlace, 0, cards.length);
        boolean placed = false;
        do {
            int place = 0;
            for (int i = 0; i < 5; ++i) {
                if ((move & cardsBeforePlace[i]) > 0) {
                    if (cards[i] != 0) {
                        lclick(xcards[i] + xoffset + width / 2, ycards[i] + yoffset + height / 2);
                        Utils.sleep(300);
                        lclick(xplace[place], yplace[place]);
                        Utils.sleep(300);
                        lclickfast(xdeck, ydeck);
                        lclickfast(xdeck, ydeck);
                    }
                    place++;
                }
            }
            //lclickfast(xdeck, ydeck);
            //lclickfast(xdeck, ydeck);
            refresh();
            placed = (hand + move  == handBeforePlace || Integer.bitCount(hand) == 5);
        } while (!placed);
        Utils.sleep(2000);

    }

    private void throwCard(int move) {
        for (int i = 0; i < 5; ++i) {
            if (cards[i] == move) {
                rclick(xcards[i] + xoffset + width / 2, ycards[i] + yoffset + height / 2);
                rclick(xcards[i] + xoffset + width / 2, ycards[i] + yoffset + height / 2);
                rclick(xcards[i] + xoffset + width / 2, ycards[i] + yoffset + height / 2);
                rclick(xcards[i] + xoffset + width / 2, ycards[i] + yoffset + height / 2);
            }
        }
    }



    public MRControllerImpl() throws AWTException {
        try {
            initTemplates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        screen = robot.createScreenCapture(frame);
        hand = 0;
        for (int i = 0; i < 5; ++i) {
            cards[i] = readCard(i);
            hand |= cards[i];
        }
        //printcards();
    }

    private void printcards() {
        StringBuilder sb = new StringBuilder();
        for (int card : cards) {
            if (card == 0) {
                sb.append("_");
            } else {
                sb.append(format(card));
            }
        }
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
    private int readCard(int number) {
        BufferedImage subimage = screen.getSubimage(xcards[number] + xoffset, ycards[number] + yoffset, width, height);
        int bestmatch = 100;
        int bestcard = -1;
        for (int i = 0; i < 24; ++i) {
            double percent = Utils.getDifferencePercent(subimage, templates[number][i]);
            if (percent < bestmatch) {
                bestmatch = (int) Math.floor(percent);
                bestcard = i;
            }
        }
        if (bestcard >= 0 && bestmatch < 8) {
            return (1 << bestcard);
        } else {
            return 0;
            //throw new RuntimeException("Card can not be read, number " + number);
        }
    }

    public static void main(String[] args) {
        try {
            MRControllerImpl mrControllerImpl = new MRControllerImpl();
            mrControllerImpl.initTemplates();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTemplates() throws IOException {
        /*
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.println("Enter input:");
            String cardsString = input.readLine();
            refresh();
            String[] split = cardsString.split(" ");
            for (int i = 0; i < split.length; ++i) {
                BufferedImage subimage = screen.getSubimage(xcards[i] + xoffset, ycards[i] + yoffset, width, height);
                int card = Utils.parseCard(split[i]);
                ImageIO.write(subimage, "png", new FileOutputStream(String.format("cards/%s/%s.png", i, card)));
            }
        }
*/      for (int number = 0; number < 5; ++number) {
            for (int i = 0; i < 24; ++i) {
                InputStream input = getClass().getClassLoader().getResourceAsStream(String.format("cards/%s/%s.png", number, 1 << i));
                templates[number][i] = ImageIO.read(input);
            }
        }
    }
    private void clickDeck() {
        lclickfast(xdeck, ydeck);
        lclickfast(xdeck, ydeck);
        lclickfast(xdeck, ydeck);
        lclickfast(xdeck, ydeck);
        Utils.sleep(300);
    }

    private void clickNormal() {

        rclick(xnormal, ynormal);
        lclick(xnormal, ynormal);
        Utils.sleep(300);
    }
    private void clickStart() {

        rclick(xstart, ystart);
        lclick(xstart, ystart);
        Utils.sleep(300);
    }
    private void clickOk() {
        lclick(xok, yok);
        Utils.sleep(300);
    }


    private void lclick(int x, int y) {
        robot.mouseMove(x, y);
        Utils.sleep(250);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        Utils.sleep(250);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Utils.sleep(100);
    }
    private void lclickfast(int x, int y) {
        robot.mouseMove(x, y);
        Utils.sleep(30);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        Utils.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Utils.sleep(30);
    }
    private void rclick(int x, int y) {
        robot.mouseMove(x, y);
        Utils.sleep(30);
        robot.mousePress(InputEvent.BUTTON3_MASK);
        Utils.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
        Utils.sleep(30);
    }
}
