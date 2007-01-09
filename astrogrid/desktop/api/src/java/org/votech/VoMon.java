/**
 * 
 */
package org.votech;

import java.net.URI;

import org.astrogrid.acr.ServiceException;

/** Monitor service availability using the VoMon service
 * @see vomon.sourceforge.net
 * @author Noel Winstanley
 * @since Dec 11, 20063:49:22 PM
 * @service votech.vomon
 */
public interface VoMon {
	/** forces the status information to be reloaded from the vomon server 
	 * - potentially expensive operation*/
	public void reload() throws ServiceException;
	
	/** check the availability of a service 
	 * 
	 * @param id registry id of the service
	 * @return a monitor bean describing this service's availability, or null if this
	 * service is not known
	 */
	public VoMonBean checkAvailability(URI id);
	
	
	/** check the availability of a cea application
	 * 
	 * @param id registry id of the application
	 * @return an array of monitoring beans, one for each server that
	 * states it provides this application. May be null if the application is unknown, 
	 * i.e if no servers provide this application.
	 */
	public VoMonBean[] checkCeaAvailability(URI id);
}
