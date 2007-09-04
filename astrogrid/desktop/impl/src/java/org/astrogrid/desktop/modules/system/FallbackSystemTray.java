/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections.Factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.DraggableWindow;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;

import EDU.oswego.cs.dl.util.concurrent.SynchronizedInt;

/** Fallback system tray when not running on Java 6, on on OS where systray is not supported.
 *  * @todo add login/ logout icon and actions..
 * @todo add window lists ?
 * @todo share throbbing model
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 21, 20076:24:07 PM
 */
public class FallbackSystemTray  implements SystemTrayInternal, ActionListener {

    protected static final Log logger = LogFactory.getLog(SystemTray.class);
    
    /**
     */
    protected static final String EXIT = "exit";
    protected static final String PREF = "pref";  
    protected static final String ABOUT = "about";
    protected static final String HELP = "HELP";
    
    protected final org.astrogrid.acr.builtin.Shutdown shutdown;
    protected final UIContext context;
    protected final Runnable configDialogue;
    protected final PopupFactory popups;
    private final Icon defaultImage;
    private final Icon throbbingImage;
    private final JButton ico;
    private final PopupDraggableWindow window;
    
    public FallbackSystemTray( UIContext context, Shutdown shutdown,
            Runnable configDialogue) {
        super();
        this.shutdown = shutdown;
        this.context = context;
        this.configDialogue = configDialogue;
        this.popups = PopupFactory.getSharedInstance();
        this.defaultImage = IconHelper.loadIcon("ivoa.gif");
        this.throbbingImage = IconHelper.loadIcon("running16.png"); // @todo find a larger icon here.
        this.window = new PopupDraggableWindow(createPopupMenu());
        this.ico = new JButton(defaultImage);
        
    }

    public void run() {

                timer = new Timer(5000,FallbackSystemTray.this);
                timer.setRepeats(false);

                currentPopups = new ArrayList();

                ico.addActionListener(window);
                // NB: can't set tooltip - as this seems to destroy the mouse draggable behaviour - how odd.
                //ico.setToolTipText("Astro Runtime");
                javax.swing.JPanel p = new JPanel();
                p.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(1,1,1,1)
                        ,"Astro Runtime"
                        ,TitledBorder.LEFT
                        ,TitledBorder.TOP
                        ,UIConstants.SMALL_DIALOG_FONT
                        ,Color.GRAY));
                p.add(ico);
                window.getContentPane().add(p);

                window.pack();
                window.setVisible(true);

    }
    
    //@todo add in icons to this.
    private  JPopupMenu createPopupMenu() {
        JPopupMenu m = new JPopupMenu();

        //window factories.
        for (Iterator facs = context.getWindowFactories().keySet().iterator(); facs.hasNext(); ) {
            final String key = (String) facs.next();
            JMenuItem f = new JMenuItem("New " + key);
            f.setActionCommand(key.toString());
            f.addActionListener(this);
            m.add(f);
        }
        m.addSeparator();
        JMenuItem pref = new JMenuItem("Preferences..");
        pref.setActionCommand(PREF);
        pref.addActionListener(this);
        m.add(pref);
        m.addSeparator();

        
        JMenuItem h = new JMenuItem("Help contents");
        h.setActionCommand(HELP);
        h.addActionListener(this);
        m.add(h);
        m.addSeparator();              
        /* @todo implement UIContextImpl.getAbout first 
        JMenuItem a = new JMenuItem("About AstroRuntime..");
        a.setActionCommand(ABOUT);
        a.addActionListener(this);
        m.add(a);
      */
        
        JMenuItem sd = new JMenuItem("Exit");
        sd.setIcon(IconHelper.loadIcon("exit16.png"));
        sd.setActionCommand(EXIT);
        sd.addActionListener(this);
        m.add(sd);
        return m;
    }
    

    // callbacks from menu items.
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == timer) { // it's a timer call.
            for (Iterator i = currentPopups.iterator(); i.hasNext();) {
                Popup p = (Popup) i.next();
                p.hide();
                i.remove();
            }           
        } else {
            final String cmd = arg0.getActionCommand();
            if (cmd.equals(EXIT)) {
                shutdown.halt();
            } else if (cmd.equals(PREF)) {
                configDialogue.run();
            } else if (cmd.equals(HELP)) {
                context.getHelpServer().showHelp();
            } else if (cmd.equals(ABOUT)) {
                context.showAboutDialog();
            } else {
                // assume it's the name of a new window facotry.
                Factory fac = (Factory)context.getWindowFactories().get(cmd);
                if (fac != null) {
                    fac.create();
                }
            }
        }
    }

    public void displayErrorMessage(String arg0, String arg1) {
        JLabel l = new JLabel("<html><b>" + arg0 + "</b><br>" + arg1);
        l.setIcon(UIManager.getIcon("OptionPane.errorIcon")); // wish these magic keys were documented somewhere.
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        showMessage(l);
    }

    public void displayInfoMessage(String arg0, String arg1) {
        JLabel l = new JLabel("<html><b>" + arg0 + "</b><br>" + arg1);
        l.setIcon(UIManager.getIcon("OptionPane.informationIcon")); // wish these magic keys were documented somewhere.
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        showMessage(l);        
    }

    public void displayWarningMessage(String arg0, String arg1) {
        JLabel l = new JLabel("<html><b>" + arg0 + "</b><br>" + arg1);
        l.setIcon(UIManager.getIcon("OptionPane.warningIcon")); // wish these magic keys were documented somewhere.
        l.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        showMessage(l);        
    }
    
    private void showMessage(JLabel l) {
        //@todo show the popup message offset from main window, and allow messages to stack
        javax.swing.Popup p = popups.getPopup(window,l,window.getX(),window.getY());
        currentPopups.add(p);
        window.toFront();
        window.requestFocus(); // hope this isn;t too intrusive.
        timer.restart(); // instruct timer to start, triggering p.hide() in 5s time.
        p.show();
    }
    
    private List currentPopups;
    private Timer timer;


    // no need to synchronize, as hivemind ensures this is only ever called from EDT.
  protected int throbberCallCount = 0;
    
    public void startThrobbing() {
        if (++throbberCallCount > 0) {    
                    ico.setIcon(throbbingImage);
        }
    }

    public void stopThrobbing() {
        if (! (--throbberCallCount> 0)) {
                    ico.setIcon(defaultImage);
        }        
    }
    
    /** draggable window with a popup menu */
    private static class PopupDraggableWindow extends DraggableWindow implements ActionListener{

        private final  JPopupMenu menu;

        /**
         * @param createPopupMenu
         */
        public PopupDraggableWindow(JPopupMenu createPopupMenu) {
            super();
            this.menu = createPopupMenu;
        }
        public void mousePressed(MouseEvent me) {
            super.mousePressed(me);
            checkForTriggerEvent(me);
        }
        
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            checkForTriggerEvent(e);
        }
        
     // determine whether event should trigger popup menu
        private void checkForTriggerEvent( MouseEvent event ){
           if ( event.isPopupTrigger() ) { //|| SwingUtilities.isRightMouseButton(event) ) {
               showMenu();
                 //event.getX(), event.getY() );
           }
        }
        
        public void showMenu() {
            menu.show(this,15,15);
        }
        public void actionPerformed(ActionEvent e) {
            showMenu();
        }
    }

}
