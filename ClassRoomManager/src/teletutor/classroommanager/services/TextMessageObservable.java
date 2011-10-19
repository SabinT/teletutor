/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.services;

/**
 *
 * @author Rae
 */
public interface TextMessageObservable {
    void addTMObserver (TextMessageObserver obs);
    
    void removeTMObserver (TextMessageObserver obs);
    
    void notifyTMObservers (TextMessage message);
}
