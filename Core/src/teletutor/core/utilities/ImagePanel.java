package teletutor.core.utilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Rae
 */
public class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String dir, String file) {
        this.img = new ImageIcon(dir + "/" + file).getImage();
        
        Dimension size = new Dimension(img.getWidth(null),
                img.getHeight(null));
        
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        
        setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
