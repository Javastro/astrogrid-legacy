/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/FileManagerClientTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClientTest.java,v $
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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import org.astrogrid.store.Ivorn;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver;

import org.astrogrid.community.common.security.data.SecurityToken;

import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver;

import org.astrogrid.community.client.security.manager.SecurityManagerDelegate;
import org.astrogrid.community.resolver.security.manager.SecurityManagerResolver;

import org.astrogrid.filemanager.common.BaseTest ;

import org.astrogrid.filemanager.client.delegate.FileManagerDelegate;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolver;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;

import org.astrogrid.filestore.common.transfer.TransferUtil;

/**
 * Junit test for the FileManagerClient.
 *
 */
public class FileManagerClientTest
    extends BaseTest
    {

    /**
     * A set of ivorn identifiers for our target file stores.
     *
     */
    protected Ivorn[] filestores ;

    /**
     * The root FileManager node for our space.
     *
     */
    protected FileManagerNode accountNode ;

    /**
     * The Community data for our account.
     *
     */
    protected AccountData accountData ;

    /**
     * Our account password.
     *
     */
    private String accountPass = "secret" ;

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        super.setUp();
        }

    /**
     * Test that we can setup a Community account.
     * Not part of the FileManagerClient API, just testing that the test environment works.
     *
     */
    public void testCreateCommunityAccount()
        throws Exception
        {
        //
        // Create our Community account.
        assertNotNull(
            createCommunityAccount(
                accountIvorn
                )
            );
        }

    /**
     * Test that we can setup a FileManager account.
     * Not part of the FileManagerClient API, just testing that the test environment works.
     *
     */
    public void testCreateFileManagerAccount()
        throws Exception
        {
        //
        // Create our FileManager account.
        assertNotNull(
            createFileManagerAccount(
                accountIvorn
                )
            );
        }

    /**
     * Test that we can register our FileManager space.
     * Not part of the FileManagerClient API, just testing that the test environment works.
     *
     */
    public void testRegisterCommunityHome()
        throws Exception
        {
        //
        // Create our Community account.
        accountData =
            createCommunityAccount(
                accountIvorn
                );
        //
        // Create our FileManager account.
        accountNode =
            createFileManagerAccount(
                accountIvorn
                );
        //
        // Register our FileManager space.
        assertNotNull(
            registerCommunityHome(
                accountIvorn,
                accountNode.ivorn()
                )
            );
        }

    /**
     * Test that we can register our account(s).
     * Not part of the FileManagerClient API, just testing that the test environment works.
     *
     */
    public void testRegister()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Check we have a Community account.
        assertNotNull(
            accountData
            );
        //
        // Check we have a FileManager account.
        assertNotNull(
            accountNode
            );
        }

    /**
     * Test that we can login anonymously.
     *
     */
    public void testLoginAnon()
        throws Exception
        {
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login();
        //
        // Check our client.
        assertNotNull(
            client
            );
        }

    /**
     * Test that an anon login does not have a home node.
     *
     */
    public void testLoginAnonHome()
        throws Exception
        {
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login();
        //
        // Check our home node.
        try {
            client.home();
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException");
        }

    /**
     * Test that we can login with name and password.
     *
     */
    public void testLoginPass()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Check our client.
        assertNotNull(
            client
            );
        }

    /**
     * Test that a login with name and password gives us access to our home.
     *
     */
    public void testLoginPassHome()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Check our home node.
        assertNotNull(
            client.home()
            );
        }

    /**
     * Check that we can login with name and password and add a file.
     *
     */
    public void testLoginAddFile()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode frog = home.addNode(
			"frog"
			);
		FileManagerNode toad = frog.addNode(
			"toad"
			);
		//
		// Add a file.
		FileManagerNode newt = toad.addFile(
			"newt"
			);
        }

    /**
     * Check that we can login with name and password, add a file and then find it again.
     *
     */
    public void testLoginAddFileGetFile()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode frog = home.addNode(
			"frog"
			);
		FileManagerNode toad = frog.addNode(
			"toad"
			);
		//
		// Add a file.
		FileManagerNode newt = toad.addFile(
			"newt"
			);
		//
		// Try to find our file again.
		FileManagerNode file = home.child(
			"frog/toad/newt"
			);
        }

    /**
     * Check that we can login with name and password, add a file, login again, and then find it.
     *
     */
    public void testLoginAddFileLoginGetFile()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode frog = home.addNode(
			"frog"
			);
		FileManagerNode toad = frog.addNode(
			"toad"
			);
		//
		// Add a file.
		FileManagerNode newt = toad.addFile(
			"newt"
			);

        //
        // Resolve a new client.
        FileManagerClient other =
            factory.login(
                accountIvorn,
                accountPass
                );
		//
		// Try to find our file again.
		FileManagerNode file = other.home().child(
			"frog/toad/newt"
			);
        }

    /**
     * Check that we can login with name and password, add a file, login as anon, and then find it.
     *
     */
    public void testLoginAddFileAnonGetFile()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode frog = home.addNode(
			"frog"
			);
		FileManagerNode toad = frog.addNode(
			"toad"
			);
		//
		// Add a file.
		FileManagerNode newt = toad.addFile(
			"newt"
			);
        //
        // Resolve a new client.
        FileManagerClient anon =
            factory.login();
		//
		// Try to find our file again.
		FileManagerNode file = anon.node(
			newt.ivorn()
			);
        }

    /**
     * Check that we can login with name and password, add a file and transfer some data into it.
     *
     */
    public void testLoginPassAddFileAddData()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode frog = home.addNode(
			"frog"
			);
		FileManagerNode toad = frog.addNode(
			"toad"
			);
		//
		// Add a file.
		FileManagerNode newt = toad.addFile(
			"newt"
			);
        //
        // Open a stream to our file.
        OutputStream output = newt.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        }

    /**
     * Check that we can transfer some data into a file and then read it back again.
     *
     */
    public void testLoginPassAddFileAddDataGetData()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                accountIvorn,
                accountPass
                );
        //
        // Get our home node.
		FileManagerNode home = client.home();
		//
		// Add some containers.
		FileManagerNode frog = home.addNode(
			"frog"
			);
		FileManagerNode toad = frog.addNode(
			"toad"
			);
		//
		// Add a file.
		FileManagerNode newt = toad.addFile(
			"newt"
			);
        //
        // Open a stream to our file.
        OutputStream output = newt.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;

        //
        // Open an input stream from our file.
        InputStream input = newt.exportStream() ;
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read the data from the file into the buffer.
        new TransferUtil(
            input,
            buffer
            ).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );
        }







    /**
     * Test that we can login with a token.
     *
     */
    public void testLoginToken()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Login to our community account.
        SecurityToken token =
            loginCommunityAccount(
                accountIvorn,
                accountPass
                );
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                token
                );
        //
        // Check our client.
        assertNotNull(
            client
            );
        }

    /**
     * Test that a login with a token gives us access to our home.
     *
     */
    public void testLoginTokenHome()
        throws Exception
        {
        //
        // Register our accounts.
        registerAccounts();
        //
        // Login to our community account.
        SecurityToken token =
            loginCommunityAccount(
                accountIvorn,
                accountPass
                );
        //
        // Create our factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                token
                );
        //
        // Check our home node.
        assertNotNull(
            client.home()
            );
        }




