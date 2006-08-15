/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.ivoa.resource.RegistryService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.w3c.dom.Document;

/** Access  the system-configured  registry service.
 * 
 * ACR uses an IVOA-compliant registry to retreive details of available resources
 *  - servers, applications, catalogues, etc.
 *  
 *  The endpoint of this registry service can be inspected by calling {@link #getSystemRegistryEndpoint()}.
 *  In cases where this service is unavailable, registry queries will automatically fall-back to the
 *  backup registry service, whose endpoint is defined by {@link #getFallbackSystemRegistryEndpoint()}
 * 
 * The query functions in this interface are the equivalent to their counterparts in the 
 * {@link ExternalRegistry} interface, but against the System and Fallback registries.
 * 
 * 
 * @see <a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 * @service ivoa.registry
 * @see org.astrogrid.acr.ui.RegistryBrowser
 * @see org.astrogrid.acr.ivoa.ExternalRegistry - to query other IVOA registries, and the Registry of Registries.

 * @author Noel Winstanley
 * @since 2006.03
 */
public interface Registry {
    /** Perform an ADQL/x registry search, return a list of matching resources
     * @see ExternalRegistry#adqlxSearch*/
	Resource[] adqlxSearch(Document adqlx)  throws ServiceException, InvalidArgumentException;
  
	/** Perform an ADQL/s registry search, return a list of matching resources
     * @see ExternalRegistry#adqlsSearch*/
	Resource[] adqlsSearch(String adqls)  throws ServiceException, InvalidArgumentException;	
  
	/** Perform a keyword registry search, return a list of matching resources
     * @see ExternalRegistry#keywordSearch*/
    Resource[] keywordSearch(String keywords, boolean orValues)  throws ServiceException;
	
    /** Retrieve a resource by identifier
     * 
     * @see ExternalRegistry#getResource 
     */
    Resource getResource(URI id)  throws NotFoundException, ServiceException;

    /** Perform an xquery registry search, return a list of matching resources
     * @todo docuiment the namespaces prepended 
     * @see ExternalRegistry#xquerySearch*/
    Resource[] xquerySearch(String xquery)  throws ServiceException;
    
    /** Perform an xquery registry search, return a document
     * @todo docuiment the namespaces prepended 
     * @see ExternalRegistry#xquerySearchXML*/
    Document xquerySearchXML(String xquery)  throws ServiceException;
    
    /** Access the registry entry describing the system registry itself
     * @see ExternalRegistry#getIdentity
     */
	RegistryService getIdentity()  throws ServiceException; 
	
	/** gives the endpoint of the system registry */
	URI getSystemRegistryEndpoint();
	/** gives the endpoint of the fallback system registry */
	URI getFallbackSystemRegistryEndpoint();
}
