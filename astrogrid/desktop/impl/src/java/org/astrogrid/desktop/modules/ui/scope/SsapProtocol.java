/*$Id: SsapProtocol.java,v 1.5 2006/05/17 15:45:17 nw Exp $
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

import java.util.Calendar;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.desktop.modules.ui.UIComponent;

public class SsapProtocol extends SpatialDalProtocol {

    public SsapProtocol(Registry reg, Ssap ssap) {
        super("Spectra");
        this.reg = reg;
        this.ssap = ssap;
    }
    private final Registry reg;
    private final Ssap ssap;

    public ResourceInformation[] listServices() throws Exception{
        return reg.adqlSearchRI(ssap.getRegistryQuery());
    }

	public Retriever createRetriever(UIComponent parent, ResourceInformation i, double ra, double dec, double raSize, double decSize) {
	    return new SsapRetrieval(parent,i,getPrimaryNode(),getVizModel(),ssap,ra,dec,raSize,decSize);
	    
	}
    

}


/* 
$Log: SsapProtocol.java,v $
Revision 1.5  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.4  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.3  2006/03/24 10:30:15  KevinBenson
new checkboxes on heliosope for the Format, and the ability to query by Format
for stap services on helioscope

Revision 1.2  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/