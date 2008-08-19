/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.apache.commons.lang.WordUtils;

/**
 * Convenience class that displays a popup window for a fixed period of time, after which it
 * is hidden.
 * popups over the same component stack vertically.
 * 
 * @todo detect when popups are going off the screen.
 *  - or add ability to control stacking position.
 *  
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 21, 200710:42:20 AM
 */
public class TimedPopup {
    
    /**internal class that handles hiding the popup after a set amount of time, or when clicked. 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Sep 21, 200712:39:01 PM
     */
    private static final class PopupDismisser extends MouseAdapter implements ActionListener  {
        /**
         * 
         */
        private final Popup popup;
        /**
         * 
         */
        private final Component message;
        /**
         * 
         */
        private final Component owner;

        /**
         * @param popup
         * @param message
         * @param owner
         */
        private PopupDismisser(final Popup popup, final Component message, final Component owner) {
            this.popup = popup;
            this.message = message;
            this.owner = owner;
            this.message.addMouseListener(this);
        }

        public void actionPerformed(final ActionEvent e) {
            final Object o = popupsForComponent.get(this.owner);
            if (o != null) {
                if (o == this.message) {
                    // single popup - remove the binding altogether.
                    popupsForComponent.remove(this.owner);
                } else if (o instanceof List) {
                    final List l = (List)o;
                    // set this list slot to null - indicates it can be reused.
                    final int ix = l.indexOf(this.message);
                    if (ix != -1) {
                        l.set(ix,null);
                    }
                    //future - could do with a way to collapse the list again, when the tail is all nulled.
                }
            }
            this.popup.hide();
            
        }
        @Override
        public void mouseClicked(final MouseEvent e) {
            actionPerformed(null);
        }
    }
    /**
     * display a transient error message
     * @param owner the component to display in front of.
     * @param title the title of the error
     * @param message the content of the error
     */
    public static void showErrorMessage(final Component owner,final String title, final String message) {
        final JLabel l = new JLabel("<html><b>" + title + "</b><br>" + WordUtils.wrap(message,100,"<br>",false));
        l.setIcon(UIManager.getIcon("OptionPane.errorIcon")); // wish these magic keys were documented somewhere.
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        showMessage(owner,l,UIConstants.POPUP_DISMISS_TIME*2);
    }
    /**
     * display a transient information message
     * @param owner the component to display in front of.
     * @param title the title of the message
     * @param message the content of the message
     */
    public static void showInfoMessage(final Component owner,final String title, final String message) {
        final JLabel l = new JLabel("<html><b>" + title + "</b><br>" + message);
        l.setIcon(UIManager.getIcon("OptionPane.informationIcon")); // wish these magic keys were documented somewhere.
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        showMessage(owner,l);        
    }

    /**
     * display a transient warning message
     * @param owner the component to display in front of.
     * @param title the title of the error
     * @param message the content of the error
     */
    public static void showWarningMessage(final Component owner,final String title, final String message) {
        final JLabel l = new JLabel("<html><b>" + title + "</b><br>" + message);
        l.setIcon(UIManager.getIcon("OptionPane.warningIcon")); // wish these magic keys were documented somewhere.
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        showMessage(owner,l,UIConstants.POPUP_DISMISS_TIME * 2);        
    }
    
    
    
    /**
     * show a transient popup message
     * @param owner the component to display the popup above of
     * @param msg the text of the popup message
     */
    public static void showMessage(final Component owner,final String msg) {
        showMessage(owner,msg,UIConstants.POPUP_DISMISS_TIME);
    }

    /** show a transient popup message for a specified amount of time 
     * 
     * @param owner the component to display the popup above of
     * @param msg the text of the popup message
     * @param period milliseconds to display the mesage for
     */
    public static void showMessage(final Component owner, final String msg, final int period) {
        final JLabel label = new JLabel(msg);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        showMessage(owner,label,period);
    }

    /** show a transient popup message for a specified amount of time 
     * 
     * @param owner the component to display the popup above of
     * @param message the component to display as the content of the popup
     */
    public static void showMessage(final java.awt.Component owner,final Component message) {
        showMessage(owner,message,UIConstants.POPUP_DISMISS_TIME);
    }

    /** show a transient popup message for a specified amount of time 
     * 
     * @param owner the component to display the popup above of
     * @param message the component to display as the content of the popup
     * @param period milliseconds to display the mesage for
     */
    public static void showMessage(final Component owner, final Component message, final int period) {
        if (! owner.isShowing()) {
            return; // owning window isn't visible, so don't show the popup.
        }
        final Object o = popupsForComponent.get(owner);
      //  int xoffset = 0;
        int yoffset = 0;
        final Dimension d = new Dimension();
        if (o == null) {
            popupsForComponent.put(owner,message);
        } else { // already got some popups.
            if (o instanceof Component) {
                ((Component)o).getSize(d);
             //   xoffset = d.width + 1;
                yoffset = d.height + 1;
                //store new popup too.
                final List l = new ArrayList();
                l.add(o);
                l.add(message);
                popupsForComponent.put(owner,l);
            } else if (o instanceof List) {
                final List l = (List)o;
                boolean foundSlot = false;
                for (int i = 0; i < l.size(); i++) {
                    final Object e = l.get(i);
                    if (e == null) {
                        // found a vacant slot - stop here.
                        l.set(i,message);
                        foundSlot = true;
                        break;
                    }
                    ((Component)e).getSize(d);
                //    xoffset += d.width + 1;
                    yoffset += d.height + 1;                    
                }
                if (!foundSlot) { // need to extend the list then
                    l.add(message);
                }
            }
        }
      
        final Point loc = owner.getLocationOnScreen(); // top left corner of owner
        
        //want to place bottom left corner of message in center of owner, and then take stacking into account 
        final int x =loc.x + (owner.getWidth() / 2)  ; //xoffset not used
        final int y = loc.y + (owner.getHeight() / 2) - message.getPreferredSize().height - yoffset;
        
        
        final javax.swing.Popup popup = popupFac.getPopup(owner,message,x,y);
        // note - could call with owner=null here to force use of heavyweight popup - might fix platform-differences, if any are seen.

        // setup a new timer to clean up - remove the popup, and it's entry in the map.
        final ActionListener handler = new PopupDismisser(popup, message, owner);
        final Timer timer = new Timer(period, handler);
        timer.setRepeats(false);
        timer.start();
       popup.show();
    }
    
    private static final PopupFactory popupFac = PopupFactory.getSharedInstance();
    /** a map of the popups being displayed for each component -used to implement 'stacking'
     * key - the component popups are being displayed upon
     * value - a single message component, or list of message components, of the popups currenlty being shown
     *  */
    private static final Map popupsForComponent = new WeakHashMap();
    // main method for testing.
    public static void main(final String[] args) {
       final javax.swing.JComponent l = new JTree();
       final JPanel p = new javax.swing.JPanel(new BorderLayout());
       p.add(l,BorderLayout.CENTER);
       final JButton b = new JButton("new popup");
       b.addActionListener(new ActionListener() {

        public void actionPerformed(final ActionEvent e) {
            showMessage(l,"hi there");
        }
       });
       p.add(b,BorderLayout.NORTH);
        final JFrame f = new JFrame();
        f.getContentPane().add(p);
        f.setSize(200,200);
        f.show();
        
    }
}
