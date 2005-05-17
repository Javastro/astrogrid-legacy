/*$Id: AllTests.java,v 1.2 2005/05/17 23:07:54 jdt Exp $
 * Created on 22-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.integration.commandlinewindows;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author John Taylor jdt@roe.ac.uk
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
Revision 1.2  2005/05/17 23:07:54  jdt
merge from cea_jdt_1012

Revision 1.1.2.2  2005/05/09 17:49:39  jdt
now the commandline version is plumbed in too

Revision 1.1.2.1  2005/05/07 00:51:16  jdt
Added tests for a windows version of the commandline app.

Revision 1.1  2004/07/01 11:43:33  nw
cea refactor
 
*/