/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/junit/org/astrogrid/filestore/client/FileStoreMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreMockDelegateTestCase.java,v $
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/06 09:16:12  dave
 *   Added delegate interface and mock implementation
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.client ;

import org.astrogrid.filestore.common.FileStoreTest ;

/**
 * A JUnit test case for the store service mock delegate.
 *
 */
public class FileStoreMockDelegateTestCase
	extends FileStoreTest
	{
	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		{
		this.target = new FileStoreMockDelegate() ;
		}
	}
