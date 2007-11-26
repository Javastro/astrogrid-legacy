/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.awt.Event;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang.SystemUtils;
import org.apache.hivemind.ApplicationRuntimeException;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/** abstract baseclass for a menubar for a UI componnet.
 * 
 * defines all the common menus
 * 
 * @future - add option to open HTML interface??
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 2, 20071:37:21 PM
 */
public abstract class UIComponentMenuBar extends JMenuBar {
    
    protected final JMenu fileMenu;
    protected final JMenu editMenu;
    protected final JMenu windowMenu;
    protected final JMenu helpMenu;
    protected final UIComponentImpl uiParent;
    protected final UIContext context;
    protected final ActionBridge bridge;
    public static final char ELLIPSIS = '\u2026';
    /** platform-dependent keymodifier to trigger a menu operation - ctrl on linux, command on apple */
    public static final int MENU_KEYMASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    /** keymodifier - shift + menuKeyMask */
    public static final int SHIFT_MENU_KEYMASK = KeyEvent.SHIFT_DOWN_MASK  | MENU_KEYMASK;

    public UIComponentMenuBar(UIComponentImpl parent) {
        this(parent,false);
    }

    public UIComponentMenuBar(UIComponentImpl parent, boolean minimalistic) {
        super();
        boolean hideSystemOperations = SystemUtils.IS_OS_MAC_OSX;
        this.bridge = new ActionBridge();
        this.uiParent = parent;
        this.context = uiParent.getContext();                
        FileMenuBuilder fmb = new FileMenuBuilder(hideSystemOperations);        
        populateFileMenu(fmb);
        fileMenu = fmb.create();
        if (! minimalistic) {
            EditMenuBuilder emb = new EditMenuBuilder(hideSystemOperations);
            populateEditMenu(emb);
            editMenu = emb.create();
        } else {
            editMenu = null;
        }
        windowMenu = uiParent.getContext().createWindowMenu();
        helpMenu = new HelpMenuBuilder(uiParent.applicationName, uiParent.helpKey,hideSystemOperations).create();

        add(fileMenu);
        if (! minimalistic) {
            add(editMenu);
            constructAdditionalMenus();
        }
        add(windowMenu);
        add(helpMenu);                
    }
    
    /** null implementation - ovrrride to populate the edit menu.
     * @param emb 
     * 
     */
    protected abstract void populateEditMenu(EditMenuBuilder emb) ;

    /** null implementation
     * override to populate the file menu
     * @param fmb 
     */
    protected  abstract void populateFileMenu(FileMenuBuilder fmb) ;


    /** subclasses to override this to create and add additional menus */
    protected void constructAdditionalMenus() {
    }
    
    
    /** builder for assembling a menu */
    public class MenuBuilder {
        protected final JMenu menu;
        /** construct a new menu builder
         * 
         * @param menuName name for the menu
         * @param i mnemonic to use
         */
        public MenuBuilder(String menuName,int i) {
            menu = new JMenu(menuName);
            menu.setMnemonic(i);
        }
        /** add an operation that is sourced from the entire application */
        public MenuBuilder applicationOperation(Action act) {
            JMenuItem add = menu.add(act);
            add.setIcon(null); // no icons in menu            
            return this;
        }
        /** add an operation that is sourced from the current window
         * doesn't seem to make any difference implementation-wise.
         * */
        public MenuBuilder windowOperation(Action act) {
            JMenuItem add = menu.add(act);
            add.setIcon(null); // no icons in menu             
            return this;
        }
        public MenuBuilder windowOperationWithIcon(Action act) {
            menu.add(act);          
            return this;
        }        
        /** add an operation that is dynamic - and is sourced from wherever focus lies */
        public MenuBuilder componentOperation(String title,String cmdName, KeyStroke accel) {
            JMenuItem add = new JMenuItem(title) {
                
                public void setAction(Action a) {
                    super.setAction(a);
                    // finally remove this actions as an action listener.
                    listenerList.remove(ActionListener.class, a);
                }

                  // only take enablement state from the action.
                protected void configurePropertiesFromAction(Action a) {
                    
                    setEnabled(a != null && a.isEnabled());
                }
            };
            add.setActionCommand(cmdName);
            if (accel != null) {
                add.setAccelerator(accel);
            }
            bridge.manage(add);
            menu.add(add);
            return this;
        }
        
        public MenuBuilder checkbox(JCheckBoxMenuItem i) {
            menu.add(i);
            return this;
        }
        public MenuBuilder radiobox(JRadioButtonMenuItem i) {
            menu.add(i);
            return this;
        }

