package org.astrogrid.community.server.policy.manager ;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityPolicyException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.server.CommunityConfiguration;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration;
import org.astrogrid.community.server.sso.CredentialStore;
import org.astrogrid.store.Ivorn;

/**
 * A JUnit test case for our AccountManager.
 *
 */
public class AccountManagerImplTestCase extends TestCase {
    
  private static Log log = LogFactory.getLog(AccountManagerImplTestCase.class);
    
    private DatabaseConfiguration dbConfig;
    private VOSpace vospace;
    private CredentialStore store;
    private CommunityConfiguration comConfig;

    /**
     * Setup our test.
     * Creates a new AccountManagerImpl to test.
     *
     */
    @Override
    public void setUp() throws Exception {
      URL u = this.getClass().getResource("/test-database-001.xml");
      dbConfig = new DatabaseConfiguration("test-database-001", u);
      dbConfig.resetDatabaseTables();
      comConfig = new CommunityConfiguration();
      comConfig.setPublishingAuthority("org.astrogrid.local.community/community");
      comConfig.setVoSpaceIvorn("ivo://pond/vospace");
      comConfig.setCredentialDirectory(new File("target"));
      this.vospace = new VOSpace(new MockNodeDelegate());
      this.store = new MockCredentialStore(dbConfig);
    }
    
    /**
     * Try creating a null Account.
     *
     */
    public void testCreateNull()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testCreateNull()") ;
        
