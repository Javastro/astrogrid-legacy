/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filestore/integration/AllTests.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/19 23:42:07 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: AllTests.java,v $
 *   Revision 1.2  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.1.2.1  2004/07/16 12:40:02  dave
 *   Added filestore tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.integration;

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
		TestSuite suite = new TestSuite("Filestore");
		//$JUnit-BEGIN$
		suite.addTest(
			new TestSuite(
				FileStoreIntegrationTest.class
				)
			);
		//$JUnit-END$
		return suite;
		}
	}

