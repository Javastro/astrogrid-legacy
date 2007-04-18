/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.List;

import org.astrogrid.acr.ui.HelioScope;

/** Internal interface to additional funcitonality for helioscope
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 27, 20071:36:00 PM
 */
public interface HelioScopeInternal extends HelioScope {
	/** run astroscope against a specified list of resources */
	public void runSubset(List resources);

}
