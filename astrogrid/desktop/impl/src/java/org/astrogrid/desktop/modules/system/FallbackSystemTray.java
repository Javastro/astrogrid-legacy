/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.PopupFactory;

import org.apache.commons.collections.Factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.DraggableWindow;
import org.astrogrid.desktop.modules.ui.comp.MyTitledBorder;
import org.astrogrid.desktop.modules.ui.comp.TimedPopup;

/** Fallback system tray when not running on Java 6, on on OS where systray is not supported.
 *  * @todo add login/ logout icon and actions..
 * @todo add window lists ?
 * @todo share throbbing model
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 21, 20076:24:07 PM
 */
public class FallbackSystemTray  implements SystemTrayInternal{

    protected static final Log logger = LogFactory.getLog(SystemTray.class);
    
    protected final UIContext context;
    protected final PopupFactory popups;
    private final ImageIcon defaultImage;
    private final Icon throbbingImage;
    private final JButton ico;
    private final DraggableWindow window;

    private JPopupMenu popup;
    
    public FallbackSystemTray( UIContext context) {
        super();
        this.context = context;
        this.popups = PopupFactory.getSharedInstance();
        this.defaultImage = IconHelper.loadIcon("ar16.png");
        this.throbbingImage = IconHelper.loadIcon("running16.png"); // @todo find a larger icon here.
        this.window = new DraggableWindow();
        this.window.setTitle("VO Desktop"); // although wndow title is not shown, this appears in the systray.
        this.window.setIconImage(defaultImage.getImage());
        this.ico = new JButton("Start",defaultImage);
        this.popup = createPopupMenu();
    }

    public void run() {
                ico.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Component c = (Component) e.getSource();
                        int px = c.getX();
                        int py = c.getY() + c.getHeight();        
                        if(!popup.isShowing()) {                 
                            popup.show( c, 0, py  );
                        } else { 
                            popup.setVisible(false);
                        }                  
                    }
                });
                // NB: can't set tooltip - as this seems to destroy the mouse draggable behaviour - how odd.
                //ico.setToolTipText("Astro Runtime");
                javax.swing.JPanel p = new JPanel();
                p.setBorder(MyTitledBorder.createEmpty("VO Desktop"));
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
            f.setActionCommand(key);
            f.addActionListener(context);
            m.add(f);
        }
        m.addSeparator();
        JMenuItem test = new JMenuItem("Self Tests");
        test.setActionCommand(UIContext.SELFTEST);
        test.addActionListener(context);
        m.add(test);
        
        m.addSeparator();
        m.add(new UIComponentMenuBar.LoginMenuItem(context));
        m.add(new UIComponentMenuBar.LogoutMenuItem(context));
        JMenuItem pref = new JMenuItem("Preferences" + UIComponentMenuBar.ELLIPSIS);
        pref.setActionCommand(UIContext.PREF);
        pref.addActionListener(context);
                
        m.add(pref);
        m.addSeparator();     
        
        JMenuItem h = new JMenuItem("VO Desktop Help");
        h.setActionCommand(UIContext.HELP);
        h.addActionListener(context);
                
        m.add(h);
 
        JMenuItem a = new JMenuItem("About VO Desktop");
        a.setActionCommand(UIContext.ABOUT);
        a.addActionListener(context);
        m.add(a);
        m.addSeparator();              
        
        JMenuItem sd = new JMenuItem("Exit");
        sd.setIcon(IconHelper.loadIcon("exit16.png"));
        sd.setActionCommand(UIContext.EXIT);
         sd.addActionListener(context);
        m.add(sd);
        return m;
    }
    
    public void displayErrorMessage(String arg0, String arg1) {
        TimedPopup.showErrorMessage(window,arg0,arg1);
    }

    public void displayInfoMessage(String arg0, String arg1) {
        TimedPopup.showInfoMessage(window,arg0,arg1);
    }

    public void displayWarningMessage(String arg0, String arg1) {
        TimedPopup.showWarningMessage(window,arg0,arg1);     
    }
    
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

}
