/*$Id: AllTests.java,v 1.6 2004/08/05 14:38:30 nw Exp $
 * Created on 27-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.jes.jobscheduler.impl.groovy;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Jul-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }
    public static Test suite() {
        TestSuite suite = new TestSuite(
            "Test for org.astrogrid.jes.jobscheduler.impl.groovy");
        //$JUnit-BEGIN$
        suite.addTestSuite(GroovyResumeJobSuccesssTest.class);
        suite.addTestSuite(GroovySchedulerSetupTest.class);
        suite.addTestSuite(SetFeatureTest.class);
        suite.addTestSuite(GroovyAbortJobTest.class);
        suite.addTestSuite(ScriptFeatureTest.class);
        suite.addTestSuite(ActivityStatusStoreTest.class);
        suite.addTestSuite(FlowFeatureTest.class);
        suite.addTestSuite(RuleStoreTest.class);
        suite.addTestSuite(UnsetFeatureTest.class);
        suite.addTestSuite(GroovySubmitNewJobSuccessTest.class);
        suite.addTestSuite(GroovyInterpreterFactoryTest.class);
        suite.addTestSuite(RuleTest.class);
        suite.addTestSuite(SequenceFeatureTest.class);
        suite.addTestSuite(FlowErrorFeatureTest.class);
        suite.addTestSuite(ActivityStatusTest.class);
        suite.addTestSuite(GroovySubmitNewJobNotifierFailsTest.class);
        suite.addTestSuite(XStreamPicklerTest.class);
        suite.addTestSuite(StepErrorFeatureTest.class);
        suite.addTestSuite(FlowSingletonFeatureTest.class);
        suite.addTestSuite(ScriptErrorFeatureTest.class);
        suite.addTestSuite(AnnotatorTest.class);
        suite.addTestSuite(FlowEmptyFeatureTest.class);
        suite.addTestSuite(JesInterfaceTest.class);
        suite.addTestSuite(GroovyTransformersTest.class);
        suite.addTestSuite(EmptyWorkflowFeatureTest.class);
        suite.addTestSuite(StepFeatureTest.class);
        suite.addTestSuite(VarsTest.class);
        suite.addTestSuite(CompilerTest.class);
        suite.addTestSuite(GroovyAssumptionsTest.class);
        suite.addTestSuite(ScopeFeatureTest.class);
        suite.addTestSuite(IfThenTrueFeatureTest.class);
        suite.addTestSuite(IfThenFalseFeatureTest.class);
        suite.addTestSuite(IfThenElseTrueFeatureTest.class);
        suite.addTestSuite(IfThenElseFalseFeatureTest.class);
        suite.addTestSuite(WhileOnceFeatureTest.class);
        suite.addTestSuite(WhileNeverFeatureTest.class);
        suite.addTestSuite(WhileManyFeatureTest.class);
        suite.addTestSuite(ForEmptyFeatureTest.class);
        suite.addTestSuite(ForSingletonFeatureTest.class);
        suite.addTestSuite(ForManyFeatureTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.6  2004/08/05 14:38:30  nw
 tests for sequential for construct

 Revision 1.5  2004/08/05 10:57:03  nw
 tests for while construct

 Revision 1.4  2004/08/05 09:59:46  nw
 tests for if construct

 Revision 1.3  2004/08/03 14:27:38  nw
 added set/unset/scope features.

 Revision 1.2  2004/07/30 15:42:34  nw
 merged in branch nww-itn06-bz#441 (groovy scripting)

 Revision 1.1.2.4  2004/07/30 14:00:10  nw
 first working draft

 Revision 1.1.2.3  2004/07/28 16:24:23  nw
 finished groovy beans.
 moved useful tests from old python package.
 removed python implemntation

 Revision 1.1.2.2  2004/07/27 23:50:09  nw
 removed betwixt (duff). replaces with xstream.

 Revision 1.1.2.1  2004/07/27 23:37:59  nw
 refactoed framework.
 experimented with betwixt - can't get it to work.
 got XStream working in 5 mins.
 about to remove betwixt code.
 
 */