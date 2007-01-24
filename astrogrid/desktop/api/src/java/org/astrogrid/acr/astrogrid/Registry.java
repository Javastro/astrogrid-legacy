/*$Id: Registry.java,v 1.8 2007/01/24 14:04:44 nw Exp $
 * Created on 18-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;
import java.net.URL;

/** access the  AstroGrid registry.
 * 
 * Astrogrid use an IVOA-compliant registry to store details of available resources - servers, applications,
 * catalogues, etc.
 * 
 * Later, a generic interface to arbitrary IVOA registries will be added. This will allow other registries to be interrogated.
 * @see <a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 18-Mar-2005
 * @service astrogrid.registry
 * @see org.astrogrid.acr.ui.RegistryBrowser
 * @see org.astrogrid.acr.astrogrid.ResourceInformation
 * @deprecated use {@link org.astrogrid.acr.ivoa.Registry}
 */
public interface Registry {
    /**Resolve a registry identifier to an endpoint.
     * 
     * @param ivorn registry identifier for the registered service
     * @return a URL endpoint of this service
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @throws NotApplicableException if no endpoint is associated with this registry entry.
     * @example 
     * <pre>
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.astrogrid.Registry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * Registry reg = (Registry)acr.getService(Registry.class);
     * URI ivorn =new URI("ivo://uk.ac.le.star/filemanager");
     * URL endpoint = reg.resolveIdentifier(ivorn);
     * </pre>
     */
    URL resolveIdentifier(URI ivorn) throws NotFoundException, ServiceException, NotApplicableException;

    /**Retreive a record from the registry.
     * 
     * @param ivorn identifier of the registry entry to retrrieve
     * @return xml document of the registry entry - a <tt>Resource</tt> document i
     * in the <tt>http://www.ivoa.net/xml/VOResource/v1.0</tt> namespace
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a string containing the xml document
     * @example
     * <pre> 
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.astrogrid.Registry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * Registry reg = (Registry)acr.getService(Registry.class);
     * URI ivorn =new URI("ivo://uk.ac.le.star/filemanager");
     * Document d = reg.getRecord(ivorn);
     * </pre>
     */
    Document getRecord(URI ivorn) throws NotFoundException, ServiceException;

    /** Retrieve a record from the registry, returning it as a datastructure that contains the most commonly used parts.
     * 
     * For most uses, it's better to use this method instead of {@link #getRecord} as the result is easier to work with.
     * 
     * @param ivorn identifier of the registry entry to retrieve
     * @return a  summary of the most significant parts of the registry entry - will be a {@link ResourceInformation}
     * or a subclass - e.g. {@link ApplicationInformation} or {@link TabularDatabaseInformation} depending
     * on resource type
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a map. See {@link ResourceInformation} for details of keys
     */
    ResourceInformation getResourceInformation(URI ivorn) throws  NotFoundException, ServiceException;
    
    
    /** Perform a ADQL query.
     * 
     * @param adql a string query (string form of ADQL)
     * @return  xml document of search results -  a series of matching registry records contained within an element
     * called <tt>SearchResponse</tt> in the namespace <tt>http://www.astrogrid.org/registry/wsdl</tt>
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a string containing the xml document 
     * 
     * @example
     * <pre> 
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.astrogrid.Registry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * Registry reg = (Registry)acr.getService(Registry.class);
     * String query ="select * from Registry where vr:identifier='ivo://uk.ac.le.star/filemanager'"; 
     * Document d = reg.adqlSearch(query);
     * </pre>
     */ 
    Document adqlSearch(String adql) throws ServiceException;
    
    /**  Perform an ADQL query, returning a list of datastructures.
     * 
     * Equivalent of {@link #adqlSearch(String)} but returning a ist of datastructues that contain the most comonly used pats of the registry record. 
     * @see #adqlSearch(String) */
    ResourceInformation[] adqlSearchRI(String adql) throws NotFoundException, ServiceException;
    
    /** @deprecated - renamed to {@link #adqlSearch(String)} */
    Document searchForRecords(String adql) throws ServiceException;
    
    
    /** perform an XQuery 
     * @param xquery the query to perform.
     * @return the result of executing this xquery over the registry - a document of arbitrary structure.
     * If the result of executing this query is not a well-formed xml document itself (e.g. it's a list of nodes, or a
     * text result, it will be wrapped in a <tt>&lt;vor:resources&gt;</tt> element. The name of this element is liable change in 
     * later versions - so queries that return well-formed documents re recommended).
     * @throws ServiceException if there's a problem connecting to the registry
         * @example
         * <pre> 
     * import org.astrogrid.acr.*;
     * import java.net.URI;
     * import org.astrogrid.acr.astrogrid.Registry;
     * import org.astrogrid.acr.builtin.ACR
     * Finder f = new Finder();
     * ACR acr = f.find();
     * Registry reg = (Registry)acr.getService(Registry.class);
     * String query = "&lt;stores&gt;\n" + 
          "{\n" + 
          "for $x in //vor:Resource where $x/vr:identifier &= 'filemanager' \n" +  
          "return &lt;service curator="{$x/vr:curation/vr:contact/vr:name}"&gt;{data($x/vr:identifier)}&lt;/service&gt;\n" + 
          "}\n" + 
          "&lt;/storesgt;"   
     * Document d = reg.xquerySearch(query)
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
    Document xquerySearch(String xquery) throws ServiceException;
    
    /** List the namespaces that are used to qualify adql and xquery searches.
     * 
     * These namepsaces are automatically prepended to queries submitted, which simplifies query entry.
     * 
     * @return an arry of 2-element arrays. the first in each pair is the namespace name used within the queries., while the second is the uri that defines this namespace
     * @since 1.3
     */
    String[][] listNamespaces();
    
    /** perform a keyword search
     * 
     * @param keywords list of keywords to search for
     * @param orValues - true to 'OR' together matches. false to 'AND' together matches
     * @return xml document of search results, same format as result of {@link #adqlSearch(String)}
     * @throws ServiceException
     */
    Document keywordSearch(String keywords,boolean orValues) throws ServiceException;
    
    /** Perform a keyword search and return a list of datastructures.
     * 
     *  
     *  Equivalent of {@link #keywordSearch(String, boolean)} but more convenient to use. */
    ResourceInformation[] keywordSearchRI(String keywords, boolean orValues) throws ServiceException;
    
   }

/* 
 $Log: Registry.java,v $
 Revision 1.8  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.7  2006/08/15 09:48:55  nw
 added new registry interface, and bean objects returned by it.

 Revision 1.6  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.5  2005/11/24 01:18:42  nw
 merged in final changes from release branch.

 Revision 1.4.10.1  2005/11/23 04:32:36  nw
 added new method

 Revision 1.4  2005/09/12 15:21:43  nw
 added stuff for adql.

 Revision 1.3  2005/08/25 16:59:44  nw
 1.1-beta-3

 Revision 1.2  2005/08/12 08:45:16  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

 Revision 1.6  2005/08/09 17:33:07  nw
 finished system tests for ag components.

 Revision 1.5  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.4  2005/05/12 15:59:09  clq2
 nww 1111 again

 Revision 1.2.8.1  2005/05/11 09:52:45  nw
 exposed new registry methods

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */