/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BrushFrame.java
 *
 * Created on Nov 4, 2011, 7:42:36 AM
 */
package teletutor.blackboard.expansion.toolpack;

import teletutor.core.utilities.FrameUtil;

/**
 *
 * @author msi
 */
public class EraserFrame extends javax.swing.JFrame {

    BrushTool bTool = null;
       /** Creates new form BrushFrame */
    public EraserFrame(BrushTool bTool) {
        initComponents();
        this.bTool = bTool;      
        
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

        size1 = new javax.swing.JButton();
        size2 = new javax.swing.JButton();
        size3 = new javax.swing.JButton();
        size4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        size1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/size1.jpg"))); // NOI18N
        size1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size1ActionPerformed(evt);
            }
        });

        size2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/size2.jpg"))); // NOI18N
        size2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size2ActionPerformed(evt);
            }
        });

        size3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/size3.jpg"))); // NOI18N
        size3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size3ActionPerformed(evt);
            }
        });

        size4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/size4.jpg"))); // NOI18N
        size4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                size4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(size1)
                    .addComponent(size4)
                    .addComponent(size2)
                    .addComponent(size3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(size1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(size2)
                .addGap(12, 12, 12)
                .addComponent(size3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(size4)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void size3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size3ActionPerformed
        bTool.setBrushWidth(15);
    }//GEN-LAST:event_size3ActionPerformed

    private void size1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size1ActionPerformed
       bTool.setBrushWidth(2);
    }//GEN-LAST:event_size1ActionPerformed

    private void size2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size2ActionPerformed
        bTool.setBrushWidth(6);
    }//GEN-LAST:event_size2ActionPerformed

    
    private void size4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_size4ActionPerformed
       bTool.setBrushWidth(40);
    }//GEN-LAST:event_size4ActionPerformed

    /**
     * @param args the command line arguments
     */
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton size1;
    private javax.swing.JButton size2;
    private javax.swing.JButton size3;
    private javax.swing.JButton size4;
    // End of variables declaration//GEN-END:variables
}
