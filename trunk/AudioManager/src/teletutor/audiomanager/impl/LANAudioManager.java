/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.audiomanager.impl;

import teletutor.audiomanager.services.AudioManager;
import teletutor.core.services.SimpleMessage;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.UpdateInfo;

/**
 * An implementation of AudioManager for LAN environments, based on 
 * multicasting.
 * 
 * @author Sabin Timalsena
 */
public class LANAudioManager extends TeleObject implements AudioManager {

    // entitities needed for reception and playback.    
    private LANTransmitter transmitter;
    private LANReceiver receiver;
    String mcastAddress;
    int port;

    public LANAudioManager(String name, TeleChannel chan, String mcastAddress, int port) throws Exception {
        super(name, chan);
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
//        try {
//            LANAudioManager mgr = new LANAudioManager("AudioManager", null, "224.144.251.104", 22200);
//            mgr.initialize();
//        } catch (Exception ex) {
//            Logger.getLogger(LANAudioManager.class.getName()).log(Level.SEVERE, null, ex);
//            System.exit(0);
//        }
//        
//        mgr.resume();
//        
//        try {
//            Thread.currentThread().sleep(60000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(LANAudioManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        mgr.stop();
    }

    @Override
    public void received(Object obj) {
        if (obj instanceof SimpleMessage) {
            String msg = ((SimpleMessage)obj).getMessage();
            if (msg.equals("pause")) {
                pause();
            } else if (msg.equals("resume")) {
                resume();
            }
        }
    }

    @Override
    public void objectUpdated(UpdateInfo ui) {
        return;
    }
}
