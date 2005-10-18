/*$Id: SiapInformationBuilder.java,v 1.1 2005/10/18 16:53:34 nw Exp $
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
import org.astrogrid.acr.ivoa.SiapInformation;
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
public class SiapInformationBuilder extends ResourceInformationBuilder {

    /** Construct a new SiapInformationBuilder
     * 
     */
    public SiapInformationBuilder() {
        super();
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.builders.InformationBuilder#isApplicable(org.apache.xpath.CachedXPathAPI, org.w3c.dom.Element)
     */
    public boolean isApplicable(CachedXPathAPI xpath, Element el) {
        try {
            String type = xpath.eval(el,"@xsi:type",nsNode).str();
            return StringUtils.contains(type,"SimpleImageAccess");
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
            String imageServiceType = "";
            float maxQueryRegionSizeRa = -1;
            float maxQueryRegionSizeDec = -1;
            float maxImageExtentRa = -1;
            float maxImageExtentDec = -1;
            int  maxImageSizeRa = -1;
            int maxImageSizeDec = -1;
            int maxFileSize = -1;
            int maxRecords = -1;
            //try to extract some data from the cone.            
            Node cap = xpath.selectSingleNode(element,".//sia:capability",nsNode); // some capability have vr namespace, others sia. try to find any with sia contents
            if (cap != null) {
                
                String val = xpath.eval(cap,"sia:imageServiceType",nsNode).str();
                if (val != null) {
                    imageServiceType = val.trim();
                }
                
                val = null;
                val = xpath.eval(cap,"sia:maxQueryRegionSize/sia:long",nsNode).str();
                if (val != null) {
                    try {
                    maxQueryRegionSizeRa = Float.parseFloat(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }
                val = null;
                val = xpath.eval(cap,"sia:maxQueryRegionSize/sia:lat",nsNode).str();
                if (val != null) {
                    try {
                    maxQueryRegionSizeDec = Float.parseFloat(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }      
                
                val = null;
                val = xpath.eval(cap,"sia:maxImageExtent/sia:long",nsNode).str();
                if (val != null) {
                    try {
                    maxImageExtentRa = Float.parseFloat(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }
                val = null;
                val = xpath.eval(cap,"sia:maxImageExtent/sia:lat",nsNode).str();
                if (val != null) {
                    try {
                    maxImageExtentDec = Float.parseFloat(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }    
                
                val = null;
                val = xpath.eval(cap,"sia:maxImageSize/sia:long",nsNode).str();
                if (val != null) {
                    try {
                    maxImageSizeRa = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }
                val = null;
                val = xpath.eval(cap,"sia:maxImageSize/sia:lat",nsNode).str();
                if (val != null) {
                    try {
                    maxImageSizeDec = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }                    
                
                val = null;
                val = xpath.eval(cap,"sia:maxFileSize",nsNode).str();
                if (val != null) {
                    try {
                    maxFileSize = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }             
                
                val = null;
                val = xpath.eval(cap,"sia:maxRecords",nsNode).str();
                if (val != null) {
                    try {
                    maxRecords = Integer.parseInt(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }                    
                }                             
            }
        return new SiapInformation(
                findId(xpath,element)
                ,findName(xpath,element)
                ,findDescription(xpath,element)
                ,findAccessURL(xpath,element)
                ,imageServiceType
                ,maxQueryRegionSizeRa, maxQueryRegionSizeDec
                ,maxImageExtentRa, maxImageExtentDec
                ,maxImageSizeRa, maxImageSizeDec
                ,maxFileSize
                ,maxRecords
                );
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }    
    }

}


/* 
$Log: SiapInformationBuilder.java,v $
Revision 1.1  2005/10/18 16:53:34  nw
refactored common functionality.
added builders for siap and cone.
 
*/