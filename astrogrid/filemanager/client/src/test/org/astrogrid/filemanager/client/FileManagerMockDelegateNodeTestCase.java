/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/client/Attic/FileManagerMockDelegateNodeTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMockDelegateNodeTestCase.java,v $
 *   Revision 1.2  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.3  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.2  2004/11/26 04:22:24  dave
 *   Added SOAP delegate node test ...
 *   Added node export test ..
 *
 *   Revision 1.1.2.1  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerConfigMock;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock;

/**
 * A JUnit test for the FileManager mock delegate nodes.
 *
 */
public class FileManagerMockDelegateNodeTestCase
	extends FileManagerNodeTest
	{

	/**
	 * Setup our test ...
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Setup our account identifiers.
		super.setUp();
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
		// Create our target delegate.
		this.delegate = new FileManagerMockDelegate();
		}

	}

