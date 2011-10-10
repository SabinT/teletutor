/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;

import java.io.Serializable;

/**
 *
 * @author Rae
 */
public interface TeleChannel extends ViewObservable {
    enum Privilege {TUTOR, STUDENT_PASSIVE, STUDENT_ACTIVE}
    
    void registerSubChannel (TeleObject obj) throws Exception;
    
    void unregisterSubChannel (TeleObject obj);
    
    void send (TeleObject dest, Serializable obj) throws Exception;
    
    void setPrivilege (Privilege prv);
    
    Privilege getPrivilege ();
}
