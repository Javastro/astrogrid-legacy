/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/myspace/integration/Attic/AllTests.java,v $</cvs:source>
 * <cvs:author>$Author: pah $</cvs:author>
 * <cvs:date>$Date: 2004/09/10 19:40:24 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: AllTests.java,v $
 *   Revision 1.3  2004/09/10 19:40:24  pah
 *   try to test that the myspace users have had containers created
 *
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.1.2.1  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 * </cvs:log>
 *
 */
package org.astrogrid.myspace.integration;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Myspace");
        //$JUnit-BEGIN$
        suite.addTestSuite(HasCommunityCreatedMyspaceUsersTest.class);
        suite.addTestSuite(MySpaceIntegrationTest.class);
        suite.addTestSuite(VoSpaceIntegrationTest.class);
        //$JUnit-END$
        return suite;
    }
}

