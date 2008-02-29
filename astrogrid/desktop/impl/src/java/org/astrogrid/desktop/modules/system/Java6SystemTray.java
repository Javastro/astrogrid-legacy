/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;
import org.astrogrid.desktop.modules.system.ui.UIContextImpl;

/** System tray implementation for Java6
 * reuses much of the machinery of the fallback system tray.
 * 
 * 
 * added lots of code armour - the first time that a java6 call fails, behaviour reverts 
 * to the fallback system tray.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 20, 200710:30:07 AM
 */
public class Java6SystemTray extends FallbackSystemTray implements SystemTrayInternal {

	public Java6SystemTray(UIContext context) throws Exception {
	    super(context);
	    // so got all the fallback machinery all ready. see if java6 will behave..
	    
        Class systrayClz = Class.forName("java.awt.SystemTray");
        Object b =ReflectionHelper.callStatic(systrayClz,"isSupported");
        if (((Boolean)b).booleanValue() == false) { // not supported
            logger.info("System tray is not supported on this platform. Falling back");
            throw new Exception("System tray not supported");
        }	    
	    
        systemTray = ReflectionHelper.callStatic(systrayClz,"getSystemTray");
        Class trayIconClz = Class.forName("java.awt.TrayIcon");
        Constructor trayConstructor = trayIconClz.getConstructor(new Class[]{Image.class,String.class,PopupMenu.class});
        // looks good - initialize the rest of our stuff...
        ImageIcon ic = IconHelper.loadIcon("ar16.png");//"AGlogo16x16.png");
        defaultImage = ic.getImage();
        ic = IconHelper.loadIcon("running16.png");
        throbbingImage = ic.getImage();
        
        String tooltip = "Astro Runtime";
        PopupMenu menu = createPopupMenu();
        trayIcon = trayConstructor.newInstance(new Object[]{defaultImage,tooltip,menu});
        ReflectionHelper.call(trayIcon,"setImageAutoSize",Boolean.TRUE);
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
            } catch (Throwable x) {
                logger.warn("Failed to call SystemTray.add() - belatedly falling back",x);
                java6Failed = true;
                super.run();
            } 
        }
    }
    
    private  PopupMenu createPopupMenu() {
        PopupMenu m = new PopupMenu();

        //window factories.
        for (Iterator facs = context.getWindowFactories().keySet().iterator(); facs.hasNext(); ) {
            final String key = (String) facs.next();
            MenuItem f = new MenuItem("New " + key);
            f.setActionCommand(key);
            f.addActionListener(context);
            m.add(f);
        }
        m.addSeparator();
        MenuItem test = new MenuItem("Self Tests");
        test.setActionCommand(UIContext.SELFTEST);
        test.addActionListener(context);
        m.add(test);
        
        MenuItem processes = new MenuItem("Background Processes");
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
            public void stateChanged(ChangeEvent e) {
                login.setEnabled(! model.isEnabled());
                logout.setEnabled(model.isEnabled());                
            }            
        });
                
        MenuItem pref = new MenuItem("Preferences...");
        pref.setActionCommand(UIContext.PREF);
        pref.addActionListener(context);
        m.add(pref);        
                        
        MenuItem h = new MenuItem("VO Desktop Help");
        h.setActionCommand(UIContext.HELP);
        h.addActionListener(context);
        m.add(h);
        
        MenuItem a = new MenuItem("About VO Desktop");
        a.setActionCommand(UIContext.ABOUT);
        a.addActionListener(context);
        m.add(a);   
        m.addSeparator();        
        
        MenuItem sd = new MenuItem("Exit");
        sd.setActionCommand(UIContext.EXIT);
        sd.addActionListener(context);
        m.add(sd);
        return m;
    }
	
	public void displayErrorMessage(String arg0, String arg1) {
	    if (java6Failed) {
	        super.displayErrorMessage(arg0,arg1);
	    } else {
	        try {
	            displayMsg(arg0,arg1,"ERROR");
            } catch (Throwable t) {
                logger.warn("Failed to call java6 - falling back",t);
                java6Failed = true;
                super.run();
                super.displayErrorMessage(arg0,arg1);
            }	            
	    }
	}

	public void displayInfoMessage(String arg0, String arg1) {
	    if (java6Failed) {
	        super.displayInfoMessage(arg0,arg1);
	    } else {
	        try {
	            displayMsg(arg0,arg1,"INFO");
            } catch (Throwable t) {
                logger.warn("Failed to call java6 - falling back",t);
                java6Failed = true;
                super.run();
                super.displayInfoMessage(arg0,arg1);
            }	            
	    }
	}

	public void displayWarningMessage(String arg0, String arg1) {
	    if (java6Failed) {
	        super.displayWarningMessage(arg0,arg1);
	    } else {
	        try {
	            displayMsg(arg0,arg1,"WARNING");
	        } catch (Throwable t) {
	            logger.warn("Failed to call java6 - falling back",t);
	            java6Failed = true;
	            super.run();
	            super.displayWarningMessage(arg0,arg1);
	        }
	    }
	}
	
	private void displayMsg(String caption, String text, String type) throws ClassNotFoundException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
			// first, convert string type into new-fangled enumeration..
			Class en = Class.forName("java.awt.TrayIcon$MessageType");
			Object o = ReflectionHelper.callStatic(en,"valueOf",type);
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
	            } catch (Throwable x) {
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
	            } catch (Throwable x) {
	                logger.warn("Failed to stop throbbing - falling back",x);
	                java6Failed = true;
	                super.run(); // show fallback
	                throbberCallCount += 1;
	                super.stopThrobbing();
	            }
	        }		
	    }
	}
}
