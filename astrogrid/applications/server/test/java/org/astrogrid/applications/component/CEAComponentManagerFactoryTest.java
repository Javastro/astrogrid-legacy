/*$Id: CEAComponentManagerFactoryTest.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 02-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
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
        JavaClassCEAComponentManagerTest.setupConfigForJavaClassComponentManager();
    }
    public void testDefaulttGetInstance() {
        CEAComponentManager man = CEAComponentManagerFactory.getInstance();
        assertNotNull(man);
        assertTrue(man instanceof JavaClassCEAComponentManager);
        // would be nice to test that the picocontainer has been started too, but no method on the container for this.
    }
    
    public static class MyJavaClassCEAComponentManager extends JavaClassCEAComponentManager {
    };
    
  
    public void testConfguredGetInstance() {
        SimpleConfig.getSingleton().setProperty(CEAComponentManagerFactory.COMPONENT_MANAGER_IMPL,MyJavaClassCEAComponentManager.class.getName());
        CEAComponentManager man = CEAComponentManagerFactory.getInstance();
        assertNotNull(man);
        assertTrue(man instanceof MyJavaClassCEAComponentManager);        
    }
    public void testSuite() {
        Test suite = CEAComponentManagerFactory.suite();
        assertNotNull(suite);
    }
}


/* 
$Log: CEAComponentManagerFactoryTest.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/