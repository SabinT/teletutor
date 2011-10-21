package teletutor.audiomanager.impl;

/*
 * @(#)AVTransmit2.java	1.4 01/03/13
 *
 * Copyright (c) 1999-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
import java.io.*;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.*;
import javax.media.protocol.*;
import javax.media.protocol.DataSource;
import javax.media.format.*;
import javax.media.control.TrackControl;
import javax.media.rtp.*;
import javax.media.rtp.rtcp.*;
import java.util.Vector;

public class LANTransmitter {

    private boolean initialized = false;
    
    private String ipAddress;
    private int portBase;
    private Processor processor = null;
    private RTPManager rtpMgr;
    private DataSource dataOutput = null;
    
    // to control the transmission, call stop or initialize on this SendStream
    private SendStream sendStream;

    public LANTransmitter( String ipAddress, int pb) {
        this.ipAddress = ipAddress;
        this.portBase = pb;
    }

    /**
     * Starts the transmission. Returns null if transmission started ok.
     * Otherwise it returns a string with the reason why the setup failed.
     */
    public synchronized String initialize() {
        String result;

        // Create a processor for the specified media locator
        // and program it to output JPEG/RTP
        result = createProcessor();
        if (result != null) {
            return result;
        }

        // Create an RTP session to transmit the output of the
        // processor to the specified IP address and port no.
        result = createTransmitter();
        if (result != null) {
            processor.close();
            processor = null;
            return result;
        }

        // Start the transmission
        processor.start();
        // but pause the sendStream
        pause();
        
        return null;
    }

    /**
     * Stops the transmission if already started
     */
    
    public void pause() {
        try {
            sendStream.stop();
        } catch (IOException ex) {
            Logger.getLogger(LANTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resume() {
        try {
            sendStream.start();
        } catch (IOException ex) {
            Logger.getLogger(LANTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop() {
        synchronized (this) {
            if (processor != null) {
                processor.stop();
                processor.close();
                processor = null;
                
                rtpMgr.removeTargets("Session ended.");
                rtpMgr.dispose();
            }
        }
    }
    
    public RTPManager getRTPManager() {
        return rtpMgr;
    }

    private String createProcessor() {

        DataSource ds;

        CaptureDeviceInfo cdinfo;
        Format fmt = new AudioFormat(AudioFormat.LINEAR, 8000, 8, 1);
        Vector deviceList = CaptureDeviceManager.getDeviceList(fmt);

        if (deviceList.size() > 0) {
            System.out.println("Yeah!!");
            cdinfo = (CaptureDeviceInfo) deviceList.firstElement();
        } else {
            return "No Device.";
        }
        try {
            ds = Manager.createDataSource(cdinfo.getLocator());
        } catch (IOException ex) {
            Logger.getLogger(LANTransmitter.class.getName()).log(Level.SEVERE, null, ex);
            return "Could the not create a the DataSource.";
        } catch (NoDataSourceException ex) {
            Logger.getLogger(LANTransmitter.class.getName()).log(Level.SEVERE, null, ex);
            return "Could the not create a the DataSource.";
        }

        // Try to create a processor to handle the input device datasource
        try {
            processor = javax.media.Manager.createProcessor(ds);
        } catch (NoProcessorException npe) {
            return "Couldn't create processor";
        } catch (IOException ioe) {
            return "IOException creating processor";
        }

        // Wait for it to configure
        boolean result = waitForState(processor, Processor.Configured);
        if (result == false) {
            return "Couldn't configure processor";
        }

        // Get the tracks from the processor
        TrackControl[] tracks = processor.getTrackControls();

        // Do we have atleast one track?
        if (tracks == null || tracks.length < 1) {
            return "Couldn't find tracks in processor";
        }

        // Set the output content descriptor to RAW_RTP
        // This will limit the supported formats reported from
        // Track.getSupportedFormats to only valid RTP formats.
        ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
        processor.setContentDescriptor(cd);
        
        Format supported[];
        Format chosen;
        boolean atLeastOneTrack = false;

        // Program the tracks.
        for (int i = 0; i < tracks.length; i++) {
            Format format = tracks[i].getFormat();
            if (tracks[i].isEnabled()) {

                supported = tracks[i].getSupportedFormats();

                // We've set the output content to the RAW_RTP.
                // So all the supported formats should work with RTP.
                // We'll just pick the first one.

                if (supported.length > 0) {
                    if (supported[0] instanceof VideoFormat) {
                        //Ignore video
                        continue;
                    } else {
                        chosen = supported[0];
                    }
                    tracks[i].setFormat(chosen);
                    System.err.println("Track " + i + " is set to transmit as:");
                    System.err.println("  " + chosen);
                    atLeastOneTrack = true;
                } else {
                    tracks[i].setEnabled(false);
                }
            } else {
                tracks[i].setEnabled(false);
            }
        }

        if (!atLeastOneTrack) {
            return "Couldn't set any of the tracks to a valid RTP format";
        }

        // Realize the processor. This will internally create a flow
        // graph and attempt to create an output datasource for JPEG/RTP
        // audio frames.
        result = waitForState(processor, Controller.Realized);
        if (result == false) {
            return "Couldn't realize processor";
        }

        // Get the output data source of the processor
        dataOutput = processor.getDataOutput();

        return null;
    }

    /**
     * Use the RTPManager API to create sessions for each media 
     * track of the processor.
     */
    private String createTransmitter() {

        // Cheated.  Should have checked the type.
        PushBufferDataSource pbds = (PushBufferDataSource) dataOutput;
        PushBufferStream pbss[] = pbds.getStreams();
        
        SessionAddress localAddr, destAddr;
        InetAddress ipAddr;
        
        // Create a RTPManager for only the first track, if more than one are 
        // available
        rtpMgr = RTPManager.newInstance(); 
        
        int port;
        SourceDescription srcDesList[];


        try {
            // The local session address will be created on the
            // same port as the the target port. This is necessary
            // if you use AVTransmit2 in conjunction with JMStudio.
            // JMStudio assumes -  in a unicast session - that the
            // transmitter transmits from the same port it is receiving
            // on and sends RTCP Receiver Reports back to this port of
            // the transmitting host.

            port = portBase;
            ipAddr = InetAddress.getByName(ipAddress);

            //localAddr = new SessionAddress(InetAddress.getLocalHost(), port);
            //localAddr = new SessionAddress( InetAddress.getLocalHost(), SessionAddress.ANY_PORT);
            localAddr= new SessionAddress(ipAddr, port);
            destAddr = new SessionAddress(ipAddr, port);

            rtpMgr.initialize(localAddr);

            rtpMgr.addTarget(destAddr);

            System.err.println("Created RTP session: " + ipAddress + " " + port);

            sendStream = rtpMgr.createSendStream(dataOutput, 0);
            sendStream.start();
        } catch (Exception e) {
            return e.getMessage();
        }

        return null;
    }


    /****************************************************************
     * Convenience methods to handle processor's state changes.
     ****************************************************************/
    private Integer stateLock = new Integer(0);
    private boolean failed = false;

    Integer getStateLock() {
        return stateLock;
    }

    void setFailed() {
        failed = true;
    }

    private synchronized boolean waitForState(Processor p, int state) {
        p.addControllerListener(new StateListener());
        failed = false;

        // Call the required method on the processor
        if (state == Processor.Configured) {
            p.configure();
        } else if (state == Processor.Realized) {
            p.realize();
        }

        // Wait until we get an event that confirms the
        // success of the method, or a failure event.
        // See StateListener inner class
        while (p.getState() < state && !failed) {
            synchronized (getStateLock()) {
                try {
                    getStateLock().wait();
                } catch (InterruptedException ie) {
                    return false;
                }
            }
        }

        if (failed) {
            return false;
        } else {
            return true;
        }
    }

    /****************************************************************
     * Inner Classes
     ****************************************************************/
    class StateListener implements ControllerListener {

        public void controllerUpdate(ControllerEvent ce) {

            // If there was an error during configure or
            // realize, the processor will be closed
            if (ce instanceof ControllerClosedEvent) {
                setFailed();
            }

            // All controller events, send a notification
            // to the waiting thread in waitForState method.
            if (ce instanceof ControllerEvent) {
                synchronized (getStateLock()) {
                    getStateLock().notifyAll();
                }
            }
        }
    }

    /****************************************************************
     * Sample Usage for AVTransmit2 class
     ****************************************************************/
    public static void main(String[] args) {
        LANTransmitter at = new LANTransmitter("224.144.251.104", 22200);
        // Start the transmission
        String result = at.initialize();

        // result will be non-null if there was an error. The return
        // value is a String describing the possible error. Print it.
        if (result != null) {
            System.err.println("Error : " + result);
            System.exit(0);
        }

        System.err.println("Start transmission for 60 seconds...");

        // Transmit for 60 seconds and then close the processor
        // This is a safeguard when using a capture data source
        // so that the capture device will be properly released
        // before quitting.
        // The right thing to do would be to have a GUI with a
        // "Stop" button that would call stop on AVTransmit2
//        boolean stopped = false;
//        for (int i = 0; i < 20; i++) {
        
        at.resume();
        
        try {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LANTransmitter.class.getName()).log(Level.SEVERE, null, ex);
        }
            
//            if (stopped) {
//                at.resume();
//            } else {
//                at.pause();
//            }
//            
//            stopped = !stopped;
//        }

        // Stop the transmission
        at.stop();

        System.err.println("...transmission ended.");

        System.exit(0);
    }

    static void prUsage() {
        System.err.println("Usage: AVTransmit2 <sourceURL> <destIP> <destPortBase>");
        System.err.println("     <sourceURL>: input URL or file name");
        System.err.println("     <destIP>: multicast, broadcast or unicast IP address for the transmission");
        System.err.println("     <destPortBase>: network port numbers for the transmission.");
        System.err.println("                     The first track will use the destPortBase.");
        System.err.println("                     The next track will use destPortBase + 2 and so on.\n");
        System.exit(0);
    }
}
