/* $Id: RegistryQuerierImpl.java,v 1.8 2005/03/02 12:38:46 clq2 Exp $
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.types.ApplicationKindType;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.beans.cea.CeaHttpApplicationType;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.util.DomHelper;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A RegistryQuerier that gets generates a list of Http applications from an
 * actual registry. Much of the code pinched from Workflow's
 * RegistryApplicationRegistry. Handles any transformation from objects/docs in
 * the registry to the format we want to work with in the CEA.
 * 
 * @author jdt
 */
public class RegistryQuerierImpl implements RegistryQuerier {
    private static final Log log = LogFactory.getLog(RegistryQuerierImpl.class);

    private RegistryService service;

    /**
     * ctor allowing delegate to find own registry end-point
     */
    public RegistryQuerierImpl() {
        if (log.isTraceEnabled()) {
            log.trace("RegistryQuerierImpl() - start");
        }

        service = RegistryDelegateFactory.createQuery();
        assert service != null;

        if (log.isTraceEnabled()) {
            log.trace("RegistryQuerierImpl() - end");
        }
    }

    /**
     * Construct a new RegistryQuerierImpl
     * 
     * @param endpoint endpoint for the astrogrid registry web service
     */
    public RegistryQuerierImpl(URL endpoint) {
        if (log.isTraceEnabled()) {
            log.trace("RegistryQuerierImpl(URL endpoint = " + endpoint + ") - start");
        }

        service = RegistryDelegateFactory.createQuery(endpoint);
        assert service != null;

        if (log.isTraceEnabled()) {
            log.trace("RegistryQuerierImpl(URL) - end");
        }
    }

    /**
     * Query string to select only CeaHttpApplications in the registry
     */
    /*
    private final static String LIST_QUERY_STRING =   "<query><selectionSequence>" +
    "<selection item='searchElements' itemOp='EQ' value='vr:Resource'/>" +
    "<selectionOp op='$and$'/>" +
    "<selection item='@xsi:type' itemOp='EQ' value='CeaHttpApplicationType'/>"  +
    "<selectionOp op='OR'/>" +
    "<selection item='@xsi:type' itemOp='EQ' value='cea:CeaHttpApplicationType'/>"  +
    "</selectionSequence></query>";
    */
    private final static String LIST_QUERY_STRING = "Select * from Registry " + 
    " where @xsi:type='cea:CeaHttpApplicationType'";
   

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
            //Document doc = service.submitQuery(LIST_QUERY_STRING);
            Document doc = service.searchFromSADQL(LIST_QUERY_STRING);
            //.submitQuery(LIST_QUERY_STRING);
            assert doc != null;
            NodeList nl = doc.getElementsByTagNameNS("*", "Identifier");
            log.debug("...got doc with "+nl.getLength()+" Identifier elements");
            List namesList = new ArrayList();
            for (int i = 0; i < nl.getLength(); i++) {
                IdentifierType it;
                try {
                    it = (IdentifierType) Unmarshaller.unmarshal(IdentifierType.class, nl.item(i));
                    String name = it.getAuthorityID() + "/" + it.getResourceKey();
                    namesList.add(name);
                    log.debug("Adding "+name+" to list of applications");
                } catch (MarshalException e1) {
                    log.error("listApplications(): problem unmarshalling " + nl.item(i) + " cannot add to list", e1);
                } catch (ValidationException e1) {
                    log.error("listApplications(): problem validating " + nl.item(i) + " cannot add to list", e1);
                }
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
     * @throws CastorException castor unmarshalling probs
     * @throws RegistryCallException on registry comms probs
     */
    private CeaHttpApplicationType getDescriptionFor(String applicationName) throws RegistryException, CastorException {
        if (log.isTraceEnabled()) {
            log.trace("getDescriptionFor(String applicationName = " + applicationName + ") - start");
        }

        try {
            final Document doc = service.getResourceByIdentifier(applicationName);
            assert doc != null;

            final CeaHttpApplicationType webApplication = (CeaHttpApplicationType) unmarshal(doc,
                    CeaHttpApplicationType.class, "Resource");
            
            if (log.isTraceEnabled()) {
                log.trace("getDescriptionFor(String) - end - return value = " + webApplication);
            }
            return webApplication;
        } catch (CastorException e) {
            log.error("getDescriptionFor " + applicationName + " - castor failed to parse result", e);
            throw e;
        } catch (RegistryException e) {
            log.error("getDescriptionFor " + applicationName + " - exception from registry", e);
            throw e;
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
            //@TODO in SNAPSHOT get this from RegistryDelegateFactory
            //RegistryDelegateFactory.;
            String key = "org.astrogrid.registry.query.endpoint";
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
            Object doc = service.loadRegistry();
            assertNotNull("No registry service found.  Check that your registry is up and running and that the property org.astrogrid.registry.query.endpoint is set correctly.", doc);
        }
        public void testRegistryContainsOneOrMoreHttpApplications() throws IOException {
            Collection apps = getHttpApplications();
            assertFalse("There were no http applications in the registry.   Check your registry entries and this error might go away.", apps.isEmpty());
        }

    }
    /**
     * Unmarshall a node of a Document into a class
     * 
     * @param doc the document
     * @param clazz the class you hope to get back
     * @param elementName the element name in the document you hope matches the
     *            class
     * @return object that will need casting...
     * @throws MarshalException
     * @throws ValidationException
     * @throws RegistryCallException
     */
    private Object unmarshal(final Document doc, final Class clazz, final String elementName) throws MarshalException,
            ValidationException, RegistryException {
        if (log.isTraceEnabled()) {
            log.trace("unmarshal(Document doc = " + doc + ", Class clazz = " + clazz + ", String elementName = "
                    + elementName + ") - start");
        }

        if (log.isDebugEnabled()) {
        	log.debug("Unmarshalling document:");
        	log.debug(DomHelper.DocumentToString(doc));
        	log.debug("=======================");
        }
        //      navigate down to the bit we're interested in.
        final NodeList nls = doc.getElementsByTagNameNS("*", elementName);
        if (nls.getLength() == 0) {
            throw new RegistryException("Registry entry has no " + elementName + " Element");
        }
        final Element ns = (Element) nls.item(0);
        ns.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance"); //bug-fix work around.
        Object returnObject = Unmarshaller.unmarshal(clazz, ns);
        if (log.isTraceEnabled()) {
            log.trace("unmarshal(Document, Class, String) - end - return value = " + returnObject);
        }
        return returnObject;
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
                    final CeaHttpApplicationType description = getDescriptionFor(name);
                    ApplicationKindType type=description.getApplicationDefinition().getApplicationKind();
                    if (ApplicationKindType.HTTP.equals(type)) {
                        apps.add(description);
                        log.debug("Adding "+description.getShortName()+" to list");
                    } else {
                        log.warn("Description "+description.getShortName()+" is listed in the registry as a CeaHttpApplication, but is actually a "+type+" app.  Must be an http app.");
                        log.warn("=>not adding to list");
                    }
                } catch (CastorException e) {
                    log.info("getHttpApplications(): problem unmarshalling " + name + ", not adding to list", e);
                }
            }

            if (log.isTraceEnabled()) {
                log.trace("getHttpApplications() - end - return value = " + apps);
            }
            return apps;
        } catch (RegistryException e) {
            log.error("getHttpApplications(): RegistryException", e);
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