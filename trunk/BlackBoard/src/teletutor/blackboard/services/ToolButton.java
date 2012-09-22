/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.services;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 *
 * @author Samyam
 */
public class ToolButton extends JToggleButton {

    BoardTool tool = null;

    public ToolButton() {
    }

    public ToolButton(BoardTool _tool, String text, ImageIcon icon) {
        this.tool = _tool;
        this.setText(text);
        
        // TODO icon

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractButton eSource = (AbstractButton) e.getSource();
                boolean selected = eSource.getModel().isSelected();
                if (selected) {
                    tool.activate();
                } else {
                    tool.deactivate();
                }
            }
        });
    }
}
