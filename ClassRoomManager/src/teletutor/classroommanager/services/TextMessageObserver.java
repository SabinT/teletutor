/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.services;

/**
 * The interface to be implemented by the classes that wish to handle incoming 
 * text messages. E.g. the window that displays private messages.
 * @author Rae
 */
public interface TextMessageObserver {
    void newTextMessage(TextMessage message);
}
