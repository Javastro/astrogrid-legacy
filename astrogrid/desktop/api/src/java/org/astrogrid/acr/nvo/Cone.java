/*$Id: Cone.java,v 1.8 2008/09/25 16:02:10 nw Exp $
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

import java.net.URI;
import java.net.URL;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.w3c.dom.Document;

/** Query  catalogs using Cone-search services
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Oct-2005
 * @service nvo.cone

 * @exclude
 *  @deprecated use the ivoa.cone interface instead.
 * 
 */
@Deprecated
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
    
    /** execute a cone query.
     * 
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
    
    /** helper method - returns an ADQL/s query that should be passed to a registry to list all available cone services.
     *
     * Can be used as a starting point for building filters, etc
     * @return an adql query string
     * @exclude @deprecated use getRegistryAdqlQuery()
     */
    String getRegistryQuery();
    
    /** returns an ADQL/s query that should be passed to a registry to list all available cone services
     */
    String getRegistryAdqlQuery();
    /** returns an xquery that should be passed to a registry to list all available cone services 
    */
    String getRegistryXQuery();
}


/* 
$Log: Cone.java,v $
Revision 1.8  2008/09/25 16:02:10  nw
documentation overhaul

Revision 1.7  2007/03/20 09:57:09  nw
revived nvo cone for now.

Revision 1.5  2007/01/24 14:04:46  nw
updated my email address

Revision 1.4  2006/10/10 14:06:59  nw
deprecated this interface.

Revision 1.3  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/10/18 07:58:21  nw
added first DAL interfaces
 
*/