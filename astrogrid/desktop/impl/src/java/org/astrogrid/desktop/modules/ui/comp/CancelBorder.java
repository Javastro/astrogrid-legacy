package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.text.JTextComponent;

/**
 * Wrap a text field to draw a cancel button as a gray circle with a white cross inside.
 * 
 * install on a text field by subclassing to implement the buttonActivated 
 * method, and then call .attachTo(textField);
 * 
 */
public abstract class CancelBorder extends InteractiveBorder {
    public CancelBorder() {
        super(15, false);
        setActivateOnPress(false);
    }
    
    public void paintBorder(final Component c, final Graphics oldGraphics, final int x, final int y, final int width, final int height) {
        if (c instanceof SearchField) {
            if (((SearchField) c).showingPlaceholderText ) {
                return;
            }
        }
        if (c instanceof JTextComponent && ((JTextComponent)c).getText().length() ==0) {
            return;
        }
        
        final Graphics2D g = (Graphics2D) oldGraphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        final int circleL = 14;
        final int circleX = x + width - circleL;
        final int circleY = y + (height - 1 - circleL)/2;
        g.setColor(isArmed() ? Color.GRAY : SearchField.GRAY);
        g.fillOval(circleX, circleY, circleL, circleL);
        
        final int lineL = circleL - 8;
        final int lineX = circleX + 4;
        final int lineY = circleY + 4;
        g.setColor(Color.WHITE);
        g.drawLine(lineX, lineY, lineX + lineL, lineY + lineL);
        g.drawLine(lineX, lineY + lineL, lineX + lineL, lineY);
    }
    
    /**
     * Handles a click on the cancel button by clearing the text and
     * notifying any ActionListeners.
     */
    public abstract void buttonActivated(MouseEvent e) ;
}