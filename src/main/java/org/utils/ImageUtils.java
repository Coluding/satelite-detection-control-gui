package org.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static Image addCenterLabel(BufferedImage image, int x, int y){
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = modifiedImage.createGraphics();
        // draw image onto graphics
        g2d.drawImage(image, 0,0, null);
        g2d.setColor(new Color(255,0,0));
        g2d.fillOval(x, y, 10,10);
        g2d.dispose();
        return modifiedImage;
    }

}
