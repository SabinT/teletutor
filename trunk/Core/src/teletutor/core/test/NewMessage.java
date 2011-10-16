/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.test;

import java.io.Serializable;

/**
 *
 * @author Sabin Timalsena
 */
public class NewMessage implements Serializable {
    String msg;

    public NewMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
}
