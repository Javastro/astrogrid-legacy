/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.ivoa.resource.Service;

/**
 * @author Noel Winstanley
 * @since Aug 5, 200610:15:52 PM
 */
public interface CeaService extends Service {
	public CeaServerCapability findCeaServerCapability();
}
