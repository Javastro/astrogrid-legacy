/*$Id: RegistryImpl.java,v 1.2 2005/08/25 16:59:58 nw Exp $
 * Created on 02-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.registry.NoResourcesFoundException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.Ivorn;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/** implementation of the registry component
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
 *
 */
public class RegistryImpl implements Registry {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RegistryImpl.class);

    /** Construct a new Registry
     * 
     */
    public RegistryImpl() {
        super();
        reg = RegistryDelegateFactory.createQuery();        
    }
    private final RegistryService reg;

    public URL resolveIdentifier(URI ivorn) throws NotFoundException, ServiceException{
            try {
                return new URL(reg.getEndPointByIdentifier(new Ivorn(ivorn.toString())));
            } catch (MalformedURLException e) {
                throw new NotFoundException(e);
            } catch (NoResourcesFoundException e) {
                throw new NotFoundException(e);
            } catch (RegistryException e) {
                throw new ServiceException(e); 
            } catch (URISyntaxException e) {
                throw new ServiceException(e); // wonder what would cause this?
            }
 

    }
    public Document getRecord(URI ivorn) throws NotFoundException, ServiceException {
    
            try {
                Document doc =  reg.getResourceByIdentifier(new Ivorn(ivorn.toString() ));
                NodeList l = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"Resource");
                Document result = XMLUtils.newDocument();
                if (l.getLength() > 0) { // otherwise we'll reutrn the empty document
                    Element el = (Element)l.item(0);                    
                    result.appendChild(result.importNode(el,true));
                }
                return result;
            } catch (NoResourcesFoundException e) {
                throw new NotFoundException(e);
            } catch (RegistryException e) {
                throw new ServiceException(e);
            } catch (URISyntaxException e) {
                throw new ServiceException(e);
            } catch (ParserConfigurationException e) {
                throw new ServiceException(e);
            }
    }
    

    /**
     * @see org.astrogrid.acr.astrogrid.Registry#getResourceData(java.lang.String)
     */
    public ResourceInformation getResourceInformation(URI ivorn) throws  NotFoundException, ServiceException {
            
                Document d = getRecord(ivorn);
                CachedXPathAPI xpath = new CachedXPathAPI();
                return buildResourceInformationFromResourceElement(xpath,d.getDocumentElement());

    }

    public Document  searchForRecords(String arg0) throws ServiceException {
        return adqlSearch(arg0);
    }

    /**@todo implement
     */
    public Document keywordSearch(String arg0, boolean arg1) throws ServiceException {
        throw new ServiceException("Not implemented - waiting for delegate support");
    }
    /** @todo implement */
    public ResourceInformation[] keywordSearchRI(String arg0, boolean arg1) throws ServiceException {
        throw new ServiceException("Not implemented - waiting for delegate support");
    }    

    public Document adqlSearch(String adql) throws ServiceException  {
        try {
            Document doc =  reg.searchFromSADQL(adql);
            NodeList l = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"VOResources");
            Document result = XMLUtils.newDocument();
            if (l.getLength() > 0) { // otherwise we'll reutrn the empty document
                Element el = (Element)l.item(0);                    
                result.appendChild(result.importNode(el,true));
            } else {
                result.appendChild(result.createElementNS(XPathHelper.VOR_NS,"VOResources"));
            }
            return result;            
        } catch (NoResourcesFoundException e) {
            // shouldn't ever return this - should return an empty result document instead.
            logger.fatal("search is never expected to return a 'NoResourcesFoundException'",e);
            try {
                Document result =  XMLUtils.newDocument(); 
                result.appendChild(result.createElementNS(XPathHelper.VOR_NS,"VOResources"));
                return result;
            } catch (ParserConfigurationException e1) {
                throw new ServiceException(e); 
            }
        } catch (RegistryException e) {
            throw new ServiceException(e);
        } catch (ParserConfigurationException e1) {
            throw new ServiceException(e1); 
        }        
    }
    
    public ResourceInformation[] adqlSearchRI(String arg0) throws NotFoundException, ServiceException {
        try {
            Document doc = reg.searchFromSADQL(arg0);
            NodeList l = doc.getElementsByTagNameNS(XPathHelper.VOR_NS,"Resource");
            ResourceInformation[] results = new ResourceInformation[l.getLength()];
            CachedXPathAPI xpath = new CachedXPathAPI(); // create one once for the entire document
            for (int i =0 ; i < l.getLength(); i++) {
                results[i] = buildResourceInformationFromResourceElement(xpath,(Element)l.item(i));
            }
            return results;
        } catch (NoResourcesFoundException e) {
            logger.fatal("search is never expected to return a 'NoResourcesFoundException'",e);
                return new ResourceInformation[]{};    
        } catch (RegistryException e) {
            throw new ServiceException(e);
        }                 
    }
    
    

    
    private ResourceInformation buildResourceInformationFromResourceElement(CachedXPathAPI xpath,Element element) throws ServiceException{
        try {
            Element nsNode = XPathHelper.createNamespaceNode();
            
        URI uri;
        try {
            uri = new URI(xpath.eval(element,"vr:identifier",nsNode).str());
        } catch (URISyntaxException e) {
            
            uri = null;
        }
        String name = xpath.eval(element,"vr:title",nsNode).str();
        String description = xpath.eval(element,"vr:content/vr:description",nsNode).str();       
        URL accessURL ;
        try {
            accessURL =  new URL(xpath.eval(element,"vr:interface/vr:accessURL",nsNode).str());
        } catch (MalformedURLException e) {
            accessURL = null;
        }
        return new ResourceInformation(
                uri
                ,name
                ,description
                ,accessURL
                );
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }
    }

    /**@todo add declaration of common prefixes to front of query?
     * @see org.astrogrid.acr.astrogrid.Registry#xquery(java.lang.String)
     */
    public Document xquerySearch(String xquery) throws ServiceException {        
            try {
                return reg.xquerySearch(xquery);
            } catch (RegistryException e) {
                throw new ServiceException(e);
            }
            
    }    

    
}


/* 
$Log: RegistryImpl.java,v $
Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.6  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.5  2005/05/12 15:59:11  clq2
nww 1111 again

Revision 1.3.8.2  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3.8.1  2005/05/11 09:52:45  nw
exposed new registry methods

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/