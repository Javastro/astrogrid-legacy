/*$Id: RegistryEndpointFromConfig.java,v 1.1 2004/03/08 00:36:34 nw Exp $
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

import org.astrogrid.config.Config;
import org.astrogrid.jes.component.SimpleComponentDescriptor;
import org.astrogrid.jes.jobscheduler.locator.RegistryToolLocator.RegistryEndpoint;
import org.astrogrid.registry.client.RegistryDelegateFactory;

import java.net.URL;

/** Configuration component that retreives registry endpoint from the configuration.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 * @todo add unit test, fallback registry endpoint..
 */
public class RegistryEndpointFromConfig extends SimpleComponentDescriptor implements RegistryEndpoint {
    /** @todo replace by proper constant, once its made public */
    public static final String REGISTRY_ENDPOINT_KEY = "org.astrogrid.registry.query.endpoint";
    public RegistryEndpointFromConfig(Config c) {
        url = c.getUrl(REGISTRY_ENDPOINT_KEY);
        name="Retreive registry endpoint from config";
        description="key: " + REGISTRY_ENDPOINT_KEY
            + "\nvalue: " + url.toString();
    }
    protected final URL url;
    /**
     * @see org.astrogrid.jes.jobscheduler.locator.RegistryToolLocator.RegistryEndpoint#getURL()
     */
    public URL getURL() {
        return null;
    }
}


/* 
$Log: RegistryEndpointFromConfig.java,v $
Revision 1.1  2004/03/08 00:36:34  nw
added configuration of registry tool locator to production components
 
*/