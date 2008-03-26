/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.AvailabilityBean;
import org.astrogrid.acr.ivoa.resource.Service;

/** Prototypical interface to Vosi Availability. May move into public AR interface once it stabilizes
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 26, 20085:09:58 PM
 */
public interface VosiAvailability {

    /** prompt the availability of a service.
     * service needs an availability capability, obviously.
     * @param s service to prompt for availabilty.
     * @return a bean if successfully queried, null if no availability capability is provided.     
     * @throws ServiceException if getting the availability failed.
     */
    VosiAvailabilityBean getAvailability(Service s) throws ServiceException;
}
