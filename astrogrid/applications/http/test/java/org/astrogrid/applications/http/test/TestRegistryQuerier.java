/* $Id: TestRegistryQuerier.java,v 1.4 2004/09/01 15:42:26 jdt Exp $
 * Created on 30-July-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.applications.http.test;

import junit.framework.Test;

import org.astrogrid.applications.http.registry.AbstractRegistryQuerier;
import org.astrogrid.registry.beans.cea.CeaHttpApplicationType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/**
 * Returns pretend meta data about the TestWebServer test services
 * 
 * @author jdt
 */
public class TestRegistryQuerier extends AbstractRegistryQuerier {

    /**
     * Ctor 
     * @throws ValidationException
     * @throws MarshalException
     */
    public TestRegistryQuerier() throws MarshalException, ValidationException {

      addApplication("helloWorld-app.xml");
      addApplication("Echoer-app.xml");
      addApplication("HelloYou-app.xml"); 
      addApplication("Adder-app.xml");
      addApplication("Adder-post-app.xml");
      addApplication("Bad404-app.xml");
      addApplication("BadTimeOut-app.xml");
      addApplication("BadMalformedURL-app.xml");
      addApplication("Adder-preprocess-app.xml");
        
    }
    
    /**
     * Get a specific application by its filename
     * Useful additional method for testing purposes
     * You need to check that you don't get a null
     * @param name filename of xml describing application
     * @return the unmarshalled CeaHttpApplicationType
     */
    public CeaHttpApplicationType getHttpApplication(final String name) {
        return (CeaHttpApplicationType) applications.get(name);
    }

    /**
     * Add a CeaHttpApplicationType to our "registry" given the object serialised to xml
     * @param file filename of serialised object
     * @throws MarshalException 
     * @throws ValidationException
     */
    private void addApplication(String file) throws MarshalException, ValidationException {
        FileUnmarshaller unmarshaller = new FileUnmarshaller(CeaHttpApplicationType.class);
        CeaHttpApplicationType testApplication = (CeaHttpApplicationType) unmarshaller.unmarshallFromFile(file);
        applications.put(file, testApplication);
    }

    /* (non-Javadoc)
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "TestRegistryQuerier";
    }

    /* (non-Javadoc)
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "A test RegistryQuerier for unit testing";
    }

    /* (non-Javadoc)
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     * No installation tests required, since only used for unit testing.
     */
    public Test getInstallationTest() {
        return null;
    }


}