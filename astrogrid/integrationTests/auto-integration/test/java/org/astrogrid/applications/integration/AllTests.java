/*$Id: AllTests.java,v 1.2 2004/04/21 13:41:34 nw Exp $
 * Created on 20-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 20-Apr-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("CEA");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ApplicationsInstallationTest.class));
        suite.addTest(new TestSuite(DataCenterIntegrationTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.2  2004/04/21 13:41:34  nw
set up applications integration tests

Revision 1.1  2004/04/21 10:43:03  nw
fixed complaint about lacking interface field
 
*/