package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.astrogrid.desktop.icons.IconHelper;

import com.l2fprod.common.swing.icons.EmptyIcon;


/** a button that appears to be a label, and can be 'pinned' */
public class PinnableLabel extends JToggleButton {

    private static final Font fnt = new JLabel().getFont();
    
    public PinnableLabel(String text) {
        super(text,UIConstants.PIN_ICON);
        setFont(fnt);
        setBorder(null);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setMargin(new Insets(0,0,0,0));
        setDisabledIcon(new EmptyIcon());
        setSelectedIcon(UIConstants.PIN_ICON); 
        //setRolloverIcon(UIConstants.PIN_ICON);
        setHorizontalAlignment(SwingConstants.LEFT);
        setHorizontalTextPosition(SwingConstants.LEFT);
        setFocusable(false);
    }

    
    // if a param is disabled, make sure it's not still selectd.
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        if (! b && isSelected() ) {
            setSelected(false);
        }
    }
    
    
    public static void main(String[] args) {
        javax.swing.JPanel p = new javax.swing.JPanel();
        PinnableLabel l = new PinnableLabel("hi there");
        p.add(l);
        PinnableLabel l1 = new PinnableLabel("ho");
        l1.setEnabled(false);
        p.add(l1);
        PinnableLabel l2 =new PinnableLabel("foo");
        l2.setIcon(IconHelper.loadIcon("edit16.png"));
        p.add(l2);
        JFrame f = new javax.swing.JFrame();
        f.getContentPane().add(p);
        f.pack();
        f.show();
        
    } 
   
    
}