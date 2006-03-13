/*$Id: StapProtocol.java,v 1.1 2006/03/13 14:55:09 KevinBenson Exp $
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
import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.desktop.modules.ui.UIComponent;

import java.util.Calendar;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public class StapProtocol extends DalProtocol {

    /** Construct a new SiapProtocol
     * @param name
     */
    public StapProtocol(UIComponent parent,Registry reg,Stap stap) {
        super("Images",parent);
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

}


/* 
$Log: StapProtocol.java,v $
Revision 1.1  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/