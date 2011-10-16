/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;


/**
 *
 * @author Sabin Timalsena
 */
public interface ViewObservable {
    void addViewObserver(ViewObserver obs);
    
    void removeViewObserver(ViewObserver obs);
    
    void notifyViewObservers();
}
