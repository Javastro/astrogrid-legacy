/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/test/org/astrogrid/filemanager/common/Attic/FileManagerMockTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMockTestCase.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.5  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.4  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.3  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.2  2004/10/21 21:08:52  dave
 *   Added config interface and mock implementation.
 *   Partial implementation of data import into FileStore via direct URL transfer.
 *
 *   Revision 1.1.2.1  2004/10/07 14:29:08  dave
 *   Added initial interface and implementations ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.client.FileStoreMockDelegate;

import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory ;

/**
 * A JUnit test case for the mock file manager implementation.
 *
 */
public class FileManagerMockTestCase
	extends FileManagerTest
	{

	/**
	 * A set of ivorn identifiers for target file stores.
	 *
	 */
	protected Ivorn[] filestores ;

	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Setup our base class.
		super.setUp() ;
		//
		// Setup our filestore identifiers.
		filestores = new Ivorn[]
			{
			new Ivorn("ivo://filestore/alpha"),
			new Ivorn("ivo://filestore/beta")
			} ;
		//
		// Setup the default config.
		FileManagerMock.defaultConfig = 
			new FileManagerConfigMock(
				new Ivorn("ivo://filemanager"),
				filestores[0]
				);
		//
		// Initialise our filestore resolver.
		FileStoreDelegateResolverMock resolver = new FileStoreDelegateResolverMock() ;
		//
		// Register our test filestore(s).
		resolver.register(
			new FileStoreMockDelegate(
				filestores[0]
				)
			);
		resolver.register(
			new FileStoreMockDelegate(
				filestores[1]
				)
			);
		//
		// Setup the default resolver.
		FileManagerMock.defaultResolver = resolver ;
		//
		// Setup the default factory.
		FileManagerMock.defaultFactory = new FileManagerIvornFactory();
		//
		// Create our target service.
		this.target = new FileManagerMock();
		}
	}
