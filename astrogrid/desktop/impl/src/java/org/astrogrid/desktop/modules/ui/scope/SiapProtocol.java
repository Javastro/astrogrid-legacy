/*$Id: SiapProtocol.java,v 1.17 2008/11/04 14:35:48 nw Exp $
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
import java.util.List;

import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SiapCapability;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;

/** Protocol for SIAP services.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 27-Jan-2006
 * @author Mark Taylor
 *@TEST
 */
public class SiapProtocol extends SpatialDalProtocol {

    /** Construct a new SiapProtocol
     */
    public SiapProtocol(final RegistryInternal reg,final Siap siap) {
        super("Images",IconHelper.loadIcon("siap16.png").getImage(),reg);
        this.siap = siap;
    }
    private final Siap siap;

    
    @Override
    public String getXQuery() {
        return siap.getRegistryXQuery();
    }



    public AbstractRetriever[] createRetrievers(final Service service, final double ra, final double dec, final double raSize, final double decSize) {
        final Capability[] capabilities = service.getCapabilities();
        final List cList = new ArrayList();
        for (int i = 0; i < capabilities.length; i++) {
            if (capabilities[i] instanceof SiapCapability && findParamUrl(capabilities[i]) != null) {
                cList.add(capabilities[i]);
            }
        }
        final SiapCapability[] siaps = (SiapCapability[]) cList.toArray(new SiapCapability[0]);
        final int nsiap = siaps.length;
        final AbstractRetriever[] retrievers;
        if (nsiap == 0) {
            retrievers = new AbstractRetriever[0];
        }
        else if (nsiap == 1) {
            retrievers = new AbstractRetriever[] {
                new SiapRetrieval(service, siaps[0], findParamUrl(siaps[0]), getDirectNodeSocket(), getVizModel(), siap, ra, dec, raSize, decSize),
            };
        }
        else {
            final NodeSocket socket = createIndirectNodeSocket(service);
            retrievers = new AbstractRetriever[nsiap];
            for (int i = 0; i < nsiap; i++) {
                retrievers[i] = new SiapRetrieval(service, siaps[i], findParamUrl(siaps[i]), socket, getVizModel(), siap, ra, dec, raSize, decSize);
            }
        }
        setSubNames(capabilities, retrievers);
        return retrievers;
    }
    
	   
    protected boolean isSuitable(final Resource r)  {
        return r instanceof SiapService;
    }

}


/* 
$Log: SiapProtocol.java,v $
Revision 1.17  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.16  2008/05/28 12:27:49  nw
Complete - task 408: Adjust count reporting in astroscope and voexplorer.

Revision 1.15  2008/05/09 11:33:04  nw
Complete - task 394: process reg query results in a stream.

Incomplete - task 391: get to grips with new CDS

Complete - task 393: add waveband column.

Revision 1.14  2008/04/25 08:59:36  nw
extracted interface from retriever, to ease unit testing.

Revision 1.13  2008/04/23 11:17:53  nw
marked as needing test.

Revision 1.12  2008/02/22 17:03:35  mbt
Merge from branch mbt-desktop-2562.
Basically, Retrievers rather than Services are now the objects (associated
with TreeNodes) which communicate with external servers to acquire results.
Since Registry v1.0 there may be multiple Retrievers (even of a given type)
per Service.

Revision 1.11.18.3  2008/02/22 15:18:29  mbt
Fix so that multiple capabilities of a single service are anchored at a single node representing that service, rather than direct from the primary node

Revision 1.11.18.2  2008/02/21 15:35:15  mbt
Now does multiple-capability-per-service for all known protocols

Revision 1.11.18.1  2008/02/21 11:06:09  mbt
First bash at 2562.  AstroScope now runs multiple cone searches per Service

Revision 1.11  2007/12/12 13:54:13  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.10  2007/06/18 16:42:36  nw
javadoc fixes.

Revision 1.9  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.8  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.7  2006/08/15 09:59:58  nw
migrated from old to new registry models.

Revision 1.6  2006/05/26 15:11:58  nw
tidied imported.corrected number formatting.

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
