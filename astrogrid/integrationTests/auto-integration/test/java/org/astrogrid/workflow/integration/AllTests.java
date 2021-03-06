/*$Id: AllTests.java,v 1.17 2005/03/14 22:03:53 clq2 Exp $
 * Created on 12-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Mar-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Workflow");
        suite.addTest(new TestSuite(JesInstallationTest.class));
        suite.addTest(new TestSuite(JesSelfTest.class));

        suite.addTest(new TestSuite(MySpaceIntegrationTest.class));
        suite.addTest(new TestSuite(RegistryIntegrationTest.class));
        

        suite.addTest(new TestSuite(SimpleCommandlineWorkflowEndToEndTest.class));
        suite.addTest(new TestSuite(SimpleJavaWorkflowEndToEndTest.class));
        suite.addTest(new TestSuite(SimpleDSAWorkflowEndToEndTest.class));        
        suite.addTest(new TestSuite(MySpaceCommandlineWorkflowEndToEndTest.class));
        
        suite.addTest(new TestSuite(FairlyGroovyWorkflowTest.class));
        suite.addTest(new TestSuite(DynamicWorkflowTest.class));
        
        suite.addTest(new TestSuite(CompositeWorkflowEndToEndTest.class));
        suite.addTest(new TestSuite(FlowWorkflowTest.class));

        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.17  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.16.104.1  2005/03/11 17:17:17  nw
changed bunch of tests to use FileManagerClient instead of VoSpaceClient

Revision 1.16  2004/08/27 13:51:41  nw
added test that checks operation of dynamically-modified workflows.

Revision 1.15  2004/08/04 16:50:18  nw
added test for scripting extensions to workflow

Revision 1.14  2004/07/01 11:47:39  nw
cea refactor

Revision 1.13  2004/04/26 12:17:56  nw
*** empty log message ***

Revision 1.12  2004/04/23 16:12:49  pah
added the myspace testapp test

Revision 1.11  2004/04/23 00:27:56  nw
reorganized end-to-end tests. added test to verify flows are executed in parallel

Revision 1.10  2004/04/21 13:42:59  nw
set up applications integration tests

Revision 1.9  2004/04/21 10:57:46  nw
added jes installation test

Revision 1.8  2004/04/15 23:11:20  nw
tweaks

Revision 1.7  2004/04/15 13:02:38  jdt
whoops.  I must have caught this one mid cvs commit while
it was out of synch with the other tests.

Revision 1.6  2004/04/15 12:38:24  jdt
Think I've fixed a typo?

Revision 1.5  2004/04/15 12:12:28  nw
testing

Revision 1.4  2004/04/14 10:16:40  nw
added to the workflow integration tests

Revision 1.3  2004/04/08 14:50:54  nw
polished up the workflow integratioin tests

Revision 1.2  2004/04/06 12:08:30  nw
fixes

Revision 1.1  2004/03/16 17:48:34  nw
first stab at an auto-integration project
 
*/