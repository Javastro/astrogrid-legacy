/*$Id: MonitorEndpointFromConfig.java,v 1.5 2004/03/17 00:26:09 nw Exp $
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

import org.apache.axis.AxisFault;
import org.apache.axis.ConfigurationException;
import org.apache.axis.MessageContext;
import org.apache.axis.description.ServiceDesc;
import org.apache.axis.server.AxisServer;

import com.sun.corba.se.connection.GetEndPointInfoAgainException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/** Configuration object for {@link org.astrogrid.jes.jobscheduler.dispatcher.ApplicationControllerDispatcher}
 * <p>
 * looks in configuration for a key, otherwise guesses job monitor service by examing axis engine configuration.
 * @todo which doesn't seem to work at the moment  - need to fix
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class MonitorEndpointFromConfig extends SimpleComponentDescriptor implements MonitorEndpoint {
    /** key to look in config for  job monitor endpoint */
    public static final String MONITOR_ENDPOINT_KEY = "jes.monitor.endpoint.url";
    /** absolute fallback endpoint - if no value specified in config, and we can't calculate value by querying axis message endpoint */
    public static final String MONITOR_DEFAULT_ENDPOINT ="http://localhost:8080/jes/services/JobMonitorService" ;
    public MonitorEndpointFromConfig(Config conf) throws MalformedURLException {
        URL defaultURL = new URL(MONITOR_DEFAULT_ENDPOINT);
        try {
        Iterator i = MessageContext.getCurrentContext().getAxisEngine().getConfig().getDeployedServices();
        // instead try
        //Iterator i = AxisServer.getServer(new HashMap()).getConfig().getDeployedServices();
        while (i.hasNext()) {
            ServiceDesc sDesc = (ServiceDesc)i.next();
            if (sDesc.getName().equals("JobMonitorService") && sDesc.getEndpointURL() != null) {
                    defaultURL = new URL(sDesc.getEndpointURL());               
            }            
        }      
        } catch (ConfigurationException e ) {
            // oh well.
        } catch (NullPointerException e) {
            // oh well
        }

         url = conf.getUrl(MONITOR_ENDPOINT_KEY,defaultURL);
   
        name = "ApplicationControllerDispatcher - Monitor Endpoint configuration";
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
Revision 1.5  2004/03/17 00:26:09  nw
attempt at determining monitor endpoint by querying axis engine

Revision 1.4  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.3  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:26  nw
added implementation of a self-configuring production set of component
 
*/