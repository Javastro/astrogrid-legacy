/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.component;

import org.astrogrid.config.SimpleConfig;

import junit.framework.Test;
import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Jun-2004
 *
 */
public class CEAComponentManagerFactoryTest extends TestCase {
    /**
     * Constructor for CEAComponentManagerFactoryTest.
     * @param arg0
     */
    public CEAComponentManagerFactoryTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        CEAComponentManagerFactory.clearInstance();
    }
    public void testDefaulttGetInstance() {
        CEAComponentManager man = CEAComponentManagerFactory.getInstance();
        assertNotNull(man);
        assertTrue(man instanceof BaseCEAComponentManager);
        man.getContainer().verify();
        man.informationHTML();
        // would be nice to test that the picocontainer has been started too, but no method on the container for this.
    }
    
    public static class MyCEAComponentManager extends BaseCEAComponentManager {
    };
    
  
    public void testConfguredGetInstance() {
        SimpleConfig.getSingleton().setProperty(CEAComponentManagerFactory.COMPONENT_MANAGER_IMPL,MyCEAComponentManager.class.getName());
        CEAComponentManager man = CEAComponentManagerFactory.getInstance();
        assertNotNull(man);
        assertTrue(man instanceof MyCEAComponentManager);        
    }
    public void testSuite() {
        Test suite = CEAComponentManagerFactory.suite();
        assertNotNull(suite);
    }
    
    public void testStop() {
        CEAComponentManagerFactory.stop();
    }
}