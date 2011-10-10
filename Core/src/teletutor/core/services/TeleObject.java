/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.input.ClassLoaderObjectInputStream;

/**
 * The base class for all objects that can register a subchannel on the TeleChannel. 
 * Instances of TeleObjects with the same name (across JGroups instances) form a
 * messaging group
 * @author Rae
 */
public abstract class TeleObject {
    /**
     * The name must contain 8-bit characters.
     */
    private String name = null;
    private TeleChannel channel = null;
    
    public TeleObject () {
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
        
        // remove old registration
        unregisterSubChannel();
        chan.registerSubChannel(this);
        // if successful, keep a reference to the channel for future use
        this.channel = chan;
    }
    
    public final void unregisterSubChannel() {
        if (channel != null) channel.unregisterSubChannel(this);
    }
    
    /**
     * Send an externalizable/serializable object that is disseminated to all
     * ends of the subchannel.
     * @param obj the object to send
     * @throws Exception 
     */
    public void sendObject(Serializable obj) throws Exception {
        if (channel == null) {
            System.out.println("Null Channel");
            return;
        }
        channel.send(this, obj);
        System.out.println("sent..");
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
     */
    public final void receivedBytes(final byte[] buf, int offset, int length) {
        System.out.println("Received ze byte array: " + offset + ":"+ length);
        
        try {
            ClassLoaderObjectInputStream in = new ClassLoaderObjectInputStream(this.getClass().getClassLoader(),
                    new ByteArrayInputStream(buf, offset + 1, length -1));
            received(in.readObject());
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "Could not unmarshall message payload: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TeleObject.class.getName()).log(Level.SEVERE, "Could not load Class: {0}", ex.getMessage());
        }
    }
}
