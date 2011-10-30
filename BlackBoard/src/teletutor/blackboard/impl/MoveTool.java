/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.AbstractButton;
import teletutor.blackboard.services.BlackBoard;
import teletutor.blackboard.services.BoardTool;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class MoveTool extends BoardTool {

    public MoveTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);
    }

    public void init(BlackBoard board) {
        mListener = new MoveMouseListener(board);
        toolButton = new ToolButton();
        toolButton.setText("Move");
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
    public void activate() {
        deselectOther();
        board.getBoardPanel().addMouseListener(mListener);
        board.getBoardPanel().addMouseMotionListener(mListener);
    }

    @Override
    public void deactivate() {
        board.getBoardPanel().removeMouseListener(mListener);
        board.getBoardPanel().removeMouseMotionListener(mListener);
    }

    @Override
    public void actionComplete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionAborted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void received(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void objectUpdated(UpdateInfo ui) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
