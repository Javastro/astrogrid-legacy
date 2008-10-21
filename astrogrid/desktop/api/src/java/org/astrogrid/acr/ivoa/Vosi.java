/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;

/** 
 * Check availability of services.
 * 
 * <p/>
 * There's an upcoming standard IVOA standard for VOSI (VO Support Interfaces), which
 * defines web-service methods that all services participating in the VO <i>should</i> implement.
 * <p />
 * The most useful of these interfaces is <b>Availability</b>, which allows a client to check whether a service 
 * is functional, and be notified of any forthcoming service interruptions.
 * 
 * <p />
 * This component provides availability information for services that support v0.4 of the VOSI standard,
 * and will also make a best effort to provide basic availability information for services that support earlier 
 * versions of the VOSI standard (e.g. those that register a {@code ivo://org.astrogrid/std/VOSI/v0.3#availability} capability)  
 * @see <a href='http://www.ivoa.net/cgi-bin/twiki/bin/view/IVOA/IvoaGridAndWebServices#Internal_Drafts'>VOSI Draft Specification</a>
 * @author Noel.Winstanley@manchester.ac.uk
 * @service ivoa.vosi
 * @since 1.3.0
 */
public interface Vosi {

    /** Check the availability of a service.
     * 
     * @param serviceID Resource ID of the service to query. The service have a VOSI Availability capability.
     * @return a description of the service's availability. If the service fails to 
     * respond to the availabilitiy check, a bean will be returned which has {@code available=false}, which contains a description
     * of the problem that was encountered.     
     * @throws InvalidArgumentException if {@code serviceID} is not a service that provides a VOSI capability     
     */
    VosiAvailabilityBean checkAvailability(URI serviceID) throws InvalidArgumentException;
}
