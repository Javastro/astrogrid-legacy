/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/client/Attic/StoreClientSoapDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: StoreClientSoapDelegateTestCase.java,v $
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.3  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.1.2.2  2005/01/10 15:36:27  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 *   Revision 1.1.2.1  2005/01/07 12:18:00  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerStoreMock;
import org.astrogrid.filemanager.common.FileManagerConfigMock;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock;

/**
 * A JUnit test for the StoreClient mock delegate implementation.
 *
 */
public class StoreClientSoapDelegateTestCase
    extends StoreClientWrapperTest
    {

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
                new Ivorn("ivo://filemanager"),
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
        //
        // Create our target delegate.
        this.delegate = new FileManagerSoapDelegate(
            "local:///FileManager"
            ) ;
        //
        // Create our target wrapper.
        this.wrapper = new StoreClientWrapper(
            this.delegate
            );
        }
    }

