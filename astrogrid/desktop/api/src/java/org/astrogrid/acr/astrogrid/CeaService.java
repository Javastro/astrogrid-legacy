/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.ivoa.resource.Service;

/** Registry record for a CEA server.
 * @author Noel Winstanley
 * @since Aug 5, 200610:15:52 PM
 */
public interface CeaService extends Service {
	/** access a descirption of the capabilities of this cea server
	 * 
	 * @return an item in the list of <tt>Service.getCapabilities()</tt> that describes what this CEA server can do. 
	 */
	public CeaServerCapability findCeaServerCapability();
}
