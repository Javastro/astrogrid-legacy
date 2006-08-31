/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** Registrion description for a Simple Image Access Service.
 * @author Noel Winstanley
 * @since Aug 5, 200610:26:36 PM
 */
public interface SiapService extends Service{
	/** returns the capability that describes this siap service */
	public SiapCapability findSiapCapability();
}
