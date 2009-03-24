/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.apache.commons.collections.Factory;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.builtin.Shutdown;
import org.astrogrid.acr.ivoa.CacheFactory;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.SplashWindow;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.modules.auth.ChangePasswordDialog;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.desktop.modules.dialogs.ConfirmDialog;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.system.messaging.ExternalMessageTarget;
import org.astrogrid.desktop.modules.system.messaging.Messaging;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.EventListMenuManager;
import org.astrogrid.desktop.modules.ui.comp.ObservableConnector;
import org.astrogrid.desktop.modules.ui.comp.UIConstants;
import org.astrogrid.desktop.modules.util.SelfTester;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FunctionList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;

import com.l2fprod.common.swing.icons.EmptyIcon;

/** 
 * Implementation of {@code UIContext}.
 *  {Provides generally useful services to various UI windows, 
 * and mantains a list of current UI windows, common UI models, etc.
 * 
 * means that a lot of other services get passed into this component - so that 
 * it's easier for ui windows to access these service occasionally without excess plumbing.
 * 
 * also implements the single handler for all global events.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 20075:52:29 PM
 */
public class UIContextImpl implements UIContext{


    private final CacheFactory cache;
    private final CommunityInternal community;
    private final Shutdown shutdown;
    private final SelfTester tester;
    private final BackgroundWorkersMonitor monitor;
    private final Runnable configDialog;
    private final Runnable aboutDialog;
    private final Messaging messaging;

    /** convenience method - access the configuraiton componoent
     * @todo hide this. - have it passed into components, if required.
     *  
     *  */
    public Configuration getConfiguration() {
        return configuration;
    }
    /** convenience mehtod - access the help server component */
    public HelpServerInternal getHelpServer() {
        return help;
    }
    /** convenience method - access the executor */
	public BackgroundExecutor getExecutor() {
		return executor;
	}
	
	public BrowserControl getBrowser() {
		return browser;
	}
	

	
	//convenience constructor used while testing oter parts of system.
	public UIContextImpl(final Configuration configuration,  final BackgroundExecutor executor,final HelpServerInternal help, final BrowserControl browser) {
		this(configuration,executor,help,browser,null,null,null,null,null,null,null, new HashMap(),null,null);
	}
	
	public UIContextImpl(final Configuration configuration
	        ,  final BackgroundExecutor executor,
	        final HelpServerInternal help
	        , final BrowserControl browser
	        , final CacheFactory cache
	        , final CommunityInternal community
	        , final Shutdown shutdown
	        , final SelfTester tester
	        ,final BackgroundWorkersMonitor monitor
	        ,final Runnable configDialog
	        ,final Runnable aboutDialog
	        ,final Map<String, Factory>windowFactories
	        ,final String launchAppName
	        ,final Messaging messaging
	        ) {
		super();
		SplashWindow.reportProgress("Starting User Interface...");
		this.messaging = messaging;
		this.configuration = configuration;
		this.help = help;
		this.executor = executor;
		this.browser = browser;
        this.cache = cache;
        this.community = community;
        this.shutdown = shutdown;
        this.tester = tester;
        this.monitor = monitor;
        this.configDialog = configDialog;
        this.aboutDialog = aboutDialog;
	// just creates a map ordered by whatever order hivemind takes.	
	//	this.windowFactories = new LinkedHashMap(windowFactories);
		this.windowFactories = new TreeMap<String, Factory>(
		        
		        new Comparator<String>() {	        
		        //WARNING - this is horribly fragile - string constants must match
		        // the names of the window factories.
		        //@todo re-implement sorting based on a value in the configuration.
		     private final List<String> order = Collections.unmodifiableList(Arrays.asList( new String[]{
		                "VO Explorer"
		                ,"File Explorer"
		                ,"Task Runner"
		                ,"All-VO Astroscope"
		                ,"All-VO Helioscope"
		             }));
            public int compare(final String o1, final String o2) {
                return order.indexOf(o1) - order.indexOf(o2);
            }
		        });
		this.windowFactories.putAll(windowFactories);
		windows = new BasicEventList<UIComponent>();
		windowsView = GlazedLists.readOnlyList(windows);
		
		loggedInState = new DefaultButtonModel();
		loggedInState.setActionCommand("Not logged in"); // hack - am using 'action command' to store a tooltip.
		loggedInState.setEnabled(false);
		throbbingState = new DefaultButtonModel();
		throbbingState.setEnabled(false);
		visibleState = new DefaultButtonModel();
		visibleState.setEnabled(false);
		
    	//create an event list that observes notifications emitted by items in the list.
		// implementation note: all adds / deletes and in-place update notifications 
		// must take place on the EDT.
    	tasksList = new ObservableElementList<BackgroundWorker>(new BasicEventList<BackgroundWorker>(),
    			new ObservableConnector<BackgroundWorker>());
				
    	
    	// launch the preferred window.
    	// had tried using the ar scheduler here, but causes a deadlock - thinkn it 
    	// introduces a circular dep between the two components too early in the startup process.
    	final Timer t = new Timer(3000,new ActionListener(){

            public void actionPerformed(final ActionEvent e) {
                SplashWindow.reportProgress("Opening " + launchAppName);
                UIContextImpl.this.actionPerformed(new ActionEvent(this,0,launchAppName));
            }
    	});
    	t.setRepeats(false);
    	t.start();
	}
	final Map<String, Factory> windowFactories;
	private final BrowserControl browser;
	private final BackgroundExecutor executor;
	private final Configuration configuration;
	private final HelpServerInternal help;
	
