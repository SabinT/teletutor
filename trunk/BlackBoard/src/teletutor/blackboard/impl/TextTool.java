/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.Point;
import java.awt.Toolkit;
import teletutor.blackboard.services.ToolButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.blackboard.services.ConstructionInfo;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class TextTool extends BoardTool {      
     
    public TextTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);
        icon = new ImageIcon(getClass().getResource("/text.png"));
        createCursor();
    }

    public void init(Blackboard board) {
        mListener = new TextMouseListener(board);
        toolButton = new ToolButton();
        toolButton.setIcon(new ImageIcon(getClass().getResource("/text.png")));
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
    public void received(Object obj) {
        if (obj instanceof ConstructionInfo) {
            ConstructionInfo cinfo = (ConstructionInfo) obj;
            try {
                TextObject tObj =  new TextObject(cinfo.getName(), channel, board);
                tObj.init(cinfo.getParams());
                board.addObject(tObj);
            } catch (Exception ex) {
                Logger.getLogger(TextTool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void objectUpdated(UpdateInfo ui) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionComplete(BoardObject obj) {

    }

    @Override
    public void actionAborted(BoardObject obj) {

    }
    
    @Override
    public void createCursor() {
       Toolkit kit = Toolkit.getDefaultToolkit();
       cursor = kit.createCustomCursor(icon.getImage(), new Point(25, 2), "text");
    }
}
