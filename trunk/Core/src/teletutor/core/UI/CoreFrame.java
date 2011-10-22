/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.osgi.framework.BundleContext;
import teletutor.core.Activator;
import teletutor.core.impl.TeleChannelImpl;
import teletutor.core.services.CoreMessenger;
import teletutor.core.services.TeleChannel;
import teletutor.core.utilities.FrameUtil;
import teletutor.core.utilities.LectureBean;
import teletutor.core.utilities.UserBean;

/**
 * This frame persists as long as the application is running, and is responsible
 * for creating and stopping sessions.
 * @author Sabin Timalsena
 */
public class CoreFrame extends JFrame {

    public static final String LOGIN_PANEL = "LoginPanel";
    public static final String START_PANEL = "StartPanel";
    private static final String CONFIG_FILE = "settings/UDP.xml";
    LoginPanel loginPanel;
    StartPanel_Test startPanel;
    CardLayout cl;
    JPanel cards;
    CoreMessenger messenger;
    TeleChannelImpl channel;
    UserBean user = new UserBean();
    LectureBean lecture;

    public CoreFrame() {
        //super();
        loginPanel = new LoginPanel(this);
        startPanel = new StartPanel_Test(this);

        cl = new CardLayout();
        cards = new JPanel(cl);
        cards.add(LOGIN_PANEL, loginPanel);
        cards.add(START_PANEL, startPanel);

        getContentPane().add(cards);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // exit();
            }
        });

        showLoginPanel();

        setResizable(false);

        setVisible(true);
    }

    public void showLoginPanel() {
        cl.show(cards, LOGIN_PANEL);
        loginPanel.resetControls();
        loginPanel.resetStatusText();
        setSize(loginPanel.getPreferredSize());
        setTitle("TeleTutor - Login");
        FrameUtil.centerFrameOnScreen(this);
    }

    public void showStartPanel() {
        cl.show(cards, START_PANEL);
        setSize(startPanel.getPreferredSize());
        setTitle("TeleTutor - Start Lecture");
        FrameUtil.centerFrameOnScreen(this);
        startPanel.refresh();
    }

    public void startChannel(LectureBean lecture) {
        this.lecture = lecture;
        startPanel.disableControls();
        startPanel.statusText.setText("Starting the Channel, please wait...");

        Thread th = new ChannelStartThread(this);
        th.start();

    }

    public void stopChannel() {
        if (channel != null) {
            Activator.serviceReg.unregister();
            channel.closeChannel();
            channel = null;
        }
        // show the login page again
        showLoginPanel();
        this.setVisible(true);
    }

    public void exit() {
        if (channel != null) {
            //Activator.serviceReg.unregister();
            channel.closeChannel();
            channel = null;
        }
        // TODO stop the framework bundle
        System.exit(0);
    }

    public TeleChannel getChannel() {
        return channel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                CoreFrame frame = new CoreFrame();
            }
        });

        //frame.setVisible(true);
    }

    class ChannelStartThread extends Thread {

        CoreFrame coreFrame;

        public ChannelStartThread(CoreFrame coreFrame) {
            this.coreFrame = coreFrame;
        }

        @Override
        public void run() {
            try {
                channel = new TeleChannelImpl(CONFIG_FILE, coreFrame.lecture, user.getUsername());
                messenger = new CoreMessenger("CoreMessenger", channel, coreFrame);
                // if successful, register the channel service
                if (Activator.bc != null) {
                    Activator.serviceReg =
                            Activator.bc.registerService(TeleChannel.class.getName(), (TeleChannel) channel, new Hashtable());
                }
                // then hide this frame
                coreFrame.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(CoreFrame.class.getName()).log(Level.SEVERE, null, ex);
                coreFrame.startPanel.statusText.setText("Error: " + ex.getMessage().toString());
            }
        }
    }
}
