/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerClientTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClientTest.java,v $
 *   Revision 1.3  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.2.4.7  2005/03/01 23:41:14  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.2.4.6  2005/03/01 16:37:06  nw
 *   implemented newFile() and newFolder()
 *
 *   Revision 1.2.4.5  2005/03/01 15:07:29  nw
 *   close to finished now.
 *
 *   Revision 1.2.4.4  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.2.4.3  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.2.4.2  2005/02/11 14:27:52  nw
 *   refactored, split out candidate classes.
 *
 *   Revision 1.2.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.5  2005/01/26 13:54:50  dave
 *   Added additional client tests ..
 *
 *   Revision 1.1.2.4  2005/01/26 12:24:19  dave
 *   Removed type from add(), replaced with addNode() and addFile() ...
 *
 *   Revision 1.1.2.3  2005/01/26 09:51:39  dave
 *   Fixed bug in client node resolver ...
 *
 *   Revision 1.1.2.2  2005/01/25 14:57:38  dave
 *   Fixed config problem for integration tests ..
 *
 *   Revision 1.1.2.1  2005/01/25 11:15:57  dave
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

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate;
import org.astrogrid.community.client.security.manager.SecurityManagerDelegate;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver;
import org.astrogrid.community.resolver.security.manager.SecurityManagerResolver;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver;
import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.filemanager.client.delegate.NodeDelegate;
import org.astrogrid.filemanager.common.AccountIdent;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filemanager.resolver.NodeDelegateResolver;
import org.astrogrid.filemanager.resolver.NodeDelegateResolverImpl;
import org.astrogrid.filestore.common.transfer.TransferUtil;
import org.astrogrid.store.Ivorn;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Junit test for the FileManagerClient.
 *  
 */
public class FileManagerClientTest extends BaseTest {


    /**
     * The root FileManager node for our space.
     *  
     */
    protected FileManagerNode accountNode;

    /**
     * The Community data for our account.
     *  
     */
    protected AccountData accountData;

    /**
     * Our account password.
     *  
     */
    private String accountPass = "secret";



    /**
     * Test that we can setup a Community account. Not part of the
     * FileManagerClient API, just testing that the test environment works.
     *  
     */
    public void testCreateCommunityAccount() throws Exception {
        //
        // Create our Community account.
        assertNotNull(createCommunityAccount(accountIdent));
    }

    /**
     * Test that we can setup a FileManager account. Not part of the
     * FileManagerClient API, just testing that the test environment works.
     *  
     */
    public void testCreateFileManagerAccount() throws Exception {
        //
        // Create our FileManager account.
        assertNotNull(createFileManagerAccount(accountIdent));
    }

    /**
     * Test that we can register our FileManager space. Not part of the
     * FileManagerClient API, just testing that the test environment works.
     *  
     */
    public void testRegisterCommunityHome() throws Exception {
        //
        // Create our Community account.
        accountData = createCommunityAccount(accountIdent);
        //
        // Create our FileManager account.
        accountNode = createFileManagerAccount(accountIdent);
        //
        // Register our FileManager space.
        assertNotNull(registerCommunityHome(accountIdent, accountNode.getIvorn()));
    }

