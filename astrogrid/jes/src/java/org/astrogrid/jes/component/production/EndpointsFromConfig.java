/*$Id: EndpointsFromConfig.java,v 1.3 2005/03/13 07:13:39 clq2 Exp $
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

import org.astrogrid.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.config.Config;
import org.astrogrid.jes.jobscheduler.dispatcher.CeaApplicationDispatcher.Endpoints;

import org.apache.axis.ConfigurationException;
import org.apache.axis.MessageContext;
import org.apache.axis.description.ServiceDesc;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

/** Configuration object for {@link org.astrogrid.jes.jobscheduler.dispatcher.CeaApplicationDispatcher}
 * <p>
 * looks in configuration for a key, otherwise guesses job monitor service by examing axis engine configuration.
 * @todo which doesn't seem to work at the moment  - need to fix
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 *
 */
public class EndpointsFromConfig extends SimpleComponentDescriptor implements Endpoints {
    /** key to look in config for  job monitor endpoint */
    public static final String MONITOR_ENDPOINT_KEY = "jes.monitor.endpoint.url";
    /** key to look in config for results listener endpoint */
    public static final String RESULTS_ENDPOINT_KEY = "jes.results.endpoint.url";
    
    /** absolute fallback endpoint - if no value specified in config, and we can't calculate value by querying axis message endpoint */
    public static final String MONITOR_DEFAULT_ENDPOINT ="http://localhost:8080/jes/services/JobMonitorService" ;
    public static final String RESULTS_DEFAULT_ENDPOINT = "http://localhost:8080/jes/services/ResultListener";
    
    public EndpointsFromConfig(Config conf) throws MalformedURLException, URISyntaxException {
        URL monitorURL = new URL(MONITOR_DEFAULT_ENDPOINT);
        URL resultsURL = new URL(RESULTS_DEFAULT_ENDPOINT);
        try {
        Iterator i = MessageContext.getCurrentContext().getAxisEngine().getConfig().getDeployedServices();
        // instead try
        //Iterator i = AxisServer.getServer(new HashMap()).getConfig().getDeployedServices();
        while (i.hasNext()) {
            ServiceDesc sDesc = (ServiceDesc)i.next();
            if (sDesc.getName().equals("JobMonitorService") && sDesc.getEndpointURL() != null) {
                    monitorURL = new URL(sDesc.getEndpointURL());               
            }      
            if (sDesc.getName().equals("ResultsListenerService") && sDesc.getEndpointURL() != null) {
                    resultsURL = new URL(sDesc.getEndpointURL());               
            }                    
        }      
        } catch (ConfigurationException e ) {
            // oh well.
        } catch (NullPointerException e) {
            // oh well
        }

         URL url = conf.getUrl(MONITOR_ENDPOINT_KEY,monitorURL);
         finalMonitorEndpoint = new URI(url.toString());
        url = conf.getUrl(RESULTS_ENDPOINT_KEY,resultsURL);
        finalResultsEndpoint = new URI(url.toString());
       name = "ApplicationControllerDispatcher - Monitor Endpoint configuration";
       description = "Loads job-monitor endpoint (used by callback from application controller) from Config\n" +
                "key :" + MONITOR_ENDPOINT_KEY+ " current value:" + finalMonitorEndpoint.toString() 
                + "\n key : "+ RESULTS_ENDPOINT_KEY + "current value:" + finalResultsEndpoint.toString();                           
    }

    protected final URI finalMonitorEndpoint;
    protected final URI finalResultsEndpoint;

    public URI monitorEndpoint() {
        return finalMonitorEndpoint;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.dispatcher.CeaApplicationDispatcher.Endpoints#resultListenerEndpoint()
     */
    public URI resultListenerEndpoint() {
        return finalResultsEndpoint;
    }
}


/* 
$Log: EndpointsFromConfig.java,v $
Revision 1.3  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.2.124.1  2005/03/11 14:02:10  nw
changes to work with pico1.1, and linked in the In-process
cea server

Revision 1.2  2004/07/05 18:34:13  nw
tweaked default value to be sensible

Revision 1.1  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.6  2004/07/01 11:19:05  nw
updated interface with cea - part of cea componentization

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