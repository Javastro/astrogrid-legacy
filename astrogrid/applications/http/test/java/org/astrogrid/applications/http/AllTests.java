/*$Id: AllTests.java,v 1.4 2004/09/01 15:42:26 jdt Exp $
 * Created on 29-Julyish-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.http;
import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * Auto generated from Eclipse
 * @author jdt/Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.astrogrid.applications.http");
        //$JUnit-BEGIN$
        suite.addTestSuite(HttpApplicationProviderTest.class);
        suite.addTestSuite(HttpServiceClientTest.class);
        suite.addTestSuite(HttpApplicationDescriptionTest.class);
        suite.addTestSuite(HttpApplicationCEAServerTest.class);
        suite.addTestSuite(HttpApplicationDescriptionLibraryTest.class);
        //$JUnit-END$
        return suite;
    }
}
/* 
 $Log: AllTests.java,v $
 Revision 1.4  2004/09/01 15:42:26  jdt
 Merged in Case 3

 Revision 1.1.2.3  2004/08/15 10:58:19  jdt
 more test updates

 Revision 1.1.2.2  2004/07/30 16:59:50  jdt
 limping along.


 
 */