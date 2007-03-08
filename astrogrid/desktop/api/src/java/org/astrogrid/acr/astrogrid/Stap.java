/*$Id: Stap.java,v 1.6 2007/03/08 17:46:56 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.net.URI;
import java.net.URL;
import java.util.Calendar;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ivoa.Dal;

/**Query for Images from Simple Time Access Protocol (STAP) services
 * @see http://software.astrogrid.org/schema/vo-resource-types/Stap/v0.1/Stap.xsd
 * @service astrogrid.stap
 * @since 1.4
 * @author Kevin Benson
 */
public interface Stap extends Dal  {
    
    /** construct query on START, END 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param start start date and time
     * @param end end date and time
     * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)
    */ 
    URL constructQuery(URI service,Calendar start, Calendar end) throws InvalidArgumentException, NotFoundException;

    /** construct query on START, DATE, FORMAT 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param start start date and time
     * @param end end date and time
     * @param format format of images or time series data (as described in stap spec)     * 
     * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
    URL constructQueryF(URI service,Calendar start, Calendar end, String format) throws InvalidArgumentException, NotFoundException;

    
    /** construct query on START, END RA, DEC, SIZE 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param start start date and time
     * @param end end date and time 
     * @param ra right ascension (as described in siap spec)
     * @param dec declination (as described in siap spec)
     * @param size radius of cone ( as described in siap spec)
     * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
    URL constructQuery(URI service, Calendar start, Calendar end, double ra, double dec, double size) throws InvalidArgumentException, NotFoundException;
   
    /** construct query on START, END, RA, DEC, SIZE, FORMAT 
     * @param service URL of the service endpoint, or ivorn of the service description
     * @param start start date and time
     * @param end end date and time
     * @param ra right ascension (as described in siap spec)
     * @param dec declination (as described in siap spec)
     * @param size radius of cone ( as described in siap spec)
     * @param format format of images or time series data (as described in stap spec)     *
     * @return query URL that can be fetched using a HTTP GET to execute query
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
     * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
   URL constructQueryF(URI service, Calendar start, Calendar end, double ra,double dec, double size, String format) throws InvalidArgumentException, NotFoundException;
   
   /** construct query on START, END, RA, DEC, RA_SIZE, DEC_SIZE
    * @param service URL of the service endpoint, or ivorn of the service description
    * @param start start date and time
    * @param end end date and time 
    * @param ra right ascension (as described in siap spec)
    * @param dec declination (as described in siap spec)
    * @param ra_size size of ra ( as described in siap spec)
    * @param dec_size size of dec (as described in siap spec)
    * @return query URL that can be fetched using a HTTP GET to execute query
    * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
    * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/   
   URL constructQueryS(URI service, Calendar start, Calendar end, double ra,double dec,double ra_size, double dec_size) throws InvalidArgumentException, NotFoundException;
   
   /** construct query on START, END, RA, DEC, RA_SIZE, DEC_SIZE, FORMAT
    * @param service URL of the service endpoint, or ivorn of the service description
    * @param start start date and time
    * @param end end date and time  
    * @param ra right ascension (as described in siap spec)
    * @param dec declination (as described in siap spec)
    * @param ra_size size of ra ( as described in siap spec)
    * @param dec_size size of dec (as described in siap spec)
    * @param format format of images or time series data (as described in stap spec)     * 
    * @return query URL that can be fetched using a HTTP GET to execute query
    * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// reference.
    * @throws NotFoundException if the service does not exist (i.e. cannot be resolved in registry)*/ 
   URL constructQuerySF(URI service, Calendar start, Calendar end, double ra, double dec, double ra_size, double dec_size, String format) throws InvalidArgumentException, NotFoundException;   
   
}


/* 
$Log: Stap.java,v $
Revision 1.6  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.5  2006/10/10 14:07:56  nw
upgraded the dal interfaces.

Revision 1.4  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.3  2006/03/21 10:49:33  nw
replaced & with 'and' - xml gets generated from the javadoc, and loose '&' make the build break

Revision 1.2  2006/03/16 09:14:21  KevinBenson
usually comment/clean up type changes such as siap to stap

Revision 1.1  2006/03/13 14:47:21  KevinBenson
This is the first rough draft of Helioscope which deals with the STAP spec.

 
*/