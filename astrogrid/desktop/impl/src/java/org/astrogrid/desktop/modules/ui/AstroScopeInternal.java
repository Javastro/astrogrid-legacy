/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import java.util.List;

import org.apache.commons.collections.Factory;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ui.AstroScope;

/** Internal interface to additional funcitonality for astroscope.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 27, 20071:36:00 PM
 */
public interface AstroScopeInternal extends AstroScope , Factory{
	/** run astroscope against a specified list of resources */
	public void runSubset(List<? extends Resource> resources);
	
	   /** run astroscope against a specified list of resources, mark it as 'helioscope'  - sheesh. */
    public void runSubsetAsHelioscope(List<? extends Resource> resources);

}
