package com.alsa.control;

import com.alsa.analysis.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by alsa on 17.04.2018.
 */
public class TestRobot {
    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();
        try {
            Rectangle size = new Rectangle(Toolkit.getDefaultToolkit()
                    .getScreenSize());
            BufferedImage buf = robot.createScreenCapture(size);
            ImageIO.write(buf, "png", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.sleep(4000);
        robot.mouseMove(100, 200);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
