/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

import org.astrogrid.acr.ivoa.Siap;

/** An Image Access Service (SIAP)
 * @see Siap
 * @author Noel Winstanley
 */
public interface SiapService extends Service{
	/** returns the capability that describes this siap service 
    * @return one of the items within {@link #getCapabilities()}
    * */
	public SiapCapability findSiapCapability();
}
