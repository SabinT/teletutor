/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.services;

import java.awt.Container;
import teletutor.classroommanager.services.TextMessageObserver;
import teletutor.classroommanager.services.TextMessage;
import teletutor.classroommanager.services.TextMessageObservable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import teletutor.classroommanager.UI.MemberPMFrame;
import teletutor.classroommanager.UI.MemberThumb;
import teletutor.classroommanager.services.MemberStateObserver;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.UpdateInfo;

/**
 * This class represents a member attending the lecture. It holds all the info
 * regarding that member including username, real name, picture, and the state 
 * of the member. This class has the responsibility of fetching all the data; 
 * given the username. Also, this class encapsulates the visual components 
 * required for member representation in a GUI (i.e icon) and a PM window.
 * 
 * This class is also used for sending PM from one node to another. The @received
 * callback in this class should handle incoming messages
 * 
 * If there are 5 members in the group, then there are 5 MemberProxy objects in
 * every node corresponding to every member, and hence a total of 25 objects in 
 * the network.
 * 
 * Proxy object naming convention: USERNAME_proxy
 * @author Sabin Timalsena
 */
public class MemberProxy extends TeleObject implements TextMessageObservable {

    public static final int TUTOR = 0;
    public static final int STUDENT_PASSIVE = 1;
    public static final int STUDENT_ACTIVE = 2;
    public static final int STUDENT_CURIOUS = 3;
    public static final int SUSPECT = 4;
    /**
     * The name of the user whom this proxy represents. Also the target of PMs.
     * unlike other properties, username is set during initialization and cannot 
     * be changed afterwards
     */
    private String username;
    // similarly other fields    
    
    // the user on whose node this proxy resides. This user's proxy on the other
    // end should receive messages sent by this proxy
    private String localUser;
    private String targetProxy;
    
    protected Integer state;
    // the note to be left to the tutor when raising a question
    protected String note;

    MemberPMFrame pmFrame = null;
    //MemberThumbPanel thumbPanel = null;
    JPanel thumbPanel = null;

    public MemberProxy(String name, TeleChannel chan) throws Exception {
        super(name, chan);
        localUser = chan.getChannelName();
        targetProxy = localUser + "_proxy";
    }

    /**
     * This method initialized the member proxy, i.e. the representation of the
     * member with the given name in this node
     * @param username 
     */
    public void init(String username) {
        this.username = username;

        // set the appropriate starting state for this member
        String tutorName = getChannel().getTutorName();
        if (username.equals(tutorName)) {
            state = TUTOR;
        } else {
            state = STUDENT_PASSIVE;
        }

        // TODO fetch all the necessary data from the database, possibly in a new thread
        // e.g. real name, image, etc

        // now set up the interface components
        // the PM window
        final MemberProxy mp = this;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                pmFrame = new MemberPMFrame(mp);
                thumbPanel = new MemberThumb(mp);
            }
        });

    }

    /**
     * Send a PM to the user represented by this MemberProxy object. The recipient
     * of the member will be the MemberProxy object in the target user's node.
     * 
     * @param message 
     */
    public void sendPM(String message) {
        try {
            sendObject(username, targetProxy, new TextMessage(localUser, message));
        } catch (Exception ex) {
            Logger.getLogger(MemberProxy.class.getName()).log(Level.SEVERE, "Could not send PM to" + username, ex);
        }
    }

    /**
     * If received a message, pass it to the appropriate observers.
     * @param obj 
     */
    @Override
    public void received(Object obj) {
        if (obj instanceof TextMessage) {
            notifyTMObservers((TextMessage) obj);
        }
    }
    /**
     * The mechanism for registration and notification of observers on new PM 
     * arrival. Observers could display the message on a window, or play a sound
     * or flash some icon if the window is not visible.
     */
    private ArrayList<TextMessageObserver> tmObservers = new ArrayList<TextMessageObserver>();

    @Override
    public void addTMObserver(TextMessageObserver obs) {
        tmObservers.add(obs);
    }

    @Override
    public void removeTMObserver(TextMessageObserver obs) {
        tmObservers.remove(obs);
    }

    @Override
    public void notifyTMObservers(TextMessage message) {
        for (TextMessageObserver obs : tmObservers) {
            obs.newTextMessage(message);
        }
    }
    /**
     * MemberStateObserver example: the list displaying icons of members should
     * reflect changes in state of members. The icon objects could be observers 
     * registered on respective MemberProxy object, and hence change appearance 
     * on state change.
     */
    private ArrayList<MemberStateObserver> memberStateObservers = new ArrayList<MemberStateObserver>();

    public void addMemberStateObserver(MemberStateObserver obs) {
        memberStateObservers.add(obs);
        obs.memberStateChanged(state);
    }

    public void removeMemberStateObserver(MemberStateObserver obs) {
        memberStateObservers.remove(obs);
    }

    public void notifyMemberStateObservers(int state) {
        for (MemberStateObserver obs : memberStateObservers) {
            obs.memberStateChanged(state);
        }
    }

    /**
     * See if state change has arrived from the network and notify the observers
     * @param ui 
     */
    @Override
    public void objectUpdated(UpdateInfo ui) {
        Integer newState = (Integer) ui.get("state");
        if (newState != null) {
            notifyMemberStateObservers(newState);
        }
    }

    /********* Getters and Setters ************/
    public Integer getState() {
        return state;
    }

    /**
     * Changes the state of the member. This change is flushed immediately in
     * order to reflect changes across the network.
     * @param state 
     */
    public void setState(Integer state) {
        this.state = state;
        registerFieldChange("state", state);
        flushChanges();
    }

    public String getUsername() {
        return username;
    }

    public String getLocalUser() {
        return localUser;
    }

    public boolean isTutor() {
        return username.equals(channel.getTutorName());
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        registerFieldChange("note", note);
    }

    public JPanel getThumbPanel() {
        return thumbPanel;
    }

    /**************** Convenience methods *****************/
    public void showPMFrame() {
        if (pmFrame != null) {
            SwingUtilities.invokeLater(pmFrame.showCommand);
        }
    }

    public void hidePMFrame() {
        if (pmFrame != null) {
            SwingUtilities.invokeLater(pmFrame.hideCommand);
        }
    }

    public void dispose() {
        unregisterSubChannel();

        final JFrame frame = pmFrame;
        final JPanel thumb = thumbPanel;
        final Container parent = thumb.getParent();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                frame.setVisible(false);
                frame.dispose();

                if (parent != null) {
                    parent.remove(thumb);
                    parent.repaint();
                }
            }
        });
    }
}
