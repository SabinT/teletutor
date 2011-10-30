/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.test;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Sabin Timalsena
 */
public class TimerTest {
    public static void main(String[] args) {
        final Timer timer = new Timer("jpt");
        TimerTask task = new TimerTask() {
            int count = 0;
            @Override
            public void run() {
                System.out.println("Meow ");
                count++;
                if (count == 2) {
                    cancel();
                    timer.schedule(this, 0, 50);
                } else if (count == 4) {
                    cancel();
                    timer.cancel();
                }
            }
            
        };
        timer.schedule(task, 0, 50);
    }
}
