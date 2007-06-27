/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.collections.Factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.system.SystemTray;
import org.astrogrid.desktop.framework.ReflectionHelper;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.system.ui.UIContext;

import EDU.oswego.cs.dl.util.concurrent.SynchronizedInt;

/** System tray implementation for Java6
 * @todo add login/ logout icon and actions..
 * @todo add window lists ?
 * @todo share throbbing model
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 20, 200710:30:07 AM
 */
public class Java6SystemTray implements SystemTray, ActionListener {
	/**
	 */
	private static final String EXIT = "exit";
	private static final String PREF = "pref";	
	private static final String ABOUT = "about";
	private static final String HELP = "HELP";
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(Java6SystemTray.class);

	
	public Java6SystemTray(UIContext context,org.astrogrid.acr.builtin.Shutdown shutdown,Runnable config) {
		this.shutdown = shutdown;
		this.context = context;
		this.configDialogue = config;
		// crap, got to do all this by reflection...
		logger.info("Starting Java 1.6 Systemtray");
		try {
			Class systrayClz = Class.forName("java.awt.SystemTray");
			Object b =ReflectionHelper.callStatic(systrayClz,"isSupported");
			if (((Boolean)b).booleanValue() == false) { // not supported
				logger.warn("System tray is not supported on this platform. Falling back");
				//@todo implement some kind of fallback.
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
	private final org.astrogrid.acr.builtin.Shutdown shutdown;
	private final UIContext context;
	private final Runnable configDialogue;
	private Object trayIcon;
	private Image defaultImage;
	private Image throbbingImage;
	
	private PopupMenu createPopupMenu() {
		PopupMenu m = new PopupMenu();
		MenuItem a = new MenuItem("About AstroRuntime..");
		a.setActionCommand(ABOUT);
		a.addActionListener(this);
		m.add(a);
		
		MenuItem h = new MenuItem("Help");
		h.setActionCommand(HELP);
		h.addActionListener(this);
		m.add(h);
		m.addSeparator();
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
		MenuItem sd = new MenuItem("Exit");
		sd.setActionCommand(EXIT);
		sd.addActionListener(this);
		m.add(sd);
		return m;
	}
	
	
	public void displayErrorMessage(String arg0, String arg1) {
		displayMsg(arg0,arg1,"ERROR");
	}

	public void displayInfoMessage(String arg0, String arg1) {
		displayMsg(arg0,arg1,"INFO");
	}

	public void displayWarningMessage(String arg0, String arg1) {
		displayMsg(arg0,arg1,"WARNING");
	}
	
	protected void displayMsg(String caption, String text, String type) {
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
	
	protected SynchronizedInt throbberCallCount = new SynchronizedInt(0);
	public void startThrobbing() {
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

	// callbacks from menu items.
	public void actionPerformed(ActionEvent arg0) {
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
