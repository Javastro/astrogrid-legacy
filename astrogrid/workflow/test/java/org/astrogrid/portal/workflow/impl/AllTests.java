/*$Id: AllTests.java,v 1.3 2004/04/14 13:46:06 nw Exp $
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
        suite.addTest(new TestSuite(RegistryApplicationRegistryTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.3  2004/04/14 13:46:06  nw
implemented cut down workflow store interface over Ivo Delegate

Revision 1.2  2004/03/11 13:53:51  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.1  2004/03/11 13:37:52  nw
tests for impls
 
*/