/*$Id: ConeInformationBuilder.java,v 1.1 2005/10/18 16:53:34 nw Exp $
 * Created on 18-Oct-2005
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
import org.astrogrid.acr.nvo.ConeInformation;

import org.apache.commons.lang.StringUtils;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Oct-2005
 *
 */
public class ConeInformationBuilder extends ResourceInformationBuilder {
 

    /**
     * @see org.astrogrid.desktop.modules.ag.builders.InformationBuilder#isApplicable(org.apache.xpath.CachedXPathAPI, org.w3c.dom.Element)
     */
    public boolean isApplicable(CachedXPathAPI xpath, Element el) {
        try {
        String type = xpath.eval(el,"@xsi:type",nsNode).str();
        return StringUtils.contains(type,"ConeSearch");
        } catch (TransformerException e) {
            logger.debug("TransformerException",e);
            return false;
        }
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.builders.InformationBuilder#build(org.apache.xpath.CachedXPathAPI, org.w3c.dom.Element)
     */
    public ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException {
        try {
            // default values.
            float maxSR = -1;
            int maxRecords = -1;
            boolean verbosity = false;
            //try to extract some data from the cone.            
            Node cap = xpath.selectSingleNode(element,".//cs:capability",nsNode);
            if (cap != null) {
                String val = xpath.eval(cap,"cs:maxSR",nsNode).str();
                if (val != null) {
                    try {
                    maxSR = Float.parseFloat(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }
                }
                val = null;
                val = xpath.eval(cap,"cs:maxRecords",nsNode).str();
                if (val != null) {
                    try {
                    maxRecords = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }
                val = null;
                val = xpath.eval(cap,"cs:verbosity",nsNode).str();
                if (val != null) {
                    
                    verbosity = Boolean.valueOf(val).booleanValue();
                }
            }
        return new ConeInformation(
                findId(xpath,element)
                ,findName(xpath,element)
                ,findDescription(xpath,element)
                ,findAccessURL(xpath,element)
                ,maxSR
                ,maxRecords
                ,verbosity
                );
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }        
    }

}


/* 
$Log: ConeInformationBuilder.java,v $
Revision 1.1  2005/10/18 16:53:34  nw
refactored common functionality.
added builders for siap and cone.
 
*/