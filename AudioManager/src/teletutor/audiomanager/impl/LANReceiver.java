package teletutor.audiomanager.impl;

/*
 * @(#)LANReceiver.java	1.3 01/03/13
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

import java.awt.*;
import java.net.*;
import java.util.Vector;

import javax.media.*;
import javax.media.rtp.*;
import javax.media.rtp.event.*;
import javax.media.protocol.DataSource;
import javax.media.control.BufferControl;


/**
 * LANReceiver to receive RTP transmission using the new RTP API.
 * USAGE: LANReceiver  224.122.122.122/8800  224.122.122.122/8802
 */
public class LANReceiver implements ReceiveStreamListener, SessionListener, 
	ControllerListener
{
    private boolean initialized = false;
    
    String session = null;
    RTPManager mgr = null;
    Vector playerWindows = null;

    boolean dataReceived = false;
    final Object dataSync = new Object();


    public LANReceiver(String session) {
	this.session = session;
    }

    public boolean initialize() {

        try {
	    InetAddress ipAddr;
	    SessionAddress localAddr = new SessionAddress();
	    SessionAddress destAddr;

	    //mgrs = new RTPManager();
	    playerWindows = new Vector();

	    SessionLabel sessionLabel;

            // Parse the session addresses.
            try {
                sessionLabel = new SessionLabel(session);
            } catch (IllegalArgumentException e) {
                System.err.println("Failed to parse the session address given: " + session);
                return false;
            }

            System.err.println("  - Open RTP session for: addr: " + sessionLabel.addr + " port: " + sessionLabel.port + " ttl: " + sessionLabel.ttl);

            mgr= RTPManager.newInstance();
            mgr.addSessionListener(this);
            mgr.addReceiveStreamListener(this);

            ipAddr = InetAddress.getByName(sessionLabel.addr);

            if( ipAddr.isMulticastAddress()) {
                // local and remote address pairs are identical:
                localAddr= new SessionAddress( ipAddr,
                                               sessionLabel.port,
                                               sessionLabel.ttl);
                destAddr = new SessionAddress( ipAddr,
                                               sessionLabel.port,
                                               sessionLabel.ttl);
            } else {
                localAddr= new SessionAddress( InetAddress.getLocalHost(),
                                               sessionLabel.port);
                destAddr = new SessionAddress( ipAddr, sessionLabel.port);
            }

            mgr.initialize( localAddr);

            // You can try out some other buffer size to see
            // if you can get better smoothness.
            BufferControl bc = (BufferControl)mgr.getControl("javax.media.control.BufferControl");
            if (bc != null)
                bc.setBufferLength(50);

            mgr.addTarget(destAddr);

        } catch (Exception e){
            System.err.println("Cannot create the RTP Session: " + e.getMessage());
            return false;
        }

        return true;
    }
    
    /**
     * This method allows us to reuse an existing RTPManager for receiving purposes.
     * It is assumed that the RTPManager was created for a transmitter class, and 
     * has been properly set up with the necessary RTPConnector or a multicast
     * address.
     * @param man The RTPManager that has been already prepared for use.
     * @return 
     */
    public boolean initialize(RTPManager man) {
        mgr = man;
        mgr.addSessionListener(this);
        mgr.addReceiveStreamListener(this);
        
        playerWindows = new Vector();
        
        BufferControl bc = (BufferControl)mgr.getControl("javax.media.control.BufferControl");
        if (bc != null)
            bc.setBufferLength(50);
        
        return true;
    }


    public boolean isDone() {
	return playerWindows.size() == 0;
    }


    /**
     * Close the players and the session managers.
     */
    protected void stop() {

	for (int i = 0; i < playerWindows.size(); i++) {
	    try {
		((PlayerWindow)playerWindows.elementAt(i)).close();
	    } catch (Exception e) {}
	}

	playerWindows.removeAllElements();

	// close the RTP session.

        if (mgr != null) {
            mgr.removeTargets( "Closing session from AVReceive2");
            mgr.dispose();
            mgr = null;
        }

    }


    PlayerWindow find(Player p) {
	for (int i = 0; i < playerWindows.size(); i++) {
	    PlayerWindow pw = (PlayerWindow)playerWindows.elementAt(i);
	    if (pw.player == p)
		return pw;
	}
	return null;
    }


    PlayerWindow find(ReceiveStream strm) {
	for (int i = 0; i < playerWindows.size(); i++) {
	    PlayerWindow pw = (PlayerWindow)playerWindows.elementAt(i);
	    if (pw.stream == strm)
		return pw;
	}
	return null;
    }


    /**
     * SessionListener.
     */
    public synchronized void update(SessionEvent evt) {
	if (evt instanceof NewParticipantEvent) {
	    Participant p = ((NewParticipantEvent)evt).getParticipant();
	    System.err.println("  - A new participant had just joined: " + p.getCNAME());
	}
    }


    /**
     * ReceiveStreamListener
     */
    public synchronized void update( ReceiveStreamEvent evt) {

	RTPManager mgr = (RTPManager)evt.getSource();
	Participant participant = evt.getParticipant();	// could be null.
	ReceiveStream stream = evt.getReceiveStream();  // could be null.

	if (evt instanceof RemotePayloadChangeEvent) {
     
	    System.err.println("  - Received an RTP PayloadChangeEvent.");
	    System.err.println("Sorry, cannot handle payload change.");
	    System.exit(0);

	}
    
	else if (evt instanceof NewReceiveStreamEvent) {

	    try {
		stream = ((NewReceiveStreamEvent)evt).getReceiveStream();
		DataSource ds = stream.getDataSource();

		// Find out the formats.
		RTPControl ctl = (RTPControl)ds.getControl("javax.media.rtp.RTPControl");
		if (ctl != null){
		    System.err.println("  - Recevied new RTP stream: " + ctl.getFormat());
		} else
		    System.err.println("  - Recevied new RTP stream");

		if (participant == null)
		    System.err.println("      The sender of this stream had yet to be identified.");
		else {
		    System.err.println("      The stream comes from: " + participant.getCNAME()); 
		}

		// create a player by passing datasource to the Media Manager
		Player p = javax.media.Manager.createPlayer(ds);
		if (p == null)
		    return;

		p.addControllerListener(this);
		p.realize();
		PlayerWindow pw = new PlayerWindow(p, stream);
		playerWindows.addElement(pw);

		// Notify intialize() that a new stream had arrived.
		synchronized (dataSync) {
		    dataReceived = true;
		    dataSync.notifyAll();
		}

	    } catch (Exception e) {
		System.err.println("NewReceiveStreamEvent exception " + e.getMessage());
		return;
	    }
        
	}

	else if (evt instanceof StreamMappedEvent) {

	     if (stream != null && stream.getDataSource() != null) {
		DataSource ds = stream.getDataSource();
		// Find out the formats.
		RTPControl ctl = (RTPControl)ds.getControl("javax.media.rtp.RTPControl");
		System.err.println("  - The previously unidentified stream ");
		if (ctl != null)
		    System.err.println("      " + ctl.getFormat());
		System.err.println("      had now been identified as sent by: " + participant.getCNAME());
	     }
	}

	else if (evt instanceof ByeEvent) {

	     System.err.println("  - Got \"bye\" from: " + participant.getCNAME());
	     PlayerWindow pw = find(stream);
	     if (pw != null) {
		pw.close();
		playerWindows.removeElement(pw);
	     }
	}

    }


    /**
     * ControllerListener for the Players.
     */
    public synchronized void controllerUpdate(ControllerEvent ce) {

	Player p = (Player)ce.getSourceController();

	if (p == null)
	    return;

	// Get this when the internal players are realized.
	if (ce instanceof RealizeCompleteEvent) {
	    PlayerWindow pw = find(p);
	    if (pw == null) {
		// Some strange happened.
		System.err.println("Internal error!");
		System.exit(-1);
	    }
	    pw.initialize();
	    pw.setVisible(true);
	    p.start();
	}

	if (ce instanceof ControllerErrorEvent) {
	    p.removeControllerListener(this);
	    PlayerWindow pw = find(p);
	    if (pw != null) {
		pw.close();	
		playerWindows.removeElement(pw);
	    }
	    System.err.println("AVReceive2 internal error: " + ce);
	}

    }


    /**
     * A utility class to parse the session addresses.
     */
    class SessionLabel {

	public String addr = null;
	public int port;
	public int ttl = 1;

	SessionLabel(String session) throws IllegalArgumentException {

	    int off;
	    String portStr = null, ttlStr = null;

	    if (session != null && session.length() > 0) {
		while (session.length() > 1 && session.charAt(0) == '/')
		    session = session.substring(1);

		// Now see if there's a addr specified.
		off = session.indexOf('/');
		if (off == -1) {
		    if (!session.equals(""))
			addr = session;
		} else {
		    addr = session.substring(0, off);
		    session = session.substring(off + 1);
		    // Now see if there's a port specified
		    off = session.indexOf('/');
		    if (off == -1) {
			if (!session.equals(""))
			    portStr = session;
		    } else {
			portStr = session.substring(0, off);
			session = session.substring(off + 1);
			// Now see if there's a ttl specified
			off = session.indexOf('/');
			if (off == -1) {
			    if (!session.equals(""))
				ttlStr = session;
			} else {
			    ttlStr = session.substring(0, off);
			}
		    }
		}
	    }

	    if (addr == null)
		throw new IllegalArgumentException();

	    if (portStr != null) {
		try {
		    Integer integer = Integer.valueOf(portStr);
		    if (integer != null)
			port = integer.intValue();
		} catch (Throwable t) {
		    throw new IllegalArgumentException();
		}
	    } else
		throw new IllegalArgumentException();

	    if (ttlStr != null) {
		try {
		    Integer integer = Integer.valueOf(ttlStr);
		    if (integer != null)
			ttl = integer.intValue();
		} catch (Throwable t) {
		    throw new IllegalArgumentException();
		}
	    }
	}
    }


    /**
     * GUI classes for the Player.
     */
    class PlayerWindow extends Frame {

	Player player;
	ReceiveStream stream;

	PlayerWindow(Player p, ReceiveStream strm) {
	    player = p;
	    stream = strm;
	}

	public void initialize() {
	    add(new PlayerPanel(player));
	}

	public void close() {
	    player.close();
	    setVisible(false);
	    dispose();
	}

	public void addNotify() {
	    super.addNotify();
	    pack();
	}
    }


    /**
     * GUI classes for the Player.
     */
    class PlayerPanel extends Panel {

	Component vc, cc;

	PlayerPanel(Player p) {
	    setLayout(new BorderLayout());
	    if ((vc = p.getVisualComponent()) != null)
		add("Center", vc);
	    if ((cc = p.getControlPanelComponent()) != null)
		add("South", cc);
	}

	public Dimension getPreferredSize() {
	    int w = 0, h = 0;
	    if (vc != null) {
		Dimension size = vc.getPreferredSize();
		w = size.width;
		h = size.height;
	    }
	    if (cc != null) {
		Dimension size = cc.getPreferredSize();
		if (w == 0)
		    w = size.width;
		h += size.height;
	    }
	    if (w < 160)
		w = 160;
	    return new Dimension(w, h);
	}
    }


    public static void main(String argv[]) {
//	if (argv.length == 0)
//	    prUsage();

	//AVReceive2 avReceive = new LANReceiver(argv);
        String session = "224.144.251.104/22200";
	LANReceiver avReceive = new LANReceiver(session);
	if (!avReceive.initialize()) {
	    System.err.println("Failed to initialize the sessions.");
	    System.exit(-1);
	}

	// Check to see if LANReceiver is done.
	try {
	    while (!avReceive.isDone())
		Thread.sleep(1000);
	} catch (Exception e) {}

	System.err.println("Exiting AVReceive2");
    }


    static void prUsage() {
	System.err.println("Usage: AVReceive2 <session> <session> ...");
	System.err.println("     <session>: <address>/<port>/<ttl>");
	System.exit(0);
    }

}// end of LANReceiver 




