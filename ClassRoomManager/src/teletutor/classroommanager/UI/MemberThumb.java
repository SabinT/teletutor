/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.classroommanager.UI;

import teletutor.classroommanager.services.MemberProxy;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import teletutor.classroommanager.services.MemberStateObserver;
import teletutor.core.services.SimpleMessage;
import teletutor.core.utilities.ImageButton;
import teletutor.core.utilities.ImagePanel;
import teletutor.core.utilities.ImageToggleButton;
import teletutor.core.utilities.JarImageButton;
import teletutor.core.utilities.JarImagePanel;
import teletutor.core.utilities.JarImageToggleButton;

/**
 *
 * @author Sabin Timalsena
 */
public class MemberThumb extends JarImagePanel implements MemberStateObserver {
    // TODO the controls and the methods to handle CURIOSITY aka the MemberProxy.STUDENT_CURIOUS state
    // TODO the picture in the thumbnail, if not available in database, use from local store

    private final String baseDir = "icons/memberThumb";
    MemberProxy mProxy;
    private JarImageToggleButton audioButton;
    private JLabel audioEnabledLabel;
    private JLabel pictureLabel;
    private JLabel nameLabel;
    private JarImageButton pmButton;
    private JLabel questionMark;
    //private javax.swing.JLabel stateText;

    /** Creates new form MemberThumbPanel */
    public MemberThumb(MemberProxy mProxy) {
        // setup the background
        super(MemberThumb.class, "icons/memberThumb", "back.jpg");

        initComponents();

        this.mProxy = mProxy;
        mProxy.addMemberStateObserver(this);

        // modify component values and wire them up with the component
        nameLabel.setText(mProxy.getUsername());
        // TODO get the image from the proxy
        // imagePanel.setImage etc

        // if this is not on the tutor node, or this proxy represents the 
        // tutor node, disable the audio button
        if (!mProxy.isOnTutorNode() || mProxy.isTutor()) {
            audioButton.setEnabled(false);
        }
        
        // if this proxy represents the local user, disable the PM Button
        if (mProxy.getLocalUser().equals(mProxy.getUsername())) {
            pmButton.setEnabled(false);
        }
    }

    void initComponents() {
        audioButton = new JarImageToggleButton(getClass(), baseDir, "audioButton", 102, 9);
        audioEnabledLabel = new JLabel(audioButton.getSelectedIcon());
        audioEnabledLabel.setBounds(audioButton.getBounds());

        // TODO use actual picture later, but initialization code won't change here
        //pictureLabel = new JLabel(new ImageIcon(getClass().getResource("/" + baseDir + "/default_user.jpg")));
        pictureLabel = new JLabel(new ImageIcon(getClass().getResource("/" + baseDir + "/default_user.jpg")));
        pictureLabel.setBounds(13, 13, 83, 95);
        // scaling and resizing of the image to make it fit into 83 x 95 pixels

        nameLabel = new JLabel();
        nameLabel.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        nameLabel.setText("Member Name");
        nameLabel.setBounds(11, 118, 129, 25);

        pmButton = new JarImageButton(getClass(), baseDir, "messageButton", 102, 63);

        questionMark = new JLabel(new ImageIcon(getClass().getResource("/" + baseDir + "/question.png")));
        questionMark.setBounds(0, 0, 50, 50);
        questionMark.setToolTipText("<html><h3>Hello there!</h3></html>");

        audioButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                audioButtonActionPerformed(evt);
            }
        });


        pmButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pmButtonActionPerformed(evt);
            }
        });

        questionMark.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    if (!audioButton.isSelected()) {
                        audioButton.doClick();
                    }
                    pmButton.doClick();
                } else {
                    // just show the message
                    questionMark.setToolTipText("Here again!!");
                }
            }
        });

        add(audioButton);
        add(pictureLabel);
        add(nameLabel);
        add(pmButton);
        add(audioEnabledLabel);
        add(questionMark, 0);
    }

    private void audioButtonActionPerformed(java.awt.event.ActionEvent evt) {
        /* send a message to the AudioManager of the member and tell it to
         * pause/resume transmission. At the same time, change the state of this
         * member to STUDENT_ACTIVE/STUDENT_PASSIVE (toggle behavior)
         */
        if (audioButton.isSelected()) {
            if (mProxy.getState() == MemberProxy.STUDENT_PASSIVE
                    || mProxy.getState() == MemberProxy.STUDENT_CURIOUS) {
                mProxy.setState(MemberProxy.STUDENT_ACTIVE);

                try {
                    // send a message to AudioManager
                    mProxy.sendObject(mProxy.getUsername(), "AudioManager", new SimpleMessage("resume"));
                } catch (Exception ex) {
                    Logger.getLogger(MemberThumbPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } // if button unselected, pause the audio
        else {
            if (mProxy.getState() == MemberProxy.STUDENT_ACTIVE) {
                mProxy.setState(MemberProxy.STUDENT_PASSIVE);
                try {
                    // send a message to AudioManager
                    mProxy.sendObject(mProxy.getUsername(), "AudioManager", new SimpleMessage("pause"));
                } catch (Exception ex) {
                    Logger.getLogger(MemberThumbPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void pmButtonActionPerformed(java.awt.event.ActionEvent evt) {
        mProxy.showPMFrame();
    }

    /**
     * This method will be called as soon as this Panel is registered as a 
     * member state observer
     * @param memberState 
     */
    @Override
    public void memberStateChanged(int memberState) {
        // TODO sounds
        switch (memberState) {
            case MemberProxy.STUDENT_ACTIVE:
                questionMark.setVisible(false);
                //audioEnabledLabel.setVisible(true);
                audioButton.setSelected(true);
                //stateText.setText("Active Student");
                break;
            case MemberProxy.STUDENT_PASSIVE:
                questionMark.setVisible(false);
                //audioEnabledLabel.setVisible(false);
                audioButton.setSelected(false);
                //stateText.setText("Passive Student");
                break;
            case MemberProxy.STUDENT_CURIOUS:
                questionMark.setVisible(true);
                audioEnabledLabel.setVisible(false);
                String note = "<html><h3>" + mProxy.getNote() + "</h3></html>";
                questionMark.setToolTipText(note);
                //audioButton.setVisible(false);
                //stateText.setText("Curious");
                break;
            case MemberProxy.TUTOR:
                questionMark.setVisible(false);
                audioButton.setVisible(false);
                audioEnabledLabel.setVisible(true);
                //stateText.setText("Tutor");
                break;
            case MemberProxy.SUSPECT:
                // TODO disable the controls on this event, re-enable later
                break;

        }
    }
}
