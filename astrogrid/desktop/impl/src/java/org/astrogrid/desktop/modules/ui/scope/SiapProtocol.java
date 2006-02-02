/*$Id: SiapProtocol.java,v 1.1 2006/02/02 14:51:11 nw Exp $
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
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public class SiapProtocol extends DalProtocol {

    /** Construct a new SiapProtocol
     * @param name
     */
    public SiapProtocol(UIComponent parent,Registry reg,Siap siap) {
        super("Images",parent);
        this.reg =  reg;
        this.siap = siap;
    }
    private final Registry reg;
    private final Siap siap;

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#listServices()
     */
    public ResourceInformation[] listServices() throws Exception{
        return reg.adqlSearchRI(siap.getRegistryQuery());
    } 

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#createRetriever(org.astrogrid.acr.astrogrid.ResourceInformation, double, double, double, double)
     */
    public Retriever createRetriever(ResourceInformation i, double ra, double dec, double raSize, double decSize) {
        return new SiapRetrieval(parent,i,getPrimaryNode(),getVizModel(),siap,ra,dec,raSize,decSize);
    }

}


/* 
$Log: SiapProtocol.java,v $
Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/