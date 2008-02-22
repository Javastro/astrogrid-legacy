/*$Id: ConeProtocol.java,v 1.17 2008/02/22 17:03:35 mbt Exp $
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponent;

/**
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 * @author Mark Taylor
 *
 */
public class ConeProtocol extends SpatialDalProtocol {


    public ConeProtocol(Registry reg, Cone cone) {
        super("Catalogues",IconHelper.loadIcon("cone16.png").getImage());
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

    public Retriever[] createRetrievers(Service service,double ra, double dec, double raSize, double decSize) {
        Capability[] capabilities = service.getCapabilities();
        List cList = new ArrayList();
        for (int i = 0; i < capabilities.length; i++) {
            if (capabilities[i] instanceof ConeCapability && findParamUrl(capabilities[i]) != null) {
                cList.add(capabilities[i]);
            }
        }
        ConeCapability[] cones = (ConeCapability[]) cList.toArray(new ConeCapability[0]);
        int ncone = cones.length;
        final Retriever[] retrievers;
        if (ncone == 0) {
            retrievers = new Retriever[0];
        }
        else if (ncone == 1) {
            retrievers = new Retriever[] {
                new CatalogTerminalConeRetrieval(service, cones[0], findParamUrl(cones[0]), getDirectNodeSocket(), getVizModel(), cone, ra, dec, raSize),
            };
        }
        else {
            NodeSocket socket = createIndirectNodeSocket(service);
            retrievers = new Retriever[ncone];
            for (int i = 0; i < ncone; i++) {
                retrievers[i] = new CatalogTerminalConeRetrieval(service, cones[i], findParamUrl(cones[i]), socket, getVizModel(), cone, ra, dec, raSize);
            }
        }
        setSubNames(capabilities, retrievers);
        return retrievers;
    }

	public Service[] filterServices(List resourceList) {
		List result = new ArrayList();
		for (Iterator i = resourceList.iterator(); i.hasNext();) {
			Resource r = (Resource) i.next();
			if (r instanceof ConeService
					// special case for CDS.
					|| isConeSearchableCdsCatalog(r)) { 
				result.add(r);
			}
		}
		return (Service[])result.toArray(new Service[result.size()]);
	}
    /** test whether a resource is from CDS,and is a catalogue which
     * can be cone-searched - i.e. has a column wiht a position UCD
     * @param r
     * @return
     */
	public static boolean isConeSearchableCdsCatalog(Resource r) {
		if (! isCdsCatalog(r)) {
		    return false;
		}
		CatalogService c = (CatalogService)r;
		TableBean[] tables = c.getTables();
		for (int i = 0; i < tables.length; i++) {
			ColumnBean[] columns = tables[i].getColumns();
			for (int j = 0; j < columns.length; j++) {
				if (POSITION_UCD.equals(columns[j].getUCD())) {
					return true;
				}
			}
		}
		return false;
	}

    /** test whether a resource is a catgalogue from CDS.
     * 
     * @param r
     */
    public static boolean isCdsCatalog(Resource r) {
        return r instanceof CatalogService 
            && r.getId().toString().startsWith("ivo://CDS/VizieR/");

    }

	public static final String POSITION_UCD = "POS_EQ_RA_MAIN";
    

}


/* 
$Log: ConeProtocol.java,v $
Revision 1.17  2008/02/22 17:03:35  mbt
Merge from branch mbt-desktop-2562.
Basically, Retrievers rather than Services are now the objects (associated
with TreeNodes) which communicate with external servers to acquire results.
Since Registry v1.0 there may be multiple Retrievers (even of a given type)
per Service.

Revision 1.16.18.3  2008/02/22 15:18:29  mbt
Fix so that multiple capabilities of a single service are anchored at a single node representing that service, rather than direct from the primary node

Revision 1.16.18.2  2008/02/21 15:35:15  mbt
Now does multiple-capability-per-service for all known protocols

Revision 1.16.18.1  2008/02/21 11:06:09  mbt
First bash at 2562.  AstroScope now runs multiple cone searches per Service

Revision 1.16  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.15  2007/09/11 12:11:48  nw
improved handling of cds resources.

Revision 1.14  2007/06/18 16:42:36  nw
javadoc fixes.

Revision 1.13  2007/05/03 19:20:43  nw
removed helioscope.merged into uberscope.

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
