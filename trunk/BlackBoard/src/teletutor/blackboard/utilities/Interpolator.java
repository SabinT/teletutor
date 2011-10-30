/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.utilities;

/**
 *
 * @author Sabin Timalsena
 */
public class Interpolator {
    public static float lerp(float a, float b, float f) {
        return a + (b - a) * f;
    }
}
