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
public class JarImagePanel extends JPanel {

    private Image img;
    Class cls;

    public JarImagePanel(Class loaderClass, String dir, String file) {
        if (loaderClass != null) {
            cls = loaderClass;
        } else {
            cls = getClass();
        }

        this.img = new ImageIcon(cls.getResource("/" + dir + "/" + file)).getImage();

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
