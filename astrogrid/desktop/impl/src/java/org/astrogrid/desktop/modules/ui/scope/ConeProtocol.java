/*$Id: ConeProtocol.java,v 1.24 2008/11/04 14:35:48 nw Exp $
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
import java.util.List;

import org.astrogrid.acr.ivoa.Cone;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.CatalogService;
import org.astrogrid.acr.ivoa.resource.ConeCapability;
import org.astrogrid.acr.ivoa.resource.ConeService;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;

/** Cone Search Protocol
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 * @author Mark Taylor
 *@TEST
 */
public class ConeProtocol extends SpatialDalProtocol {


    public ConeProtocol(final RegistryInternal reg, final Cone cone) {
        super("Cat. Objects",IconHelper.loadIcon("cone16.png").getImage(),reg);
        this.cone = cone;
    } 
    private final Cone cone;

    
    @Override
    public String getXQuery() {
        return cone.getRegistryXQuery();
    }


    @Override
    public AbstractRetriever[] createRetrievers(final Service service,final double ra, final double dec, final double raSize, final double decSize) {
        final Capability[] capabilities = service.getCapabilities();
        final List cList = new ArrayList();
        for (int i = 0; i < capabilities.length; i++) {
            if (capabilities[i] instanceof ConeCapability && findParamUrl(capabilities[i]) != null) {
                cList.add(capabilities[i]);
            }
        }
        final ConeCapability[] cones = (ConeCapability[]) cList.toArray(new ConeCapability[0]);
        final int ncone = cones.length;
        final AbstractRetriever[] retrievers;
        if (ncone == 0) {
            retrievers = new AbstractRetriever[0];
        }
        else if (ncone == 1) {
            retrievers = new AbstractRetriever[] {
                new CatalogTerminalConeRetrieval(service, cones[0], findParamUrl(cones[0]), getDirectNodeSocket(), getVizModel(), cone, ra, dec, raSize),
            };
        }
        else {
            final NodeSocket socket = createIndirectNodeSocket(service);
            retrievers = new AbstractRetriever[ncone];
            for (int i = 0; i < ncone; i++) {
                retrievers[i] = new CatalogTerminalConeRetrieval(service, cones[i], findParamUrl(cones[i]), socket, getVizModel(), cone, ra, dec, raSize);
            }
        }
        setSubNames(capabilities, retrievers);
        return retrievers;
    }


	
	@Override
    protected boolean isSuitable(final Resource r)  {
	    return r instanceof ConeService;
	}


    /** test whether a resource is a catgalogue from CDS.
     * 
     * @param r
     */
    public static boolean isCdsCatalog(final Resource r) {
        return r instanceof CatalogService
        && r.getCuration().getPublisher() != null
        && CDS.equals(r.getCuration().getPublisher().getId());

    }
    
    private static final URI CDS = URI.create("ivo://CDS");

	public static final String POSITION_UCD = "POS_EQ_RA_MAIN";
    

}


/* 
$Log: ConeProtocol.java,v $
Revision 1.24  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.23  2008/08/19 18:49:22  nw
ASSIGNED - bug 2812: assorted things on vodesktop 2008-2-rc1
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2812

Revision 1.22  2008/05/28 12:27:49  nw
Complete - task 408: Adjust count reporting in astroscope and voexplorer.

Revision 1.21  2008/05/09 11:33:04  nw
Complete - task 394: process reg query results in a stream.

Incomplete - task 391: get to grips with new CDS

Complete - task 393: add waveband column.

Revision 1.20  2008/04/25 08:59:36  nw
extracted interface from retriever, to ease unit testing.

Revision 1.19  2008/04/23 11:17:53  nw
marked as needing test.

Revision 1.18  2008/03/07 12:55:25  nw
labelling fix for andy.

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
