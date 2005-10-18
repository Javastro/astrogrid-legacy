/*$Id: Cone.java,v 1.1 2005/10/18 07:58:21 nw Exp $
 * Created on 17-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.nvo;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;
import java.net.URL;

/** Interface for querying Cone-search services
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Oct-2005
 * @service nvo.cone
 * @since 1.3
 * 
 */
public interface Cone {
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
    
    /** Add an option to a previously constructed query
     * 
     * @param coneQuery the query url
     * @param optionName the name of the option to add
     * @param optionValue value for the new option
     * @return <tt>query</tt> with the option appended.
     * @throws InvalidArgumentException if the option could not be added
     */
    URL addOption(URL coneQuery, String optionName, String optionValue) throws InvalidArgumentException;
    
    /** execute a cone query
     * <p>
     * Convenience method - just performs a 'GET' on the query URL - many programming languages
     * support this themselves
     * @param coneQuery query url to execute
     * @return a votable of results
     * @throws ServiceException if an error occurs while communicating with the cone service
     */
    Document getResults(URL coneQuery) throws ServiceException;
    
    /** execute a cone query and save the results
     * 
     * @param coneQuery the query url to execute
     * @param saveLocation location to save result document - may be file://, ivo:// (myspace), ftp://
     * @throws SecurityException if the user is not permitted to write to the save location
     * @throws ServiceException if an error occurs while communicating with the siap service
     * @throws InvalidArgumentException if the save location cannot be written to.
     */
    void saveResults(URL coneQuery, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
    
    /** helper method - returns an ADQL query that should be passed to a registry to list all available 
     * siap services
     * <p>
     * can be used as a starting point for building filters, etc
     * @return an adql query string
     */
    String getRegistryQuery();
}


/* 
$Log: Cone.java,v $
Revision 1.1  2005/10/18 07:58:21  nw
added first DAL interfaces
 
*/