/*$Id: AllTests.java,v 1.5 2004/11/12 18:14:43 clq2 Exp $
 * Created on 10-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 10-Mar-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for Workflow System Implementations");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(BasicWorkflowBuilderTest.class));
        suite.addTest(new TestSuite(FileApplicationRegistryTest.class));
        suite.addTest(new TestSuite(FileWorkflowStoreTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.5  2004/11/12 18:14:43  clq2
nww-itn07-590b again.

Revision 1.3.116.1  2004/11/10 13:34:10  nw
Removed RegistryApplicationRegitryTest - not possible to unit-test this.
replicated in code in integration tests.

Revision 1.3  2004/04/14 13:46:06  nw
implemented cut down workflow store interface over Ivo Delegate

Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:52  nw
tests for impls
 
*/