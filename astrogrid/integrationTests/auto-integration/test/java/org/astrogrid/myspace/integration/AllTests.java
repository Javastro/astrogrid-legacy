/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/myspace/integration/Attic/AllTests.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: AllTests.java,v $
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

public class AllTests
	{
	public static void main(String[] args)
		{
		junit.textui.TestRunner.run(AllTests.class);
		}

	public static Test suite()
		{
		TestSuite suite = new TestSuite("Myspace");
		//$JUnit-BEGIN$
		suite.addTest(
			new TestSuite(
				MySpaceIntegrationTest.class
				)
			);
		//$JUnit-END$
		return suite;
		}
	}

