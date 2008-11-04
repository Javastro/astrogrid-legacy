/*$Id: StapProtocol.java,v 1.18 2008/11/04 14:35:48 nw Exp $
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
import java.util.Date;
import java.util.List;

import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;

/** Protocol for STAP.
 * @author Kevin Benson
 * @author Mark Taylor
 *
 */
public class StapProtocol extends TemporalDalProtocol {

    /** Construct a new StapProtocols
     */
    public StapProtocol(     final RegistryInternal reg,final Stap stap) {
        super("Timed Data",IconHelper.loadIcon("latest16.png").getImage(),reg);
        this.stap = stap;
    }
    private final Stap stap;

    @Override
    public String getXQuery() {
        return stap.getRegistryXQuery();
    }


	public AbstractRetriever[] createRetrievers(final Service service, final Date start, final Date end, final double ra, final double dec, final double raSize, final double decSize) {
        final Capability[] capabilities = service.getCapabilities();
        final List cList = new ArrayList();
        for (int i = 0; i < capabilities.length; i++) {
            if (capabilities[i] instanceof StapCapability && findParamUrl(capabilities[i]) != null) {
                cList.add(capabilities[i]);
            }
        }
        final StapCapability[] staps = (StapCapability[]) cList.toArray(new StapCapability[0]);
        final int nstap = staps.length;
        final AbstractRetriever[] retrievers;
        if (nstap == 0) {
            retrievers = new AbstractRetriever[0];
        }
        else if (nstap == 1) {
            retrievers = new AbstractRetriever[] {
                new StapRetrieval(service, staps[0], findParamUrl(staps[0]), getDirectNodeSocket(), getVizModel(), stap, start, end, ra, dec, raSize, decSize, null),
            };
        }
        else {
            final NodeSocket socket = createIndirectNodeSocket(service);
            retrievers = new AbstractRetriever[nstap];
            for (int i = 0; i < nstap; i++) {
                retrievers[i] = new StapRetrieval(service, staps[i], findParamUrl(staps[i]), socket, getVizModel(), stap, start, end, ra, dec, raSize, decSize, null);
            }
        }
        setSubNames(capabilities, retrievers);
        return retrievers;
    }

	
	   
    protected boolean isSuitable(final Resource r)  {
        return r instanceof StapService;
    }

}


/* 
$Log: StapProtocol.java,v $
Revision 1.18  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.17  2008/05/28 12:27:49  nw
Complete - task 408: Adjust count reporting in astroscope and voexplorer.

Revision 1.16  2008/05/09 11:33:04  nw
Complete - task 394: process reg query results in a stream.

Incomplete - task 391: get to grips with new CDS

Complete - task 393: add waveband column.

Revision 1.15  2008/04/25 08:59:36  nw
extracted interface from retriever, to ease unit testing.

Revision 1.14  2008/04/23 11:17:53  nw
marked as needing test.

Revision 1.13  2008/02/22 17:03:35  mbt
Merge from branch mbt-desktop-2562.
Basically, Retrievers rather than Services are now the objects (associated
with TreeNodes) which communicate with external servers to acquire results.
Since Registry v1.0 there may be multiple Retrievers (even of a given type)
per Service.

Revision 1.12.12.3  2008/02/22 15:18:30  mbt
Fix so that multiple capabilities of a single service are anchored at a single node representing that service, rather than direct from the primary node

Revision 1.12.12.2  2008/02/22 11:01:07  mbt
Withdraw overloaded createRetrievers method.  Doesn't seem to be used anywhere

Revision 1.12.12.1  2008/02/21 15:35:15  mbt
Now does multiple-capability-per-service for all known protocols

Revision 1.12  2008/02/01 07:34:06  nw
altered to use new SsapService and StapService registry types

Revision 1.11  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.10  2007/10/23 09:26:00  nw
RESOLVED - bug 2189: How to query stap services
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2189

Revision 1.9  2007/06/18 16:42:36  nw
javadoc fixes.

Revision 1.8  2007/05/03 19:20:43  nw
removed helioscope.merged into uberscope.

Revision 1.7  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.6  2006/08/15 09:59:58  nw
migrated from old to new registry models.

Revision 1.5  2006/05/17 15:45:17  nw
factored common base class out of astroscope and helioscope.improved error-handline on astroscope input.

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
