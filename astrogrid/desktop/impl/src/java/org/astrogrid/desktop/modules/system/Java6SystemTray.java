/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.collections.Factory;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** System tray implementation for Java6 libraries.
 * reuses much of the machinery of the fallback system tray.
 * 
 * 
 * added lots of code armour - the first time that a java6 call fails, behaviour reverts 
 * to the fallback system tray.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 20, 200710:30:07 AM
 */
public class Java6SystemTray extends FallbackSystemTray implements SystemTrayInternal, ActionListener {

	private final Preference appToLaunch;

    public Java6SystemTray(final UIContext context, final Preference appToLaunch) throws Exception {
	    super(context);
	    // so got all the fallback machinery all ready. see if java6 will behave..
        this.appToLaunch = appToLaunch;
	    
        final Class systrayClz = Class.forName("java.awt.SystemTray");
        final Object b =ReflectionHelper.callStatic(systrayClz,"isSupported");
        if (((Boolean)b).booleanValue() == false) { // not supported
            logger.info("System tray is not supported on this platform. Falling back");
            throw new Exception("System tray not supported");
        }	    
	    
        systemTray = ReflectionHelper.callStatic(systrayClz,"getSystemTray");
        final Class trayIconClz = Class.forName("java.awt.TrayIcon");
        final Constructor trayConstructor = trayIconClz.getConstructor(new Class[]{Image.class,String.class,PopupMenu.class});
        // looks good - initialize the rest of our stuff...
        ImageIcon ic = IconHelper.loadIcon("ar16.png");//"AGlogo16x16.png");
        defaultImage = ic.getImage();
        ic = IconHelper.loadIcon("running16.png");
        throbbingImage = ic.getImage();
        
        final String tooltip = "VO Desktop";
        final PopupMenu menu = createPopupMenu();
        trayIcon = trayConstructor.newInstance(new Object[]{defaultImage,tooltip,menu});
        ReflectionHelper.call(trayIcon,"setImageAutoSize",true);
        // if we get this far, java6 is looking ok.
        java6Failed = false; 
	}
	
	
	private final Object trayIcon;
    private final Image defaultImage;
    private final Image throbbingImage;
    private final Object systemTray;
    private volatile boolean java6Failed = true; // assume it's going to fail by default
    
	// overridden to display as system tray.
    public void run() {
        if (java6Failed) {
            super.run();
        } else {
            logger.info("Starting Java 1.6 Systemtray");
            try {
                ReflectionHelper.call(systemTray,"add",trayIcon);
                ReflectionHelper.call(trayIcon,"addActionListener",this);
            } catch (final Throwable x) {
                logger.warn("Failed to call SystemTray.add() - belatedly falling back",x);
                java6Failed = true;
                super.run();
            } 
        }
    }
    
