/*$Id: AllTests.java,v 1.13 2005/08/01 08:15:52 clq2 Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

/** The root of all the integration tests.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Integration Tests");
        //$JUnit-BEGIN$
        //$JUnit-END$
        suite.addTest(org.astrogrid.installation.integration.AllTests.suite());
        suite.addTest(org.astrogrid.registry.integration.AllTests.suite());
        suite.addTest(org.astrogrid.community.integration.AllTests.suite());
//      suite.addTest(org.astrogrid.store.integration.AllTests.suite());    
//        suite.addTest(org.astrogrid.datacenter.integration.AllTests.suite()); 
        suite.addTest(org.astrogrid.applications.integration.AllTests.suite());   
        suite.addTest(org.astrogrid.applications.integration.commandline.AllTests.suite());
        suite.addTest(org.astrogrid.applications.integration.datacenter.AllTests.suite());
        suite.addTest(org.astrogrid.applications.integration.http.AllTests.suite());
        suite.addTest(org.astrogrid.workflow.integration.AllTests.suite());
        suite.addTest(org.astrogrid.portal.integration.AllTests.suite());
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.13  2005/08/01 08:15:52  clq2
Kmb 1293/1279/intTest1 FS/FM/Jes/Portal/IntTests

Revision 1.12.46.1  2005/07/12 11:22:06  KevinBenson
commented out of datacenter tests

Revision 1.12  2005/04/08 15:00:24  clq2
dave-dev-200503181302 taking out old myspace links.

Revision 1.11.42.1  2005/03/18 14:20:18  dave
Removed references to MySpace from integration tests.
(two tests in datacenter still need modifying)

Revision 1.11  2004/11/24 19:49:22  clq2
nww-itn07-659

Revision 1.10  2004/11/23 15:45:31  jdt
Merge from INT_JDT_757 (restoring mch's tests)

Revision 1.9.2.1  2004/11/23 15:12:52  jdt
Restored the broken tests that I removed in a hissy fit a week ago.

Revision 1.8  2004/07/01 11:45:45  nw
added in cea tests

Revision 1.7  2004/04/21 10:47:58  nw
added test suite

Revision 1.6  2004/04/21 10:43:25  nw
added more tests

Revision 1.5  2004/04/19 08:53:25  nw
added in the datacenter tests

Revision 1.4  2004/04/15 23:11:20  nw
tweaks

Revision 1.3  2004/04/15 12:27:11  nw
drags tests together

Revision 1.2  2004/04/15 12:12:28  nw
testing

Revision 1.1  2004/04/15 11:55:30  nw
binds everything together
 
*/