        public MenuBuilder submenu(JMenu sub) {
            menu.add(sub);
            return this;
        }

        public MenuBuilder separator() {
            menu.addSeparator();
            return this;
        }

        /** access the underlying menu object */
        public JMenu getMenu() {
            return menu;
        }
        
        /** create the menu object - no further manipulations of the builder or 
         * menu object should be done after this 
         */
        public JMenu create() {
            return menu;
        }
        
        // additional methods - plastic send-to sub-menu?
    }
    
    /** extension with common operations that occur on file menu */
    public class FileMenuBuilder extends MenuBuilder {

        private final boolean isOsx;

        public FileMenuBuilder(boolean isOsx) {
            super("File",KeyEvent.VK_F);
            this.isOsx = isOsx;
        }
        
        public JMenu create() {
            separator();
            menu.add(new LoginMenuItem(context));
            
            menu.add(new LogoutMenuItem(context));
            if (!isOsx) {
                addSystemActions();
            }
            return menu;
        }
        
        public FileMenuBuilder closeWindow() {
            windowOperation(uiParent.new CloseAction());
            return this;
        }

        /**
         *  add login / logout / exit actions.
         */
        private void addSystemActions() {

            separator();
            JMenuItem exit = createExitMenuItem(context);
            menu.add(exit);
        }
       
    }
    
    /** extension with common operations that occur on Edit menu */
    public class EditMenuBuilder extends MenuBuilder {

        private final boolean isOsx;

        public EditMenuBuilder(boolean isOsx) {
            super("Edit",KeyEvent.VK_E);
            this.isOsx = isOsx;

        }
        
        public JMenu create() {
            separator();
            if (!isOsx) {
                addNonOSXActions();
            }
            addFinalActions();
            return menu;
        }
        public static final String CUT = "cut-to-clipboard";
        public static final String COPY = "copy-to-clipboard";
        public static final String PASTE = "paste-from-clipboard";
        public static final String SELECT_ALL = "select-all";
        public static final String CLEAR_SELECTION = "clear-selection";
        public static final String INVERT_SELECTION = "invert-selection";
        public static final String DELETE = "delete";

        public EditMenuBuilder cut() {
            componentOperation("Cut",CUT,KeyStroke.getKeyStroke(KeyEvent.VK_X,UIComponentMenuBar.MENU_KEYMASK));
            return this;
        }
        public EditMenuBuilder copy() {
            componentOperation("Copy",COPY,KeyStroke.getKeyStroke(KeyEvent.VK_C,UIComponentMenuBar.MENU_KEYMASK));            
            return this;
        }
        public EditMenuBuilder paste() {
            componentOperation("Paste",PASTE,KeyStroke.getKeyStroke(KeyEvent.VK_V,UIComponentMenuBar.MENU_KEYMASK));            
            return this;
        }

        public EditMenuBuilder delete() {
            componentOperation("Delete",DELETE,KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,MENU_KEYMASK));
            return this;
        }
        
