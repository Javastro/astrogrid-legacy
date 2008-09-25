/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Service;

/** Registry description of a CEA server.
 * @author Noel Winstanley
 * @see Applications Executing remote applications
 * @see Registry Querying for registry resources
 */
public interface CeaService extends Service {
	/** the capabilities of this CEA server
	 * 
	 * @return the {@link CeaServerCapability} object that describes what this CEA Server can do.
	 * This will be one of the items in the list of {@link Service#getCapabilities()} 
	 */
	public CeaServerCapability findCeaServerCapability();
}
