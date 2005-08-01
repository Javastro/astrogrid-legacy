/*$Id: RegistryToolLocator.java,v 1.17 2005/08/01 08:15:52 clq2 Exp $
 * Created on 08-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.registry.RegistryException;
//import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.Test;

/** Tool locator that resolves tools using the registry.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Mar-2004
 * @todo needs to be re-thought to allow calling of different types of service - resolving to endpoints is still valid - however, many registered services will have a single reg entry, 
 * rather than cea-application and cea-service - so query pattern is different
 *
 */
public class RegistryToolLocator implements Locator, ComponentDescriptor {
    private static final Log logger = LogFactory.getLog(RegistryToolLocator.class);
    /** interface to configure registry with */
    public interface RegistryEndpoint {
        URL getURL();
    }
    
    public RegistryToolLocator(RegistryEndpoint endpoint) {
        url = endpoint.getURL();
        delegate = RegistryDelegateFactory.createQuery(url);
    }
    
    protected final URL url;
    protected final RegistryService delegate;
    protected final Random rand = new Random();

    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#locateTool(org.astrogrid.workflow.beans.v1.Step)
     */    
    public String[] locateTool(Tool tool) throws JesException{           
            String name =tool.getName();
            logger.info("Locating service endpoints for cea application  " + name);            
            // retrieve the cea application entry first - verify it exists.
            if (name == null ) {
                throw reportError("Unnamed application - cannot locate it");
            }                        
            Document toolDocument;
            try {
                toolDocument = delegate.getResourceByIdentifier(name);
            }
            catch (RegistryException e) {
                throw reportError("Could not find registry entry for cea application " + name,e);
            }            
        // @todo not quite sure why we're doing this - already have the identifier of the cea application entry.    
        String toolId = null;
        //try {           
            String[] toolIds = getIdentifiers(toolDocument);
            if (toolIds.length == 0) {
                throw reportError("No identifiers in registry entry for cea application" + name);
            }           
            toolId = toolIds[0];
        /*
        } catch (CastorException e) {
            throw reportError("Could not parse return document for cea application" + name,e);
        } 
        */           
            logger.debug("found cea application: " + toolId);
      //@todo use original application name in here instead..
            
            // now query registry for all entries the provide this tool.
            String queryString = buildQueryString(toolId);
            logger.debug("ADQL Query to find services " + queryString);
            Document results = null;
            try {
               //results = delegate.submitQuery(queryString);
                results = delegate.searchFromSADQL(queryString);
            } catch (RegistryException e) {
                throw reportError("Failed to query registry for services providing tool " + name,e);
            }
            // found identifiers
            String[] serviceIds = null;
            //try {
                serviceIds = getIdentifiers(results);
                logger.info("Found " + serviceIds.length + " matching cea services");
                if (serviceIds.length == 0) {
                    throw reportError("Tool " + name + " has no known service providers");
                }
            /*
            } catch (CastorException e) {
                throw reportError("Failed to extract identifiers from query document",e);
            }
            */
            
            // see what endpoints we can get from the services..
            logger.debug("Resolving services to endpoints");
            Set endpoints = new HashSet(); // using a set to handle duplicate endpoints.            
            for (int i = 0; i < serviceIds.length ; i++) {
                String serviceName = serviceIds[i];
                try {
                    // @todo inefficient? we've already got the reg entries, but here we're querying reg to resolve to endpoint
                    // someone with better unsterstanding of reg entries could parse them directly..
                    String endpoint = delegate.getEndPointByIdentifier(serviceName);
                    if (endpoint != null && endpoint.trim().length() > 0) { // looks ok, add it to the list.
                        try {                       
                            URL ep = new URL(endpoint); // check it's a valid url..
                            endpoints.add(ep.toString());
                        } catch (MalformedURLException e) {
                            logger.warn("Service " + serviceName + "has duff endpoint URL",e);
                        }
                    }
                    logger.debug("Service " + serviceName + " resolved to endpoint " + endpoint);
                } catch (RegistryException e) {
                    logger.warn("Resolvng endpoint for " + serviceName + " failed",e);
                }
            }
            

            if (endpoints.size() == 0) {
                throw reportError("No service providers for Tool " + name +" have a valid endpoint");
            } else if (endpoints.size() == 1) {
                logger.info("Service Endpoints for CEA application " + name + ": " + endpoints); 
                return new String[]{endpoints.iterator().next().toString()};
            } else {
                logger.debug("More that one available service - shuffling..");
                List endpointsList = new ArrayList(endpoints);
                Collections.shuffle(endpointsList);
                logger.info("Service Endpoints for CEA application " + name + ": " + endpointsList);                 
                return (String[])endpointsList.toArray(new String[endpointsList.size()]);
            }
    }
    
    
    private JesException reportError(String s, Exception e) {
        logger.error(s,e);
        return new JesException(s,e);
    }
    private JesException reportError(String s) {
        logger.error(s);
        return new JesException(s);
    }    
    

