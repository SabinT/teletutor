/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.audiomanager.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import teletutor.audiomanager.services.AudioManager;

/**
 * An implementation of AudioManager for LAN environments, based on 
 * multicasting.
 * 
 * @author Sabin Timalsena
 */
public class LANAudioManager implements AudioManager {

    // entitities needed for reception and playback.    
    private LANTransmitter transmitter;
    private LANReceiver receiver;

    String mcastAddress;
    int port;
    
    public LANAudioManager(String mcastAddress, int port) {
        this.mcastAddress = mcastAddress;
        this.port = port;
    }
    
    @Override
    public void initialize() throws Exception {
        transmitter = new LANTransmitter(mcastAddress, port);
        String error = transmitter.start();
        if (error != null) {
            throw new Exception("Could not initialize transmitter. Error: " + error);
        }
        
        receiver = new LANReceiver(mcastAddress + '/' + port);
        if (!receiver.initialize(transmitter.getRTPManager())) {
            throw new Exception("Could not initialize receiver.");
        }
    }
    
    @Override
    public void stop() {
        transmitter.stop();
        receiver.stop();
    }
    
    @Override
    public void resume() {
        transmitter.resume();
    }

    @Override
    public void pause() {
        transmitter.pause();
    }

    @Override
    public void mute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static void main(String[] args) {
        LANAudioManager mgr = new LANAudioManager("224.144.251.104", 22200);
        
        try {
            mgr.initialize();
        } catch (Exception ex) {
            Logger.getLogger(LANAudioManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
        
        mgr.resume();
        
        try {
            Thread.currentThread().sleep(60000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LANAudioManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        mgr.stop();
    }
}
