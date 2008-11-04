/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

/** Titled border with some styling tweaks. use this class,
 * rather than construcitng your own, to ensure a consistent appearance.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 10, 20077:49:44 PM
 */
public class MyTitledBorder  {
    /**
     * 
     */
    private MyTitledBorder() {
    }
    /** create a titled border, based on a lined border */
    public static TitledBorder createLined(final String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                ,title
                ,TitledBorder.LEFT
                ,TitledBorder.BELOW_TOP
                ,UIConstants.SMALL_DIALOG_FONT
                ,Color.DARK_GRAY
                )     ;  
    }
    /** create a titled border, based on an empty border */
    public static TitledBorder createEmpty(final String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(1,1,1,1)
                ,title
                ,TitledBorder.LEFT
                ,TitledBorder.TOP
                ,UIConstants.SMALL_DIALOG_FONT
                ,Color.GRAY
                )     ;  
    }
    
    /** create a simple lined border */
    public static javax.swing.border.Border createUntitledLined() {
        return BorderFactory.createLineBorder(Color.LIGHT_GRAY);
    }
    
    
}
