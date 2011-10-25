package teletutor.core.utilities;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Dimension;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 *
 * @author Rae
 */
public class JarImageToggleButton extends JToggleButton {

    private final String EXTENSION = ".jpg";
    int x, y;
    String name;
    String dir;
    Class cls;

    public JarImageToggleButton(Class loaderClass, String dir, String name, int x, int y) {
        this.name = name;
        this.dir = dir;

        if (loaderClass != null) {
            cls = loaderClass;
        } else {
            cls = getClass();
        }

        loadIcons();

        setMargin(new Insets(0, 0, 0, 0));
        setIconTextGap(0);
        setBorderPainted(false);
        setBorder(null);
        setText(null);
        setLocation(x, y);
    }

    private void loadIcons() {
        ImageIcon normalIcon = new ImageIcon(cls.getResource("/" + dir + "/normal/images/" + name + EXTENSION));
        ImageIcon selectedIcon = new ImageIcon(cls.getResource("/" + dir + "/selected/images/" + name + EXTENSION));
        ImageIcon overIcon = new ImageIcon(cls.getResource("/" + dir + "/over/images/" + name + EXTENSION));
        ImageIcon disabledIcon = new ImageIcon(cls.getResource("/" + dir + "/disabled/images/" + name + EXTENSION));

        setIcon(normalIcon);
        setRolloverIcon(overIcon);
        setSelectedIcon(selectedIcon);
        setRolloverSelectedIcon(selectedIcon);
        setPressedIcon(overIcon);
        setDisabledIcon(disabledIcon);
        setDisabledSelectedIcon(selectedIcon);

        setRolloverEnabled(true);

        Dimension d = new Dimension(normalIcon.getImage().getWidth(null), normalIcon.getImage().getHeight(null));
        setSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        setPreferredSize(d);
    }
}
