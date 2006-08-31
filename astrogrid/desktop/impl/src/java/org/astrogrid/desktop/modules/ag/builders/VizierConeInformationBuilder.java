/*$Id: VizierConeInformationBuilder.java,v 1.4 2006/08/31 21:29:14 nw Exp $
 * Created on 13-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag.builders;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.TransformerException;

import org.apache.xpath.CachedXPathAPI;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.nvo.ConeInformation;
import org.w3c.dom.Element;
/**
 *  * @deprecated part of the obsolete registry infrastructure
 *  @fixme move this info into the new parsing structure.
 * @author Noel Winstanley
 * @since Jul 31, 200611:48:06 PM
 */
public class VizierConeInformationBuilder extends ConeInformationBuilder {

    public VizierConeInformationBuilder() {
        super();
    }
    public boolean isApplicable(CachedXPathAPI xpath, Element el) {
        try {
            //@todo should test on column UCD too, but by the time it gets here, we should be safe. 
            return xpath.eval(el,"contains(@xsi:type,'TabularSkyService') and contains(vr:identifier,'ivo://CDS/')",nsNode).bool();
            } catch (TransformerException e) {
                logger.debug("TransformerException",e);
                return false;
            }
    }

    public ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException {
        try {
            // default values.
            float maxSR = -1; 
            int maxRecords = -1;
            boolean verbosity = true;

        ConeInformation ci =  new ConeInformation(
                findId(xpath,element)
                ,findName(xpath,element)
                ,findDescription(xpath,element)
                ,findVizierAccessURL(xpath,element)
                ,findLogo(xpath,element)
                ,maxSR
                ,maxRecords
                ,verbosity
                );
        return ci;
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }        
    }
    
    protected final URL findVizierAccessURL(CachedXPathAPI xpath, Element element) throws TransformerException {
        String s = null;
        try {
            //work around for duffs.
            s = xpath.eval(element,"normalize-space(vr:interface[contains(@xsi:type,'ParamHTTP')]/node()[local-name()='accessURL'])",nsNode).str();
            logger.debug("AccessURL: '" + s + "'");
            if (s == null || s.length() == 0) {
                return null;
            }
            return new URL(s);
        } catch (MalformedURLException e) {
            logger.info("Failed to parse access url " + s);
            return null;
        }
    }

}


/* 
$Log: VizierConeInformationBuilder.java,v $
Revision 1.4  2006/08/31 21:29:14  nw
doc fix.

Revision 1.3  2006/08/02 13:29:19  nw
marked all as obsolete.

Revision 1.2  2006/04/21 13:48:12  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.1  2006/03/13 18:27:56  nw
special case of a cone builder from vizier reg entries.
 
*/