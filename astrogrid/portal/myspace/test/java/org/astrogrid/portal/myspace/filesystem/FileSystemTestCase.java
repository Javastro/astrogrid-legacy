/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portal/myspace/test/java/org/astrogrid/portal/myspace/filesystem/FileSystemTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/29 22:07:56 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileSystemTestCase.java,v $
 *   Revision 1.2  2005/03/29 22:07:56  clq2
 *   jl908 merge
 *
 *   Revision 1.1.2.1  2005/02/15 15:19:39  jl99
 *   First level unit tests.
 *
 *   Revision 1.2  2005/02/10 12:44:10  jdt
 *   Merge from dave-dev-200502010902
 *
 *   Revision 1.1.2.1  2005/02/01 16:32:26  dave
 *   Added example test case ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.portal.myspace.filesystem;

import junit.framework.TestCase;

import org.astrogrid.store.Ivorn;

import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver;

import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.community.client.security.service.SecurityServiceDelegate;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver;
import org.astrogrid.community.client.security.manager.SecurityManagerDelegate;
import org.astrogrid.community.resolver.security.manager.SecurityManagerResolver;

import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;

import org.astrogrid.filemanager.client.delegate.FileManagerDelegate;
import org.astrogrid.filemanager.client.delegate.FileManagerMockDelegate;

import org.astrogrid.filemanager.resolver.FileManagerDelegateResolver;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverMock;

import org.astrogrid.filemanager.common.FileManagerStore;
import org.astrogrid.filemanager.common.FileManagerStoreMock;
import org.astrogrid.filemanager.common.FileManagerConfig;
import org.astrogrid.filemanager.common.FileManagerConfigMock;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock ;

/**
 * An example use of the FileManagerClient.
 *
 */
