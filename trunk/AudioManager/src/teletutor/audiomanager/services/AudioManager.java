/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.audiomanager.services;

/**
 * This service provides an interface that the ClassroomManager can use to start
 * or stop sending voice streams from the local computer. Setting up the channel
 * and establishing RTP Sessions shall be controlled by the implementation.
 * 
 * The AudioManager should initially be in a started but PAUSED state, which is,
 * the necessary devices have been set up but streams are not being sent. The 
 * necessary PLAYERS are also set up during construction, which automatically 
 * detect incoming streams and play them.
 * 
 * The classroom/lecture shall have a single RTP session running either on a 
 * multicast address, or through a JChannel. The JChannel used by the RTPConnector
 * shall be different from the one used by the classroom for other activities.
 * 
 * @author Sabin Timalsena
 */
public interface AudioManager {
    /**
     * Set up the necessary devices, processors etc.
     * The other functions of the AudioManager can only function when it has
     * been initialized. Remember to call @stop before quitting to release the 
     * resources.
     * 
     * @throws an exception is thrown if there was an error during initialization
     */
    void initialize () throws Exception;
    
    /**
     * Release all the resources created during initialization.
     */
    void stop();
    
    /**
     * Resume sending streams from the microphone connected to this node.
     */
    void resume();
    
    /**
     * Temporarily pause sending streams. Just stop, don't destroy the sendStream objects, because
     * we might need to start sending again soon enough.
     */
    void pause();
    
    /**
     * Mute all the receiveStreams being played. But this does not affect the sending
     * of streams from this node.
     */
    void mute();
}
