/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/client/delegate/Attic/FileManagerMockDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMockDelegateTestCase.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.3.4.1  2005/01/10 15:36:27  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.5  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.4  2004/11/26 04:22:24  dave
 *   Added SOAP delegate node test ...
 *   Added node export test ..
 *
 *   Revision 1.1.2.3  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 *   Revision 1.1.2.2  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.1  2004/11/13 01:41:26  dave
 *   Created initial client API ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client.delegate;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerStore;
import org.astrogrid.filemanager.common.FileManagerStoreMock;
import org.astrogrid.filemanager.common.FileManagerConfig;
import org.astrogrid.filemanager.common.FileManagerConfigMock;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock;

/**
 * A JUnit test for the FileManager mock delegate implementation.
 *
 */
public class FileManagerMockDelegateTestCase
    extends FileManagerDelegateTest
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
        // Setup our filestore identifiers.
        filestores = new Ivorn[]
            {
            new Ivorn("ivo://filestore/alpha"),
            new Ivorn("ivo://filestore/beta")
            } ;
        //
        // Initialise our config.
        FileManagerConfig config = 
            new FileManagerConfigMock(
                new Ivorn("ivo://filemanager"),
                filestores[0]
                );
        //
        // Initialise our filestore store.
        FileManagerStore store = new FileManagerStoreMock() ;
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
        // Initialise our ivorn  factory.
        FileManagerIvornFactory factory = new FileManagerIvornFactory();
        //
        // Create our target delegate.
        this.delegate = new FileManagerMockDelegate(
            config,
            store,
            factory,
            resolver
            );
        }

    }

