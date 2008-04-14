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

/** Query an arbitrary registry service.
 * 
 * This interface gives access to a range of querying functions - for querying using xQuery, 
 * keyword, adlq/s and adql/x. The functions either return a raw XML document, or a series of
 * datastructures that contain the parsed information of the registry entries.s
 * 
 * The first parameter to each query method is the endpoint URL of the registry service to connect to.
 these functions will also accept the IVORN name of a registry - which 
 * will then be resolved using the System Registry  before processing the query.
 *
 *These functions are useful when you want to access records in a registry
 * other than the 'system configured' registry,
 * or if you wish to access the raw xml of the records.
 * For other cases, we recommend using the simple 'ivoa.Registry' service.
 * 
 * @see <a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 18-Mar-2005
 * @service ivoa.externalRegistry
 * @see org.astrogrid.acr.ui.RegistryBrowser
 * @see org.astrogrid.acr.ivoa.Registry - queries the system-configured registry - suitable for most cases.
 * @author Noel Winstanley
 * @since 2006.03
 */
public interface ExternalRegistry {
	
	/** Perform an ADQL/x query
	 * 
	 * Equivalent to  {@link #adqlsSearchXML} but expects the full xml form of ADQL - this is less
	 * error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
	 */
	Document adqlxSearchXML(URI registry, Document adqlx, boolean identifiersOnly)  
		throws ServiceException, InvalidArgumentException;
	/** Perform an ADQL/x query, returning an array of datastructures.
	 * 
	 * Equivalent to  {@link #adqlsSearch} but expects the full xml form of ADQL - which is less
	 * error prone than the adql/s variant until someone defines adql/s properly and implements parsers for it.
	 */
	Resource[] adqlxSearch(URI registry,Document adqlx)  throws ServiceException, InvalidArgumentException;
	
    /** Perform a ADQL/s query.
     * Although convenient, prefer xquerySearch instead - as ADQL is less expressive and more poorly (especially adql/s) defined than xquery
      * @param registry identifier or endpoint of the registry to connect to    
     * @param adql a string query (string form of ADQL)
     * @return  xml document of search results -  a series of matching registry records contained within an element
     * called <tt>VOResources</tt> in the namespace <tt>http://www.ivoa.net/wsdl/RegistrySearch/v1.0</tt>
     * @throws ServiceException if an error occurs talking to the service
     * @throws InvalidArgumentException if the query is invalid in some way.
     * @xmlrpc will return a string containing the xml document 
     * 
     * @example
     * <pre> 
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.ivoa.ExternalRegistry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * ExternalRegistry reg = (ExternalRegistry)acr.getService(ExternalRegistry.class);
     * String query ="select * from Registry where vr:identifier='ivo://uk.ac.le.star/filemanager'"; 
     * Document d = reg.adqlsSearchXML(query);
     * </pre>
     */
	Document adqlsSearchXML(URI registry,String adqls, boolean identifiersOnly)  throws ServiceException, InvalidArgumentException;

	/** Perform an ADQL/s query, returning an array of datastructures.
	 * 
	 * Equivalent to {@link #adqlsSearchXML} but returning results in form that can be more easily used. 
	 */
	Resource[] adqlsSearch(URI registry,String adqls)  throws ServiceException, InvalidArgumentException;	
    /** perform a keyword search
     * 	@param registry identifier or endpoint of the registry to connect to
     * @param keywords space separated list of keywords to search for
     * @param orValues - true to 'OR' together matches. false to 'AND' together matches
     * @param identifiersOnly - true to just return a list of identifiers whose resources match. False to return 
     * full registry resource documents.
     * @return xml document of search results, same format as result of {@link #adqlSearchXML}
     * @throws ServiceException
     */
	Document keywordSearchXML(URI registry,String keywords, boolean orValues)  throws ServiceException;
	
