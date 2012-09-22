/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.utilities;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Sabin Timalsena
 */
public class ImageUtils {

    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public static Image getScaledAlphaImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    public static Image getProportionalImage(Image srcImg, int w, int h) {
        int pw = srcImg.getWidth(null);
        int ph = srcImg.getHeight(null);
        float pratio = pw / (float) ph;
        float ratio = w / (float) h;
        if (pratio > ratio) {
            // original image wider than target
            h = (int) (w / pratio);
        } else {
            w = (int) (h * pratio);
        }
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public static Image getProportionalAlphaImage(Image srcImg, int w, int h) {
        int pw = srcImg.getWidth(null);
        int ph = srcImg.getHeight(null);
        float pratio = pw / (float) ph;
        float ratio = w / (float) h;
        if (pratio > ratio) {
            // original image wider than target
            h = (int) (w / pratio);
        } else {
            w = (int) (h * pratio);
        }
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public static Dimension getProportionalDimension(Image srcImg, int w, int h) {
        int pw = srcImg.getWidth(null);
        int ph = srcImg.getHeight(null);
        float pratio = pw / (float) ph;
        float ratio = w / (float) h;
        if (pratio > ratio) {
            // original image wider than target
            h = (int) (w / pratio);
        } else {
            w = (int) (h * pratio);
        }

        return new Dimension(w, h);
    }
}