        public EditMenuBuilder selectAll() {
            componentOperation("Select All",SELECT_ALL,KeyStroke.getKeyStroke(KeyEvent.VK_A,UIComponentMenuBar.MENU_KEYMASK));
            return this;
        }
        public EditMenuBuilder clearSelection() {
            componentOperation("Select None",CLEAR_SELECTION,KeyStroke.getKeyStroke(KeyEvent.VK_A,UIComponentMenuBar.SHIFT_MENU_KEYMASK));
            return this;
        }
        public EditMenuBuilder invertSelection() {
            componentOperation("Invert Selection",INVERT_SELECTION,null);
            return this;
       }        
        /**
         * if not on OSX, add a link to the confituration diallogue.
         */
        private void addNonOSXActions() {
            JMenuItem showPrefs = new JMenuItem("Preferences" + UIComponentMenuBar.ELLIPSIS);
            showPrefs.setActionCommand(UIContext.PREF);
            showPrefs.addActionListener(context);
            showPrefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,UIComponentMenuBar.MENU_KEYMASK));
            menu.add(showPrefs);
        } 
        private void addFinalActions() { // would like to put these on the OSx application menu, but doesn't seem to be possible in java.
            JMenuItem reset = new JMenuItem("Reset Configuration" + UIComponentMenuBar.ELLIPSIS);
            reset.setToolTipText("Reset all configuration back to factory defaults");
            reset.setActionCommand(UIContext.RESET);
            reset.addActionListener(context);
            menu.add(reset);
            
            JMenuItem clearCache = new JMenuItem("Clear Cache" + UIComponentMenuBar.ELLIPSIS);
            clearCache.setToolTipText("Remove all cached data - registry entires, queries, etc");
            clearCache.setActionCommand(UIContext.CLEAR_CACHE);
            clearCache.addActionListener(context);
            menu.add(clearCache);            
        }
       
    }
   
    /** build and configure a help menu */
    public class HelpMenuBuilder extends MenuBuilder {

        private final boolean isOsx;

        /**
         * @param applicationName 
         * @param menuName
         * @param i
         */
        public HelpMenuBuilder(String applicationName, final String helpKey, boolean isOsx) {
            super("Help",KeyEvent.VK_H);
            this.isOsx = isOsx;
            final JMenuItem appHelp = createApplicationHelpMenuItem(context,applicationName, helpKey);
            menu.add(appHelp);
            
            JMenuItem contents = createHelpContentsMenuItem(context);
            menu.add(contents);
        
            // recommended apps submenu
            JMenu apps = new JMenu("Other Applications");
            apps.add(new HyperlinkMenuItem("Aladin",IconHelper.loadIcon("aladin_48pix.png"),"http://aladin.u-strasbg.fr/"));
            apps.add(new HyperlinkMenuItem("Gaia","http://star-www.dur.ac.uk/~pdraper/gaia/gaia.html"));
            apps.add(new HyperlinkMenuItem("Octet",IconHelper.loadIcon("cvo.png"),"http://www2.cadc-ccda.hia-iha.nrc-cnrc.gc.ca/cvo/"));
            apps.add(new HyperlinkMenuItem("Splat",IconHelper.loadIcon("splat.gif"),"http://star-www.dur.ac.uk/~pdraper/splat/splat.html"));
            apps.add(new HyperlinkMenuItem("Topcat",IconHelper.loadIcon("tc3.gif"),"http://www.star.bris.ac.uk/~mbt/topcat/"));
            apps.add(new HyperlinkMenuItem("Visivo",IconHelper.loadIcon("visivo.png"),"http://visivo.cineca.it/"));
            apps.add(new HyperlinkMenuItem("VOSpec",IconHelper.loadIcon("vospecLogo.png"),"http://esavo.esa.int/vospecapp"));
            menu.add(apps);

            separator();
            JMenuItem report = new HyperlinkMenuItem("Report a Bug",
                    "http://www.astrogrid.org/bugzilla/enter_bug.cgi?product=Workbench%20and%20ACR"
            );
            menu.add(report);
        }
        
        public JMenu create() {
            if (!isOsx) {
                addSystemActions();
            }
            return menu;
        }

        /**
         * 
         */
        private void addSystemActions() {
            JMenuItem about = new JMenuItem("About VODesktop" + UIComponentMenuBar.ELLIPSIS
                    ,IconHelper.loadIcon("AGlogo16x16.png"));
           about.setActionCommand(UIContext.ABOUT);
           about.addActionListener(context);
            menu.add(about);
        }
        
    }    
   
    /** simple menu item that opens a url in a browser
     * url is provided as a string for programmatic convenience.
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 5, 20073:02:11 PM
     */
    public class HyperlinkMenuItem extends JMenuItem {
        private final URL url;
        public HyperlinkMenuItem(String text, Icon icon,String urlString) {
            super(text, icon);
            try {
                url = new URL(urlString);
            } catch (MalformedURLException x) {
                throw new ApplicationRuntimeException("Invalid url",x);
            }
        }

        public HyperlinkMenuItem(String text,String urlString) {
            super(text);
            try {
                url = new URL(urlString);
            } catch (MalformedURLException x) {
                throw new ApplicationRuntimeException("Invalid url",x);
            }
        }
        {
            addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    try {
                        uiParent.getContext().getBrowser().openURL(url);
                    } catch (ACRException x) {
                       uiParent.showTransientError("Unable to open browser",ExceptionFormatter.formatException(x));
                    }
                }                   
            });
        }
    }
    
    /**
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 5, 20075:03:29 PM
     */
    public static final class LogoutMenuItem extends JMenuItem {
        private final UIContext context;

        public LogoutMenuItem(UIContext context) {
            super("Logout", KeyEvent.VK_O);
            this.context = context;
            final ButtonModel m = context.getLoggedInModel();
            setEnabled(m.isEnabled());
            setToolTipText("Log out of the virtual observatory");
            setIcon(IconHelper.loadIcon("connect_no16.png"));   
            setActionCommand(UIContext.LOGOUT);
            addActionListener(context);
            m.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setEnabled(m.isEnabled());
                }
            });
        }
    }
    
    /**
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 5, 20075:06:05 PM
     */
    public static final class LoginMenuItem extends JMenuItem {
        private final UIContext context;


        public LoginMenuItem(UIContext context) {
            super("Login"+ UIComponentMenuBar.ELLIPSIS, KeyEvent.VK_L);
            this.context = context;
            final ButtonModel m = context.getLoggedInModel();
            setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,UIComponentMenuBar.MENU_KEYMASK));
            setToolTipText("Log in to the virtual observatory");
            setIcon(IconHelper.loadIcon("connect_established16.png"));
            setEnabled(!m.isEnabled());
            setActionCommand(UIContext.LOGIN);
            addActionListener(context);
            m.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    setEnabled(! m.isEnabled());
                }
            });                
        }
    }

    /** acts as a bridge between a menu operation and the currently-focussed 
     * operation 
     * 
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 7, 20073:46:58 PM
     */
    public class ActionBridge implements  PropertyChangeListener, ActionListener {
        private JComponent focusOwner = null;
        List managed = new ArrayList();
        private final KeyboardFocusManager manager;
        public ActionBridge() {
            manager = KeyboardFocusManager.
            getCurrentKeyboardFocusManager();
            manager.addPropertyChangeListener("permanentFocusOwner", this);
        }
        /** instruct the bridge to start maaging a menu item - i.e.
         * bridging it, and taking care of it's enabled state.
         * 
         * @param item - must have actionCommand set 
         */
        public void manage(JMenuItem item) {
            if (item.getActionCommand() == null) {
                throw new IllegalStateException("item does not have an action command set");
            }
            managed.add(item);
            item.addActionListener(this);
        }
        /** called by internals when the focus changes */
        public void propertyChange(PropertyChangeEvent e) {
            if (! SwingUtilities.isDescendingFrom(UIComponentMenuBar.this,manager.getActiveWindow())) {
               return ;// not in our window. 
            }
            Object o = e.getNewValue();
            if (o instanceof JComponent) {
                focusOwner = (JComponent)o;
                ActionMap actMap = focusOwner.getActionMap();
                //System.err.println(Arrays.asList(actMap.allKeys()));
                for(int i = 0; i < managed.size(); i++) {                    
                    JMenuItem it = (JMenuItem)managed.get(i);
                    Action nu = actMap.get(it.getActionCommand());
                    it.setAction(nu); // as we've overridden setAction, this just listens to enabled changes
                }
            } else {                
                focusOwner = null;
                for(int i = 0; i < managed.size(); i++) {
                    JMenuItem it = (JMenuItem)managed.get(i);
                    it.setAction(null);
                }                
                
            }
        }
        // bridge the event to the target component.
        public void actionPerformed(ActionEvent e) {
            if (focusOwner == null)
                return;
            String action = (String)e.getActionCommand();
            Action a = focusOwner.getActionMap().get(action);
            if (a != null) {
                a.actionPerformed(new ActionEvent(focusOwner,
                        ActionEvent.ACTION_PERFORMED,
                        null));
            }
        }
     
    }

    public final JMenu getFileMenu() {
        return this.fileMenu;
    }

    public final JMenu getEditMenu() {
        return this.editMenu;
    }

    public final JMenu getWindowMenu() {
        return this.windowMenu;
    }

    public final JMenu getHelpMenu() {
        return this.helpMenu;
    }

    /**
     * @return
     */
    public static JMenuItem createHelpContentsMenuItem(UIContext context) {
        JMenuItem contents = new JMenuItem("Help Contents");
        contents.setActionCommand(UIContext.HELP);
        contents.addActionListener(context);
        return contents;
    }

    /**
     * @param applicationName
     * @param helpKey
     * @return
     */
    public  static JMenuItem createApplicationHelpMenuItem(final UIContext context,String applicationName,
            final String helpKey) {
        final JMenuItem appHelp = new JMenuItem(applicationName + " Help");
        appHelp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                context.getHelpServer().showHelpForTarget(helpKey);                    
            }
        });
        appHelp.setAccelerator(KeyStroke.getKeyStroke(new Character('?'),UIComponentMenuBar.MENU_KEYMASK));
        return appHelp;
    }

    /**
     * @return
     */
    public static JMenuItem createExitMenuItem(UIContext context) {
        JMenuItem exit = new JMenuItem("Exit"+ UIComponentMenuBar.ELLIPSIS,KeyEvent.VK_E);
        exit.setToolTipText("Close all windows and exit VODesktop");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,UIComponentMenuBar.MENU_KEYMASK));
        exit.setActionCommand(UIContext.EXIT);
        exit.addActionListener(context);
        return exit;
    }
}
