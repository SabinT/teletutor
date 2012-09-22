/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import teletutor.blackboard.services.ToolButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.BoardTool;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class MoveTool extends BoardTool {

    MoveWheelListener wheelListener = new MoveWheelListener();
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem deleteCommand = new JMenuItem("Delete");
    JMenuItem mergeCommand = new JMenuItem("Merge Down");
    MoveKeyListener keyListener = new MoveKeyListener();

    public MoveTool(String string, TeleChannel tc) throws Exception {
        super(string, tc);
        icon = new ImageIcon(getClass().getResource("/move.png"));
        createCursor();
    }

    public void init(Blackboard _board) {
        this.board = _board;
        mListener = new MoveMouseListener(board);
        toolButton = new ToolButton();
        toolButton.setIcon(new ImageIcon(getClass().getResource("/move.png")));
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

        deleteCommand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                board.removeObject(board.getSelectedObject());
            }
        });

        mergeCommand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                board.mergeDown(board.getSelectedObject());
            }
        });
        popupMenu.add(deleteCommand);
        popupMenu.add(mergeCommand);
    }

    @Override
    public void received(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void objectUpdated(UpdateInfo ui) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void activate() {
        super.activate();
        board.getBoardPanel().addMouseWheelListener(wheelListener);
        board.getBoardPanel().addKeyListener(keyListener);
        board.getBoardPanel().requestFocusInWindow();
    }

    @Override
    public void deactivate() {
        super.deactivate();
        board.getBoardPanel().removeKeyListener(keyListener);
        board.getBoardPanel().removeMouseWheelListener(wheelListener);
        board.selectObject(null);
    }

    @Override
    public void actionComplete(BoardObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionAborted(BoardObject obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createCursor() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        cursor = kit.createCustomCursor(icon.getImage(), new Point(12, 4), "move");
    }

    class MoveWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int rot = e.getWheelRotation();
            float factor = 1 - rot * 0.05f;

            BoardObject obj = board.getSelectedObject();
            if (obj != null) {
                obj.scale(factor);
            }
        }
    }

    // ***************************** Key Listener ******************************
    class MoveKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                board.deleteObject(board.getSelectedObject());
            } else if (e.getKeyCode() == KeyEvent.VK_M) {
                board.mergeDown(board.getSelectedObject());
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                BoardObject obj = board.getSelectedObject();
                if (obj != null) {
                    obj.scale(1.1f);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_O) {
                BoardObject obj = board.getSelectedObject();
                if (obj != null) {
                    obj.scale(0.9f);
                }
            }
        }
    }
}
