/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/junit/org/astrogrid/filemanager/client/SoapFileManagerClientTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: SoapFileManagerClientTestCase.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.2  2005/03/10 15:43:26  nw
 *   working now.
 *
 *   Revision 1.1.2.1  2005/03/01 23:43:39  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.3  2005/03/01 15:07:37  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.2  2005/02/25 12:33:27  nw
 *   finished transactional store
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.2.4.2  2005/02/11 17:16:03  nw
 *   knock on effect of renaming and making IvornFactory static
 *
 *   Revision 1.2.4.1  2005/02/10 16:24:17  nw
 *   formatted code, added AllTests that draw all tests together for easy execution from IDE
 *
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

import org.astrogrid.filemanager.server.TestFileManager;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/** A soap-based test.
 * @todo possble / sensible to create a non-soap variant?
 * Junit test for the FileManagerClient.
 *  
 */
public class SoapFileManagerClientTestCase extends FileManagerClientTest {

    /**
     * Setup our test.
     *  
     */
    public void setUp() throws Exception {
        //
        // Initialise our base class.
        super.setUp();
        TestFileManager.deployLocal();
/*
        //
        // Initialise our manager config.
        FileManagerMock.config = new FileManagerHardcodedConfig(new Ivorn(
                getTestProperty("service.ivorn")), filestores[0]);
        //
        // Initialise our manager store.
        //FileManagerMock.store = new FileManagerStoreMock();
        //
        // Initialise our filestore resolver.
        FileStoreDelegateResolverMock resolver = new FileStoreDelegateResolverMock();
        FileManagerMock.resolver = resolver;
        //
        // Register our test filestore(s).
        resolver.register(new FileStoreMockDelegate(filestores[0]));
        resolver.register(new FileStoreMockDelegate(filestores[1]));
        */

    }
    /** define custom suite, that includes a decorator that does setup of soap service once, before all tests are run */
    public static Test suite() {
        TestSuite suite = new TestSuite(SoapFileManagerClientTestCase.class.getName());
        TestSuite tests = new TestSuite(SoapFileManagerClientTestCase.class);
        suite.addTest(new TestSetup(tests){
            protected void setUp() throws Exception {
                TestFileManager.prepareWsdd();
            }            
        });
        return suite;
    }
    

}