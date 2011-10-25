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
public class ImageButton extends JButton {
    private final String EXTENSION = ".jpg";
    int x, y;
    String name;
    String dir;
    
    public ImageButton(String dir, String name, int x, int y) {
        this.name = name;
        this.dir = dir;
        
        loadIcons();
        
        setMargin(new Insets(0,0,0,0));
        setIconTextGap(0);
        setBorderPainted(false);
        setBorder(null);
        setText(null);
        setLocation(x, y);
    }
    
    private void loadIcons () {
        ImageIcon normalIcon = new ImageIcon(dir + "/normal/images/" + name + ".jpg");
        ImageIcon pressedIcon = new ImageIcon(dir + "/selected/images/" + name + ".jpg");
        ImageIcon overIcon = new ImageIcon(dir + "/over/images/" + name + EXTENSION);
        ImageIcon disabledIcon = new ImageIcon(dir + "/disabled/images/" + name + EXTENSION);
        
        setIcon(normalIcon);
        setRolloverIcon(overIcon);
        setPressedIcon(pressedIcon);
        setDisabledIcon(disabledIcon);
        setRolloverSelectedIcon(overIcon);
        setDisabledSelectedIcon(disabledIcon);
        
        //setRolloverEnabled(true);
        
        Dimension d = new Dimension(normalIcon.getImage().getWidth(null), normalIcon.getImage().getHeight(null));
        setSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
    }
}
