/*$Id: ConeProtocol.java,v 1.3 2006/03/13 18:29:08 nw Exp $
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

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
        /* @todo quick hack to get round memory limitations of current registyr delegate & information parsers.
        return reg.adqlSearchRI(cone.getRegistryQuery());
        shold e able to get all this data in one go when I've written a stax client to the registry
        */
        ResourceInformation[] cones = reg.adqlSearchRI("Select * from Registry where @xsi:type like '%ConeSearch'");
        List results = new ArrayList(Arrays.asList(cones));
        // nnnhg. this is so hacky. I think I'd better take a shower after this.
        /*
        char[] c = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        for (int i = 0;i < c.length;i++) { 
            ResourceInformation[] vizs = reg.adqlSearchRI("Select * from Registry where @xsi:type like '%TabularSkyService' and vr:identifier like 'ivo://CDS/%' and vs:table/vs:column/vs:ucd = 'POS_EQ_RA_MAIN' " +
                    " and vr:title like '" + c[i] + "%' ");
            results.add(vizs);
        }
*/
        return (ResourceInformation[])results.toArray(new ResourceInformation[results.size()]);
    }

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#createRetriever(org.astrogrid.acr.astrogrid.ResourceInformation, double, double, double, double)
     */
    public Retriever createRetriever(ResourceInformation i,Calendar start, Calendar end, double ra, double dec, double raSize, double decSize) {
        return new ConeRetrieval(parent,i,getPrimaryNode(),getVizModel(),cone,ra,dec,raSize);
    }

}


/* 
$Log: ConeProtocol.java,v $
Revision 1.3  2006/03/13 18:29:08  nw
temporarily adjusted to avoid out-of-memory error caused by huge swathe of vizier records.

Revision 1.2  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/