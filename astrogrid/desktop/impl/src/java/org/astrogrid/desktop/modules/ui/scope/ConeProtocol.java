/*$Id: ConeProtocol.java,v 1.12 2007/03/09 15:34:24 nw Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 *
 */
public class ConeProtocol extends SpatialDalProtocol {

    /** Construct a new ConeProtocol
     * @param name
     */
    public ConeProtocol(Registry reg, Cone cone) {
        super("Catalogues");
        this.reg = reg;
        this.cone = cone;
    } 
    private final Registry reg;
    private final Cone cone;

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#listServices()
     */
    public Service[] listServices() throws Exception{
    	Resource[] rs= reg.xquerySearch(cone.getRegistryXQuery());
        Service[] result = new Service[rs.length];
        System.arraycopy(rs,0,result,0,rs.length);
        return result;
    }

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#createRetriever(org.astrogrid.acr.astrogrid.ResourceInformation, double, double, double, double)
     */
    public Retriever createRetriever(UIComponent parent,Service i,double ra, double dec, double raSize, double decSize) {
        return new ConeRetrieval(parent,i,getPrimaryNode(),getVizModel(),cone,ra,dec,raSize);
    }

	public Service[] filterServices(List resourceList) {
		List result = new ArrayList();
		for (Iterator i = resourceList.iterator(); i.hasNext();) {
			Resource r = (Resource) i.next();
			if (r instanceof ConeService
					// special case for CDS.
					|| isCdsCatalogService(r)) { 
				result.add(r);
			}
		}
		return (Service[])result.toArray(new Service[result.size()]);
	}
    
	public static boolean isCdsCatalogService(Resource r) {
		if (! (r instanceof CatalogService)) {
			return false;
		}
		if (r.getId().toString().indexOf("CDS") == -1) {
			return false;
		}
		CatalogService c = (CatalogService)r;
		TableBean[] tables = c.getTables();
		for (int i = 0; i < tables.length; i++) {
			ColumnBean[] columns = tables[i].getColumns();
			for (int j = 0; j < columns.length; j++) {
				if (POSITION_UCD.equals(columns[i].getUCD())) {
					return true;
				}
			}
		}
		return false;
	}

	public static final String POSITION_UCD = "POS_EQ_RA_MAIN";
    

}


/* 
$Log: ConeProtocol.java,v $
Revision 1.12  2007/03/09 15:34:24  nw
vizier and voexplorer

Revision 1.11  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.10  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.9  2006/08/15 10:01:12  nw
migrated from old to new registry models.

Revision 1.8  2006/05/26 15:11:58  nw
tidied imported.corrected number formatting.

Revision 1.7  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

Revision 1.6  2006/05/11 10:02:45  KevinBenson
added history to astro and helioscope.  Along with tweaks to alignment and borders
And changing decimal places to 6 degrees.

Revision 1.5  2006/04/21 13:48:11  nw
mroe code changes. organized impoerts to reduce x-package linkage.

Revision 1.4  2006/03/24 10:30:15  KevinBenson
new checkboxes on heliosope for the Format, and the ability to query by Format
for stap services on helioscope

Revision 1.3  2006/03/13 18:29:08  nw
temporarily adjusted to avoid out-of-memory error caused by huge swathe of vizier records.

Revision 1.2  2006/03/13 14:55:09  KevinBenson
New first draft of helioscope and the stap spec protocol

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/