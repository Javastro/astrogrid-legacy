/*$Id: Registry.java,v 1.2 2005/08/12 08:45:16 nw Exp $
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

/** Service Interface to an AstroGrid registry
 * <p>
 * Astrogrid use an IVOA-compliant registry to store details of available resources - servers, applications,
 * catalogues, etc.
 * 
 * @see <a href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Identifiers</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/ResourceMetadata">Resource Metadata</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/RM.html">IVOA Resource Metadata for the VO</a>
 * @see <a href="http://www.ivoa.net/Documents/latest/ADQL.html">ADQL Query Language Specification</a>
 * @see <a href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaResReg">IVOA Registry Working Group</a>
 * @see <a href="http://www.w3schools.com/xquery/default.asp">XQuery Language Tutorial</a>
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 * @service astrogrid.registry
 * @see org.astrogrid.acr.ui.RegistryBrowser
 * @see org.astrogrid.acr.astrogrid.ResourceInformation
 */
public interface Registry {
    /**Resolve a registry identifier to an endpoint 
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

    /**Retreive a registry Entry
     * 
     * @param ivorn identifier of the registry entry to retrrieve
     * @return xml document of the registry entry - a <tt>Resource</tt> document i
     * n the <tt>http://www.ivoa.net/xml/RegistryInterface/v0.1</tt> namespace
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

    /**
     * Retreive information about a registry entry
     * 
     * @param ivorn identifier of the registry entry to retrieve
     * @return a  summary of the most significant parts of the registry entry
     * @throws NotFoundException if this resource does not exist
     * @throws ServiceException if an error occurs talking to the service
     * @xmlrpc will return a map. See {@link ResourceInformation} for details of keys
     */
    ResourceInformation getResourceInformation(URI ivorn) throws  NotFoundException, ServiceException;
    
    
    /** perform a query on the registry
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
     * Document d = reg.searchForRecords(query);
     * </pre>
     */ 
    Document searchForRecords(String adql) throws ServiceException;
    
    
    /** perform an xquery over the registry
     * @param xquery the query to perform.
     * @return the result of executing this xquery over the registry - a document of arbitrary structure.
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
     * String query =   "declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.10\"; declare namespace vor=\"http://www.ivoa.net/xml/RegistryInterface/v0.1\"; "
     *    + " for $x in //vor:Resource where $x/vr:identifier = 'ivo://uk.ac.le.star/filemanager' return $x/vr:curation"  ;      
     * Document d = reg.xquery(query)
     *</pre>
     */
    Document xquery(String xquery) throws ServiceException;
   }

/* 
 $Log: Registry.java,v $
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