    private  PopupMenu createPopupMenu() {
        final PopupMenu m = new PopupMenu();

        //window factories.
        for (final Iterator facs = context.getWindowFactories().keySet().iterator(); facs.hasNext(); ) {
            final String key = (String) facs.next();
            final MenuItem f = new MenuItem("New " + key);
            f.setActionCommand(key);
            f.addActionListener(context);
            m.add(f);
        }

        m.addSeparator();
        
        final MenuItem pref = new MenuItem("VO Desktop and Astro Runtime Preferences...");
        pref.setActionCommand(UIContext.PREF);
        pref.addActionListener(context);
        m.add(pref);
        
        final MenuItem test = new MenuItem("Run Self Tests");
        test.setActionCommand(UIContext.SELFTEST);
        test.addActionListener(context);
        m.add(test);
        
        final MenuItem processes = new MenuItem("Show Background Processes");
        processes.setActionCommand(UIContext.PROCESSES);
        processes.addActionListener(context);
        m.add(processes);
        
        final ButtonModel model = context.getLoggedInModel();        
        m.addSeparator();
        final MenuItem login = new MenuItem("Login to Community...");
        login.setActionCommand(UIContext.LOGIN);
        login.addActionListener(context);
        login.setEnabled(!model.isEnabled());
        m.add(login);
        
        final MenuItem logout = new MenuItem("Logout");
        logout.setActionCommand(UIContext.LOGOUT);
        logout.addActionListener(context);
        logout.setEnabled(model.isEnabled());
        m.add(logout);
        
        model.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                login.setEnabled(! model.isEnabled());
                logout.setEnabled(model.isEnabled());                
            }            
        });
                
   
                        
        final MenuItem h = new MenuItem("VO Desktop Help");
        h.setActionCommand(UIContext.HELP);
        h.addActionListener(context);
        m.add(h);
        
        final MenuItem a = new MenuItem("About VO Desktop");
        a.setActionCommand(UIContext.ABOUT);
        a.addActionListener(context);
        m.add(a);   
        m.addSeparator();        
        
        final MenuItem sd = new MenuItem("Exit VO Desktop");
        sd.setActionCommand(UIContext.EXIT);
        sd.addActionListener(context);
        m.add(sd);
        return m;
    }
	
	public void displayErrorMessage(final String arg0, final String arg1) {
	    if (java6Failed) {
	        super.displayErrorMessage(arg0,arg1);
	    } else {
	        try {
	            displayMsg(arg0,arg1,"ERROR");
            } catch (final Throwable t) {
                logger.warn("Failed to call java6 - falling back",t);
                java6Failed = true;
                super.run();
                super.displayErrorMessage(arg0,arg1);
            }	            
	    }
	}

	public void displayInfoMessage(final String arg0, final String arg1) {
	    if (java6Failed) {
	        super.displayInfoMessage(arg0,arg1);
	    } else {
	        try {
	            displayMsg(arg0,arg1,"INFO");
            } catch (final Throwable t) {
                logger.warn("Failed to call java6 - falling back",t);
                java6Failed = true;
                super.run();
                super.displayInfoMessage(arg0,arg1);
            }	            
	    }
	}

	public void displayWarningMessage(final String arg0, final String arg1) {
	    if (java6Failed) {
	        super.displayWarningMessage(arg0,arg1);
	    } else {
	        try {
	            displayMsg(arg0,arg1,"WARNING");
	        } catch (final Throwable t) {
	            logger.warn("Failed to call java6 - falling back",t);
	            java6Failed = true;
	            super.run();
	            super.displayWarningMessage(arg0,arg1);
	        }
	    }
	}
	
	private void displayMsg(final String caption, final String text, final String type) throws ClassNotFoundException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
			// first, convert string type into new-fangled enumeration..
			final Class en = Class.forName("java.awt.TrayIcon$MessageType");
			final Object o = ReflectionHelper.callStatic(en,"valueOf",type);
			// now call the action.
			ReflectionHelper.call(trayIcon,"displayMessage",caption,text,o);	
	}

	public void startThrobbing() {
	    if (java6Failed) {
	        super.startThrobbing();
	    } else {
	        if (++throbberCallCount > 0) {
	            try {
	                ReflectionHelper.call(trayIcon,"setImage",throbbingImage);
	            } catch (final Throwable x) {
	                logger.warn("Failed to start throbbing - falling back",x);
	                java6Failed = true;
	                super.run(); // show fallbck systray
	                throbberCallCount -= 1;
	                super.startThrobbing();			    
	            }
	        }	
	    }
	}

	public void stopThrobbing() {
	    if (java6Failed) {
	        super.stopThrobbing();
	    } else {
	        if (! (--throbberCallCount > 0)) {
	            try {
	                ReflectionHelper.call(trayIcon,"setImage",defaultImage);
	            } catch (final Throwable x) {
	                logger.warn("Failed to stop throbbing - falling back",x);
	                java6Failed = true;
	                super.run(); // show fallback
	                throbberCallCount += 1;
	                super.stopThrobbing();
	            }
	        }		
	    }
	}
	/** show main window, or create a new one */
    public void actionPerformed(final ActionEvent e) {
        final UIComponent win = context.findMainWindow();
        if (win == null || win instanceof HeadlessUIComponent) {
            final String factoryName = appToLaunch.getValue();
            final Object o = context.getWindowFactories().get(factoryName);
            if (o != null && o instanceof Factory) {
                ((Factory)o).create();
            }
        } else {
            win.setVisible(true);
            final Window w = (Window)win.getComponent();
            w.toFront();
            w.requestFocus();
        }
    }
}
