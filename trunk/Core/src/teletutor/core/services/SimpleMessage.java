/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;

import java.io.Serializable;

/**
 *
 * @author Sabin Timalsena
 */
public class SimpleMessage implements Serializable {
    String message;

    public SimpleMessage(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    
}
