/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.swing.ImageIcon;

import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;

/** System tray implementation for Java6
 * reuses much of the machinery of the fallback system tray.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 20, 200710:30:07 AM
 */
public class Java6SystemTray extends FallbackSystemTray {


	public Java6SystemTray(UIContext context,org.astrogrid.acr.builtin.Shutdown shutdown,Runnable config) {
	    super(context, shutdown,config);
	}
	
	private boolean fallback = false; 
	
	private Object trayIcon;
    private Image defaultImage;
    private Image throbbingImage;	
    
	// overridden to display as system tray.
	protected void displayUI() {
		// crap, got to do all this by reflection...
		logger.info("Starting Java 1.6 Systemtray");
		try {
			Class systrayClz = Class.forName("java.awt.SystemTray");
			Object b =ReflectionHelper.callStatic(systrayClz,"isSupported");
			if (((Boolean)b).booleanValue() == false) { // not supported
				logger.warn("System tray is not supported on this platform. Falling back");
				fallback  = true;
				super.displayUI();
				return;
			}
			Object systemTray = ReflectionHelper.callStatic(systrayClz,"getSystemTray");
			
			ImageIcon ic = IconHelper.loadIcon("ar16.png");//"AGlogo16x16.png");
			defaultImage = ic.getImage();
			ic = IconHelper.loadIcon("running16.png");
			throbbingImage = ic.getImage();
			
			Class trayIconClz = Class.forName("java.awt.TrayIcon");
			Constructor trayConstructor = trayIconClz.getConstructor(new Class[]{Image.class,String.class,PopupMenu.class}); 
			String tooltip = "Astro Runtime";
			PopupMenu menu = createPopupMenu();
			trayIcon = trayConstructor.newInstance(new Object[]{defaultImage,tooltip,menu});
			ReflectionHelper.call(trayIcon,"setImageAutoSize",Boolean.TRUE);
			ReflectionHelper.call(systemTray,"add",trayIcon);
		} catch (ClassNotFoundException x) {
			logger.warn("Failed to find java.awt.SystemTray");
		} catch (NoSuchMethodException x) {
			logger.warn("System tray lacks expected methods");
		} catch (IllegalArgumentException x) {
			logger.error("IllegalArgumentException",x);
		} catch (IllegalAccessException x) {
			logger.error("IllegalAccessException",x);
		} catch (InvocationTargetException x) {
			logger.error("InvocationTargetException",x);
		} catch (InstantiationException x) {
			logger.error("InstantiationException",x);
		}
	}

    
    private  PopupMenu createPopupMenu() {
        PopupMenu m = new PopupMenu();

        //window factories.
        for (Iterator facs = context.getWindowFactories().keySet().iterator(); facs.hasNext(); ) {
            final String key = (String) facs.next();
            MenuItem f = new MenuItem("New " + key);
            f.setActionCommand(key.toString());
            f.addActionListener(this);
            m.add(f);
        }
        m.addSeparator();
        MenuItem pref = new MenuItem("Preferences..");
        pref.setActionCommand(PREF);
        pref.addActionListener(this);
        m.add(pref);
        m.addSeparator();
        

        
        MenuItem h = new MenuItem("Help contents");
        h.setActionCommand(HELP);
        h.addActionListener(this);
        m.add(h);
        m.addSeparator();        
        /* @todo implement UIContextImpl.getAbout() first 
        MenuItem a = new MenuItem("About AstroRuntime..");
        a.setActionCommand(ABOUT);
        a.addActionListener(this);
        m.add(a);        
        */
        
        MenuItem sd = new MenuItem("Exit");
        sd.setActionCommand(EXIT);
        sd.addActionListener(this);
        m.add(sd);
        return m;
    }
    

	

	
	
	public void displayErrorMessage(String arg0, String arg1) {
	    if (fallback) {
	        super.displayErrorMessage(arg0,arg1);
	    } else {
	        displayMsg(arg0,arg1,"ERROR");
	    }
	}

	public void displayInfoMessage(String arg0, String arg1) {
	    if (fallback) {
	        super.displayInfoMessage(arg0,arg1);
	    } else {
	        displayMsg(arg0,arg1,"INFO");
	    }
	}

	public void displayWarningMessage(String arg0, String arg1) {
	    if (fallback) {
	        super.displayWarningMessage(arg0,arg1);
	    } else {
	        displayMsg(arg0,arg1,"WARNING");
	    }
	}
	
	private void displayMsg(String caption, String text, String type) {
		try {
			// first, convert string type into new-fangled enumeration..
			Class en = Class.forName("java.awt.TrayIcon$MessageType");
			Object o = ReflectionHelper.callStatic(en,"valueOf",type);
			// now call the action.
			ReflectionHelper.call(trayIcon,"displayMessage",caption,text,o);
		} catch (ClassNotFoundException x) {
			logger.error("ClassNotFoundException",x);
		} catch (IllegalArgumentException x) {
			logger.error("IllegalArgumentException",x);
		} catch (NoSuchMethodException x) {
			logger.error("NoSuchMethodException",x);
		} catch (IllegalAccessException x) {
			logger.error("IllegalAccessException",x);
		} catch (InvocationTargetException x) {
			logger.error("InvocationTargetException",x);
		}
	}

	public void startThrobbing() {
	    if(fallback) {
	        super.startThrobbing();
	        return;
	    }
        if (throbberCallCount.increment() > 0) {
        	try {
				ReflectionHelper.call(trayIcon,"setImage",throbbingImage);
			} catch (IllegalArgumentException x) {
				logger.error("IllegalArgumentException",x);
			} catch (IllegalAccessException x) {
				logger.error("IllegalAccessException",x);
			} catch (InvocationTargetException x) {
				logger.error("InvocationTargetException",x);
			} catch (NoSuchMethodException x) {
				logger.error("NoSuchMethodException",x);
			}
        }		
	}

	public void stopThrobbing() {
	    if (fallback) {
	        super.stopThrobbing();
	        return;
	    }
        if (! (throbberCallCount.decrement() > 0)) {
        	try {
				ReflectionHelper.call(trayIcon,"setImage",defaultImage);
			} catch (IllegalArgumentException x) {
				logger.error("IllegalArgumentException",x);
			} catch (IllegalAccessException x) {
				logger.error("IllegalAccessException",x);
			} catch (InvocationTargetException x) {
				logger.error("InvocationTargetException",x);
			} catch (NoSuchMethodException x) {
				logger.error("NoSuchMethodException",x);
			}
        }		
	}

	


}
