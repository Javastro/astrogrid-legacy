/*$Id: AllTests.java,v 1.1 2004/07/09 09:32:12 nw Exp $
 * Created on 08-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.impl.scripting;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jul-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.jes.jobscheduler.impl.scripting");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(AnnotatorTest.class));
        suite.addTest(new TestSuite(CompilerTranslatorTest.class));
        suite.addTest(new TestSuite(DefaultTransformersTest.class));
        suite.addTest(new TestSuite(DevelopmentJarPathsTest.class));
        suite.addTest(new TestSuite(InterpreterEnvironmentTest.class));
        suite.addTest(new TestSuite(ScriptedAbortJobTest.class));
        suite.addTest(new TestSuite(ScriptedResumeJobSuccesssTest.class));
        suite.addTest(new TestSuite(ScriptedSchedulerSetupTest.class));
        suite.addTest(new TestSuite(ScriptedSubmitNewJobNotifierFailsTest.class));
        suite.addTest(new TestSuite(ScriptedSubmitNewJobSuccessTest.class));
        suite.addTest(new TestSuite(WorkflowInterpreterTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.1  2004/07/09 09:32:12  nw
merged in scripting workflow interpreter from branch
nww-x-workflow-extensions
 
*/