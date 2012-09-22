/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.blackboard.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import teletutor.blackboard.services.Blackboard;
import teletutor.blackboard.services.BoardObject;
import teletutor.blackboard.services.ConstructionInfo;
import teletutor.core.services.TeleChannel;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Samyam
 */
public class TextObject extends BoardObject {

    private Image image = null;
    private TextFrame tFrame = null;
    private Font font = null;
    private String text = "";
    String fontName = "Arial";
    private Boolean wrap = Boolean.FALSE;
    private Integer fontSize;
    private Color color;

    public TextObject(String string, TeleChannel tc, Blackboard board) throws Exception {
        super(string, tc, board);
        width = 0;
        height = 0;
        x = 0;
        y = 0;
        fontSize = 16;
        color = Color.WHITE;
    }

    @Override
    public void init() {
        final TextObject tObj = this;
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                tFrame = new TextFrame(tObj);
            }
        });
    }

    @Override
    public UpdateInfo getConstructionInfo() {
        UpdateInfo params = new UpdateInfo();
        params.put("x", x);
        params.put("y", y);
        params.put("tx", tx);
        params.put("ty", ty);
        params.put("width", width);
        params.put("height", height);
        params.put("text", text);
        params.put("fontName", fontName);
        params.put("wrap", wrap);
        params.put("fontSize", fontSize);
        params.put("color", color);

        return params;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public JPanel getPropertiesPanel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void received(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void redraw() {
        font = new Font(fontName, Font.PLAIN, fontSize);
        Graphics2D g1 = (Graphics2D) board.getImage().getGraphics();
        FontRenderContext frc = g1.getFontRenderContext();

        String drawText = "Type your text...";

        if (!text.isEmpty()) {
            drawText = text;
        }
        if (!wrap) {
            TextLayout layout = new TextLayout(drawText, font, frc);
            Rectangle2D bounds = layout.getBounds();
            tw = width = (int) bounds.getWidth() + 5;
            th = height = (int) bounds.getHeight() + 5;
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) image.getGraphics();

            g2.setColor(color);
            layout.draw(g2, (float) bounds.getX(), 2 - (float) bounds.getY());

        } else {
            AttributedString str = new AttributedString(drawText);
            str.addAttribute(TextAttribute.FONT, font);
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) image.getGraphics();
            g2.setFont(font);
            g2.setColor(color);            
            AttributedCharacterIterator paragraph = str.getIterator();
            int paragraphStart = paragraph.getBeginIndex();
            int paragraphEnd = paragraph.getEndIndex();
            LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
            float breakWidth = image.getWidth(null);
            float posX = 0, posY = 0;
            lineMeasurer.setPosition(paragraphStart);

            while (lineMeasurer.getPosition() < paragraphEnd) {
                TextLayout layout = lineMeasurer.nextLayout(breakWidth);
                posY += layout.getAscent();
                layout.draw(g2, posX, posY);
                posY += layout.getDescent() + layout.getLeading();
            }
        }
        board.redraw();
    }

    public void showProperties() {
        final TextObject tObj = this;
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                tFrame.textArea.setText(tObj.getText());
                tFrame.setVisible(true);
            }
        });
    }

    public void hideProperties() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                tFrame.setVisible(false);
            }
        });
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        registerFieldChange("text", text);
    }

    public Boolean getWrap() {
        return wrap;
    }

    public void setWrap(Boolean wrap) {
        this.wrap = wrap;
        registerFieldChange("wrap", wrap);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
        registerFieldChange("font", font);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        registerFieldChange("fontSize", fontSize);
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
        registerFieldChange("fontName", fontName);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        registerFieldChange("color", color);
    }

    @Override
    public void dispose() {
        image.getGraphics().dispose();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                tFrame.setVisible(false);
                tFrame.dispose();
            }
        });
    }

    @Override
    public void validate() {
        // only an object that is not in the channel can be validated
        if (channel != null) {
            return;
        }

        UpdateInfo params = getConstructionInfo();
        String name = BoardObject.generateName();

        ConstructionInfo cinfo = new ConstructionInfo(name, params);
        try {
            board.sendObject(null, TextTool.class.getName(), cinfo);            
        } catch (Exception ex) {
            Logger.getLogger(TextTool.class.getName()).log(Level.SEVERE, null, ex);
        }

        board.removeObject(this);
        dispose();
    }

    @Override
    public void invalidate() {
        if (channel != null) {
            return;
        }

        if (board == null) {
            return;
        }
        board.removeObject(this);
        dispose();
    }

    @Override
    public boolean isResizable() {
        return false;
    }
}
