/*$Id: FileManagerClientTest.java,v 1.3 2005/03/14 22:03:53 clq2 Exp $
 * Created on 02-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.integration;

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.store.Ivorn;

import java.io.IOException;
import java.net.URISyntaxException;

/** reuse one of the system-tests that dave wrote, as an integration test.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Mar-2005
 */
public class FileManagerClientTest extends org.astrogrid.filemanager.client.FileManagerClientTest {

    /** Construct a new FileManagerClientTest
     * 
     */
    public FileManagerClientTest() {
        super();
    }

    protected void setUp() throws Exception {   
        super.setUp();
    }
    /** override this - in original test, this sets up keys for mocks, etc.
     * in this setting we want none of this - rely on the environment we've got already.
     * @see org.astrogrid.filemanager.BaseTest#setupKeys()
     */
    protected void setupKeys() throws IOException {
        // override unit test settings to points to stores deployed in integration tests.
        try {
            BaseTest.FILESTORE_1 = new Ivorn("ivo://org.astrogrid.localhost/filestore-one");
            BaseTest.FILESTORE_2 = new Ivorn("ivo://org.astrogrid.localhost/filestore-two");            
        } catch (URISyntaxException e) {
            fail(e.getMessage());
        }
        BaseTest.filestores = new Ivorn[]{BaseTest.FILESTORE_1,BaseTest.FILESTORE_2};        

    }
    
    /** some more quick tests */
    public void testNotExists() throws Exception {
            registerAccounts();
            FileManagerClientFactory factory = new FileManagerClientFactory();
            FileManagerClient client = factory.login(accountIvorn,"secret");
            FileManagerNode home = client.home();
            FileManagerNode frog = home.addFolder("frog");
            Ivorn target = frog.getIvorn();
            Ivorn missing = new Ivorn(target.toString() + "/wibble");
            System.err.println(missing);
            assertNull(client.exists(missing));
        }    
    public void testExistsFolder() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn,"secret");
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        Ivorn target = frog.getIvorn();
        assertEquals(NodeTypes.FOLDER,client.exists(target));

    }    
    public void testExistsFile() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn,"secret");
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFile("frog");
        Ivorn target = frog.getIvorn();
        assertEquals(NodeTypes.FILE,client.exists(target));

    }        
    
    public void testDelete() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn,"secret");
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFile("frog");
        Ivorn target = frog.getIvorn();
        assertEquals(NodeTypes.FILE,client.exists(target));
        frog.delete();
        assertNull(client.exists(target));
    }
}


/* 
$Log: FileManagerClientTest.java,v $
Revision 1.3  2005/03/14 22:03:53  clq2
auto-integration-nww-994

Revision 1.2.2.1  2005/03/14 15:20:44  nw
added some more tests.

Revision 1.2  2005/03/11 13:36:22  clq2
with merges from filemanager

Revision 1.1.4.2  2005/03/10 19:32:49  nw
bunch of tests for filemanager

Revision 1.1.4.1  2005/03/02 01:49:17  nw
made a start on integration tests for filemanager
 
*/