	/** Perform a keyword search and return a list of datastructures.
	A more convenient variant of {@link #keywordSearchXML}
	 */
	Resource[] keywordSearch(URI registry,String keywords, boolean orValues)  throws ServiceException;
    /**Retreive a record document from the registry
     * 	@param registry identifier or endpoint of the registry to connect to
     * @param id identifier of the registry entry to retrrieve
     * @return xml document of the registry entry - a <tt>Resource</tt> document 
     * probably in the <tt>http://www.ivoa.net/xml/VOResource/v1.0</tt> namespace
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a string containing the xml document
     * @example
     * <pre> 
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.ivoa.ExternalRegistry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * ExternalRegistry reg = (ExternalRegistry)acr.getService(ExternalRegistry.class);
     * URI regEndpoint = new URI("http://www.my.registry.endpoint");
     * URI ivorn =new URI("ivo://uk.ac.le.star/filemanager");
     * Document d = reg.getResourceXML(ivorn);
     * </pre>
     */
	Document getResourceXML(URI registry,URI id) throws NotFoundException, ServiceException;
	
	
	  /** Retrieve a record from the registry, returning it as a datastructure
     * 
     * For most uses, it's better to use this method instead of {@link #getResourceXML} as the result is easier to work with.
     * 	@param registry identifier or endpoint of the registry to connect to
     * @param id identifier of the registry entry to retrieve
     * @return a  datastructue representing the registry entry - will be a {@link Resource} or one of it's 
     * subclasses depending on the registry entry type.
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a struct. See {@link Resource} for details of keys
     */
	Resource getResource(URI registry,URI id) throws NotFoundException, ServiceException;
	
    /** perform an XQuery 
	 * @param registry identifier or endpoint of the registry to connect to
     * @param xquery the query to perform. Must return a well-formed xml document - i.e. starting with a single root element.
     * @return the result of executing this xquery over the specified registry - a document of arbitrary structure.
         * @throws ServiceException if there's a problem connecting to the registry
    	 * @xmlrpc will return the string representation of the xml document.
         * @example
         * <pre> 
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.ivoa.ExternalRegistry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * ExternalRegistry reg = (ExternalRegistry)acr.getService(ExternalRegistry.class);
     * String query = "&lt;stores&gt;\n" + 
          "{\n" + 
          "for $x in //vor:Resource where $x/vr:identifier &= 'filemanager' \n" +  
          "return &lt;service curator="{$x/vr:curation/vr:contact/vr:name}"&gt;{data($x/vr:identifier)}&lt;/service&gt;\n" + 
          "}\n" + 
          "&lt;/storesgt;"   
     * URI regEndpoint = new URI("http://www.my.registry.endpoint");
     * Document d = reg.xquerySearchXML(regEndpoint,query)
     *</pre>
     *will return the follwing result document
     *<pre>      
&lt;stores&gt;
  &lt;service curator ="Tony Linde"&gt;ivo://uk.ac.le.star/filemanager&lt;/ service&gt;
  &lt;service curator ="Matthew Wild"&gt;ivo://uk.ac.ral.ukssdc/astrogrid-filemanager&lt;/ service&gt;     
  &lt;service curator ="KMB"&gt;ivo://mssl.ucl.ac.uk/filemanage&lt;/ service&gt;   
  &lt;service curator ="ElizabethAuden"&gt;ivo://esdo.mssl.ucl.ac.uk/esdo-filemanager&lt;/ service&gt;    
  &lt;service curator ="Sergey Stupnikov"&gt;ivo://ipi.ac.ru/filemanager&lt;/ service&gt;   
&lt;/stores&gt;
     </pre>
     */
	Document xquerySearchXML(URI registry,String xquery)  throws ServiceException;
	
	/** Variant of xquerySearchXML that returns registry records as data structures
	 * 
	 * @param registry endpoint of registry service to connect to.
	 * @param xquery should return a document, or nodeset, containing &lt;vor:Resource&gt; elements. 
	 * Results are not required to be single-rooted, and resource elements may be embedded within other elements.
	 * @return an array containing any registry records present in the query result.
	 * @xmlrpc will return an array of  struct. See (@link {@link Resource} for details of keys	
	 * @throws ServiceException
	 */
	Resource[] xquerySearch(URI registry,String xquery)  throws ServiceException;
	
	/** Retreive a a description of this registry, returning it asan xml document
	 * 
	 * @param registry identifier or endpoint of the registry to connect to
	 * @return that registries own service description - a single Resource documnt
	 * @throws ServiceException
	 * @xmlrpc will return the string representation of the xml document.
	 */
	Document getIdentityXML(URI registry)  throws ServiceException;
	/** Retreive a a description of this registry, returning it as a datastructure
	 * 
	 * @param registry identifier or endpoint of the registry to connect to
	 * @return that registries own service description - a single ResourceDocument
	 * @throws ServiceException
	 * @xmlrpc will return a struct. See (@link {@link Resource} for details of keys
	 */
	RegistryService getIdentity(URI registry)  throws ServiceException; 
	
	/** convenience function - build an array of resouce objects from an xml document */
	Resource[] buildResources(Document doc)  throws ServiceException;

	/**
	 * @deprecated registry of registries does not provide a search interface
	 * @return always null
	 */
	URI getRegistryOfRegistriesEndpoint();

}
