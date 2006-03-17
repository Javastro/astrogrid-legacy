/*$Id: CEAComponentManagerFactoryTest.java,v 1.9 2006/03/17 17:50:58 clq2 Exp $
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
    }
    public void testDefaulttGetInstance() {
        CEAComponentManager man = CEAComponentManagerFactory.getInstance();
        assertNotNull(man);
        assertTrue(man instanceof JavaClassCEAComponentManager);
        man.getContainer().verify();
        man.informationHTML();
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
    
    public void testStop() {
        CEAComponentManagerFactory.stop();
    }
}


/* 
$Log: CEAComponentManagerFactoryTest.java,v $
Revision 1.9  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.7  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.4.20.2  2006/01/31 21:39:07  gtr
Refactored. I have altered the configuration code slightly so that the JUnit tests can impose a Configuration instance to configure the tests. I have also fixed up almost all the bad tests for commandline and http.

Revision 1.4.20.1  2005/12/18 14:48:25  gtr
Refactored to allow the component managers to pass their unit tests and the fingerprint JSP to work. See BZ1492.

Revision 1.4  2005/08/10 17:45:10  clq2
cea-server-nww-improve-tests

Revision 1.3.8.1  2005/07/21 18:12:38  nw
fixed up tests - got all passing, improved coverage a little.
still could do with testing the java apps.

Revision 1.3  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.2.172.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.2.158.1  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/