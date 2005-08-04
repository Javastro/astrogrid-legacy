/*$Id: AllTests.java,v 1.8 2005/08/04 09:40:11 clq2 Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.registry.integration;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Registry");
        //$JUnit-BEGIN$
        suite.addTestSuite(RegistryFunctionTest.class);
        suite.addTestSuite(QueryRegistryClientTest.class);
        suite.addTestSuite(QueryOAIRegistryTest.class);
        suite.addTestSuite(HarvestRegistryTest.class);
        suite.addTestSuite(XQueryRegistryTest.class);
        suite.addTestSuite(QueryRegistryClientADQLTest.class);
        suite.addTestSuite(UpdateRegistryTest.class);
        suite.addTestSuite(RegistryServiceClientTest.class);
        suite.addTestSuite(RegistryInstallationTest.class);
        suite.addTestSuite(HarvestDaemonTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.8  2005/08/04 09:40:11  clq2
 kevin's second batch

 Revision 1.7.32.1  2005/07/26 12:56:24  KevinBenson
 added xquerysearch on it

 Revision 1.7  2005/05/10 16:33:13  clq2
 1125

 Revision 1.6.138.1  2005/04/22 13:30:58  nw
 added in all new tests

 Revision 1.6  2004/08/25 11:40:50  KevinBenson
 changes back to a previous integration test

 Revision 1.3  2004/05/07 15:32:36  pah
 more registry tests to flush out the fact that new entries are not being added

 Revision 1.2  2004/04/15 12:12:28  nw
 testing

 Revision 1.1  2004/04/15 11:55:16  nw
 added registy installation test
 
 */