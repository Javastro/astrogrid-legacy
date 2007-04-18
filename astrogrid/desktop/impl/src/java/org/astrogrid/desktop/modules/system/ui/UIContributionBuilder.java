/**
 * 
 */
package org.astrogrid.desktop.modules.system.ui;

import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 10, 20072:15:36 PM
 */
public interface UIContributionBuilder {

	/** recursive part of building menu and tab structure */
	public void populateWidget(Object current, UIComponent comp, String name);

}