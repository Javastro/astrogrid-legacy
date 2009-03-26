/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

/** cribbed from http://software.jessies.org/salma-hayek/
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 1, 20075:53:16 PM
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A rectangular color swatch useful for giving a preview of a color.
 */
public final class ColorSwatchIcon extends DrawnIcon {
    private Color color;
    private Color borderColor;
    
    public ColorSwatchIcon(Color color, Dimension size) {
        super(size);
        setColor(color);
    }
    
    public void setColor(Color color) {
        this.color = color;
        this.borderColor = (color != null) ? color.darker() : null;
    }
    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (color == null) {
            return;
        }
        
        g.setColor(color);
        g.fillRect(x, y, getIconWidth(), getIconHeight());
        g.setColor(borderColor);
        g.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        JPanel p = new JPanel();
        f.getContentPane().add(p);
        ColorSwatchIcon ico = new ColorSwatchIcon(Color.RED, new Dimension(20,20));
        JLabel swatch = new JLabel(ico);
        p.add(swatch);
        f.setVisible(true);
    }
}
