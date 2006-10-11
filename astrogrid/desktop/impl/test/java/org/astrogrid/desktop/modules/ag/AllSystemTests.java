/*$Id: AllSystemTests.java,v 1.2 2006/10/11 10:40:28 nw Exp $
 * Created on 03-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.desktop.ACRTestSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/** run all the tests of the ag module in a single acr instance.
 * these tests connect to external services - hence are called 'system tests'
 *  - as is possible that will expose errors / temporary failure in remote services, 
 *  rather than local implementation.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Aug-2005
 *
 */
public class AllSystemTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllSystemTests.suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("AstroGrid Module System Tests");
       // suite.addTestSuite(RegistrySystemTest.class);
       // suite.addTestSuite(RegistryRmiSystemTest.class);
       // suite.addTestSuite(RegistryRpcSystemTest.class);
        suite.addTestSuite(MyspaceSystemTest.class);
        suite.addTestSuite(MyspaceRmiSystemTest.class);
        suite.addTestSuite(MyspaceRpcSystemTest.class);       
       suite.addTestSuite(ApplicationsSystemTest.class);
        suite.addTestSuite(ApplicationsRmiSystemTest.class);
        suite.addTestSuite(ApplicationsRpcSystemTest.class);    
        suite.addTestSuite(JobsSystemTest.class);
        suite.addTestSuite(JobsRmiSystemTest.class);
        suite.addTestSuite(JobsRpcSystemTest.class);     

        return new ACRTestSetup(suite,true);       
    }
}


/* 
$Log: AllSystemTests.java,v $
Revision 1.2  2006/10/11 10:40:28  nw
removed - won't test deprecated classes.

Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.2  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.1.66.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.66.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/