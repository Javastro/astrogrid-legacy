/*$Id: Siap.java,v 1.1 2005/10/18 07:58:21 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;
import java.net.URL;

/** Interface for querying Simple Image Access Protocol (SIAP)Services
 * @see http://www.ivoa.net/Documents/latest/SIA.html 
 * @service ivoa.siap
 * @since 1.3
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 */
public interface Siap {
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
   
   /** add an option to a previously constructed query
    * 
    * @param query the query url
    * @param optionName name of the option to add
    * @param optionValue value for the new option
    * @return <tt>query</tt> with the option appended.
 * @throws InvalidArgumentException if the parameter cannot be added.
    * @see #constructQuery
    */
   URL addOption(URL query, String optionName, String optionValue) throws InvalidArgumentException;
      
    
   /** execute a SIAP query
    * <p>
    * convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
 * @param siapQuery query url to execute
 * @return a votable of results
 * @throws ServiceException if an error occurs while communicating with the SIAP service
    * 
    */
   Document getResults(URL siapQuery) throws ServiceException;
   
   /**execute a SIAP query and save the results
 * @param siapQuery query url to execute
 * @param saveLocation location to save result document - may be file:/, ivo:// (myspace), ftp://
 * @throws SecurityException if the user is not permitted to write to the save location
 * @throws ServiceException if an error occurs while communicating with the siap service
 * @throws InvalidArgumentException if the save location cannot be written to
 */
void saveResults(URL siapQuery, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
   
   /** helper method - returns an ADQL query that should be passed to a registry to list all 
    * available siap services 
    * <p>
    * can be used as a starting point for filters, etc.
    * @return an adql query string
    */
   String getRegistryQuery();
   
   
   
}


/* 
$Log: Siap.java,v $
Revision 1.1  2005/10/18 07:58:21  nw
added first DAL interfaces
 
*/