        AccountManagerImpl sut = 
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating an Account.
        try {
            sut.addAccount((String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        //
        // Try creating an Account.
        try {
            sut.addAccount((AccountData)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can create a valid Account.
     *
     */
    public void testCreateValid()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testCreateValid()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating an Account.
        assertNotNull("Null account",
            sut.addAccount(
                createLocal("test-account").toString()
                )
            ) ;
        }

    /**
     * Check we can create a valid Account.
     *
     */
    public void testCreateData()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testCreateData()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating an Account.
        assertNotNull("Null account",
            sut.addAccount(
                new AccountData(
                    createLocal("test-account").toString()
                    )
                )
            ) ;
        }

    /**
     * Try to create a duplicate Account.
     *
     */
    public void testCreateDuplicate()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testCreateDuplicate()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating an Account.
        assertNotNull("Null account",
            sut.addAccount(
                createLocal("test-account").toString()
                )
            ) ;
        //
        // Try creating the same Account.
        try {
            sut.addAccount(
                createLocal("test-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try getting a null Account.
     *
     */
    public void testGetNull()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testGetNull()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try getting the details.
        try {
            sut.getAccount((String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try getting an unknown Account.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testGetUnknown()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try getting the details.
        try {
            sut.getAccount(
                createLocal("unknown-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try getting a valid Account.
     *
     */
    public void testGetValid()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testGetValid()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating an Account.
        AccountData created = sut.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", created) ;
        //
        // Try getting the details.
        AccountData found = sut.getAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", found) ;
        //
        // Check that they refer to the same account.
        assertEquals("Different identifiers", created.getIdent(), found.getIdent());
        }
    
    /**
     * Try getting a valid Account.
     *
     */
    public void testGetLocalAccounts()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testGetLocalAccounts()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating some Accounts.
        AccountData created01 = sut.addAccount(
            createLocal("test-account-01").toString()
            ) ;
        assertNotNull("Null account", created01) ;
        
        AccountData created02 = sut.addAccount(
            createLocal("test-account-02").toString()
            ) ;
        assertNotNull("Null account", created02) ;        
        
        Object[] localAccounts = sut.getLocalAccounts() ;
        assertNotNull( "Null local accounts array", localAccounts ) ;
        }
    
    /**
     * Try getting a valid Account.
     *
     */
    public AccountData testGetValidAccountData()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testGetValid()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating an Account.
        AccountData created = sut.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", created) ;
        //
        // Try getting the details.
        AccountData found = sut.getAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", found) ;
        //
        // Check that they refer to the same account.
        assertEquals("Different identifiers", created, found) ;
        return found;
    }
    

    /**
     * Try setting a null Account.
     *
     */
    public void testSetNull()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testSetNull()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        try {
            sut.setAccount(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try setting an unknown Account.
     *
     */
    public void testSetUnknown()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testSetUnknown()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try setting an unknown account.
        try {
            sut.setAccount(
                new AccountData(
                    createLocal("unknown-account").toString()
                    )
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try setting a valid Account.
     *
     */
    public void testSetValid()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testSetValid()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        //
        // Try creating an Account.
        AccountData account = sut.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", account) ;
        //
        // Change the details.
        account.setDisplayName("Test DisplayName") ;
        account.setDescription("Test Description") ;
        account.setEmailAddress("Test EmailAddress") ;
        account.setHomeSpace("Test HomeSpace") ;
        //
        // Try setting the details.
        account = sut.setAccount(account) ;
        assertNotNull("Null account", account) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", "Test DisplayName",  account.getDisplayName())  ;
        assertEquals("Different details", "Test Description",  account.getDescription())  ;
        assertEquals("Different details", "Test EmailAddress", account.getEmailAddress()) ;
        assertEquals("Different details", "Test HomeSpace",    account.getHomeSpace())    ;
        //
        // Try getting the details.
        account = sut.getAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", account) ;
        //
        // Check the details have been changed.
        assertEquals("Different details", account.getDisplayName(),  "Test DisplayName")  ;
        assertEquals("Different details", account.getDescription(),  "Test Description")  ;
        assertEquals("Different details", account.getEmailAddress(), "Test EmailAddress") ;
        assertEquals("Different details", account.getHomeSpace(),    "Test HomeSpace")    ;
        }

    /**
     * Try deleting a null Account.
     *
     */
    public void testDeleteNull()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testDeleteNull()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        try {
            sut.delAccount((String)null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try deleting an unknown Account.
     *
     */
    public void testDeleteUnknown()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testDeleteUnknown()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        try {
            sut.delAccount(
                createLocal("unknown-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Try deleting a valid Account.
     *
     */
    public void testDeleteValid()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testDeleteValid()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        
        Ivorn originalAccountIvorn = createLocal("test-account");
        
        //
        // Try creating the Account.
        AccountData created = sut.addAccount(originalAccountIvorn.toString());
        assertNotNull("Null account", created);
        assertEquals(originalAccountIvorn.toString(), created.getIdent());
        
        
        //
        // Try deleting the Account.
        AccountData deleted = sut.delAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", deleted) ;
        //
        // Check that the two objects represent the same Account.
        assertEquals("Different identifiers", created.getIdent(), deleted.getIdent());
        }

    /**
     * Try deleting the same Account twice.
     *
     */
    public void testDeleteTwice()
        throws Exception
        {
        System.out.println("") ;
        System.out.println("----\"----") ;
        System.out.println("AccountManagerTest:testDeleteTwice()") ;
        
        AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
        //
        // Try creating the Account.
        AccountData created = sut.addAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", created) ;
        //
        // Try deleting the Account.
        AccountData deleted = sut.delAccount(
            createLocal("test-account").toString()
            ) ;
        assertNotNull("Null account", deleted) ;
        //
        // Check that the two objects represent the same Account.
        assertEquals("Different identifiers", created.getIdent(), deleted.getIdent()) ;
        //
        // Try deleting the Account again.
        try {
            sut.delAccount(
                createLocal("test-account").toString()
                ) ;
            fail("Expected CommunityPolicyException") ;
            }
        catch (CommunityPolicyException ouch)
            {
            System.out.println("Caught expected Exception") ;
            System.out.println("Exception : " + ouch) ;
            System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

  public void testPrimaryKey() throws Exception {
    AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
    
    String s1 = sut.primaryKey(new CommunityIvornParser("ivo://frog@foo.bar/baz"));
    assertEquals("ivo://foo.bar/frog", s1);
    
    String s2 = sut.primaryKey(new CommunityIvornParser("ivo://foo.bar/toad"));
    assertEquals("ivo://foo.bar/toad", s2);
  }
  
  public void testAccountIvorn() throws Exception {
    AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
    
    String s1 = sut.accountIvorn("ivo://foo.bar/frog");
    assertEquals("ivo://frog@foo.bar/community", s1);
    
    String s2 = sut.accountIvorn("ivo://foo.bar/toad");
    assertEquals("ivo://toad@foo.bar/community", s2);
  }
  
  public void testRoundTrip() throws Exception {
    System.out.println("Begin testRoundTrip()");
    
    AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
    
    AccountData accountIn = new AccountData("ivo://frog@org.astrogrid.regtest/community");
    sut.addAccount(accountIn);
    
    AccountData accountOut = sut.getAccount("ivo://frog@org.astrogrid.regtest/community");
    assertNotNull(accountOut);
    
    System.out.println("testRoundTrip: in: " + accountIn.getIdent() + " out: " + accountOut.getIdent());
    
    assertEquals(accountIn.getIdent(), accountOut.getIdent());
  }
  
  public void testRoundTripHeadless() throws Exception {
    System.out.println("Begin testRoundTrip()");
    
    AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
    
    AccountData accountIn = new AccountData("ivo://frog@org.astrogrid.regtest/community");
    sut.addAccount(accountIn);
    
    // The stupid, broken client sends the IVORN without the scheme when getting accounts.
    AccountData accountOut = sut.getAccount("frog@org.astrogrid.regtest/community");
    assertNotNull(accountOut);
    
    System.out.println("testRoundTripHeadless: in: " + accountIn.getIdent() + " out: " + accountOut.getIdent());
    
    assertEquals(accountIn.getIdent(), accountOut.getIdent());
  }

  public void testAllocateSpace() throws Exception {
    AccountManagerImpl sut =
            new AccountManagerImpl(comConfig, dbConfig, store, vospace);
    AccountData account = new AccountData(createLocal("frog").toString());
    sut.addAccount(account);
    
    sut.allocateHomespace("ivo://frog@pond/community", "frog", "croakcroak");
  }
  
  /**
   * Create a local Ivorn.
   *
   */
  private Ivorn createLocal(String ident) 
      throws URISyntaxException,
             CommunityServiceException, 
             CommunityIdentifierException {
    //return CommunityAccountIvornFactory.createLocal(ident) ;
    return new Ivorn("ivo://" + ident + "@org.astrogrid.regtest/community");
  }

}

