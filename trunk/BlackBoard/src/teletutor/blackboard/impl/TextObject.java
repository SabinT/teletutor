/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import teletutor.blackboard.services.BlackBoard;
import teletutor.blackboard.services.BoardObject;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class TextObject extends BoardObject {

    private BlackBoard board = null;
    private Image image = null;
    private String text = "";
    private TextFrame tFrame = null;
    private Font font = null;

    public TextObject(String string, TeleChannel tc) throws Exception {
        super(string, tc);
    }

    public void init(BlackBoard board) {
        this.board = board;        
        final TextObject tObj = this;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                tFrame = new TextFrame(tObj);                
            }
        });    
    }

    @Override
    public Image getImage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JPanel getPropertiesPanel() {
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

    public void redraw() {           
        
    }

    public void showProperties() {
        tFrame.setVisible(true);
    }
    
    public void hideProperties(){
        tFrame.setVisible(false);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }    
    
}
