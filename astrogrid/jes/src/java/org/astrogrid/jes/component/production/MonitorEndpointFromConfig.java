/*$Id: MonitorEndpointFromConfig.java,v 1.3 2004/03/15 01:30:06 nw Exp $
 * Created on 07-Mar-2004
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
import org.astrogrid.jes.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher.MonitorEndpoint;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
        final URL 
        @todo implement in a more intelligent way - try getting a servlet context, etc. axis context even.
 */
public class MonitorEndpointFromConfig extends SimpleComponentDescriptor implements MonitorEndpoint {
    public static final String MONITOR_ENDPOINT_KEY = "jes.monitor.endpoint.url";
    public static final String DEFAULT_URL = "http://localhost:8080/astrogrid-jes/services/JobMonitor";

    public MonitorEndpointFromConfig(Config conf) throws MalformedURLException {
        url = conf.getUrl(MONITOR_ENDPOINT_KEY,new URL(DEFAULT_URL));
        name = "get job monitor endpoint from config";
             description = "Loads job-monitor endpoint (used by callback from application controller) from Config\n" +
                 "key :" + MONITOR_ENDPOINT_KEY
                 + "\n current value:" + url.toString();        
    }

    protected final URL url;
    /**
     * @see org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher.MonitorEndpoint#getURL()
     */
    public URL getURL() {
        return url;
    }
}


/* 
$Log: MonitorEndpointFromConfig.java,v $
Revision 1.3  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:26  nw
added implementation of a self-configuring production set of component
 
*/