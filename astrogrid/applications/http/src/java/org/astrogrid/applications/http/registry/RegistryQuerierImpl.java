/* $Id: RegistryQuerierImpl.java,v 1.11 2008/09/03 14:19:03 pah Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 * Created on Jul 29, 2004
 */
// This class needs a registry to be tested
// It is tested by the Integration Tests, so we
// don't need to include it in the clover stats.
///CLOVER:OFF 
package org.astrogrid.applications.http.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.astrogrid.applications.description.base.ApplicationKind;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotLoadedException;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.registry.IvornUtil;
import org.astrogrid.applications.description.registry.RegistryQueryLocator;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;

/**
 * A RegistryQuerier that gets generates a list of Http applications from an
 * actual registry. Much of the code pinched from Workflow's
 * RegistryApplicationRegistry. Handles any transformation from objects/docs in
 * the registry to the format we want to work with in the CEA.
 * @author jdt
 * @author pharriso@eso.org 30-May-2005 refactored to make dealing with v10 registry easier - moved stuff to abstract RegistryQuerier
 */
public class RegistryQuerierImpl extends AbstractRegistryQuerier {
 
 
    /**
    * @param locator
    */
   public RegistryQuerierImpl(RegistryQueryLocator locator) {
      super(locator);
   }

   /**
     * Query string to select only CeaHttpApplicationDefinitions in the registry
     */
    private final static String LIST_QUERY_STRING = "Select * from Registry " + 
    " where @xsi:type='cea:CeaHttpApplication'";
   

