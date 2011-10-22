/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.utilities;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.lang.reflect.Method;

/**
 * 
 * 
 * @author David Gilbert
 */

public class FrameUtil {
  /**
   * Positions the specified frame in the middle of the screen.
   *
   * @param frame  the frame to be centered on the screen.
   */
  public static void centerFrameOnScreen(final Window frame) {
      positionFrameOnScreen(frame, 0.5, 0.5);
  }
  /**
   * Positions the specified frame at a relative position in the screen, where
   * 50% is considered to be the center of the screen.
   * 
   * @param frame
   *          the frame.
   * @param horizontalPercent
   *          the relative horizontal position of the frame (0.0 to 1.0, where
   *          0.5 is the center of the screen).
   * @param verticalPercent
   *          the relative vertical position of the frame (0.0 to 1.0, where 0.5
   *          is the center of the screen).
   */
  public static void positionFrameOnScreen(final Window frame, final double horizontalPercent,
      final double verticalPercent) {

    final Rectangle s = getMaximumWindowBounds();
    final Dimension f = frame.getSize();
    final int w = Math.max(s.width - f.width, 0);
    final int h = Math.max(s.height - f.height, 0);
    final int x = (int) (horizontalPercent * w) + s.x;
    final int y = (int) (verticalPercent * h) + s.y;
    frame.setBounds(x, y, f.width, f.height);

  }

  /**
   * Computes the maximum bounds of the current screen device. If this method is
   * called on JDK 1.4, Xinerama-aware results are returned. (See Sun-Bug-ID
   * 4463949 for details).
   * 
   * @return the maximum bounds of the current screen.
   */
  public static Rectangle getMaximumWindowBounds() {
    final GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment
        .getLocalGraphicsEnvironment();
    try {
      final Method method = GraphicsEnvironment.class.getMethod("getMaximumWindowBounds",
          (Class[]) null);
      return (Rectangle) method.invoke(localGraphicsEnvironment, (Object[]) null);
    } catch (Exception e) {
      // ignore ... will fail if this is not a JDK 1.4 ..
    }

    final Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
    return new Rectangle(0, 0, s.width, s.height);
  }
}