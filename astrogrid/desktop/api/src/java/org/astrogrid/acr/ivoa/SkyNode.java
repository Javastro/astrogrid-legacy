/*$Id: SkyNode.java,v 1.4 2006/08/31 20:22:13 nw Exp $
 * Created on 21-Feb-2006
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
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.TabularDatabaseInformation;

import org.w3c.dom.Document;

import java.net.URI;

/** Query for data from SkyNode services 
 * @see http://www.ivoa.net/Documents/latest/SkyNodeInterface.html
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2006
 * @since 2006.03
 * @service ivoa.skyNode
 *
 */

public interface SkyNode {

    /** helper method - returns an adql query that should be passed to a registry to list all known skynode services
     * 
     * @return an adql query string
     */
    public String getRegistryAdqlQuery();
    
    
    /** returns an xquery that should be passed to a registry to list all known skynode services
     * @return an xquery string
     */
    public String getRegistryXQuery();
    
  // basic - tables, columns, column, formas, functions, perform query
    /**
     * interrogate skynode for  complete metadata about it's database
     * @param service identifier of the service to retrieve metadata for
     * @return  a list of will use the tablebeans - actually, {@link SkyNodeTableBean} and 
     * {@link SkyNodeColumnBean}  subclasses that present additional metadata
     * @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.
     */
    public SkyNodeTableBean[] getMetadata(URI service) throws InvalidArgumentException, NotFoundException, ServiceException;
    
    /** interrogate skynode for supported output formats 
     * @param service identifier of the service to retrieve metadata for
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.     
     * 
     * */
    public String[] getFormats(URI service) throws InvalidArgumentException, NotFoundException, ServiceException;
    
    /** interrogate skynode for the functions it supports 
  * @param service identifier of the service to retrieve metadata for
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.     
     *      
     * */
    public FunctionBean[] getFunctions(URI service)throws InvalidArgumentException, NotFoundException, ServiceException;
    
    /** execute an adql query
     * 
     * @param service identifier of the service to execute query on
     * @param adqlx the query to execute
     * @return a document containing a votable
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier, or <tt>adqlx</tt> is not a valid ADQL query
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.     
     *        
     */
    public Document getResults(URI service, Document adqlx) throws InvalidArgumentException, NotFoundException, ServiceException;
    
    
    /** execute an adql query, saving results to specified location
     * 
     * @param service identifier of the service to execute query on
     * @param adqlx the query to execute
     * @param saveLocation location to save result document - may be file:/, ivo:// (myspace), ftp://
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier, or if <tt>saveLocation</tt> is not a valid save location, 
* or <tt>adqlx</tt> is not a valid ADQL query
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.     
     * @throws SecurityException if the user is not permitted to write to the save location.
     *        
     */    
    public void saveResults(URI service, Document adqlx, URI saveLocation) throws InvalidArgumentException, NotFoundException, ServiceException, SecurityException;
    
    /** execute an adql query, specifying required output format
     * 
     * @param service identifier of the service to execute query on     
     * @param adqlx the query to execute
     * @param format required format for results (one of the results returned from {@link #getFormats()}
     * @return a string of results @todo consider whether byte[] is a safer bet here. 
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier, or <tt>adqlx</tt> is not a valid ADQL query
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.       
     */
    public String  getResultsF(URI service,Document adqlx,String format) throws InvalidArgumentException, NotFoundException, ServiceException;
    
    /** execute an adql query, saving results to specified location, specifying required output format.
     * 
     * @param service identifier of the service to execute query on
     * @param adqlx the query to execute
     * @param saveLocation location to save result document - may be file:/, ivo:// (myspace), ftp://
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier, or if <tt>saveLocation</tt> is not a valid save location, 
*   or <tt>adqlx</tt> is not a valid ADQL query
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.     
     * @throws SecurityException if the user is not permitted to write to the save location.
     *        
     */    
    public void saveResultsF(URI service, Document adqlx, URI saveLocation, String format) throws InvalidArgumentException, NotFoundException, ServiceException, SecurityException;
        
    /** query the server's footprint for this region (FULL Skynode only)
     *      * @param service identifier of the service to interrogate
     *      * @param region a STC document describing a region
     * @return another STC document describing the intersection between the parameter <tt>region</tt> and the holdings of this skynode
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier, or <tt>region</tt> is not a valid STC document
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.         
     */
    public Document getFootprint(URI service, Document region) throws InvalidArgumentException, NotFoundException, ServiceException;

    /** interrogate the service to estimate the cost of a query (FULL Skynode only)
     * @param planId not known @todo
     * @param adql query to estimate cost for.
     * @return estimation of query cost
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier, or <tt>adql</tt> is not a valid ADQL query
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.          *      
     */
    public float estimateQueryCost(long planId, Document adql) throws InvalidArgumentException, NotFoundException, ServiceException;
    
    /** execute a query plan (FULL Skynode only)
     * @todo find out what are the best types for parameters - e.g. is there any document formats?
     */
    //public Document executePlan
    
    /** interrogate server for system information 
     @param service identifier of the service to interrogate
     * @return availability information for this server
* @throws InvalidArgumentException if <tt>service</tt> is not a http:// or ivo:// identifier
     * @throws NotFoundException of the service does not exist (i.e. cannot be resolved in registry) ,
     * @throws ServiceException if the service cannot be connected to, or fails in some way.          * 
     * */
    public AvailabilityBean getAvailability(URI service) throws InvalidArgumentException, NotFoundException, ServiceException;
        
    
}


/* 
$Log: SkyNode.java,v $
Revision 1.4  2006/08/31 20:22:13  nw
doc fix.

Revision 1.3  2006/08/15 09:48:55  nw
added new registry interface, and bean objects returned by it.

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/