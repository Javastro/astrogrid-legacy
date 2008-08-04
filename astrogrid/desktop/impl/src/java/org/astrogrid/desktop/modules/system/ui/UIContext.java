/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ButtonModel;
import javax.swing.JMenu;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.UI;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;

import ca.odell.glazedlists.EventList;

/** central context object - used to store data common to all ui.
 * 
 * Any models returned from methods in this class aren't EDT-protecterd - 
 * it's the responsibility of the caller of these methods to access the model in 
 * an EDT-responsible way.
 * 
 * 
 * extends action listener so that it can process commands emitted from menus.
 * can process any of the string constants in this interface. Any other
 * action command is interpreted as a key for the WindowFactories Map, and
 * it will attempt to create a new window of the associated type.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 20077:06:31 PM
 */
public interface UIContext  extends UI, ActionListener{

	/** the action command to exit the application
     */
    public static final String EXIT = "exit";
    /** action command to show preferences */
    public static final String PREF = "pref";
    /** action command to show about dialogue */
    public static final String ABOUT = "about";
    /** action command to show root of help */
    public static final String HELP = "HELP";
    /** action command to login */
    public static final String LOGIN = "login";
    /** action command to logout */
    public static final String LOGOUT = "logout";
    /** action command to reset the configuration */
    public static final String RESET = "reset";
    /** action command to clear the cache */
    public static final String CLEAR_CACHE = "clearcache";
    /** show self-tests */
    public static final String SELFTEST = "selftest";
    /** show processes monitor */
    public static final String PROCESSES = "processes";
    

    /** convenience method - access the configuraiton componoent
	 * @todo hide this. - have it passed into components, if required.
	 *  
	 *  */
	public Configuration getConfiguration();

	/** convenience mehtod - access the help server component */
	public HelpServerInternal getHelpServer();

	/** convenience method - access the executor */
	public BackgroundExecutor getExecutor();
	/** control the system browser */
	public BrowserControl getBrowser();
	
	public Map getWindowFactories();
	
	  public void showAboutDialog();   

	  public void showPreferencesDialog();

	  
	/** button model that indicates logged in status using 'enabled' property
	 * makes it simple to plug into a button and use it to flip between enabled / disabled icons
	 * 
	 * it's the caller's responsibility to use this in an EDT-sensitive manner.
	 */
	public ButtonModel getLoggedInModel() ;
	/** indicates throbber status. same usage as {@link #getLoggedInModel} 
	 * 
	 *      * it's the caller's responsibility to use this in an EDT-sensitive manner.
	 * */
	public ButtonModel getThrobbingModel() ;
	
	/** provide access to it here, to simplify plumbing */
	public BackgroundWorkersMonitor getWorkersMonitor();
	/** indicates global visiblily of the UI. same usage as {@link #getLoggedInModel} 
	 * 
	 *      * it's the caller's responsibility to use this in an EDT-sensitive manner.*/
	public ButtonModel getVisibleModel() ;
	/** a dynamic, event-firing list of window. unmodifiable. */
	public EventList getWindowList();
	/** a dynamic, event-firing list of connected plastic applications */
	public EventList getPlasticList();
	/** a dynamic, event-firing list of running tasks 
	 * may be modified, but only on the EDT*/
	public EventList getTasksList();
	/** register a window with the context - so that it appears in windowList */
	public void registerWindow(UIComponent window);
	/** unregister a window from the context - removes it from window list */
	public void unregisterWindow(UIComponent window);
	/** return a reference to the main / uppermost window */
	public UIComponent findMainWindow();
	//@todo some way to iinform which window is uppermost.. or can this be deduced?

	// helper methods for constructing UI.
	/**
	 *  create a new menu, which shows the window list, and has operations
	 *  to hide 'owner', hide all, and create new windows of various UI types (if available)
	 */
	public JMenu createWindowMenu( ) ;
	
	/** create a new menu, which shows operations for interoperability */
	public JMenu createInteropMenu();
	

}