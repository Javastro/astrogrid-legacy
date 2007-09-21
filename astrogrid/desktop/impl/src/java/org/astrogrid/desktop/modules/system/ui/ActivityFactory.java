/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** populates widgets with hivemind-defined action structures.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 26, 20073:19:58 PM
 */
public interface ActivityFactory {
	/**
	 * create a manage which provies access to all defined activites
	 * through components controlled accroding to the flags.
	 * @param parent
	 * @param wantPopup if true, will create a popup menu. if false, then {@link ActivitiesManager#getPopupMenu()} will be null
	 * @param wantMenu  if true, will create a menu. if false, then {@link ActivitiesManager#getMenu()} will be null
	 * @param wantTaskPane  if true, will create a taskPanel. if false, then {@link ActivitiesManager#getTaskPane()} will be null
	 * @return a manager that can be used as a model to control these components.
	 */
	public ActivitiesManager create(UIComponent parent, boolean wantPopup, boolean wantMenu, boolean wantTaskPane);
		
	/**
	 * create a manager which provides access to all defined activities
	 * through all three kinds of widget - menu, popup and taskPane.
	 * @param parent the ui parent to use for task reporting, background tasks, etc.
	 * @return a manager that can be used as a model to control these components
	 */
	public ActivitiesManager create(UIComponent parent);
}