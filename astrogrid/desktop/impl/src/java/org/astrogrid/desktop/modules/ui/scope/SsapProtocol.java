/*$Id: SsapProtocol.java,v 1.1 2006/02/02 14:51:11 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.ui.UIComponent;

public class SsapProtocol extends DalProtocol {

    public SsapProtocol(UIComponent parent,Registry reg, Ssap ssap) {
        super("Spectra",parent);
        this.reg = reg;
        this.ssap = ssap;
    }
    private final Registry reg;
    private final Ssap ssap;

    public ResourceInformation[] listServices() throws Exception{
        return reg.adqlSearchRI(ssap.getRegistryQuery());
    }

    public Retriever createRetriever(ResourceInformation i, double ra, double dec, double raSize, double decSize) {
        return new SsapRetrieval(parent,i,getPrimaryNode(),getVizModel(),ssap,ra,dec,raSize,decSize);
    }

}


/* 
$Log: SsapProtocol.java,v $
Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/