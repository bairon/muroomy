package com.alsa.analysis;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 * Created by alsa on 17.04.2018.
 */
public class Utils {
    private static Random random = new Random();

    public static void shuffleArray(int[] array)
    {
        int index;
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            if (index != i)
            {
                array[index] ^= array[i];
                array[i] ^= array[index];
                array[index] ^= array[i];
            }
        }
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int parseCard(String card) {
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

    public static double getDifferencePercent(BufferedImage img1, BufferedImage img2) {
        int width = img1.getWidth();
        int height = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();
        if (width != width2 || height != height2) {
            throw new IllegalArgumentException(String.format("Images must have the same dimensions: (%d,%d) vs. (%d,%d)", width, height, width2, height2));
        }

        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 255 * width * height;

        return 100.0 * diff / maxDiff;
    }

    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >>  8) & 0xff;
        int b1 =  rgb1        & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >>  8) & 0xff;
        int b2 =  rgb2        & 0xff;
        int rdif = Math.abs(r1 - r2);
        int gdif = Math.abs(g1 - g2);
        int bdif = Math.abs(b1 - b2);
        return rdif + gdif + bdif;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage[] templates = new BufferedImage[24];
        for (int i = 0; i < 24; ++i) {
            templates[i] = ImageIO.read(new FileInputStream("cards/" + (1 << i) + ".png"));
        }
        BufferedImage test6 = ImageIO.read(new FileInputStream("test/6.png"));
        double test6red = getDifferencePercent(test6, templates[5]);
        double test6yellow = getDifferencePercent(test6, templates[8 + 5]);

        System.out.println("Test red: " + test6red);
        System.out.println("Test yellow: " + test6yellow);
    }
}
