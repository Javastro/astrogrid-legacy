/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/test/org/astrogrid/filemanager/common/Attic/FileManagerMockTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMockTestCase.java,v $
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.2  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3.4.1  2005/01/10 15:36:28  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.6  2004/12/06 13:29:02  dave
 *   Added initial code for move location ....
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
        // Initialise our ivorn factory.
        FileManagerIvornFactory factory = new FileManagerIvornFactory();
        //
        // Create our target service.
        this.target = new FileManagerMock(
            config,
            store,
            factory,
            resolver
            );
        }
    }
