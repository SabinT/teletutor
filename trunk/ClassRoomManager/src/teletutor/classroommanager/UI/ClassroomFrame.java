/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClassroomFrame.java
 *
 * Created on Oct 19, 2011, 11:29:28 AM
 */
package teletutor.classroommanager.UI;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import teletutor.classroommanager.services.ClassroomManager;
import teletutor.core.services.CoreMessenger;
import teletutor.core.services.SimpleMessage;
import teletutor.core.utilities.FrameUtil;

/**
 *
 * @author Rae
 */
public class ClassroomFrame extends javax.swing.JFrame {

    ClassroomManager crman;

    /** Creates new form ClassroomFrame */
    public ClassroomFrame(final ClassroomManager crman) {
        initComponents();
        this.crman = crman;

        setTitle("ClassroomManager: " + crman.getChannel().getChannelName()
                + " on " + crman.getChannel().getGroupName());

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                tabbedPane.addTab("Class View", null, crman.getMemberListPanel(), "Shows the state of the classroom, along "
                        + "with all the members");
                tabbedPane.addTab("Discussion", null, new DiscussionPanel(crman), "Open discussion area where all members can talk to each other");
            }
        });

        FrameUtil.centerFrameOnScreen(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        closeSessionMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        jMenu1.setText("File");

        closeSessionMenuItem.setText("Close Session");
        closeSessionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeSessionMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(closeSessionMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenuItem);

        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeSessionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeSessionMenuItemActionPerformed
        if (crman != null) {
            try {
                // stop the channel
                crman.sendObject(crman.getChannel().getChannelName(), "CoreMessenger", new SimpleMessage(CoreMessenger.LEAVE_LECTURE));
            } catch (Exception ex) {
                Logger.getLogger(ClassroomFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_closeSessionMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        if (crman != null) {
            try {
                crman.sendObject(crman.getChannel().getChannelName(), "CoreMessenger", new SimpleMessage(CoreMessenger.EXIT));
            } catch (Exception ex) {
                Logger.getLogger(ClassroomFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_exitMenuItemActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem closeSessionMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