	private final ButtonModel loggedInState ;
	private final ButtonModel throbbingState ;
	private final ButtonModel visibleState ;
	
	// ui interface, 
	private final EventList<UIComponent> windows; // list of windows
	private final EventList<UIComponent> windowsView; // read-only view of this list.
	
	private final EventList<BackgroundWorker> tasksList; // list of running tasks
	
	// causes all windows to be hidden -  - hivemind ensures this is always on EDT
	public void hide() {
			for (final Iterator<UIComponent> i = windows.iterator(); i.hasNext();) {
            	final UIComponent c = i.next();
            	c.setVisible(false);
            }		
            visibleState.setEnabled(false);
		
	}
	// show all windows. - hivemind ensures this is always on EDT
	public void show() {
				for (final Iterator<UIComponent> i = windows.iterator(); i.hasNext();) {
                	final UIComponent c = i.next();
                	c.setVisible(true);
                }		
                visibleState.setEnabled(true);
			
	}
	public ButtonModel getLoggedInModel() {
		return loggedInState;
	}
	public ButtonModel getThrobbingModel() {
		return throbbingState;
	}
	public ButtonModel getVisibleModel() {
		return visibleState;
	}
	
	public void setLoggedIn(final boolean b) {
        loggedInState.setEnabled(b);
	}
	//  - hivemind ensures this is always on EDT
	public void setStatusMessage(final String arg0) {
			findMainWindow().setStatusMessage(arg0);
	
	}
	
	private int throbberCallCount = 0;
	 //- hivemind ensures this is always on EDT
	public void startThrobbing() {
			throbbingState.setEnabled(++throbberCallCount > 0);
	
	}
	// - hivemind ensures this is always on EDT
	public void stopThrobbing() {
			throbbingState.setEnabled(--throbberCallCount > 0);
	
	}
	
	public void registerWindow(final UIComponent window) {
		if (!windows.contains(window)) {
			windows.add(window);
		}
	}
	public void unregisterWindow(final UIComponent window) {
		if (windows.contains(window)) {
			windows.remove(window);			
		}
	}
	
	public EventList<UIComponent> getWindowList() {
		return windowsView;
	}
	public UIComponent findMainWindow() {
		if (windows.size() == 0) {
			return new HeadlessUIComponent("startup",this);
		} else {
			return windows.get(0);
		}
	}
	public EventList<ExternalMessageTarget> getPlasticList() {
		return messaging.getTargetList();
	}
	// tasks list
	public EventList<BackgroundWorker> getTasksList() {
		return tasksList;
	}	
	
	public void showAboutDialog() {
		aboutDialog.run();
	}
	
	
	public Map<String, Factory> getWindowFactories() {
		return windowFactories;
	}
	public void showPreferencesDialog() {
	    configDialog.run();
	}
	
	public void showChangePasswordDialog() {
	    new ChangePasswordDialog(community,this).ask();
	}


	static final int[] FN_KEYS = new int[] {
	    KeyEvent.VK_F1
	    ,KeyEvent.VK_F2
        ,KeyEvent.VK_F3
        ,KeyEvent.VK_F4
        ,KeyEvent.VK_F5
        ,KeyEvent.VK_F6
        ,KeyEvent.VK_F7
        ,KeyEvent.VK_F8
        ,KeyEvent.VK_F9        
        ,KeyEvent.VK_F10
        ,KeyEvent.VK_F11
        ,KeyEvent.VK_F12      
	};
    private static final Icon EMPTY_ICON = new EmptyIcon(12,12);
    
    public JMenu createInteropMenu() {
        final JMenu interopMenu = new JMenu();
        interopMenu.setText("Interop");
        interopMenu.setMnemonic(KeyEvent.VK_I);
        messaging.populateInteropMenu(interopMenu);
        return interopMenu;
    }
    
