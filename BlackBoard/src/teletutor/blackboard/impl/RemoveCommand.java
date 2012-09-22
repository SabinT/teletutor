/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.io.Serializable;

/**
 *
 * @author Sabin Timalsena
 */
public class RemoveCommand implements Serializable {
    String name;

    public RemoveCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
