/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion.toolpack;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.ToolButton;
import teletutor.core.services.TeleChannel;

/**
 *
 * @author Samyam
 */
public class EraserTool extends BrushTool{  
    
    EraserFrame eraserFrame = null;
    
     public EraserTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);      
        
    }

    @Override
   public void init(Blackboard board) {
        mListener = new BrushMouseListener(board);
        toolButton = new ToolButton();
        toolButton.setIcon(new ImageIcon(getClass().getResource("/eraser.png")));
        brushSize = 2;
        color = Blackboard.BGCOLOR;
        final EraserTool eTool = this;

        toolButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton eSource = (AbstractButton) e.getSource();
                boolean selected = eSource.getModel().isSelected();
                if (selected) {
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            eraserFrame = new EraserFrame(eTool);
                            eraserFrame.setVisible(true);
                        }
                    });
                    activate();
                } else {
                    deactivate();
                    java.awt.EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            eraserFrame.setVisible(false);
                        }
                    });
                }
            }
        });
    }

    
}
