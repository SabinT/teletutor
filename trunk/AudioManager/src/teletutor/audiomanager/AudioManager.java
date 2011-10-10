/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.audiomanager;

/**
 * This service provides an interface that the ClassroomManager can use to start
 * or stop sending voice streams from the local computer. Setting up the channel
 * and establishing RTP Sessions shall be controlled by the implementation.
 * @author Rae
 */
public interface AudioManager {
    /**
     * Start sending streams from the microphone connected to this node.
     */
    void start();
    
    /**
     * Stop sending streams. Just stop, don't destroy the sendStream objects, because
     * we might need to start sending again soon enough.
     */
    void stop();
    
    /**
     * Mute all the receiveStreams being played.
     */
    void mute();
}
