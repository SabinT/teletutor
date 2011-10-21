/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.audiomanager.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.MediaLocator;
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
    private AVTransmit2 transmitter;
    private AVReceive2 receiver;
//    private LANTransmitter transmitter;
//    private LANReceiver receiver;
    String mcastAddress;
    int port;
    String cname;
    
    public LANAudioManager(String name, TeleChannel chan, String mcastAddress, int port, String cname) throws Exception {
        super(name, chan);
        this.mcastAddress = mcastAddress;
        this.port = port;
        this.cname = cname;
    }

    @Override
    public void initialize() throws Exception {
//        transmitter = new LANTransmitter(mcastAddress, port);
//        String error = transmitter.initialize();
//        if (error != null) {
//            throw new Exception("Could not initialize transmitter. Error: " + error);
//        }
//
//        receiver = new LANReceiver(mcastAddress + '/' + port);
//        // TODO maybe initialize with the same RTPManager as Transmitter
//        if (!receiver.initialize()) {
//            throw new Exception("Could not initialize receiver.");
//        }

        transmitter = new AVTransmit2(new MediaLocator("dsound://8000"), mcastAddress, Integer.toString(port), null, cname);
        String error = transmitter.initialize();
        if (error != null) {
            transmitter = new AVTransmit2(new MediaLocator("javasound://8000"), mcastAddress, Integer.toString(port), null, cname);
            error = transmitter.initialize();
            if (error != null) {
                throw new Exception("Could not initialize transmitter. Error: " + error);
            }
        }
        
        // do not transmit unless resume() is called
        // transmitter.resume();

        String[] sessions = new String[1];
        sessions[0] = mcastAddress + '/' + port;
        receiver = new AVReceive2(sessions, cname);
        // TODO maybe initialize with the same RTPManager as Transmitter
        if (!receiver.initialize()) {
            throw new Exception("Could not initialize receiver.");
        }
    }

    /** 
     * Call this method when transmission no longer required. For temporary 
     * suspension, use @pause() and @resume() instead.
     */
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
        try {
            LANAudioManager mgr = new LANAudioManager("AudioManager", null, "224.144.251.104", 22200, "Heme");
            mgr.initialize();
            mgr.resume();

            try {
                Thread.currentThread().sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(LANAudioManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            mgr.stop();
        } catch (Exception ex) {
            Logger.getLogger(LANAudioManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    @Override
    public void received(Object obj) {
        if (obj instanceof SimpleMessage) {
            String msg = ((SimpleMessage) obj).getMessage();
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