    private String buildQueryString(String toolId) {
        String queryString = "Select * from Registry where " +
        "cea:ManagedApplications/cea:ApplicationReference='" + toolId + "'";
        return queryString;
 
    }
    
    /**
     queries registry to get tool entry, extract details from this.
     */
    private String[] getIdentifiers(Document doc) {
            logger.debug("Extracting Identifiers");
            NodeList nl = doc.getElementsByTagNameNS("*","Resource");
            String[] results = new String[nl.getLength()];
            for (int i = 0; i < nl.getLength(); i++) {
                results[i] = ((org.w3c.dom.Element)nl.item(i)).getElementsByTagNameNS("*","identifier")
                             .item(0).getFirstChild().getNodeValue();
                logger.debug(results[i]);
            }            
            return results;
    }
        
    /**
     * @see org.astrogrid.jes.jobscheduler.Locator#getToolInterface(org.astrogrid.workflow.beans.v1.Step)
     */
    public String getToolInterface(Step js) throws JesException {
        logger.error("Deprecated method, not used");
        return null;
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Registry Tool Locator";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Resolve Tool locations using an astrogrid registry" +            "\n Currently looking in registry at " + url.toString();
    }
    /** @todo check we can resolve endpoint 
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: RegistryToolLocator.java,v $
Revision 1.17  2005/08/01 08:15:52  clq2
Kmb 1293/1279/intTest1 FS/FM/Jes/Portal/IntTests

Revision 1.16.20.1  2005/07/20 08:15:52  KevinBenson
now reflects 0.10 registry and does no longer require 0.9 and the 0.9 translation that the registry client was doing.

Revision 1.16  2005/05/09 11:37:51  nw
fixed bug spotted by kona.

Revision 1.15  2005/03/30 15:19:19  nw
fixed type cast problem

Revision 1.14  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.13.2.2  2005/03/11 15:21:35  nw
adjusted locator so that it returns a list of endpoints to connect to.
we can get round-robin by shuffling the list.
dispatcher tries each endpoint in the list until can connect to one wihout throwing an exception.

Revision 1.13.2.1  2005/03/11 14:05:00  nw
random-selection of application server if more than oneavailable.

Revision 1.13  2005/03/02 15:02:24  clq2
for v10 missing '

Revision 1.11.36.2  2005/03/02 14:32:26  KevinBenson
*** empty log message ***

Revision 1.11.36.1  2005/03/02 12:03:15  KevinBenson
changing to do adql queries now

Revision 1.11  2004/10/08 20:03:19  pah
optimize the tool query to use namespaces - better performance than using wildcard

Revision 1.10  2004/09/16 21:48:28  nw
tried to optimize query bulding

Revision 1.9  2004/08/25 11:42:12  KevinBenson
changed to use prefixes

Revision 1.8  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.7  2004/07/01 21:15:00  nw
added results-listener interface to jes

Revision 1.6  2004/04/21 10:05:51  nw
implemented correctly - passes integration testing

Revision 1.5  2004/04/08 14:43:26  nw
added delete and abort job functionality

Revision 1.4  2004/03/15 01:30:45  nw
factored component descriptor out into separate package

Revision 1.3  2004/03/15 00:06:57  nw
removed SchedulerNotifier interface - replaced references to it by references to JobScheduler interface - identical

Revision 1.2  2004/03/09 14:41:44  nw
updated to track changes to registry delegate

Revision 1.1  2004/03/08 00:37:07  nw
preliminary implementation of registry tool locatr
 
*/