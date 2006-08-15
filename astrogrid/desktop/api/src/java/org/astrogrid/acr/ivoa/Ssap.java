/*$Id: Ssap.java,v 1.3 2006/08/15 09:48:55 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;
import java.net.URL;

/** Querying for Spectra from Simple Spectral Access Protool (SSAP) Services.
 * <b>NB:</b> working, but based on unfinished IVOA specification - liable to change to follow specificaiton.
 * @see - url
 * @service ivoa.ssap
 * @since 2.0
 * @author Noel Winstanley nw@jb.man.ac.uk 26-Jan-2006
 *
 */
public interface Ssap {
    
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
       
     
    /**Eexecute a SSAP query.
     * 
     * convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
  * @param ssapQuery query url to execute
  * @return a votable of results
  * @throws ServiceException if an error occurs while communicating with the SIAP service
     * 
     */
    Document getResults(URL ssapQuery) throws ServiceException;
    
    /**execute a SSAP query and save the results.
  * @param ssapQuery query url to execute
  * @param saveLocation location to save result document - may be file:/, ivo:// (myspace), ftp://
  * @throws SecurityException if the user is not permitted to write to the save location
  * @throws ServiceException if an error occurs while communicating with the siap service
  * @throws InvalidArgumentException if the save location cannot be written to
  */
 void saveResults(URL ssapQuery, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
    
    /** helper method - returns an ADQL/s query that should be passed to a registry to list all 
     * available siap services. 
     *
     * Can be used as a starting point for filters, etc.
     * @return an adql query string
     * @deprecated use getRegistryAdqlQuery();
     */
    String getRegistryQuery();
    
    /** helper method - returns an ADQL/s query that should be passed to a registry to list all 
     * available siap services. 
     *
     * Can be used as a starting point for filters, etc.
     * @return an adql query string
     * @since 2006.03
     */
    String getRegistryAdqlQuery();
    
    /** helper method - returns an Xquery that should be passed to a registry to list all 
     * available siap services. 
     *
     * Can be used as a starting point for filters, etc.
     * @return an Xquery string
     * @since 2006.03
     */
    String getRegistryXQuery();
  
}


/* 
$Log: Ssap.java,v $
Revision 1.3  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.2  2006/02/24 12:17:52  nw
added interfaces for skynode

Revision 1.1  2006/02/02 14:19:47  nw
fixed up documentation.
 
*/