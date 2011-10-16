/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;

import java.util.Set;

/**
 *
 * @author Sabin Timalsena
 */
public interface ViewObserver {
    void newViewArrived(Set<String> memberList);
}
