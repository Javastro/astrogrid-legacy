/*$Id: RegistryApplicationRegistry.java,v 1.2 2004/03/11 13:53:36 nw Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.resource.ResourceType;
import org.astrogrid.registry.beans.resource.VODescription;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;

/** Implementation of ApplicationRegistry that resolves lookups using an astrogrid registry
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class RegistryApplicationRegistry implements ApplicationRegistry {
    private static final Log logger = LogFactory.getLog(RegistryApplicationRegistry.class);

   /** construct a new RegistryApplicationRegistry
    * let registry-delegate self-configure */
   public RegistryApplicationRegistry() {
     logger.info("Creating an astrogrid-backed application registry");
     logger.info("Letting delegate determine own endpoint");
     service = RegistryDelegateFactory.createQuery();
     assert service != null;
   }

    /** Construct a new RegistryApplicationRegistry
     * @param endpoint endpoint for the astrogrid registry web service
     */
    public RegistryApplicationRegistry(URL endpoint) {
        logger.info("Creating an astrogrid-backed application registry");
        service  = RegistryDelegateFactory.createQuery(endpoint);
        assert service != null;
    }
    protected final RegistryService service;
    /** string query to to pass to registry to get list of tools back
     * @todo find out what to put here 
     */
    public final static String LIST_QUERY_STRING="dunno what goes here";
    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#listApplications()
     */
    public String[] listApplications() throws WorkflowInterfaceException {
        try {
        VODescription result = service.submitQueryString(LIST_QUERY_STRING);
        if (result == null) {
            logger.error("Registry returned null when queried for application list");
            throw new WorkflowInterfaceException("Registry returned null when queried for application list");
        }
        ResourceType[] resources = result.getResource();
        if (resources == null) { // registry returned null array - we'll return a list.
            resources = new ResourceType[]{};
        }
        String[] names = new String[resources.length]; 
        for (int i = 0; i < resources.length; i++) {
            names[i] = resources[i].getShortName();
        }
        return names;        
        } catch (RegistryException e) {
            logger.error("listApplications failed",e);
            throw new WorkflowInterfaceException(e);
        }
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#getDescriptionFor(java.lang.String)
     */
    public ApplicationDescription getDescriptionFor(String applicationName) throws WorkflowInterfaceException {
        try {
            VODescription result = service.submitQueryString(mkInfoQuery(applicationName));
            if (result == null) {
                logger.error("Registry returned null when queried for " + applicationName);
                throw new WorkflowInterfaceException("Registry returned null when queried for " + applicationName);
            }
            ApplicationBase base = convertDescriptionToApplication(result);
            return new ApplicationDescription(base);
        } catch (RegistryException e) {
            logger.error("getDescriptionFor " + applicationName,e);
            throw new WorkflowInterfaceException(e);
        }
    }
    
    /** Converts a VODescription to an Application Descriptor
     * @param result
     * @return
     * @todo implement me
     */
    private ApplicationBase convertDescriptionToApplication(VODescription result) {        
        return new ApplicationBase(); // implement this!!
    }
    /** construct a query string to that will retreive info for this application 
     * @param applicationName
     * @return
     * @todo implement me
     */
    private String mkInfoQuery(String applicationName) {
        return applicationName; //implement this!!!!
    }
}


/* 
$Log: RegistryApplicationRegistry.java,v $
Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:10  nw
added implementations for the workflow interfaces

Revision 1.1.2.1  2004/03/09 17:41:59  nw
created a bunch of implementations,
 
*/