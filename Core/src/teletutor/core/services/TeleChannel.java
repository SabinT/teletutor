/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;

import java.io.Serializable;
import org.jgroups.Address;

/**
 *
 * @author Sabin Timalsena
 */
public interface TeleChannel extends ViewObservable {
   
    void registerSubChannel (TeleObject obj) throws Exception;
    
    void unregisterSubChannel (TeleObject obj);
    
    void send (String destObj, Serializable obj) throws Exception;
    
    void send (String memberStr, String destObj, Serializable obj) throws Exception;
    
    String getGroupName();
    
    String getChannelName();
    
    String getTutorName();
    
}
