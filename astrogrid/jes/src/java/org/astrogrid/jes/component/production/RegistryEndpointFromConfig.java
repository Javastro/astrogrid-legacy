/*$Id: RegistryEndpointFromConfig.java,v 1.5 2004/07/01 21:15:00 nw Exp $
 * Created on 08-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.production;

import org.astrogrid.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.config.Config;
import org.astrogrid.jes.jobscheduler.locator.RegistryToolLocator.RegistryEndpoint;

import java.net.URL;

/** Configuration component that retreives registry endpoint from the configuration.
 * Used by {@link org.astrogrid.jes.jobscheduler.locator.RegistryToolLocator}
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 * @todo add unit test, fallback registry endpoint..
 */
public class RegistryEndpointFromConfig extends SimpleComponentDescriptor implements RegistryEndpoint {
    /** key to look in config for registry endpoint. 
     * @todo replace by proper constant, once its made public */
    public static final String REGISTRY_ENDPOINT_KEY = "org.astrogrid.registry.query.endpoint";
    public RegistryEndpointFromConfig(Config c) {
        url = c.getUrl(REGISTRY_ENDPOINT_KEY);
        name="Registry Tool Locator - registry endpoint configuration";
        description="key: " + REGISTRY_ENDPOINT_KEY
            + "\nvalue: " + url.toString();
    }
    protected final URL url;
    /**
     * @see org.astrogrid.jes.jobscheduler.locator.RegistryToolLocator.RegistryEndpoint#getURL()
     */
    public URL getURL() {
        return url;
    }
}


/* 
$Log: RegistryEndpointFromConfig.java,v $
Revision 1.5  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.4  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.3  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.2  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.1  2004/03/08 00:36:34  nw
added configuration of registry tool locator to production components
 
*/