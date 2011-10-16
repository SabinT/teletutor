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
import org.jgroups.Address;
import org.jgroups.ChannelClosedException;
import org.jgroups.ChannelNotConnectedException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.ViewObserver;

/**
 *
 * @author Sabin Timalsena
 */
public class TeleChannelImpl implements TeleChannel {
    
    private Map<String, TeleObject> channelMap = new HashMap<String, TeleObject>();
    private JChannel channel = null;
    
    private TeleChannel.Privilege privilege;
    
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
    public TeleChannelImpl (String configFile, String groupName, String channelName) 
    throws Exception
    {
        try {
            channel = new JChannel(configFile);

            channel.setName(channelName);

            channel.connect(groupName);

            // some receiver that can demultiplex the messages into respective
            // subchannels
            channel.setReceiver(new SimpleReceiver());
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
        System.out.println("Object Registered, class: " + obj.getClass().getName());
    }

    @Override
    public void unregisterSubChannel(TeleObject tObj) {
        channelMap.remove(tObj.getName());
    }

    /**
     * Send object "obj" to be disseminated to the subchannel of TeleObject "tObj".
     * @param tObj The object which is a part of the required subchannel.
     * @param obj The message to be sent.
     * @throws Exception 
     */
    @Override
    public void send(TeleObject tObj, Serializable obj) throws Exception {
        // drop the message if desired subChannel does not exist
        if (!channelMap.containsKey(tObj.getName())) return;
        
        // TODO take appropriate action based on the privilege.
        
        // include a header to identify the message subChannel
        Message msg = new Message(null, null, obj);
        SubChannelHeader hdr = new SubChannelHeader(tObj.getName());
        msg.putHeader((short)2000, hdr);
        
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
     * @param tObj The subchannel
     * @param obj The message to be sent
     * @throws Exception 
     */
    @Override
    public void send(String memberStr, TeleObject tObj, Serializable obj) throws Exception {
        // drop the message if desired subChannel does not exist
        if (!channelMap.containsKey(tObj.getName())) return;
        
        // TODO take appropriate action based on the privilege.
        
        Address member = memberMap.get(memberStr);
        if (member == null) {
            System.out.println("Could not find the member to send message to " + memberStr);
            return;
        }
        Message msg = new Message(member, null, obj);
        
        // include a header to identify the message subChannel
        SubChannelHeader hdr = new SubChannelHeader(tObj.getName());
        msg.putHeader((short)2000, hdr);
        
        try {
            channel.send(msg);
        } catch (ChannelNotConnectedException ex) {
            throw new Exception(ex.getMessage());
        } catch (ChannelClosedException ex) {
            throw new Exception(ex.getMessage());
        }
    }
    
    @Override
    public void setPrivilege(Privilege prv) {
        privilege = prv;
    }

    @Override
    public Privilege getPrivilege() {
        return privilege;
    }

    // storage and notification mechanism for class members
    private Map<String,Address> memberMap = new HashMap<String, Address>();
    private ArrayList<ViewObserver> viewObservers = new ArrayList<ViewObserver>();
    
    @Override
    public void addViewObserver(ViewObserver obs) {
        viewObservers.add(obs);
    }

    @Override
    public void removeViewObserver(ViewObserver obs) {
        viewObservers.remove(obs);
    }

    @Override
    public void notifyViewObservers() {
        for (ViewObserver obs: viewObservers) {
            obs.newViewArrived(memberMap.keySet());
        }
    }

    class SimpleReceiver extends ReceiverAdapter {

        @Override
        public void receive(Message msg) {
            SubChannelHeader header = (SubChannelHeader)msg.getHeader((short)2000);
            String name = header.getSubChannelName();
            System.out.println("Received message on subchannel: " + name);
            
            // drop the message if no subchannel registered
            if (channelMap == null) return;
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
            Vector<Address> members = view.getMembers();
            memberMap.clear();
            for (Address addr :members) {
                memberMap.put(addr.toString(), addr);
            }
            notifyViewObservers();
        }
        
    }
    
}
