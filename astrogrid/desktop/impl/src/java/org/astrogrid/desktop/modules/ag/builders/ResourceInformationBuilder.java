/*$Id: ResourceInformationBuilder.java,v 1.3 2005/11/04 10:14:26 nw Exp $
 * Created on 07-Sep-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.builders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.desktop.modules.ag.XPathHelper;

import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/** builder for a vanilla resource information bean. 
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Sep-2005
 *
 */
public class ResourceInformationBuilder implements InformationBuilder {
    /**
     * Commons Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(ResourceInformationBuilder.class);

    /** Construct a new ResourceInformationBuilder
     * 
     */
    public ResourceInformationBuilder() {
        super();
        Element n = null;
        try {
            n = XPathHelper.createNamespaceNode();
        } catch (Exception e) {
           logger.fatal("Could not create static namespace node",e);          
        }           
        nsNode = n;
    }

    protected   final  Element nsNode; 
    public boolean isApplicable(CachedXPathAPI xpath, Element el) {
        return true;
    }
    
    public  ResourceInformation build(CachedXPathAPI xpath,Element element) throws ServiceException{
        try {
        return new ResourceInformation(
                findId(xpath, element)
                ,findName(xpath, element)
                ,findDescription(xpath, element)
                ,findAccessURL(xpath, element)
                ,findLogo(xpath,element)
                );
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @param xpath
     * @param element
     * @return
     * @throws TransformerException
     */
    protected final URL findAccessURL(CachedXPathAPI xpath, Element element) throws TransformerException {
        URL accessURL;
        try {
            accessURL =  new URL(xpath.eval(element,"vr:interface/vr:accessURL",nsNode).str());
        } catch (MalformedURLException e) {
            accessURL = null;
        }
        return accessURL;
    }
    
    protected final URL findLogo(CachedXPathAPI xpath, Element element) throws TransformerException {
        URL logo;
        try {
            logo = new URL(xpath.eval(element,"vr:curation/vr:creator/vr:logo",nsNode).str());
        } catch (MalformedURLException e) {
            logo = null;
        }
        return logo;            
    }

    /**
     * @param xpath
     * @param element
     * @return
     * @throws TransformerException
     */
    protected final String findDescription(CachedXPathAPI xpath, Element element) throws TransformerException {
        return xpath.eval(element,"vr:content/vr:description",nsNode).str();
    }

    /**
     * @param xpath
     * @param element
     * @return
     * @throws TransformerException
     */
    protected final String findName(CachedXPathAPI xpath, Element element) throws TransformerException {
        return xpath.eval(element,"vr:title",nsNode).str();
    }

    /**
     * @param xpath
     * @param element
     * @return
     * @throws TransformerException
     */
    protected final URI findId(CachedXPathAPI xpath, Element element) throws TransformerException {
        URI uri;
        try {
            uri = new URI(xpath.eval(element,"vr:identifier",nsNode).str());
        } catch (URISyntaxException e) {
            
            uri = null;
        }
        return uri;
    }
}

/* 
$Log: ResourceInformationBuilder.java,v $
Revision 1.3  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.2  2005/10/18 16:53:34  nw
refactored common functionality.
added builders for siap and cone.

Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/