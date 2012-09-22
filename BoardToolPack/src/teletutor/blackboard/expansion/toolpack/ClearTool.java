/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.expansion.toolpack;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.blackboard.services.ToolButton;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class ClearTool extends BoardTool {

    ImageIcon icon = new ImageIcon(getClass().getResource("/clear.png"));

    public ClearTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);
    }

    public void init(Blackboard board) {
        toolButton = new ToolButton();
        toolButton.setSize(new Dimension(32, 32));
        toolButton.setIcon(icon);
        
        toolButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton eSource = (AbstractButton) e.getSource();
                boolean selected = eSource.getModel().isSelected();
                if (selected) {
                    activate();
                } else {
                    deactivate();
                }
            }
        });
    }

    @Override
    public void actionComplete(BoardObject bo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionAborted(BoardObject bo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createCursor() {
        return;
    }

    @Override
    public void received(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void objectUpdated(UpdateInfo ui) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void activate() {
       board.clear();
       toolButton.doClick();       
    }   
    
}
