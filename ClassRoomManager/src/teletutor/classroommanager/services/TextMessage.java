/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.services;

import java.io.Serializable;

/**
 * This class encapsulates the text message, which can be sent as a private message
 * or in the open discussion room.
 * @author Sabin Timalsena
 */
public class TextMessage implements Serializable{
    String sender, message;

    public TextMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    
    @Override
    public String toString() {
        return sender + ": " + message + "\n";
    }
}
