package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

/** window that can be dragged around the screen.
 * copied from http://forum.java.sun.com/thread.jspa?threadID=406114&forumID=57
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 22, 20071:08:53 AM
 */
public class DraggableWindow extends JFrame implements MouseListener , MouseMotionListener {
    /**
     * 
     */
    public DraggableWindow() {
        addMouseListener(this);
        
        addMouseMotionListener(this);
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    // draggable window machinery
    private Point location;
    private MouseEvent pressed;
    public void mousePressed(MouseEvent me)
    {
        pressed = me;
    }

    public void mouseClicked(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
        
    }

    public void mouseDragged(MouseEvent me)
    {
        location = getLocation(location);
        int x = location.x - pressed.getX() + me.getX();
        int y = location.y - pressed.getY() + me.getY();
        setLocation(x, y);
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    public static void main(String args[])
    {
        DraggableWindow window = new DraggableWindow();
        window.getContentPane().add(new JLabel("hi"));
        window.setSize(300, 300);
        window.setLocationRelativeTo( null );
        window.setVisible(true);
    }

}