    /**
     * Test that we can register our account(s). Not part of the
     * FileManagerClient API, just testing that the test environment works.
     *  
     */
    public void testRegister() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Check we have a Community account.
        assertNotNull(accountData);
        //
        // Check we have a FileManager account.
        assertNotNull(accountNode);
    }

    /**
     * Test that we can login anonymously.
     *  
     */
    public void testLoginAnon() throws Exception {
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login();
        //
        // Check our client.
        assertNotNull(client);
    }

    /**
     * Test that an anon login does not have a home node.
     *  
     */
    public void testLoginAnonHome() throws Exception {
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login();
        //
        // Check our home node.
        try {
            client.home();
        } catch (IllegalStateException ouch) {
            return;
        }
        fail("Expected IllegalStateException");
    }

    /**
     * Test that we can login with name and password.
     *  
     */
    public void testLoginPass() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Check our client.
        assertNotNull(client);
    }

    /**
     * Test that a login with name and password gives us access to our home.
     *  
     */
    public void testLoginPassHome() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Check our home node.
        assertNotNull(client.home());
    }

    /**
     * Check that we can login with name and password and add a file.
     *  
     */
    public void testLoginAddFile() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
    }

    /**
     * Check that we can login with name and password, add a file and then find
     * it again.
     *  
     */
    public void testLoginAddFileGetFile() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
        //
        // Try to find our file again.
        FileManagerNode file = home.getChild("frog").getChild("toad").getChild("newt");
        assertNotNull(file);
        assertSame(newt,file);
    }

    /**
     * Check that we can login with name and password, add a file, login again,
     * and then find it.
     *  
     */
    public void testLoginAddFileLoginGetFile() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");

        //
        // Resolve a new client.
        FileManagerClient other = factory.login(accountIvorn, accountPass);
        //
        // Try to find our file again.
        FileManagerNode file = other.home().getChild("frog").getChild("toad").getChild("newt");
        assertEquals(newt,file); // equal, but probably not the same - depending on what's been cached _between_ delegates
    }

    /** check we can round-trip our home directory.*/
    public void testRoundTripHome() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        assertSame(home,client.node(home.getIvorn()));
        assertSame(home,client.node(home.getMetadata().getNodeIvorn()));
    
    }    

    
    /**
     * Check that we can login with name and password, add a file, login as
     * anon, and then find it.
     *  
     */
    public void testLoginAddFileAnonGetFileNodeIvorn() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
        //
        // Resolve a new client.
        FileManagerClient anon = factory.login();
        //

        FileManagerNode file = anon.node(newt.getMetadata().getNodeIvorn());
        assertNotNull(file);
        assertEquals(file,newt);
    }
    
    /** same as above, but creating a Ivorn from the nodeIvorn - i.e. check both versions of the method will accept a node ivorn). */
    public void testLoginAddFileAnonGetFileNodeIvornToIvorn() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
        //
        // Resolve a new client.
        FileManagerClient anon = factory.login();
        //

        FileManagerNode file = anon.node(new Ivorn(newt.getMetadata().getNodeIvorn().toString()));
        assertNotNull(file);
        assertEquals(file,newt);
    }    
    
    /** same as above, using ivorn, rather than nodeIvorn */
    public void testLoginAddFileAnonGetFileIvorn() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
        //
        // Resolve a new client.
        FileManagerClient anon = factory.login();
        //
        // Try to find our file again.
        FileManagerNode file = anon.node(newt.getIvorn());
        assertNotNull(file);
        assertEquals(file,newt);
    }    
    
    /** test behaviour of createFle when the file already exists */
    public void testCreateExistingFile() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFile("toad");
        try { 
            client.createFile(toad.getIvorn());
        } catch (DuplicateNodeFault e) {
            return;
        }
        fail("Expected to throw duplicateNodeFault");
    }
    /** variant with node ivorn */
    public void testCreateExistingFileNodeIvorn() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFile("toad");
        try { 
            client.createFile(new Ivorn(frog.getMetadata().getNodeIvorn().toString() + "/toad"));
        } catch (DuplicateNodeFault e) {
            return;
        }
        fail("Expected to throw duplicateNodeFault");
    }    
    
    /** test behaviour of create folder when the folder already exists */
    public void testCreateExistingFolder() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        try { 
            client.createFolder(toad.getIvorn());
        } catch (DuplicateNodeFault e) {
            return;
        }
        fail("Expected to throw duplicateNodeFault");        
    }
    /** variant with node Ivorn */
    public void testCreateExistingFolderNodeIvorn() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        try { 
            client.createFolder(new Ivorn(frog.getMetadata().getNodeIvorn().toString() + "/toad"));
        } catch (DuplicateNodeFault e) {
            return;
        }
        fail("Expected to throw duplicateNodeFault");        
    }
    
    /** test behaviour of create file when one of the parents exists and is a file */
    public void testCreateFileWhenIntermediateIsAFile() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFile("toad");
        try { 
            client.createFile(new Ivorn(toad.getIvorn().toString() + "/foo"));
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected to throw illegalArgumentExceptiont");        
    }
    /** equivalent for node ivorns */
    public void testCreateFileWhenIntermediateIsAFileNodeIvorn() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFile("toad");
        try { 
            client.createFile(new Ivorn(toad.getMetadata().getNodeIvorn() + "/foo"));
        } catch (IllegalArgumentException e) {
            return;
        }
        fail("Expected to throw illegalArgumentException");        
    }    
    
    /** test behaviour of creating a file where immediate parent exists */
    public void testCreateShallowFile() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");        
        FileManagerNode foo = client.createFile(new Ivorn(toad.getIvorn().toString() + "/foo"));
        assertNotNull(foo);
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());
        assertTrue(foo.isFile());
    }
    /** eqivalent for node ivorn. */
    public void testCreateShallowFileNodeIvorn() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");        
        FileManagerNode foo = client.createFile(new Ivorn(toad.getMetadata().getNodeIvorn() + "/foo"));
        assertNotNull(foo);
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());        
        assertTrue(foo.isFile());
    }
    /** test behaviour of creating a folder where immegaite parent exists*/
    public void testCreateShallowFolder() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");        
        FileManagerNode foo = client.createFolder(new Ivorn(toad.getIvorn().toString() + "/foo"));
        assertNotNull(foo);
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());        
        assertTrue(foo.isFolder());
    }
    /** eqivalent for node ivorn. */
    public void testCreateShallowFolderNodeIvorn() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");        
        FileManagerNode foo = client.createFolder(new Ivorn(toad.getMetadata().getNodeIvorn() + "/foo"));
        assertNotNull(foo);
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());        
        assertTrue(foo.isFolder());
    }
    
    /** test behaviour of creating a file where parents need to be created too */
    public void testCreateDeepFile() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");    
        FileManagerNode foo = client.createFile(new Ivorn(frog.getIvorn().toString() + "/toad/foo"));
        assertNotNull(foo);
        FileManagerNode toad = frog.getChild("toad");
        assertNotNull(toad);       
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());
        assertTrue(foo.isFile());
    }
    /** eqivalent for node ivorn. */
    public void testCreateDeepFileNodeIvorn() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");   
        FileManagerNode foo = client.createFile(new Ivorn(frog.getMetadata().getNodeIvorn() + "/toad/foo"));
        assertNotNull(foo);
        FileManagerNode toad = frog.getChild("toad");
        assertNotNull(toad);
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());        
        assertTrue(foo.isFile());
    }
    /** test behaviour of creating a folder where parents need to be created too */
    public void testCreateDeepFolder() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");  
        FileManagerNode foo = client.createFolder(new Ivorn(frog.getIvorn().toString() + "/toad/foo"));
        assertNotNull(foo);
        FileManagerNode toad = frog.getChild("toad");
        assertNotNull(toad);
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());        
        assertTrue(foo.isFolder());
    }
    /** eqivalent for node ivorn. */
    public void testCreateDeepFolderNodeIvorn() throws Exception {
        registerAccounts();
        FileManagerClientFactory factory = new FileManagerClientFactory();
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        FileManagerNode home = client.home();
        FileManagerNode frog = home.addFolder("frog");   
        FileManagerNode foo = client.createFolder(new Ivorn(frog.getMetadata().getNodeIvorn() + "/toad/foo"));
        assertNotNull(foo);
        FileManagerNode toad = frog.getChild("toad");
        assertNotNull(toad);
        assertEquals(toad,foo.getParentNode());
        assertEquals("foo",foo.getName());        
        assertTrue(foo.isFolder());
    }    


    
    

    /**
     * Check that we can login with name and password, add a file and transfer
     * some data into it.
     *  
     */
    public void testLoginPassAddFileAddData() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
        //
        // Open a stream to our file.
        OutputStream output = newt.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
    }

    /** check we can test for the existence of  the root.*/
    public void testExistsHome() throws Exception {
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();

        assertEquals(NodeTypes.FOLDER,client.exists(home.getIvorn()));
        // check behaviour is equivalent with nodeIvorn..        
        assertEquals(NodeTypes.FOLDER,client.exists(new Ivorn(home.getMetadata().getNodeIvorn().toString())));
    }
    /** check we can test for the existence of a file */
    public void testExistsFile() throws Exception {
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
        assertEquals(NodeTypes.FILE,client.exists(newt.getIvorn()));
        // check behaviour is equivalent with nodeIvorn..

        assertEquals(NodeTypes.FILE,client.exists(new Ivorn(newt.getMetadata().getNodeIvorn().toString())));
    }
    /** test we cah test for the existence of a filder */
    public void testExistsFolder() throws Exception {
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");

        assertEquals(NodeTypes.FOLDER,client.exists(toad.getIvorn()));
        // check behaviour is equivalent with nodeIvorn..

        assertEquals(NodeTypes.FOLDER,client.exists(new Ivorn(toad.getMetadata().getNodeIvorn().toString())));
         
    }
    
    /** test what happens when testing for a non-existent resource.*/
   public void testNotExistsResource() throws Exception {
       // Register our accounts.
       registerAccounts();
       //
       // Create our factory.
       FileManagerClientFactory factory = new FileManagerClientFactory();
       //
       // Resolve our client.
       FileManagerClient client = factory.login(accountIvorn, accountPass);
       FileManagerNode node = client.home();
       assertEquals(NodeTypes.FOLDER,client.exists(node.getIvorn()));
       Ivorn unknown = new Ivorn(node.getIvorn().toString() + "foo");
       System.out.println(unknown);
       assertNull(client.exists(unknown));
       // check behaviour is equivalent with nodeIvorn..
       assertNull(client.exists(new Ivorn(node.getMetadata().getNodeIvorn().toString() + "1")));
       
   }
    /**
     * Check that we can transfer some data into a file and then read it back
     * again.
     *  
     */
    public void testLoginPassAddFileAddDataGetData() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(accountIvorn, accountPass);
        //
        // Get our home node.
        FileManagerNode home = client.home();
        //
        // Add some containers.
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        //
        // Add a file.
        FileManagerNode newt = toad.addFile("newt");
        //
        // Open a stream to our file.
        OutputStream output = newt.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();

        //
        // Open an input stream from our file.
        InputStream input = newt.readContent();
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the file into the buffer.
        new TransferUtil(input, buffer).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }

    /**
     * Test that we can login with a token.
     *  
     */
    public void testLoginToken() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Login to our community account.
        SecurityToken token = loginCommunityAccount(accountIvorn, accountPass);
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(token);
        //
        // Check our client.
        assertNotNull(client);
    }

    /**
     * Test that a login with a token gives us access to our home.
     *  
     */
    public void testLoginTokenHome() throws Exception {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Login to our community account.
        SecurityToken token = loginCommunityAccount(accountIvorn, accountPass);
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client = factory.login(token);
        //
        // Check our home node.
        assertNotNull(client.home());
    }

    
    
    //
    // .............
    //

    /**
     * Register our Community and FileManager account. Not part of the
     * FileManagerClient API, this sets up the Community and FileManager
     * accounts used in the tests.
     *  
     */
    public void registerAccounts() throws Exception {
        System.out.println("registerAccount(Ivorn)");
        //
        // Create our Community account.
        accountData = createCommunityAccount(accountIdent);
        //
        // Set our account password.
        registerPassword(accountIdent, accountPass);
        //
        // Create our FileManager account.
        accountNode = createFileManagerAccount(accountIdent);
        //
        
        Ivorn homeIvorn = new Ivorn(accountNode.getMetadata().getNodeIvorn().toString());
        accountData = registerCommunityHome(accountIdent, homeIvorn);
    }

    /**
     * Create a Community account. Not part of the FileManagerClient API, this
     * sets up the Community account(s) used in the tests.
     *  
     */
    public static AccountData createCommunityAccount(AccountIdent acc) throws Exception {
        // convert to legacy format.
        Ivorn ivorn = new Ivorn(acc.toString());
        // Create our resolver.
        PolicyManagerResolver resolver = new PolicyManagerResolver();
        //
        // Resolve our Community service.
        PolicyManagerDelegate delegate = resolver.resolve(ivorn);
        //
        // Create the account.
        return delegate.addAccount(new CommunityIvornParser(ivorn)
                .getAccountIdent());
    }

    /**
     * Set our Community account password. Not part of the FileManagerClient
     * API, this sets up the Community account(s) used in the tests.
     *  
     */
    public static void registerPassword(AccountIdent acc, String pass) throws Exception {
        Ivorn ivorn = new Ivorn(acc.toString());
        SecurityManagerResolver resolver = new SecurityManagerResolver();
        //
        // Resolve our Community service.
        SecurityManagerDelegate delegate = resolver.resolve(ivorn);
        //
        // Update the account.
        delegate.setPassword(new CommunityIvornParser(ivorn).getAccountIdent(),
                pass);
    }

    /**
     * Create our FileManager space. Not part of the FileManagerClient API, this
     * sets up the FileManager account(s) used in the tests.
     *  
     */
    public static FileManagerNode createFileManagerAccount(AccountIdent acc)
            throws Exception {
        Ivorn ivorn = new Ivorn(acc.toString());
        //
        // Create our resolver.
        NodeDelegateResolver resolver = new NodeDelegateResolverImpl(new BundlePreferences());
        //
        // Resolve our FileManager service.
        NodeDelegate delegate = resolver.resolve(new Ivorn(
                getTestProperty("ivorn")));
        //
        // Create the account.
        return delegate.addAccount(acc);
    }

    /**
     * Register our FileManagerSpace in our CommunityAccount. Not part of the
     * FileManagerClient API, this sets up the FileManager space for the
     * Community account(s) used in the tests. This should not be needed on the
     * live system, if the Community is configured to register space with the
     * local FileManager service automagically.
     *  
     */
    public static  AccountData registerCommunityHome(AccountIdent acc, Ivorn home)
            throws Exception {
        Ivorn ivorn = new Ivorn(acc.toString());
        //
        // Create our resolver.
        PolicyManagerResolver resolver = new PolicyManagerResolver();
        //
        // Resolve our Community service.
        PolicyManagerDelegate delegate = resolver.resolve(ivorn);
        //
        // Get the account data.
        AccountData account = delegate.getAccount(new CommunityIvornParser(
                ivorn).getAccountIdent());
        //
        // Set the home space ivorn.
        account.setHomeSpace(home.toString());
        //
        // Update the account data.
        return delegate.setAccount(account);
    }

    /**
     * Login to our Community account and get a token. Not part of the
     * FileManagerClient API, this uses the Community security API to set up the
     * token(s) used in the tests.
     *  
     */
    public SecurityToken loginCommunityAccount(Ivorn ivorn, String pass)
            throws Exception {
        System.out.println("loginCommunityAccount(Ivorn, String)");
        System.out.println("  Ivorn : " + ivorn.toString());
        System.out.println("  Pass  : " + pass);
        //
        // Create our resolver.
        SecurityServiceResolver resolver = new SecurityServiceResolver();
        //
        // Resolve our Community service.
        SecurityServiceDelegate delegate = resolver.resolve(ivorn);
        //
        // Login to the account.
        return delegate.checkPassword(ivorn.toString(), pass);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }
}