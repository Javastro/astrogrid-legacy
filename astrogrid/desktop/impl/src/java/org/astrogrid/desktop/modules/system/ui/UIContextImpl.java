/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.util.EventListener;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.SwingUtilities;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.desktop.alternatives.HeadlessUIComponent;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.ObservableElementList;

/** A context object - provides generally useful services to various UI windows, 
 * and mantains a list of current UI windows, common UI models, etc.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 20075:52:29 PM
 */
public class UIContextImpl implements UIContext{

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
	//convenience constructor
	public UIContextImpl(final Configuration configuration,  BackgroundExecutor executor,final HelpServerInternal help, final BrowserControl browser) {
		this(configuration,executor,help,browser, new BasicEventList());
	}
	
	public UIContextImpl(final Configuration configuration,  BackgroundExecutor executor,final HelpServerInternal help, final BrowserControl browser, EventList plastic) {
		super();
		this.configuration = configuration;
		this.help = help;
		this.executor = executor;
		this.browser = browser;
		this.plastic = plastic;

		windows = new BasicEventList();
		windowsView = GlazedLists.readOnlyList(windows);
		
		loggedInState = new DefaultButtonModel();
		loggedInState.setEnabled(false);
		throbbingState = new DefaultButtonModel();
		throbbingState.setEnabled(false);
		visibleState = new DefaultButtonModel();
		visibleState.setEnabled(false);
		
    	//create an event list that observes notifications emitted by items in the list.
    	tasksList = new ObservableElementList(new BasicEventList(),
    			new ObservableConnector());
				
	}
	
	private final BrowserControl browser;
	private final BackgroundExecutor executor;
	private final Configuration configuration;
	private final HelpServerInternal help;
	private final EventList plastic;
	
	private final ButtonModel loggedInState ;
	private final ButtonModel throbbingState ;
	private final ButtonModel visibleState ;
	
	// ui interface, 
	private final EventList windows; // list of windows
	private final EventList windowsView; // read-only view of this list.
	
	private final EventList tasksList; // list of running tasks
	
	// causes all windows to be hidden.
	public void hide() {
		if (SwingUtilities.isEventDispatchThread()) {
			setVisible(false);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setVisible(false);
				}
			});
		}
	}
	// show all windows.
	public void show() {
		if (SwingUtilities.isEventDispatchThread()) {
				setVisible(true);
			} else {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setVisible(true);
					}
				});
			}
	}
	// always runs on EDT
	private void setVisible(boolean b){
		for (Iterator i = windows.iterator(); i.hasNext();) {
			UIComponent c = (UIComponent) i.next();
			c.setVisible(b);
		}		
		visibleState.setEnabled(b);
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
	
	public void setLoggedIn(boolean b) {
		loggedInState.setEnabled(b);
	}
	
	public void setStatusMessage(final String arg0) {
		if (SwingUtilities.isEventDispatchThread()) {
			findMainWindow().setStatusMessage(arg0);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					findMainWindow().setStatusMessage(arg0);					
				}
			});
		}
	}
	
	private int throbberCallCount = 0;
	public void startThrobbing() {
		if (SwingUtilities.isEventDispatchThread()) {
			throbbingState.setEnabled(++throbberCallCount > 0);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					throbbingState.setEnabled(++throbberCallCount > 0);
				}
			});
		}		
		
	}
	public void stopThrobbing() {
		if (SwingUtilities.isEventDispatchThread()) {
			throbbingState.setEnabled(--throbberCallCount > 0);
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					throbbingState.setEnabled(--throbberCallCount > 0);
				}
			});
		}		
	}
	
	public void registerWindow(UIComponent window) {
		if (!windows.contains(window)) {
			windows.add(window);
		}
	}
	public void unregisterWindow(UIComponent window) {
		if (windows.contains(window)) {
			windows.remove(window);			
		}
	}
	
	public EventList getWindowList() {
		return windowsView;
	}
	public UIComponent findMainWindow() {
		if (windows.size() == 0) {
			return new HeadlessUIComponent("startup",this);
		} else {
			return (UIComponent)windows.get(0);
		}
	}
	public EventList getPlasticList() {
		return plastic;
	}
	// tasks list
	public EventList getTasksList() {
		return tasksList;
	}	
	
	public void showAboutDialog() {
		//@todo implement
	}
	//@todo add in command to show the preference dialogue too.


	
	//@todo , add in this..
	/*
	 *    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
            	if (getContext().getWindowList().size() ==1) { // we're the last.
            		//@todo improve this message...
                int code = JOptionPane.showConfirmDialog(UIComponentImpl.this,"Closing the UI. Do you want  the ACR service to continue to run in the background?", 
                        "Closing UI",JOptionPane.INFORMATION_MESSAGE);
                if (code == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                hide(); // always fo this..
                if (code == JOptionPane.NO_OPTION) {                    
                        shutdown.halt(); 
                }
            }
        });        
	 */
	

	/** a connector that listens to changes to observable objects and bridges these 
     * events into the ObservableElementList. Used to display a list of running background tasks.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 27, 200710:00:01 PM
 */
private static class ObservableConnector implements ObservableElementList.Connector, Observer, EventListener {
	ObservableElementList list;
	public EventListener installListener(Object arg0) {
		((Observable)arg0).addObserver(this);
		return this;
	}
	public void setObservableElementList(ObservableElementList arg0) {
		this.list = arg0;
	}

	public void uninstallListener(Object arg0, EventListener arg1) {
		if (arg1 == this) {
			((Observable)arg0).deleteObserver(this);
		}
	}
	// update from the thing we're observing.
	public void update(Observable o, Object arg) {
		list.elementChanged(o);
	}
}

}