/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

/** cribbed from http://software.jessies.org/salma-hayek/
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 1, 20075:52:07 PM
 */

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * An 'icon' drawn by code, rather than from a bitmap.
 */
public class DrawnIcon implements Icon {
    private Dimension size;
    
    public DrawnIcon(Dimension size) {
        this.size = size;
    }
    
    public int getIconWidth() {
        return size.width;
    }
    
    public int getIconHeight() {
        return size.height;
    }
    
    /**
     * Override this to do your custom drawing.
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
    }
}

