/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgroups.Address;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.conf.ClassConfigurator;
import teletutor.core.services.CoreMessenger;
import teletutor.core.services.SimpleMessage;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.UpdateInfo;
import teletutor.core.services.ViewObserver;
import teletutor.core.utilities.LectureBean;

/**
 *
 * @author Sabin Timalsena
 */
public class TeleChannelImpl implements TeleChannel {

    private Map<String, TeleObject> channelMap = new HashMap<String, TeleObject>();
    private JChannel channel = null;
    private String groupName;
    private String channelName;
    private String tutorName;
    private LectureBean lecture;
    // storage and notification mechanism for class members
    private Map<String, Address> memberMap = new HashMap<String, Address>();
    private final Object memberLock = new Object();
    private ArrayList<ViewObserver> viewObservers = new ArrayList<ViewObserver>();

    /**
     * Start the channel based on the protocol stack in the config file.
     * @param configFile The .xml file with all the protocols
     * @param groupName The name of the group to join or create
     * @param channelName The name of this node/channel in the group; should be
     *  same as the username of the member
     * 
     * Note that starting a new channel is a lengthy process, and may take some time
     * @throws Exception 
     */
    public TeleChannelImpl(String configFile, LectureBean lecture, String channelName)
            throws Exception {

        this.lecture = lecture;
        this.tutorName = lecture.getTutor();

        try {
            ClassConfigurator.add((short) 2000, SubChannelHeader.class);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        this.groupName = "Lecture_" + lecture.getLectureID();
        this.channelName = channelName;
        try {
            channel = new JChannel(configFile);

            channel.setName(channelName);

            // need to set the receiver before in order to get initial views
            channel.setReceiver(new SimpleReceiver());

            channel.connect(groupName);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            throw ex;
        }

    }

    public void closeChannel() {
        channel.disconnect();
        channel.close();
    }

    @Override
    public void registerSubChannel(TeleObject obj) throws Exception {
        // check if the Map already has an entry for the object's name
        if (channelMap.containsKey(obj.getName())) {
            // already has an entry
            throw new Exception("TeleObject with name " + obj.getName() + " already registered.");
        }

        channelMap.put(obj.getName(), obj);
        System.out.println("Object Registered, class: " + obj.getClass().getName() + ", name: " + obj.getName());
    }

    @Override
    public void unregisterSubChannel(TeleObject tObj) {
        channelMap.remove(tObj.getName());
        System.out.println("Object Unregistered, class: " + tObj.getClass().getName() + ", name: " + tObj.getName());
    }

    /**
     * Send object "obj" to be disseminated to the subchannel of TeleObject "tObj".
     * @param tObj The object which is a part of the required subchannel.
     * @param obj The message to be sent.
     * @throws Exception 
     */
    @Override
    public void send(String destObj, Serializable obj) throws Exception {
        // do not drop the message even if desired subChannel does not exist
        // TODO make sure is this is a good idea
        // if (!channelMap.containsKey(destObj)) return;

        // include a header to identify the message subChannel
        Message msg = new Message(null, null, obj);
        SubChannelHeader hdr = new SubChannelHeader(destObj);
        msg.putHeader((short) 2000, hdr);

        try {
            channel.send(msg);
        } catch (ChannelNotConnectedException ex) {
            throw new Exception(ex.getMessage());
        } catch (ChannelClosedException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * Send a message on a specific subchannel (TeleObject) to a specific member
     * of the group.
     * @param member
     * @param destObj The subchannel
     * @param obj The message to be sent
     * @throws Exception 
     */
    @Override
    public void send(String memberStr, String destObj, Serializable obj) throws Exception {
        // if (!channelMap.containsKey(destObj)) return;
        Address member = null;
        synchronized (memberLock) {
            member = memberMap.get(memberStr);
        }
        if (member == null) {
            System.out.println("Could not find the member to send message to " + memberStr);
            return;
        }

        Message msg = new Message(member, null, obj);
        // include a header to identify the message subChannel
        SubChannelHeader hdr = new SubChannelHeader(destObj);
        msg.putHeader((short) 2000, hdr);

        try {
            channel.send(msg);
        } catch (ChannelNotConnectedException ex) {
            throw new Exception(ex.getMessage());
        } catch (ChannelClosedException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public synchronized void addViewObserver(ViewObserver obs) {
        viewObservers.add(obs);
        synchronized (memberLock) {
            obs.newViewArrived(memberMap.keySet());
        }
    }

    @Override
    public synchronized void removeViewObserver(ViewObserver obs) {
        viewObservers.remove(obs);
    }

    @Override
    public synchronized void notifyViewObservers() {
        synchronized (memberLock) {
            for (ViewObserver obs : viewObservers) {
                obs.newViewArrived(memberMap.keySet());
            }
        }
    }

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getChannelName() {
        return channelName;
    }

    @Override
    public String getTutorName() {
        // TODO store the name of the tutor on creation through database lookup
        return tutorName;
    }

    @Override
    public LectureBean getLecture() {
        return lecture;
    }

    @Override
    public boolean isTutorChannel() {
        return (tutorName.equals(channelName));
    }

    class SimpleReceiver extends ReceiverAdapter {

        @Override
        public void receive(Message msg) {
            SubChannelHeader header = (SubChannelHeader) msg.getHeader((short) 2000);
            String name = header.getSubChannelName();
            System.out.println("Received message on subchannel: " + name);

            // drop the message if no subchannel registered
            if (channelMap == null) {
                return;
            }
            if (channelMap.containsKey(name)) {
                // call the appropriate callback
                //channelMap.get(name).sayHello();
                channelMap.get(name).receivedBytes(msg.getBuffer(), msg.getOffset(), msg.getLength());
            }
        }

        @Override
        /**
         * Update the internal map containing the members' names, then notify the
         * observers
         */
        public void viewAccepted(View view) {
            synchronized (memberLock) {
                Vector<Address> members = view.getMembers();
                memberMap.clear();
                for (Address addr : members) {
                    memberMap.put(addr.toString(), addr);
                }

                // check if the tutor is present in the new view; if not, send
                // a LEAVE_LECTURE message to the CoreMessenger
                if (!memberMap.containsKey(tutorName)) {
                    try {
                        send(channelName, CoreMessenger.class.getCanonicalName(), new SimpleMessage(CoreMessenger.LEAVE_LECTURE));
                    } catch (Exception ex) {
                        Logger.getLogger(TeleChannelImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                notifyViewObservers();
            }
        }
        // TODO suspect messages
    }

    @Override
    public void pushMessage(String destObj, Serializable obj) {
        if (obj instanceof UpdateInfo) {
            channelMap.get(destObj).update((UpdateInfo) obj);
        } else {
            channelMap.get(destObj).received(obj);
        }
    }
}
