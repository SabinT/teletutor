/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.utilities;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calls web service operations required for gathering information about the
 * classroom
 * 
 * @author Sabin Timalsena
 */
public class WebOperations {
    // TODO replace all these dummy methods with original operations
    public static UserBean checkLogin(String username, String password) {
        try {
            // simulate the delay in web operation call, 3 seconds
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(WebOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!username.equalsIgnoreCase("Samyam"))  {
            return new UserBean(username, username + " Bahadur", username + "@email.com");
        }
        else {
            return new UserBean();
        }
    }
    
    public static ArrayList<LectureBean> getScheduledLectures (String username) {
        ArrayList<LectureBean> list = new ArrayList<LectureBean>();
        if (username.equalsIgnoreCase("Heme")) {
            list.add(new LectureBean(1, "Quantum Physics", "Heme"));
            list.add(new LectureBean(2, "Dragon grooming tutorial", "Heme"));
            return list;
        } else {
            return null;
        }
    }
    
    public static ArrayList<LectureBean> getJoinedLectures (String username) {
        ArrayList<LectureBean> list = new ArrayList<LectureBean>();
        if (!username.equalsIgnoreCase("Heme")) {
            list.add(new LectureBean(1, "Quantum Physics", "Heme"));
            list.add(new LectureBean(2, "Dragon grooming tutorial", "Heme"));
            return list;
        } else {
            return null;
        }
    }
}
