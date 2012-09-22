/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.services;

import java.io.Serializable;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class ConstructionInfo implements Serializable{
    String name;
    String creator;
    UpdateInfo params;

    public ConstructionInfo(String name, UpdateInfo params) {
        this.name = name;
        this.params = params;
        this.creator = "Unknown";
    }

    public ConstructionInfo(String name, UpdateInfo params, String creator) {
        this.name = name;
        this.creator = creator;
        this.params = params;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UpdateInfo getParams() {
        return params;
    }

    public void setParams(UpdateInfo params) {
        this.params = params;
    }
    
    
}
