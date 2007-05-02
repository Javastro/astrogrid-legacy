/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import org.astrogrid.desktop.modules.ui.UIComponent;

/** populates widgets  with hivemind-defined menu structures
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 20072:15:36 PM
 */
public interface UIContributionBuilder {

	/**
	 * populat the widget with stuff
	 * @param current the object to populate
	 * @param comp the current object's parent
	 * @param name the name of this object - used to select correct set of menu structures from hivemind.
	 */
	public void populateWidget(Object current, UIComponent comp, String name);

}