//
// .............
//

    /**
     * Register our Community and FileManager account.
     * Not part of the FileManagerClient API, this sets up the Community and FileManager accounts used in the tests.
     *
     */
    public void registerAccounts()
        throws Exception
        {
        System.out.println("registerAccount(Ivorn)") ;
        //
        // Create our Community account.
        accountData =
            createCommunityAccount(
                accountIvorn
                );
        //
        // Set our account password.
        registerPassword(
            accountIvorn,
            accountPass
            );
        //
        // Create our FileManager account.
        accountNode =
            createFileManagerAccount(
                accountIvorn
                );
        //
        // Register our FileManager space.
        accountData =
            registerCommunityHome(
                accountIvorn,
                accountNode.ivorn()
                );
        }

    /**
     * Create a Community account.
     * Not part of the FileManagerClient API, this sets up the Community account(s) used in the tests.
     *
     */
    public AccountData createCommunityAccount(Ivorn ivorn)
        throws Exception
        {
        System.out.println("createCommunityAccount(Ivorn)") ;
        System.out.println("  Ivorn : " + ivorn.toString()) ;
        //
        // Create our resolver.
        PolicyManagerResolver resolver = new PolicyManagerResolver() ;
        //
        // Resolve our Community service.
        PolicyManagerDelegate delegate = resolver.resolve(ivorn) ;
        //
        // Create the account.
        return delegate.addAccount(
            new CommunityIvornParser(
                ivorn
                ).getAccountIdent()
            );
        }

    /**
     * Set our Community account password.
     * Not part of the FileManagerClient API, this sets up the Community account(s) used in the tests.
     *
     */
    public void registerPassword(Ivorn ivorn, String pass)
        throws Exception
        {
        System.out.println("registerPassword(Ivorn, String)") ;
        System.out.println("  Ivorn : " + ivorn.toString()) ;
        System.out.println("  Pass  : " + pass) ;
        //
        // Create our resolver.
        SecurityManagerResolver resolver = new SecurityManagerResolver() ;
        //
        // Resolve our Community service.
        SecurityManagerDelegate delegate = resolver.resolve(ivorn) ;
        //
        // Update the account.
        delegate.setPassword(
            new CommunityIvornParser(
                ivorn
                ).getAccountIdent(),
            pass
            );
        }

    /**
     * Create our FileManager space.
     * Not part of the FileManagerClient API, this sets up the FileManager account(s) used in the tests.
     *
     */
    public FileManagerNode createFileManagerAccount(Ivorn ivorn)
        throws Exception
        {
        System.out.println("createFileManagerAccount(Ivorn)") ;
        System.out.println("  Ivorn : " + ivorn.toString()) ;
        //
        // Create our resolver.
        FileManagerDelegateResolver resolver = new FileManagerDelegateResolverImpl() ;
        //
        // Resolve our FileManager service.
        FileManagerDelegate delegate = 
            resolver.resolve(
                new Ivorn(
                    getTestProperty(
                        "ivorn"
                        )
                    )
                ) ;
        //
        // Create the account.
        return delegate.addAccount(
            ivorn
            );
        }

    /**
     * Register our FileManagerSpace in our CommunityAccount.
     * Not part of the FileManagerClient API, this sets up the FileManager space for the Community account(s) used in the tests.
     * This should not be needed on the live system, if the Community is configured
     * to register space with the local FileManager service automagically.
     *
     */
    public AccountData registerCommunityHome(Ivorn ivorn, Ivorn home)
        throws Exception
        {
        System.out.println("registerCommunityHome(Ivorn, Ivorn)");
        System.out.println("  Ivorn : " + ivorn.toString());
        System.out.println("  Home  : " + home.toString());
        //
        // Create our resolver.
        PolicyManagerResolver resolver = new PolicyManagerResolver();
        //
        // Resolve our Community service.
        PolicyManagerDelegate delegate = resolver.resolve(ivorn);
        //
        // Get the account data.
        AccountData account = delegate.getAccount(
            new CommunityIvornParser(
                ivorn
                ).getAccountIdent()
            );
        //
        // Set the home space ivorn.
        account.setHomeSpace(
            home.toString()
            );
        //
        // Update the account data.
        return delegate.setAccount(
            account
            );
        }

    /**
     * Login to our Community account and get a token.
     * Not part of the FileManagerClient API, this uses the Community security API to set up the token(s) used in the tests.
     *
     */
    public SecurityToken loginCommunityAccount(Ivorn ivorn, String pass)
        throws Exception
        {
        System.out.println("loginCommunityAccount(Ivorn, String)") ;
        System.out.println("  Ivorn : " + ivorn.toString()) ;
        System.out.println("  Pass  : " + pass) ;
        //
        // Create our resolver.
        SecurityServiceResolver resolver = new SecurityServiceResolver() ;
        //
        // Resolve our Community service.
        SecurityServiceDelegate delegate = resolver.resolve(ivorn) ;
        //
        // Login to the account.
        return delegate.checkPassword(
            ivorn.toString(),
            pass
            );
        }

    }