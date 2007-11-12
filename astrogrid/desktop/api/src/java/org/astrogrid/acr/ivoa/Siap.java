/*$Id: Siap.java,v 1.8 2007/11/12 13:36:27 pah Exp $
 * Created on 17-Oct-2005
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
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;

/**Query for Images from Simple Image Access Protocol (SIAP) services
 * @see http://www.ivoa.net/Documents/latest/SIA.html 
 * @service ivoa.siap
 * @since 1.3
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 */
public interface Siap extends Dal {
    /** construct query on RA, DEC, SIZE 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param ra right ascension (as described in siap spec)
     * @param dec declination (as described in siap spec)
     * @param size radius of cone ( as described in siap spec)
     * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
    URL constructQuery(URI service,double ra, double dec, double size) throws InvalidArgumentException, NotFoundException;
   
    /** construct query on RA, DEC, SIZE, FORMAT 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param ra right ascension (as described in siap spec)
     * @param dec declination (as described in siap spec)
     * @param size radius of cone ( as described in siap spec)
     * @param format format of images (as described in siap spec)
     * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
   URL constructQueryF(URI service, double ra,double dec,double size, String format) throws InvalidArgumentException, NotFoundException;
   
   
   /** construct query on RA, DEC, RA_SIZE, DEC_SIZE
    * @param service URL of the service endpoint, or ivorn of the service description
    * @param ra right ascension (as described in siap spec)
    * @param dec declination (as described in siap spec)
    * @param ra_size size of ra ( as described in siap spec)
    * @param dec_size size of dec (as described in siap spec)
     * @return query URL that can be fetched using a HTTP GET to execute query
    * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
    * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/   
   URL constructQueryS(URI service, double ra,double dec,double ra_size, double dec_size) throws InvalidArgumentException, NotFoundException;
   
   /** construct query on RA, DEC, RA_SIZE, DEC_SIZE, FORMAT
    * @param service URL of the service endpoint, or ivorn of the service description
    * @param ra right ascension (as described in siap spec)
    * @param dec declination (as described in siap spec)
    * @param ra_size size of ra ( as described in siap spec)
    * @param dec_size size of dec (as described in siap spec)
     * @param format format of images (as described in siap spec) 
     * @return query URL that can be fetched using a HTTP GET to execute query
    * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
    * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
   URL constructQuerySF(URI service, double ra, double dec, double ra_size, double dec_size, String format) throws InvalidArgumentException, NotFoundException;
   
   /** Extends function in DAL interface so that keys that  are SIAP-defined UCDs
    * are replaced by the equivalent descriptor in the SIA dataset data model.
    * 
    * 
    * {@inheritDoc}*/
public Map[] execute(URL query) throws ServiceException;
   /* FIXME - this is a redefinition that simply changes the documentation  - causes problems for the C code generation. */
   
   
}


/* 
$Log: Siap.java,v $
Revision 1.8  2007/11/12 13:36:27  pah
change parameter name to structure

Revision 1.7  2007/03/08 17:48:06  nw
tidied.

Revision 1.6  2007/01/24 14:04:45  nw
updated my email address

Revision 1.5  2006/10/10 14:07:44  nw
upgraded the dal interfaces.

Revision 1.4  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.3  2006/02/24 12:17:52  nw
added interfaces for skynode

Revision 1.2  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.1  2005/10/18 07:58:21  nw
added first DAL interfaces
 
*/