    /**
     * Get the names of all the applications in the registry matching
     * LIST_QUERY_STRING An empty List could indicate entries in the registry
     * aren't in the right format and can't be unmarshalled.
     * 
     * @return List of application name strings
     * @throws RegistryException on registry comms probs
     */
    protected List listApplications() throws RegistryException {
        if (log.isTraceEnabled()) {
            log.trace("listApplications() - start");
        }

        try {
            log.debug("Querying registry: "+LIST_QUERY_STRING);
            Document doc = service.searchFromSADQL(LIST_QUERY_STRING);
            assert doc != null;
            NodeList nl = doc.getElementsByTagNameNS("*","identifier");
            log.debug("...got doc with "+nl.getLength()+" Identifier elements");
            List namesList = new ArrayList();
            for (int i = 0; i < nl.getLength(); i++) {
               
               String element = XMLUtils.getChildCharacterData((Element)nl.item(i));
               log.debug("found application="+element);
               namesList.add(element);
               log.debug("Adding "+element+" to list of applications");
            
            }
                     
            if (log.isTraceEnabled()) {
                log.trace("listApplications() - end - return value = " + namesList);
            }
            return namesList;
        } catch (RegistryException e) {
            log.error("listApplications Failed with exception from registry", e);
            throw e;
        }
    }
    /**
     * Get application by name from the registry
     * 
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#getDescriptionFor(java.lang.String)
     * @param applicationName application name in registry as returned by
     *            listApplications()
     * @return a WebHttpApplication containing all the details stored in the
     *         registry
    * @throws ApplicationDescriptionNotLoadedException
    */
    private CeaHttpApplicationDefinition getDescriptionFor(String applicationName) throws ApplicationDescriptionNotLoadedException  {
        if (log.isTraceEnabled()) {
            log.trace("getDescriptionFor(String applicationName = " + applicationName + ") - start");
        }

            try {
               //have to add the ivo:// part as the applications are stored internally with just the name
               final Document doc = service.getResourceByIdentifier(applicationName);
               assert doc != null;

               final CeaHttpApplicationDefinition webApplication = unmarshallHttpApplication(doc);
               if (log.isTraceEnabled()) {
                   log.trace("getDescriptionFor(String) - end - return value = " + webApplication);
               }
               return webApplication;
            } catch (Exception e) {
               throw new ApplicationDescriptionNotLoadedException("application "+applicationName+" not loaded",e);
            } 
     }
    

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        TestSuite suite = new TestSuite();
        suite.addTest(new InstallationTest("testRegistryEndPointPropertyFound"));
        suite.addTest(new InstallationTest("testRegistryServiceFound"));
        suite.addTest(new InstallationTest("testRegistryContainsOneOrMoreHttpApplications"));
        return suite;
    }
    /**
     * Some installation tests.
     * There's a certain amount of redundancy here (test 2 will fail if test 1 does etc)
     * but the tests are performed separately to make problems
     * clearer to the user.
     * @author jdt
     */
    public class InstallationTest extends TestCase {
        public InstallationTest(String arg0) {
            super(arg0);
        }
        /**
         * Can we find the endpoint of the registry in properties?
         * Not convinced this should be here, but it has
         * the side effect that it helps us determine if *any*
         * properties can be found 
         *
         */
        public void testRegistryEndPointPropertyFound() {
            String key = RegistryDelegateFactory.QUERY_URL_PROPERTY;
            try {
                String endPoint = SimpleConfig.getProperty(key);
                assertNotNull("The Property "+key+" is not set - check your configuration file");
            } catch (ConfigException e ) {
                fail("There was a problem accessing the configuration file: "+e.getMessage());
            } catch (PropertyNotFoundException e) {
                fail("The Property "+key+" is not set - check your configuration file");
            }
        }
        public void testRegistryServiceFound() throws RegistryException {
            assertNotNull("No registry service found", service);
            // The registry client seems to return a non-null service
            // even when it is up.  The following is a hack that should
            // fail when the registry is down.
            // @TODO replace with something more elegant when the registry allows
            Document doc = service.loadRegistry();
            assertNotNull("No registry service found.  Check that your registry is up and running and that the property org.astrogrid.registry.query.endpoint is set correctly.", doc);
        }
        public void testRegistryContainsOneOrMoreHttpApplications() throws IOException {
            Collection apps = getHttpApplications();
            assertFalse("There were no http applications in the registry.   Check your registry entries and this error might go away.", apps.isEmpty());
        }

    }
    /**
     * Returns a List of WebHttpApplications from the Registry. Note, that if
     * this method returns an empty list, it might indicate communication
     * problems with the registry, or that the registry entries are invalid and
     * not unmarshalable
     * 
     * @throws
     * @see org.astrogrid.applications.http.registry.RegistryQuerier#getHttpApplications()
     */
    public Collection getHttpApplications() throws IOException {
        if (log.isTraceEnabled()) {
            log.trace("getHttpApplications() - start");
        }
        

        try {
            List apps = new ArrayList();

            List names = listApplications();
            log.debug("Got a list of "+names.size()+" applications");
            Iterator it = names.iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                    try {
                     final CeaHttpApplicationDefinition description = getDescriptionFor(name);
                       ApplicationKind type=description.getApplicationDefinition().getApplicationType().get(0);
                       if (ApplicationKind.HTTP.equals(type)) {
                           apps.add(description);
                           log.debug("Adding "+description.getShortName()+" to list");
                       } else {
                           log.warn("Description "+description.getShortName()+" is listed in the registry as a CeaHttpApplicationDefinition, but is actually a "+type+" app.  Must be an http app.");
                           log.warn("=>not adding to list");
                       }
                  } catch (ApplicationDescriptionNotLoadedException e1) {
                    log.warn("application not added to list", e1);
                  }
              
            }

            if (log.isTraceEnabled()) {
                log.trace("getHttpApplications() - end - return value = " + apps);
            }
            return apps;
        } catch (RegistryException e) {
            log.error("getHttpApplications(): RegistryException", e);
            e.printStackTrace();
            throw new IOException("Problem communicating with Registry");
        }
    }

    public String toString() {
        if (log.isTraceEnabled()) {
            log.trace("toString() - start");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("[RegistryQuerierImpl:");
        buffer.append(" log: ");
        buffer.append(log);
        buffer.append(" service: ");
        buffer.append(service);
        buffer.append(" LIST_QUERY_STRING: ");
        buffer.append(LIST_QUERY_STRING);
        buffer.append("]");
        String returnString = buffer.toString();
        if (log.isTraceEnabled()) {
            log.trace("toString() - end - return value = " + returnString);
        }
        return returnString;
    }

    /* (non-Javadoc)
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "RegistryQuerierImpl";
    }

    /* (non-Javadoc)
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Talks to the registry to get http applications";
    }
}