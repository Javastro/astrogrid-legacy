/*$Id: ConeProtocol.java,v 1.1 2006/02/02 14:51:11 nw Exp $
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
import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jan-2006
 *
 */
public class ConeProtocol extends DalProtocol {

    /** Construct a new ConeProtocol
     * @param name
     */
    public ConeProtocol(UIComponent parent,Registry reg, Cone cone) {
        super("Catalogs",parent);
        this.reg = reg;
        this.cone = cone;
    }
    private final Registry reg;
    private final Cone cone;

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#listServices()
     */
    public ResourceInformation[] listServices() throws Exception{
        return reg.adqlSearchRI(cone.getRegistryQuery());
    }

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#createRetriever(org.astrogrid.acr.astrogrid.ResourceInformation, double, double, double, double)
     */
    public Retriever createRetriever(ResourceInformation i, double ra, double dec, double raSize, double decSize) {
        return new ConeRetrieval(parent,i,getPrimaryNode(),getVizModel(),cone,ra,dec,raSize);
    }

}


/* 
$Log: ConeProtocol.java,v $
Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/