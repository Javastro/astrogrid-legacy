/*$Id: SkyNodeInformationBuilder.java,v 1.3 2006/08/02 13:29:19 nw Exp $
 * Created on 23-Feb-2006
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.CachedXPathAPI;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ivoa.SkyNodeInformation;
import org.w3c.dom.Element;

/**
 * builder of a skynode information bean
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Feb-2006
 * * @deprecated part of the obsolete registry infrastructure
 */
public class SkyNodeInformationBuilder extends ResourceInformationBuilder {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SkyNodeInformationBuilder.class);

    public SkyNodeInformationBuilder() {
        super();
    }
    
    public boolean isApplicable(CachedXPathAPI xpath, Element el) {
        try {
            return xpath.eval(el,"contains(@xsi:type,'OpenSkyNode')",nsNode).bool();
        } catch (TransformerException e) {
            logger.debug(e);
            return false;
        }
    }
    
    public ResourceInformation build(CachedXPathAPI xpath, Element element) throws ServiceException {
        try {
        String compliance = SkyNodeInformation.BASIC;
        double lattitude = 0.0;
        double longitude = 0.0;
        long maxRecords = -1;
        String primaryTable = "";
        String primaryKey = "";
        
        //@todo add parsing in here.
        
        return new SkyNodeInformation(
                findId(xpath,element)
                , findName(xpath,element)
                , findDescription(xpath,element)
                , findAccessURL(xpath,element)
                , findLogo(xpath,element)
                , compliance
                , lattitude
                , longitude
                , maxRecords
                , primaryTable
                , primaryKey
                );
        } catch (TransformerException e) {
            throw new ServiceException(e);
        }
    }
  

}


/* 
$Log: SkyNodeInformationBuilder.java,v $
Revision 1.3  2006/08/02 13:29:19  nw
marked all as obsolete.

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:03  nw
finished code.extruded plastic hub.

Revision 1.1  2006/02/24 15:22:29  nw
integration necessary for skynode
 
*/