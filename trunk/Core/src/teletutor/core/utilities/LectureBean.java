/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.utilities;

/**
 *
 * @author Sabin Timalsena
 */
public class LectureBean {
    int lectureID;
    String title;
    String tutor;

    public LectureBean() {
    }

    public LectureBean(int lectureID, String title, String tutor) {
        this.lectureID = lectureID;
        this.title = title;
        this.tutor = tutor;
    }

    public int getLectureID() {
        return lectureID;
    }

    public void setLectureID(int lectureID) {
        this.lectureID = lectureID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }
    
}
