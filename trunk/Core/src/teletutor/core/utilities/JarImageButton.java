package teletutor.core.utilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Rae
 */
public class JarImageButton extends JButton {
    private final String EXTENSION = ".jpg";
    int x, y;
    String name;
    String dir;
    // the class from whose classpath to load the images
    Class cls;
    
    public JarImageButton(Class loaderClass,String dir, String name, int x, int y) {
        this.name = name;
        this.dir = dir;
        if (loaderClass != null) {
            cls = loaderClass;
        } else {
            cls = getClass();
        }
        
        loadIcons();
        
        setMargin(new Insets(0,0,0,0));
        setIconTextGap(0);
        setBorderPainted(false);
        setBorder(null);
        setText(null);
        setLocation(x, y);
    }
    
    private void loadIcons () {
        ImageIcon normalIcon = new ImageIcon(cls.getResource("/" + dir + "/normal/images/" + name + EXTENSION));
        ImageIcon pressedIcon = new ImageIcon(cls.getResource("/" + dir + "/selected/images/" + name + EXTENSION));
        ImageIcon overIcon = new ImageIcon(cls.getResource("/" + dir + "/over/images/" + name + EXTENSION));
        ImageIcon disabledIcon = new ImageIcon(cls.getResource("/" + dir + "/disabled/images/" + name + EXTENSION));
        
        setIcon(normalIcon);
        setRolloverIcon(overIcon);
        setPressedIcon(pressedIcon);
        setDisabledIcon(disabledIcon);
        setRolloverSelectedIcon(overIcon);
        setDisabledSelectedIcon(disabledIcon);
        
        Dimension d = new Dimension(normalIcon.getImage().getWidth(null), normalIcon.getImage().getHeight(null));
        setSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
    }
}
