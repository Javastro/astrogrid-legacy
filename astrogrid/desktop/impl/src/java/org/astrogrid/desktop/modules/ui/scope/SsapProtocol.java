/*$Id: SsapProtocol.java,v 1.12 2008/04/23 11:17:53 nw Exp $
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

import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Capability;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.acr.ivoa.resource.SsapService;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.UIComponent;
/**
 * @TEST
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 17, 20089:20:37 PM
 */
public class SsapProtocol extends SpatialDalProtocol {

    public SsapProtocol(Registry reg, Ssap ssap) {
        super("Spectra",IconHelper.loadIcon("ssap16.png").getImage());
        this.reg = reg;
        this.ssap = ssap;
    }
    private final Registry reg;
    private final Ssap ssap;

    public Service[] listServices() throws Exception{
        Resource[] rs = reg.xquerySearch(ssap.getRegistryXQuery());
        Service[] result = new Service[rs.length];
        System.arraycopy(rs,0,result,0,rs.length);
        return result;
    }

	public Retriever[] createRetrievers(Service service, double ra, double dec, double raSize, double decSize) {
        Capability[] capabilities = service.getCapabilities();
        List cList = new ArrayList();
        for (int i = 0; i < capabilities.length; i++) {
            if (capabilities[i] instanceof SsapCapability && findParamUrl(capabilities[i]) != null) {
                cList.add(capabilities[i]);
            }
        }
        SsapCapability[] ssaps = (SsapCapability[]) cList.toArray(new SsapCapability[0]);
        int nssap = ssaps.length;
        final Retriever[] retrievers;
        if (nssap == 0) {
            retrievers = new Retriever[0];
        }
        else if (nssap == 1) {
            retrievers = new Retriever[] {
                new SsapRetrieval(service, ssaps[0], findParamUrl(ssaps[0]), getDirectNodeSocket(), getVizModel(), ssap, ra, dec, raSize, decSize),
            };
        }
        else {
            NodeSocket socket = createIndirectNodeSocket(service);
            retrievers = new Retriever[nssap];
            for (int i = 0; i < nssap; i++) {
                retrievers[i] = new SsapRetrieval(service, ssaps[i], findParamUrl(ssaps[i]), socket, getVizModel(), ssap, ra, dec, raSize, decSize);
            }
        }
        setSubNames(capabilities, retrievers);
        return retrievers;
    }
    
	public Service[] filterServices(List resourceList) {
		List result = new ArrayList();
		for (Iterator i = resourceList.iterator(); i.hasNext();) {
			Resource r = (Resource) i.next();
			if (r instanceof SsapService) {
				result.add(r);
			}
		}
		return (Service[])result.toArray(new Service[result.size()]);
	}

}


/* 
$Log: SsapProtocol.java,v $
Revision 1.12  2008/04/23 11:17:53  nw
marked as needing test.

Revision 1.11  2008/02/22 17:03:35  mbt
Merge from branch mbt-desktop-2562.
Basically, Retrievers rather than Services are now the objects (associated
with TreeNodes) which communicate with external servers to acquire results.
Since Registry v1.0 there may be multiple Retrievers (even of a given type)
per Service.

Revision 1.10.12.3  2008/02/22 15:18:30  mbt
Fix so that multiple capabilities of a single service are anchored at a single node representing that service, rather than direct from the primary node

Revision 1.10.12.2  2008/02/21 15:35:15  mbt
Now does multiple-capability-per-service for all known protocols

Revision 1.10.12.1  2008/02/21 11:06:09  mbt
First bash at 2562.  AstroScope now runs multiple cone searches per Service

Revision 1.10  2008/02/01 07:34:06  nw
altered to use new SsapService and StapService registry types

Revision 1.9  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.8  2007/03/08 17:43:56  nw
first draft of voexplorer

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
