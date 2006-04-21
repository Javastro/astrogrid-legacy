/*$Id: StapProtocol.java,v 1.4 2006/04/21 13:48:11 nw Exp $
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
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Kevin Benson
 *
 */
public class StapProtocol extends DalProtocol {

    /** Construct a new StapProtocol
     * @param name
     */
    public StapProtocol(UIComponent parent,Registry reg,Stap stap) {
        super("Stap",parent);
        this.reg =  reg;
        this.stap = stap;
    }
    private final Registry reg;
    private final Stap stap;

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#listServices()
     */
    public ResourceInformation[] listServices() throws Exception{
        return reg.adqlSearchRI(stap.getRegistryQuery());
    } 

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#createRetriever(org.astrogrid.acr.astrogrid.ResourceInformation, double, double, double, double)
     */
    public Retriever createRetriever(ResourceInformation i, Calendar start, Calendar end, double ra, double dec, double raSize, double decSize) {
        return new StapRetrieval(parent,i,getPrimaryNode(),getVizModel(),stap, start, end, ra,dec, raSize, decSize);
    }
    
    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#createRetriever(org.astrogrid.acr.astrogrid.ResourceInformation, double, double, double, double)
     */
    public Retriever createRetriever(ResourceInformation i,Calendar start, Calendar end, double ra, double dec, double raSize, double decSize, String format) {
        return new StapRetrieval(parent,i,getPrimaryNode(),getVizModel(),stap, start, end, ra,dec, raSize, decSize, format);
    }
    

}


/* 
$Log: StapProtocol.java,v $
Revision 1.4  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.3  2006/03/24 10:30:15  KevinBenson
new checkboxes on heliosope for the Format, and the ability to query by Format
for stap services on helioscope

Revision 1.2  2006/03/16 09:16:20  KevinBenson
usually comment/clean up type changes such as siap to stap

Revision 1.1  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

 
*/