/*$Id: ConeInformationBuilder.java,v 1.4 2006/04/18 23:25:47 nw Exp $
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

import javax.xml.transform.TransformerException;

import org.apache.xpath.CachedXPathAPI;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.nvo.ConeInformation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        return xpath.eval(el,"contains(@xsi:type,'ConeSearch')",nsNode).bool();
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
                String val = xpath.eval(cap,"cs:maxSR",nsNode).str(); //@todo parse int in xpath? - need way to specify default.
                if (val != null) {
                    try {
                    maxSR = Float.parseFloat(val);
                    } catch (NumberFormatException e) {
                        // crappy reg entries - try to continue
                    }
                }
                val = null;
                val = xpath.eval(cap,"cs:maxRecords",nsNode).str(); //@todo parse int in xpath
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
                ,findLogo(xpath,element)
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
Revision 1.4  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.3.26.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.3  2005/12/02 13:40:32  nw
optimized, and made more error-tolerant

Revision 1.2  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.1  2005/10/18 16:53:34  nw
refactored common functionality.
added builders for siap and cone.
 
*/