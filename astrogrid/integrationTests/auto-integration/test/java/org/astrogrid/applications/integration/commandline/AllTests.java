/*$Id: AllTests.java,v 1.1 2004/07/01 11:43:33 nw Exp $
 * Created on 22-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.commandline;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Jun-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for CEA Commandline Provider");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(CommandLineVOSpaceIndirectExecutionTest.class));
        suite.addTest(new TestSuite(CommandLineServerInstallationTest.class));
        suite.addTest(new TestSuite(CommandLineDirectExecutionTest.class));
        suite.addTest(new TestSuite(CommandLineFileIndirectExecutionTest.class));
        //$JUnit-END$
        return suite;
    }
}
/* 
$Log: AllTests.java,v $
Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/