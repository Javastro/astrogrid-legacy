/*$Id: AllTests.java,v 1.4 2004/11/22 14:48:37 jdt Exp $
 * Created on 21-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.community.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Apr-2004
 *
 */
public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }
    public static Test suite() {
        TestSuite suite = new TestSuite("Community");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(CommunityAccountResolverTest.class));
        suite.addTest(new TestSuite(CommunityGroupTest.class));
        suite.addTest(new TestSuite(CommunityPermissionTest.class));
        //$JUnit-END$
        return suite;
    }
}


/* 
$Log: AllTests.java,v $
Revision 1.4  2004/11/22 14:48:37  jdt
Merges from Comm_KMB_585

Revision 1.3.38.1  2004/11/08 22:18:38  KevinBenson
The new integration tests for community and also the ability to split out community
into 2 communities now in our auto-integration.

Revision 1.3  2004/09/09 01:19:50  dave
Updated MIME type handling in MySpace.
Extended test coverage for MIME types in FileStore and MySpace.
Added VM memory data to community ServiceStatusData.

Revision 1.2.82.2  2004/09/07 13:46:51  dave
Restored AllTests test ...

Revision 1.2  2004/04/21 13:41:59  nw
minor tweaks

Revision 1.1  2004/04/21 10:47:16  nw
added test suite
 
*/