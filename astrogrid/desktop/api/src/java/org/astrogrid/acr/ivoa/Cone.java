/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;

/** Query catalogs using Cone-search services.
 * @author Noel Winstanley
 * @service ivoa.cone
 * @since 2006.3.rc4
 */
public interface Cone extends Dal {
	 /** construct a query on RA, DEC, SR
     * 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param ra right ascension
     * @param dec declination
     * @param sr search radius
     * @return query URL that can be fetched using a HTTP GET to execute the query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in reg)
     */
    URL constructQuery(URI service, double ra, double dec, double sr) throws InvalidArgumentException, NotFoundException;
 
}
