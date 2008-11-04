/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** Creates and populates an activities manager.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 26, 20073:19:58 PM
 */
public interface ActivityFactory {

	/**
	 * create a manager which provides access to selected activities.
	 * @param parent the ui parent to use for task reporting, background tasks, etc.
	 * @param activitynames the activities to instantiate and add to this manager.
	 * @return a manager that can be used as a model to control these components
	 */
	public ActivitiesManager create(UIComponent parent,Class[] activityClasses);
}