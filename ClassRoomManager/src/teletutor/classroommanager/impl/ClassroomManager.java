/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.impl;

import teletutor.classroommanager.UI.MemberProxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.classroommanager.UI.MemberListPanel;
import teletutor.classroommanager.services.TextMessage;
import teletutor.classroommanager.services.TextMessageObservable;
import teletutor.classroommanager.services.TextMessageObserver;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.UpdateInfo;
import teletutor.core.services.ViewObserver;

/**
 *
 * @author Sabin Timalsena
 */
public class ClassroomManager extends TeleObject implements ViewObserver, TextMessageObservable {

    /**
     * a map containing the current members' names and proxy objects
     */
    Map<String, MemberProxy> memberMap = new HashMap<String, MemberProxy>();
    // TODO maybe maintain a separate reference to the tutor proxy
    // the visual component showing the list of students
    MemberListPanel memberListPanel;

    public ClassroomManager(String name, TeleChannel chan) throws Exception {
        super(name, chan);
        memberListPanel = new MemberListPanel(this);

        chan.addViewObserver(this);

    }

    /**
     * Sends a text message to the open discussion, which is received by the 
     * ClassroomManagers in each node, and hence visible everywhere
     * @param msg 
     */
    public void sendTextMessage(String msg) {
        try {
            sendObject(new TextMessage(getChannel().getChannelName(), msg));
        } catch (Exception ex) {
            Logger.getLogger(ClassroomManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Whenever a new member joins or leaves the lecture, this method is called.
     * If new member, add a new MemberProxy
     * If old member absent, remove the MemberProxy, and the visual components
     * associated.
     * @param set 
     */
    @Override
    public synchronized void newViewArrived(Set<String> newMemberSet) {
        Set<String> memberSet = memberMap.keySet();

        // first add the new members        
        for (String memberName : newMemberSet) {
            if (!memberSet.contains(memberName)) {
                try {
                    // add new MemberProxy
                    MemberProxy newProxy = new MemberProxy(memberName + "_proxy", getChannel());
                    newProxy.init(memberName);
                    memberMap.put(memberName, newProxy);

                    // setup visual components too, add them to a panel
                    memberListPanel.addMember(newProxy);
                } catch (Exception ex) {
                    Logger.getLogger(ClassroomManager.class.getName()).log(Level.SEVERE, "Could not register new member: " + memberName, ex);
                }
            }
        }

        // then remove any members that disconnected
        Set<String> toRemove = new HashSet<String>();
        for (String memberName : memberSet) {
            if (!newMemberSet.contains(memberName)) {
                // remove everything associated with that member
                MemberProxy deadMember = memberMap.get(memberName);
                deadMember.dispose();

                toRemove.add(memberName);
            }
        }

        for (String memberName : toRemove) {
            memberMap.remove(memberName);
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
     * Do necessary stuff when new message arrives
     * @param o 
     */
    @Override
    public synchronized void received(Object obj) {
        if (obj instanceof TextMessage) {
            notifyTMObservers((TextMessage) obj);
        }
    }

    @Override
    public synchronized void objectUpdated(UpdateInfo ui) {
        return;
    }

    public synchronized MemberProxy getMemberProxy(String username) {
        return memberMap.get(username);
    }
    
    public synchronized MemberProxy gteLocalProxy() {
        return memberMap.get(getChannel().getChannelName());
    }

    public MemberListPanel getMemberListPanel() {
        return memberListPanel;
    }
}
