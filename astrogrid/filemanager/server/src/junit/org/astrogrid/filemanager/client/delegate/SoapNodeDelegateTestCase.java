/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/junit/org/astrogrid/filemanager/client/delegate/SoapNodeDelegateTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: SoapNodeDelegateTestCase.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/03/01 23:43:38  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.2  2005/03/01 15:07:27  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.1.2.2  2005/02/11 17:16:03  nw
 *   knock on effect of renaming and making IvornFactory static
 *
 *   Revision 1.1.2.1  2005/02/11 14:30:23  nw
 *   changes to follow source refactoring - tests themselves may
 *   need to be refactoed later
 *
 *   Revision 1.2.4.1  2005/02/10 16:24:17  nw
 *   formatted code, added AllTests that draw all tests together for easy execution from IDE
 *
 *   Revision 1.2  2005/01/28 10:43:59  clq2
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
 *   Revision 1.1.2.4  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.3  2004/11/26 04:22:24  dave
 *   Added SOAP delegate node test ...
 *   Added node export test ..
 *
 *   Revision 1.1.2.2  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
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
package org.astrogrid.filemanager.client.delegate;

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.FileManagerLocator;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.server.TestFileManager;

import java.net.URL;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A JUnit test for the FileManagerSoapDelegate implementation.
 *  
 */
public class SoapNodeDelegateTestCase extends NodeDelegateTest {


    /**
     * @see org.astrogrid.filemanager.client.delegate.NodeDelegateTest#createDelegate()
     */
    protected NodeDelegate createDelegate() throws Exception {
        TestFileManager.deployLocal();
        // client side.       
        // Initialise our FileManager
        FileManagerPortType port = (new FileManagerLocator()).getFileManagerPort(
                new URL(BaseTest.LOCAL_FILEMANAGER));     
        return new CachingNodeDelegate(port,new BundlePreferences());               
        
    }
    
    /** define custom suite, that includes a decorator that does setup of soap service once, before all tests are run */
    public static Test suite() {
        TestSuite suite = new TestSuite(SoapNodeDelegateTestCase.class.getName());
        TestSuite tests = new TestSuite(SoapNodeDelegateTestCase.class);
        suite.addTest(new TestSetup(tests){
            protected void setUp() throws Exception {
                TestFileManager.prepareWsdd();
            }            
        });
        return suite;
    }
}

