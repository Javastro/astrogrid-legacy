/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/server/test/java/org/astrogrid/mySpace/mySpaceManager/FileStoreDriverTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDriverTest.java,v $
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.1.2.2  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 *   Revision 1.1.2.1  2004/07/28 05:01:09  dave
 *   Started adding the FileStore driver .... extremely broke at the moment
 *
 * </cvs:log>
 *
 */
package org.astrogrid.mySpace.mySpaceManager;

import junit.framework.TestCase;

import org.astrogrid.mySpace.mySpaceStatus.Logger ;
import org.astrogrid.mySpace.mySpaceManager.FileStoreDriver ;

/**
 * Junit tests for the <code>FileStoreDriverTest</code> class.
 *
 */
public class FileStoreDriverTest
	extends TestCase
	{
	/**
	 * Test we can create a FileStoreDriver.
	 *
	 */
	public void testCreate()
		throws Exception
		{
		assertNotNull(
			"Failed to create FileStoreDriver",
			FileStoreDriver.create()
			) ;
		}
	}
