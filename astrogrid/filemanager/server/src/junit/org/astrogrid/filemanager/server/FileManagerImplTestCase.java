/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/junit/org/astrogrid/filemanager/server/Attic/FileManagerImplTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 14:17:21 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerImplTestCase.java,v $
 *   Revision 1.4  2005/02/10 14:17:21  jdt
 *   merge from  filemanager-nww-903
 *
 *   Revision 1.3.4.1  2005/02/10 13:14:00  nw
 *   test case for persistent implementation
 *
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:28:46  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2.4.1  2005/01/10 15:36:28  dave
 *   Refactored store into a separate interface and mock impl ...
 *
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

import org.astrogrid.filemanager.common.FileManagerConfigMock;
import org.astrogrid.filemanager.common.FileManagerTest;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;
import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock;
import org.astrogrid.store.Ivorn;


import java.io.File;
import java.io.IOException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A JUnit test case for the mock file manager implementation.
 *
 */
public class FileManagerImplTestCase
    extends FileManagerTest
    {

    public static final class TemporaryStoreLocations implements FileManagerStoreImpl.StoreLocations {
        protected File nodeDir;
        protected File accountDir;
        public TemporaryStoreLocations() throws IOException {
            nodeDir = File.createTempFile("nodes",null);
            nodeDir.delete();
            nodeDir.mkdirs();
           // nodeDir.deleteOnExit();
            System.out.println("Node Dir: " + nodeDir);
            
            accountDir = File.createTempFile("accounts",null);
            accountDir.delete();
            accountDir.mkdirs();
           // accountDir.deleteOnExit();
            System.out.println("Accounts Dir: " + accountDir);
        }

        public File getNodeDir() {
            return nodeDir;
        }

        public File getAccountDir() {
            return accountDir;
        }
    }
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
             //new FileManagerStoreImpl(storeLocations);
            new FileManagerStoreImpl(new TemporaryStoreLocations()),
            new FileManagerIvornFactory(),
            resolver
            ) ;
        }
    
    /** assigned to in the static suite() method */
   // protected static FileManagerStoreImpl.StoreLocations storeLocations;
    /** if we wanted all tests to operate in the same test store, 
     * define a test suite explicitly, so we can setup and teardown 
     * the file manager store once for all the tests.,
     * and then refer to storeLocations when creating the target service (above)
     * @return
     */
    /*
    public static Test suite() {
        TestSuite tests = new TestSuite(FileManagerImplTestCase.class); // all the tests in this class.
        return  new TestSetup(tests) {
            protected void setUp() throws Exception{
                System.out.println("Setting up");
                storeLocations = new TemporaryStoreLocations(); // end StoreLocations
            }
        }; // end TestSetup
    }
    */
    }
