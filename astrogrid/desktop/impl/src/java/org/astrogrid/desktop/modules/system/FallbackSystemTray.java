/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.PopupFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.DraggableWindow;
import org.astrogrid.desktop.modules.ui.comp.TimedPopup;

/** Fallback UI system tray, used when not running on Java 6, or on OS where systray is not supported.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 21, 20076:24:07 PM
 */
public class FallbackSystemTray  implements SystemTrayInternal{

    protected static final Log logger = LogFactory.getLog(SystemTray.class);
    
    protected final UIContext context;
    private final PopupFactory popups;
    private final ImageIcon defaultImage;
    private final Icon throbbingImage;
    private final JButton ico;
    private final DraggableWindow window;

    private final JPopupMenu popup;
    
    public FallbackSystemTray( final UIContext context) {
        super();
        this.context = context;
        this.popups = PopupFactory.getSharedInstance();
        this.defaultImage = IconHelper.loadIcon("ar16.png");
        this.throbbingImage = IconHelper.loadIcon("running16.png"); // @todo find a larger icon here.
        this.window = new DraggableWindow();
        this.window.setTitle("VO Desktop"); // although wndow title is not shown, this appears in the systray.
        this.window.setIconImage(defaultImage.getImage());
        this.ico = new JButton("VO Desktop",defaultImage);
        this.popup = createPopupMenu();
    }

    public void run() {
                ico.addActionListener(new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        if(!popup.isShowing()) {                 
                            final Component c = (Component) e.getSource();
                            popup.show( c, 0, c.getHeight() );
                        } else { 
                            popup.setVisible(false);
                        }                  
                    }
                });
                // NB: can't set tooltip - as this seems to destroy the mouse draggable behaviour - how odd.
                //ico.setToolTipText("Astro Runtime");
                final javax.swing.JPanel p = new JPanel();
             //   p.setBorder(MyTitledBorder.createEmpty("VO Desktop"));
                p.add(ico);
                window.getContentPane().add(p);

                window.pack();
                window.setVisible(true);

    }
    
    private  JPopupMenu createPopupMenu() {
        final JPopupMenu m = new JPopupMenu();

        //window factories.
        for (final String key : context.getWindowFactories().keySet()) {
            final JMenuItem f = new JMenuItem("New " + key);            
            f.setActionCommand(key);
            f.addActionListener(context);
            m.add(f);
        }
        m.addSeparator();
        final JMenuItem pref = new JMenuItem("VO Desktop and Astro Runtime Preferences" + UIComponentMenuBar.ELLIPSIS);
        pref.setActionCommand(UIContext.PREF);
        pref.addActionListener(context);                
        m.add(pref);
        
        final JMenuItem test = new JMenuItem("Run Self Tests");
        test.setActionCommand(UIContext.SELFTEST);
        test.addActionListener(context);
        m.add(test);
        
        final JMenuItem processes = new JMenuItem("Show Background Processes");
        processes.setActionCommand(UIContext.PROCESSES);
        processes.addActionListener(context);
        m.add(processes);
        
        m.addSeparator();
        m.add(new UIComponentMenuBar.LoginMenuItem(context));
        m.add(new UIComponentMenuBar.LogoutMenuItem(context));

        m.addSeparator();     
        
        final JMenuItem h = new JMenuItem("VO Desktop Help");
        h.setActionCommand(UIContext.HELP);
        h.addActionListener(context);
                
        m.add(h);
 
        final JMenuItem a = new JMenuItem("About VO Desktop");
        a.setActionCommand(UIContext.ABOUT);
        a.addActionListener(context);
        m.add(a);
        m.addSeparator();              
        
        final JMenuItem sd = new JMenuItem("Exit VO Desktop");
        sd.setIcon(IconHelper.loadIcon("exit16.png"));
        sd.setActionCommand(UIContext.EXIT);
         sd.addActionListener(context);
        m.add(sd);
        return m;
    }
    
    public void displayErrorMessage(final String arg0, final String arg1) {
        TimedPopup.showErrorMessage(window,arg0,arg1);
    }

    public void displayInfoMessage(final String arg0, final String arg1) {
        TimedPopup.showInfoMessage(window,arg0,arg1);
    }

    public void displayWarningMessage(final String arg0, final String arg1) {
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
