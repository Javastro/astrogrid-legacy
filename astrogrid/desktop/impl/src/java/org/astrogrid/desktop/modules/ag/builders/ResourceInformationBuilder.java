/*$Id: ResourceInformationBuilder.java,v 1.1 2005/09/12 15:21:16 nw Exp $
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

    /** Construct a new ResourceInformationBuilder
     * 
     */
    public ResourceInformationBuilder() {
        super();
    }

    public boolean isApplicable(CachedXPathAPI xpath, Element el) {
        return true;
    }
    
    public  ResourceInformation build(CachedXPathAPI xpath,Element element) throws ServiceException{
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
}

/* 
$Log: ResourceInformationBuilder.java,v $
Revision 1.1  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder
 
*/