public class FileSystemTestCase	extends TestCase
	{

	/**
	 * Our test FileManager ivorn.
	 *
	 */
	private static final String FILE_MANAGER_ALPHA = "ivo://org.astrogrid.mock.example/filemanager/alpha" ;

	/**
	 * Our test FileStore ivorn.
	 *
	 */
	private static final String FILE_STORE_ALPHA = "ivo://org.astrogrid.mock.example/filestore/alpha" ;

	/**
	 * The test Community account password.
	 *
	 */
	protected static final String ACCOUNT_PASSWORD = "secret" ;

	/**
	 * Our mock FileManager resolver.
	 * This uses an internal HashMap of services rather than calling the Registry.
	 *
	 */
	private FileManagerDelegateResolverMock fileManagerResolver ;

	/**
	 * Setup our test.
	 *
	 */
	protected void setUp()
		throws Exception
		{
        System.out.println("") ;
        System.out.println("setUp()") ;
		//
		// Initialise our base class.
		super.setUp();
        //
        // Initialise our manager config.
        // This sets up a mock config for the FileManager, with fixed
		// Ivorns for the default FileManager and FileStore services.
        FileManagerConfig fileManagerConfig = 
            new FileManagerConfigMock(
                new Ivorn(
                    FILE_MANAGER_ALPHA
                    ),
                new Ivorn(
					FILE_STORE_ALPHA
                	)
                );
        //
        // Initialise our manager store.
        // This sets the default storage for the FileManager to use.
        FileManagerStore fileManagerStore = new FileManagerStoreMock();
        //
        // Initialise our filestore resolver.
        // This sets the default FileStoreResolver for the FileManager to use.
        FileStoreDelegateResolverMock fileStoreResolver = new FileStoreDelegateResolverMock() ;
        //
        // Register our test filestore(s).
        // This registers our mock FileStore with its Ivorn.
        fileStoreResolver.register(
            new FileStoreMockDelegate(
		        new Ivorn(
		        	FILE_STORE_ALPHA
		        	)
                )
            );
        //
        // Initialise our Ivorn factory.
        // This sets the Ivorn factory used by our FileManager service.
        FileManagerIvornFactory fileManagerIvornFactory = new FileManagerIvornFactory();
		//
		// Initialise our FileManagerResolver.
		fileManagerResolver = new FileManagerDelegateResolverMock() ;
		//
		// Register our mock FileManager.
		fileManagerResolver.register(
			new FileManagerMockDelegate(
		        fileManagerConfig,
		        fileManagerStore,
		        fileManagerIvornFactory,
		        fileStoreResolver
		        )
			);
		}

    /**
     * Check that we can setup a Community account.
     * Not part of the FileManagerClient API, just testing that the test environment works.
     *
     */
    public void _testCreateCommunityIvorn()
        throws Exception
        {
        //
        // Create our Community account.
        assertNotNull(
            createCommunityIvorn()
            );
        }

    /**
     * Check that we can setup a Community account.
     * Not part of the FileManagerClient API, just testing that the test environment works.
     *
     */
    public void _testCreateCommunityAccount()
        throws Exception
        {
		//
		// Create our account Ivorn.
		Ivorn ivorn = createCommunityIvorn();
        //
        // Create our Community account.
        assertNotNull(
            createCommunityAccount(
                ivorn
                )
            );
        }

	/**
	 * Check that we can set the community password.
     * Not part of the FileManagerClient API, just testing that the test environment works.
	 *
	 */
    public void _testCreateCommunityPassword()
        throws Exception
        {
		//
		// Create our account Ivorn.
		Ivorn ivorn = createCommunityIvorn();
        //
        // Create our Community account.
        AccountData account =
            createCommunityAccount(
                ivorn
                );
		//
		// Set the account password.
		registerPassword(
			ivorn,
			ACCOUNT_PASSWORD
			);
        }

    /**
     * Test that we can setup a FileManager account.
     * Not part of the FileManagerClient API, just testing that the test environment works.
     * This would normally be done by the Community service when a new account is created.
     * At the moment, the Community service defaults to allocating space in the MySpace service.
     *
     */
    public void _testCreateFileManagerAccount()
        throws Exception
        {
		//
		// Create our account Ivorn.
		Ivorn ivorn = createCommunityIvorn();
        //
        // Create our FileManager account.
        assertNotNull(
            createFileManagerAccount(
                ivorn
                )
            );
        }

/*
 * Now that we can create Filemanager accounts, we can test the FileManagerClient API.
 *
 */

    /**
     * Test that we can login anonymously.
     *
     */
    public void _testLoginAnon()
        throws Exception
        {
        //
        // Create our FileManagerClient factory (using our mock resolver).
        FileManagerClientFactory factory = new FileManagerClientFactory(
        	fileManagerResolver
        	);
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
     * Test that we can login with name and password.
     *
     */
    public void _testLoginPass()
        throws Exception
        {
        //
        // Register our accounts.
        Ivorn ivorn = register();
        //
        // Create our FileManagerClient factory (using our mock resolver).
        FileManagerClientFactory factory = new FileManagerClientFactory(
        	fileManagerResolver
        	);
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                ivorn,
                ACCOUNT_PASSWORD
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
    public void _testLoginPassHome()
        throws Exception
        {
        //
        // Register our accounts.
        Ivorn ivorn = register();
        //
        // Create our FileManagerClient factory (using our mock resolver).
        FileManagerClientFactory factory = new FileManagerClientFactory(
        	fileManagerResolver
        	);
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                ivorn,
                ACCOUNT_PASSWORD
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
    public void _testLoginAddFile()
        throws Exception
        {
        //
        // Register our accounts.
        Ivorn ivorn = register();
        //
        // Create our FileManagerClient factory (using our mock resolver).
        FileManagerClientFactory factory = new FileManagerClientFactory(
        	fileManagerResolver
        	);
        //
        // Resolve our client.
        FileManagerClient client =
            factory.login(
                ivorn,
                ACCOUNT_PASSWORD
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

/*
 * Toolkit of methods for creating Community an FileManager accounts.
 *
 */

	/**
	 * Create a new (mock) Community Ivorn.
	 * This creates a new Ivorn like "ivo://org.astrogrid.mock.example/1107257601672"
	 * The Community PolicyManagerResolver and SecurityManagerResolver check for an
	 * Ivorn starting with "ivo://org.astrogrid.mock...." and return a mock service implementation.
	 *
	 */
	public Ivorn createCommunityIvorn()
        throws Exception
		{
        return CommunityAccountIvornFactory.createMock(
            "example",
            String.valueOf(
                System.currentTimeMillis()
                )
            ) ;
		}

    /**
     * Create a Community account.
     * Not part of the FileManagerClient API, this sets up the Community account(s) used in the tests.
	 * The PolicyManagerResolver checks for an Ivorn starting with "ivo://org.astrogrid.mock...."
	 * and returns a mock service implementation.
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
	 * The SecurityManagerResolver checks for an Ivorn starting with "ivo://org.astrogrid.mock...."
	 * and returns a mock service implementation.
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
        // Resolve our FileManager service.
        FileManagerDelegate delegate = 
            fileManagerResolver.resolve(
                new Ivorn(
					FILE_MANAGER_ALPHA
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

	/**
	 * Register our Community and FileManager accounts.
	 *
	 */
	public Ivorn register()
		throws Exception
		{
		//
		// Create our account Ivorn.
		Ivorn ivorn = createCommunityIvorn();
        //
        // Create our Community account.
        AccountData account =
            createCommunityAccount(
                ivorn
                );
		//
		// Set the account password.
		registerPassword(
			ivorn,
			ACCOUNT_PASSWORD
			);
        //
        // Create our FileManager account.
        FileManagerNode home = createFileManagerAccount(
            ivorn
            );
		//
		// Register our FileManager space in our Community account.
		registerCommunityHome(
			ivorn,
			home.ivorn()
			);
		//
		// Return our (Community) account Ivorn.
		return ivorn ;
		}

    /**
     * @return Returns the fileManagerResolver.
     */
    protected FileManagerDelegateResolverMock getFileManagerResolver() {
        return fileManagerResolver;
    }
    
	}
