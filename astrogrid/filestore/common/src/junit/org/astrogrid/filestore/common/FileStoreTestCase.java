/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/junit/org/astrogrid/filestore/common/FileStoreTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreTestCase.java,v $
 *   Revision 1.4  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.3.10.1  2005/01/15 04:50:58  dave
 *   Added created and modified dates to server ....
 *   Removed log debug messages from JUnit tests ...
 *
 *   Revision 1.3  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.2.84.4  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.2.84.3  2004/10/26 14:39:21  dave
 *   Added extended test to include tests for PUT transfer.
 *
 *   Revision 1.2.84.2  2004/10/26 11:13:11  dave
 *   Changed transfer properties 'source' to 'location', makes more sense for PUT transfers.
 *
 *   Revision 1.2.84.1  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

/**
 * A JUnit test case for the store service.
 *
 */
public class FileStoreTestCase
	extends FileStoreTest
	{
	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		this.target = new FileStoreMock() ;
		}
	}
