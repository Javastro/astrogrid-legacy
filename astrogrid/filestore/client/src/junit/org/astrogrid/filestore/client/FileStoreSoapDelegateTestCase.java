/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/junit/org/astrogrid/filestore/client/FileStoreSoapDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:01 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreSoapDelegateTestCase.java,v $
 *   Revision 1.4  2005/01/28 10:44:01  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.3.10.1  2005/01/15 04:50:58  dave
 *   Added created and modified dates to server ....
 *   Removed log debug messages from JUnit tests ...
 *
 *   Revision 1.3  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.2.84.3  2004/11/18 14:40:20  dave
 *   Removed redundant static
 *
 *   Revision 1.2.84.2  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.2.84.1  2004/10/26 15:50:48  dave
 *   Extended support for testing PUT transfers ...
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/06 10:49:45  dave
 *   Added SOAP delegate
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.client ;

import org.apache.axis.client.Call ;

import org.astrogrid.filestore.common.FileStoreTest ;

/**
 * A JUnit test case for the store service soap delegate.
 *
 */
public class FileStoreSoapDelegateTestCase
	extends FileStoreTest
	{
    /**
     * Setup our test.
     * Creates the Soap delegate to test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Create our test targets.
		this.target = new FileStoreSoapDelegate(
			"local:///FileStore"
			) ;
        }
	}
