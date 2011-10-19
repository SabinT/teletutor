/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;
import teletutor.core.utilities.FieldUtil;

/**
 * The base class for all objects that can register a subchannel on the TeleChannel. 
 * Instances of TeleObjects with the same name (across JGroups instances) form a
 * messaging group
 * @author Sabin Timalsena
 */
public abstract class TeleObject {
    /**
     * The name must contain 8-bit characters.
     */
    private String name = null;
    protected TeleChannel channel = null;
    
    /**
     * The map used to record the changes that are propagated across the network
     */
    private UpdateInfo updateInfo = new UpdateInfo();
    
    /**
     * Construct a new TeleObject to communicate on the given channel
     * @param name A unique name for the object. Make sure you don't pass null
     * @param chan The channel to send/receive messages on
     */
    public TeleObject (String name, TeleChannel chan) throws Exception {
        this.name = name;
        registerSubChannel(chan);
    }
    
    public final void setName(String name) {
        this.name = name;
    }
    
    public final String getName () {
        return name;
    }
    
    /**
     * Register a subchannel on the given Channel, identified by the object's name.
     * If subchannel with given name already exists, this method throws an exception.
     * @param chan the channel on which to register
     * @throws Exception 
     */
    public final void registerSubChannel (TeleChannel chan) throws Exception {
        if (name == null) throw new Exception("Name of Object has not been set yet");
        if (chan == null) {
            channel = chan;
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "Supplied empty TeleChannel.");
            return;
        }
        // remove old registration
        unregisterSubChannel();
        chan.registerSubChannel(this);
        // if successful, keep a reference to the channel for future use
        this.channel = chan;
    }
    
    /**
     * Called when the object no longer needs to be on the network; typically when
     * it is being destroyed.
     */
    public final void unregisterSubChannel() {
        if (channel != null) channel.unregisterSubChannel(this);
    }
    
    public final TeleChannel getChannel() {
        return channel;
    }
    
    /**
     * Used to check is the node that this object is residing in is the node of
     * the Tutor, i.e. if the channel name is the same as the tutor name
     * @return 
     */
    public boolean isOnTutorNode () {
        return (channel.getChannelName().equals(channel.getTutorName()));
    }
    
    /**
     * Send an externalizable/serializable object that is disseminated to all
     * ends of the subchannel.
     * @param obj the object to send
     * @throws Exception 
     */
    public void sendObject(Serializable obj) throws Exception {
        if (channel == null) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "TeleChannel not set up yet");
            return;
        }
        channel.send(this.getName(), obj);
    }
    
    /**
     * Send the message only to a particular member of the group.
     * @param member
     * @param obj
     * @throws Exception 
     */
    public void sendObject(String member, Serializable obj) throws Exception {
        if (channel == null) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "TeleChannel not set up yet");
            return;
        }
        channel.send(member, this.getName(), obj);
    }
    
    /**
     * Send the message only to a particular member of the group, on a particular
     * subchannel i.e. TeleObject with a particular name.
     * @throws Exception 
     */
    public void sendObject(String member, String destObj, Serializable obj) throws Exception {
        if (channel == null) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "TeleChannel not set up yet");
            return;
        }
        channel.send(member, destObj, obj);
    }
    
    /**
     * A callback to handle the received object.
     * Note: check the class (instanceof) the object to make sure it indeed is
     * a message for this TeleObject.
     * The implementing class must ensure synchronization (if necessary) and NOT
     * do the heavy lifting in this function.
     * @param obj the object received
     */
    public abstract void received(Object obj);
    
    /*
     * A method to unmarshall the message object from the byte buffer
     * obtained from JGroups Message. Deserialization using Message.getObject()
     * avoided due to ClassLoader issues.
     * WARNING: The object must be Serializable or Externalizable.
     * The Object Stream is from offset + 1 to offset + length - 1
     * For other types, see Message.java --> getObject() method. 
     * 
     * This method is for callback purposes and will be called automatically by
     * the TeleChannel.
     */
    public final void receivedBytes(final byte[] buf, int offset, int length) {
        try {
            ClassLoaderObjectInputStream in = new ClassLoaderObjectInputStream(this.getClass().getClassLoader(),
                    new ByteArrayInputStream(buf, offset + 1, length -1));
            Object obj = in.readObject();
            in.close();
            
            // check if the object is meant for updating properties
            if (obj instanceof UpdateInfo) {
                update((UpdateInfo)obj);
            } else {
                received(obj);
            }
        } catch (IOException ex) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "Could not unmarshall message payload: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "Could not load Class: {0}", ex.getMessage());
        }
    }
    
    /**
     * Update the map containing field name/value pairs with the new data, for a 
     * given field. To be used in the SETTER methods.
     * @param fieldName 
     */
    protected final void registerFieldChange (String fieldName, Serializable value) {
        // TODO maybe flush automatically if changes have not been flushed for
        // over a second (or some such interval of time)
        updateInfo.put(fieldName, value);
    }
    
    /**
     * Send the changes (the updateInfo map) across the network, so the 
     * corresponding objects on the other ends can get the changes.
     */
    public final void flushChanges () {
        // TODO it could be better to disallow flushing for a certain period of
        // time after a flush has been made. Timers
        try {
            sendObject(updateInfo);
            updateInfo.clear();
        } catch (Exception ex) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "Failed to flush object properties.", ex);
        }
    }
    
    /**
     * Update this properties based on the key/value pairs obtained from the network.
     * Or elsewhere.
     * @param changes 
     */
    public final void update(UpdateInfo changes) {
        Set<String> keySet = changes.keySet();
        for (String field: keySet) {
            setField(field, changes.get(field));
        }
        objectUpdated(changes);
    }
    
    /**
     * A hook to allow the object to be notified of changes arriving from the network
     * and take appropriate action
     * @param changes 
     */public abstract void objectUpdated(UpdateInfo changes);
    
    /**
     * This method uses reflection to set one of its fields to a given value
     * @param obj
     * @param fieldName
     * @param value 
     */
    protected final void setField(String fieldName, Object value) {
        Class cls = this.getClass();
        
        // TODO or maybe check the superclasses just as high as the TeleObject 
        // class
        while (cls.getSuperclass() != null) {
            System.out.println(cls.getName());
            
            try {
                Field field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(this, value);
                break;
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // if field not found in this class, check the superclass
            cls = cls.getSuperclass();
        }
    }
}
