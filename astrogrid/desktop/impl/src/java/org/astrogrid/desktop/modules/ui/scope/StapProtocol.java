/*$Id: StapProtocol.java,v 1.14 2008/04/23 11:17:53 nw Exp $
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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.acr.astrogrid.Stap;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.StapCapability;
import org.astrogrid.acr.ivoa.resource.StapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponent;

/** @TEST
 * @author Kevin Benson
 * @author Mark Taylor
 *
 */
public class StapProtocol extends TemporalDalProtocol {

    /** Construct a new StapProtocols
     */
    public StapProtocol(     Registry reg,Stap stap) {
        super("Timed Data",IconHelper.loadIcon("latest16.png").getImage());
        this.reg =  reg;
        this.stap = stap;
    }
    private final Registry reg;
    private final Stap stap;

    /**
     * @see org.astrogrid.desktop.modules.ui.scope.DalProtocol#listServices()
     */
    public Service[] listServices() throws Exception{
        Resource[] rs = reg.xquerySearch(stap.getRegistryXQuery());
        Service[] result = new Service[rs.length];
        System.arraycopy(rs,0,result,0,rs.length);
        return result;        
    } 

	public Retriever[] createRetrievers(Service service, Date start, Date end, double ra, double dec, double raSize, double decSize) {
        Capability[] capabilities = service.getCapabilities();
        List cList = new ArrayList();
        for (int i = 0; i < capabilities.length; i++) {
            if (capabilities[i] instanceof StapCapability && findParamUrl(capabilities[i]) != null) {
                cList.add(capabilities[i]);
            }
        }
        StapCapability[] staps = (StapCapability[]) cList.toArray(new StapCapability[0]);
        int nstap = staps.length;
        final Retriever[] retrievers;
        if (nstap == 0) {
            retrievers = new Retriever[0];
        }
        else if (nstap == 1) {
            retrievers = new Retriever[] {
                new StapRetrieval(service, staps[0], findParamUrl(staps[0]), getDirectNodeSocket(), getVizModel(), stap, start, end, ra, dec, raSize, decSize, null),
            };
        }
        else {
            NodeSocket socket = createIndirectNodeSocket(service);
            retrievers = new Retriever[nstap];
            for (int i = 0; i < nstap; i++) {
                retrievers[i] = new StapRetrieval(service, staps[i], findParamUrl(staps[i]), socket, getVizModel(), stap, start, end, ra, dec, raSize, decSize, null);
            }
        }
        setSubNames(capabilities, retrievers);
        return retrievers;
    }

	public Service[] filterServices(List resourceList) {
		List result = new ArrayList();
		for (Iterator i = resourceList.iterator(); i.hasNext();) {
			Resource r = (Resource) i.next();
			if (r instanceof StapService) {
				result.add(r);
			}
		}
		return (Service[])result.toArray(new Service[result.size()]);
	}

}


/* 
$Log: StapProtocol.java,v $
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
