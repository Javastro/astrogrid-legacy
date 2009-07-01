/*
 * $Id: AbstractServiceDefinition.java,v 1.1 2009/07/01 14:28:43 pah Exp $
 * 
 * Created on 26 Jun 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.ivoa.resource.Service;

import org.astrogrid.applications.description.jaxb.DescriptionValidator;
import org.astrogrid.applications.description.jaxb.DescriptionValidator.Validation;
import org.astrogrid.component.descriptor.ComponentDescriptor;

public abstract class AbstractServiceDefinition implements ServiceDefinitionFactory,
ComponentDescriptor {


    public String getDescription() {
        try {
            Service desc = getCECDescription();

            return desc.getTitle()+" id="+ desc.getIdentifier(); 
        } catch (ServiceDescriptionException e) {

            return e.getMessage();
        }
    }

    private static Service descForTest;
    private static String message;
    public Test getInstallationTest() {
        try {
            descForTest = getCECDescription();
        } catch (ServiceDescriptionException e) {
            descForTest = null;
            message = e.getMessage();

        }
        return new TestSuite(InstallationTest.class);
    }


    public static class InstallationTest extends TestCase {


        public void testServiceDescription() throws Exception {
            assertNotNull(message, descForTest);
            Validation isvalid = DescriptionValidator.validate(descForTest);
            assertTrue(isvalid.message, isvalid.valid );
        }
    }

}


/*
 * $Log: AbstractServiceDefinition.java,v $
 * Revision 1.1  2009/07/01 14:28:43  pah
 * registration template directly argument of builder object - removed from config
 *
 */
