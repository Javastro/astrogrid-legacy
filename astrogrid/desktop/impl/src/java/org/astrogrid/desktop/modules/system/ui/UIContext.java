/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import javax.swing.ButtonModel;

import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.acr.system.Configuration;
import org.astrogrid.acr.system.UI;
import org.astrogrid.desktop.modules.system.BackgroundExecutor;
import org.astrogrid.desktop.modules.system.HelpServerInternal;
import org.astrogrid.desktop.modules.ui.UIComponent;

import ca.odell.glazedlists.EventList;

/** central context object - used to store data common to all ui.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 20077:06:31 PM
 */
public interface UIContext  extends UI{

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
	
	
	
	
	// assistance for UI.
	// irritating. but need to put it somewhere.
	  public void showAboutDialog();   
	
	/** button model that indicates logged in status using 'enabled' property
	 * makes it simple to plug into a button and use it to flip between enabled / disabled icons
	 * @return
	 */
	public ButtonModel getLoggedInModel() ;
	/** indicates throbber status. same usage as {@link #getLoggedInModel} */
	public ButtonModel getThrobbingModel() ;
	/** indicates global visiblily of the UI. same usage as {@link #getLoggedInModel} */
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

}