	public JMenu createWindowMenu() {
		final JMenu windowMenu = new JMenu();
		windowMenu.setText("Window");
		windowMenu.setMnemonic(KeyEvent.VK_W);
		// splice in the new window factories.
		addWindowFactories(windowMenu);
		windowMenu.addSeparator();
	
		final JMenuItem selftest = new JMenuItem("Run Self Tests");
		selftest.setActionCommand(SELFTEST);
		selftest.addActionListener(this);
		windowMenu.add(selftest);
		
		final JMenuItem processes = new JMenuItem("Show Background Processes");
		processes.setActionCommand(PROCESSES);
		processes.addActionListener(this);
		windowMenu.add(processes);
		
		windowMenu.addSeparator();
		final EventList<JMenuItem> w = new FunctionList<UIComponent,JMenuItem>(this.getWindowList()
		        ,new FunctionList.Function<UIComponent,JMenuItem>() {

			    public JMenuItem evaluate(final UIComponent c) {
				final Window win = (Window)c.getComponent();
				final JMenuItem mi = new JMenuItem(c.getTitle(),EMPTY_ICON);				
				win.addPropertyChangeListener("title",new PropertyChangeListener() {
					public void propertyChange(final PropertyChangeEvent evt) {
						mi.setText(c.getTitle());
					}
				});
				
				win.addWindowFocusListener(new WindowFocusListener() {
                    public void windowGainedFocus(final WindowEvent e) {
                        mi.setIcon(UIConstants.UNKNOWN_ICON);
                    }
                    public void windowLostFocus(final WindowEvent e) {
                        mi.setIcon(EMPTY_ICON);
                    }
				});
				
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						c.setVisible(true);
					}
				});
				return mi;
			}
		});
		new EventListMenuManager(w,windowMenu,false);
//@todo consider whether this is a good thing to do on all platofrms.
//		windowMenu.add(new HideAllAction());
		return windowMenu;
	}
    /**
     * @param windowMenu
     */
    private void addWindowFactories(final JMenu windowMenu) {
        int i = 0;
		for (final Map.Entry<String,Factory> entry : windowFactories.entrySet()){
		    i++;		
			final String key = entry.getKey();
            final JMenuItem mi = new JMenuItem("New " + key);
			mi.setAccelerator(KeyStroke.getKeyStroke(FN_KEYS[i],Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			mi.setActionCommand(key);
			mi.addActionListener(this);
			windowMenu.add(mi);
		}
    }
	
    //@todo add a preference on acr.advanced to only enable this at certain times.
    private final class HideAllAction extends AbstractAction {
        public HideAllAction() {
            super("Hide All Windows");
            this.putValue(SHORT_DESCRIPTION,"Hide All Windows");
        }
        public void actionPerformed(final ActionEvent e) {
        	hide();
        }
    }

// action listener interface
    // callbacks from menu items.
    // intercepted - so will always be on EDT.
    public void actionPerformed(final ActionEvent arg0) {
            final String cmd = arg0.getActionCommand();
            if (cmd == null) {
                return;
            }
            if (cmd.equals(UIContext.EXIT)) {
                final ConfirmDialog exitDialog = new ConfirmDialog("Exit VO Desktop Application","This will exit all VO Desktop tools and shut down the Astro Runtime background software. OK?",new Runnable(){
                    public void run() {
                        shutdown.halt();
                    }
                });
                exitDialog.setModal(true);
                exitDialog.setVisible(true);
            } else if (cmd.equals(UIContext.PREF)) {
                showPreferencesDialog();
            } else if (cmd.equals(UIContext.HELP)) {
                getHelpServer().showHelp();
            } else if (cmd.equals(UIContext.ABOUT)) {
                showAboutDialog();
            } else if (cmd.equals(UIContext.LOGIN)) {
                community.guiLogin();
            } else if (cmd.equals(UIContext.LOGOUT)) {
                community.logout();
            } else if (cmd.equals(UIContext.CHANGE_PASSWORD)) {
                showChangePasswordDialog();
            } else if (cmd.equals(UIContext.SELFTEST)){
                tester.show();
            } else if (cmd.equals(UIContext.PROCESSES)) {
                monitor.showAll();
                
            } else if (cmd.equals(UIContext.RESET)) { // only called in headful mode.
                new ConfirmDialog("Reset Configuration","All user settings will be lost. Continue?",new Runnable() {
                    public void run() {
                        try {
                            getConfiguration().reset();
                        } catch (final ServiceException x) {
                            // ignore.
                        }
                    }
                }).setVisible(true);                                          
            } else if (cmd.equals(UIContext.CLEAR_CACHE)) { // only called in headful mode.
                new ConfirmDialog("Clear Cache","All cached data will be removed. Continue?",new Runnable() {                   
                    public void run() {
                        cache.flush();
                    }
                }).setVisible(true);
                               
            } else {
                // assume it's the name of a new window facotry.
                final Factory fac = getWindowFactories().get(cmd);
                if (fac != null) {
                    fac.create();
                }
            }
       
    }
    public BackgroundWorkersMonitor getWorkersMonitor() {
        return monitor;
    }

}
