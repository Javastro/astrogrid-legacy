/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/client/Attic/FileManagerClientTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClientTestCase.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/25 11:15:58  dave
 *   Fixed NullPointer bug in manager.
 *   Refactored client test case ...
 *
 *   Revision 1.1.2.1  2005/01/25 08:01:16  dave
 *   Added tests for FileManagerClientFactory ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.client.FileManagerTestMock;

import org.astrogrid.filemanager.common.FileManagerStore;
import org.astrogrid.filemanager.common.FileManagerStoreMock;
import org.astrogrid.filemanager.common.FileManagerConfigMock;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock ;

/**
 * Junit test for the FileManagerClient.
 *
 */
public class FileManagerClientTestCase
	extends FileManagerClientTest
	{

	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Initialise our base class.
		super.setUp();
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        //
        // Initialise our filestore identifiers.
        filestores = new Ivorn[]
            {
            new Ivorn("ivo://filestore/alpha"),
            new Ivorn("ivo://filestore/beta")
            } ;
        //
        // Initialise our manager config.
        FileManagerTestMock.config = 
            new FileManagerConfigMock(
                new Ivorn(
                    getTestProperty(
                        "service.ivorn"
                        )
                    ),
                filestores[0]
                );
        //
        // Initialise our manager store.
        FileManagerTestMock.store = new FileManagerStoreMock();
        //
        // Initialise our filestore resolver.
        FileStoreDelegateResolverMock resolver = new FileStoreDelegateResolverMock() ;
        FileManagerTestMock.resolver = resolver ;
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
        // Initialise our ivorn factory.
        FileManagerTestMock.factory = new FileManagerIvornFactory();
		}

	}