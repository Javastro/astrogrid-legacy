/*$Id: RegistryToolLocator.java,v 1.15 2005/03/30 15:19:19 nw Exp $
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
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
            if (name == null ) {
                throw reportError("Unnamed tool - cannot locate it");
            }
            logger.debug("retreiving tool location for " +name);
            Document toolDocument;
            try {
                toolDocument = delegate.getResourceByIdentifier(name);
                if (logger.isDebugEnabled()) {
                    StringWriter sw = new StringWriter();
                    XMLUtils.PrettyDocumentToWriter(toolDocument,sw);
                    logger.debug("ToolDocument\n" + sw.toString());
                }
            }
            catch (RegistryException e) {
                throw reportError("Could not get registry entry for tool " + name,e);
            }            

        IdentifierType toolId = null;
        try {            
            IdentifierType[] toolIds = getIdentifiers(toolDocument);
            if (toolIds.length == 0) {
                throw reportError("No identifiers in registry entry for tool" + name);
            }           
            toolId = toolIds[0];            
        } catch (CastorException e) {
            throw reportError("Could not parse return document for tool" + name,e);
        }            
            logger.debug("found tool: " + toolId.getAuthorityID() + " / " + toolId.getResourceKey());
             
            // now query registry for all entries the provide this.
            String queryString = buildQueryString(toolId);
            logger.debug("Query to find services supporting this:" + queryString);
            Document results = null;
            try {
               //results = delegate.submitQuery(queryString);
                results = delegate.searchFromSADQL(queryString);
               if (logger.isDebugEnabled()) {
                   StringWriter sw = new StringWriter();
                   XMLUtils.PrettyDocumentToWriter(results,sw);
                   logger.debug("Query Results\n" + sw.toString());
               }
            } catch (RegistryException e) {
                throw reportError("Failed to query registry for services providing tool " + name,e);
            }
            // found identifiers
            IdentifierType[] serviceIds = null;
            try {
                serviceIds = getIdentifiers(results);
                logger.debug("Found " + serviceIds.length + " matching services");
                if (serviceIds.length == 0) {
                    throw reportError("Tool " + name + " has no known service providers");
                }
            } catch (CastorException e) {
                throw reportError("Failed to extract identifiers from query document",e);
            }
            
            // see what endpoints we can extract from the services..
            List endpoints = new ArrayList();            
            for (int i = 0; i < serviceIds.length ; i++) {
                String serviceName = serviceIds[i].getAuthorityID() + "/" + serviceIds[i].getResourceKey();
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
                    logger.warn("Query registry about " + serviceName + " failed",e);
                }
            }
            
            // select an endpoint to use.
            String[] endpointList = null;
            if (endpoints.size() == 0) {
                throw reportError("No service providers for Tool " + name +" have a valid endpoint");
            } else if (endpoints.size() == 1) {
                endpointList = new String[]{endpoints.get(0).toString()};
            } else { // more than one alternative - shuffle them up.
                Collections.shuffle(endpoints);
                endpointList = (String[])endpoints.toArray(new String[endpoints.size()]);
            }
            logger.debug("registry resolved to " + endpointList);
            return endpointList;
    }
    
    
    private JesException reportError(String s, Exception e) {
        logger.error(s,e);
        return new JesException(s,e);
    }
    private JesException reportError(String s) {
        logger.error(s);
        return new JesException(s);
    }    
    
    /**
     * @param toolId
     */
    
    private final static String PRE_QUERY= "<query><selectionSequence>" +
        "<selection item='searchElements' itemOp='EQ' value='vr:Resource'/>" +
        "<selectionOp op='$and$'/>" +
        /* don't bother with this for now - not nice to search in xsi:type anyway
        "<selection item='@*:type' itemOp='EQ' value='CeaServiceType'/>"  +
        "<selectionOp op='AND'/>" +
        */
        "<selection item='cea:ManagedApplications/cea:ApplicationReference/vr:AuthorityID' itemOp='EQ' value='" ;
    private final static String MID_QUERY = "'/>"  +
        "<selectionOp op='AND'/>" +
        "<selection item='cea:ManagedApplications/cea:ApplicationReference/vr:ResourceKey' itemOp='EQ' value='" ;
    private final static String END_QUERY =  "'/>"  +                                           
        /* don't think that we need these....
         "<selectionOp op='OR'/>" +
         "<selection item='@*:type' itemOp='EQ' value='CeaServiceType'/>"  +
         */
        "</selectionSequence></query>";   
    
    private String buildQueryString(IdentifierType toolId) {
        String queryString = "Select * from Registry where " +
        "cea:ManagedApplications/cea:ApplicationReference='ivo://" +
        toolId.getAuthorityID() + "/" + toolId.getResourceKey() + "'";
        /*
        StringBuffer sb = new StringBuffer(PRE_QUERY);
        sb.append( toolId.getAuthorityID());
        sb.append(MID_QUERY);
        sb.append(toolId.getResourceKey());
        sb.append(END_QUERY);
        return sb.toString();
        */
        return queryString;
 
    }
    
    /**
     queries registry to get tool entry, extract details from this.
     */
    private IdentifierType[] getIdentifiers(Document doc)throws CastorException  {
            NodeList nl = doc.getElementsByTagNameNS("*","Identifier");
            IdentifierType[] results = new IdentifierType[nl.getLength()];
            for (int i = 0; i < nl.getLength(); i++) {
                results[i] = (IdentifierType)Unmarshaller.unmarshal(IdentifierType.class,nl.item(0));
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