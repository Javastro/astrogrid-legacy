/*$Id: Ssap.java,v 1.6 2007/03/08 17:48:06 nw Exp $
 * Created on 26-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;

/** Querying for Spectra from Simple Spectral Access Protool (SSAP) Services.
 * <b>NB:</b> working, but based on unfinished IVOA specification - interface may need to change to follow specificaiton.
 * @service ivoa.ssap
 * @since 2.0
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 26-Jan-2006
 *
 */
public interface Ssap extends Dal {
    
    /** construct query on RA, DEC, SIZE 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param ra right ascension (as described in ssap spec)
     * @param dec declination (as described in ssap spec)
     * @param size radius of cone ( as described in ssap spec)
     * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
    URL constructQuery(URI service,double ra, double dec, double size) throws InvalidArgumentException, NotFoundException;

    /** construct query on RA, DEC, RA_SIZE, DEC_SIZE
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param ra right ascension (as described in ssap spec)
     * @param dec declination (as described in ssap spec)
     * @param ra_size size of ra ( as described in ssap spec)
     * @param dec_size size of dec (as described in ssap spec)
      * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/   
    URL constructQueryS(URI service, double ra,double dec,double ra_size, double dec_size) throws InvalidArgumentException, NotFoundException;
    
}


/* 
$Log: Ssap.java,v $
Revision 1.6  2007/03/08 17:48:06  nw
tidied.

Revision 1.5  2007/01/24 14:04:45  nw
updated my email address

Revision 1.4  2006/10/10 14:07:44  nw
upgraded the dal interfaces.

Revision 1.3  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.2  2006/02/24 12:17:52  nw
added interfaces for skynode

Revision 1.1  2006/02/02 14:19:47  nw
fixed up documentation.
 
*/