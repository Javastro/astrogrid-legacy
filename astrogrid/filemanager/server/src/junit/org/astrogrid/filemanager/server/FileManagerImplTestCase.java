/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/junit/org/astrogrid/filemanager/server/Attic/FileManagerImplTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerImplTestCase.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/17 07:56:33  dave
 *   Added server mock and webapp build scripts ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.server ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.client.FileStoreMockDelegate;

import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerTest;
import org.astrogrid.filemanager.common.FileManagerConfig;
import org.astrogrid.filemanager.common.FileManagerConfigMock;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolver;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock;

/**
 * A JUnit test case for the mock file manager implementation.
 *
 */
public class FileManagerImplTestCase
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
		// Create our target service.
		this.target = new FileManagerImpl(
			new FileManagerConfigMock(
				new Ivorn("ivo://filemanager"),
				filestores[0]
				),
			new FileManagerIvornFactory(),
			resolver
			) ;
		}
	}
