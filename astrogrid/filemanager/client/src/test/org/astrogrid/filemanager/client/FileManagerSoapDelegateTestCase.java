/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/client/Attic/FileManagerSoapDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerSoapDelegateTestCase.java,v $
 *   Revision 1.2  2004/11/25 00:20:30  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.1  2004/11/13 01:41:26  dave
 *   Created initial client API ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerConfigMock;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock;

/**
 * A JUnit test for the FileManagerSoapDelegate implementation.
 *
 */
public class FileManagerSoapDelegateTestCase
	extends FileManagerDelegateTest
	{

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(FileManagerSoapDelegateTestCase.class);

	/**
	 * A set of ivorn identifiers for target file stores.
	 *
	 */
	protected Ivorn[] filestores ;

    /**
     * Base URL for our SOAP endpoints.
     *
     */
//    private static String URL_BASE = "local://" ;

	/**
	 * Setup our test ...
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Setup our base class.
		super.setUp() ;
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
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
		this.delegate = new FileManagerSoapDelegate(
			"local:///FileManager"
			) ;
		}
	}

