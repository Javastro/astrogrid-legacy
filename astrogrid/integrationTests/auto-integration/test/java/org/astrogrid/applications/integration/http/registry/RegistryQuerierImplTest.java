/* $Id: RegistryQuerierImplTest.java,v 1.4 2005/07/21 15:23:36 pah Exp $
 * Created on Aug 5, 2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.applications.integration.http.registry;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.types.ApplicationKindType;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.applications.http.registry.RegistryQuerierImpl;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;

/**
 * JUnit Test
 * 
 * @author jdt
 * 
 * @TODO this will need to be moved to the integration tests project
 */
public class RegistryQuerierImplTest extends TestCase {
    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(RegistryQuerierImplTest.class);


    public void testListApplications() throws Exception {
        if (log.isTraceEnabled()) {
            log.trace("testListApplications() - start");
        }
       

        RegistryQuerierImpl querier = new RegistryQuerierImpl(new RegistryQueryLocator(){
           /* (non-Javadoc)
          * @see org.astrogrid.applications.description.registry.RegistryQueryLocator#getClient()
          */
         public RegistryService getClient() throws RegistryException {
            return RegistryDelegateFactory.createQuery();
         }
        });
        Collection allApps = querier.getHttpApplications();
        log.debug("testListApplications() - All Apps details:");
        Iterator it = allApps.iterator();
        Map allAppsAgain = new HashMap();
        while (it.hasNext()) {
            CeaHttpApplicationType app = (CeaHttpApplicationType) it.next();
            log.debug("testListApplications()" + app.getShortName());
            log.debug("testListApplications()" + app.getApplicationDefinition().getApplicationKind());
            log.debug("testListApplications()" + app.getCeaHttpAdapterSetup().getURL());
            allAppsAgain.put(app.getTitle(),app);
            //check that this is a http-get or http-post application
            assertTrue("Application was not of http type", app.getApplicationDefinition().getApplicationKind().equals(ApplicationKindType.HTTP) );
        }

        assertTrue("An expected application was not present", allAppsAgain.containsKey("HelloWorldHttpApp"));
       
        if (log.isTraceEnabled()) {
            log.trace("testListApplications() - end");
        }
    }

}