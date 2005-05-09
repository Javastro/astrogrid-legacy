/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/community/stress/CommunityMultLoadStressTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/05/09 15:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityMultLoadStressTest.java,v $
 *   Revision 1.2  2005/05/09 15:10:07  clq2
 *   Kevin's commits
 *
 *   Revision 1.1.2.1  2005/04/29 07:30:45  KevinBenson
 *   Added some stress test and fixed this small bug with the community querying on relationships
 *
 *   Revision 1.1  2005/02/14 13:46:27  jdt
 *   added a stress test for community accounts
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.stress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate;
import org.astrogrid.community.client.security.manager.SecurityManagerDelegate;
import org.astrogrid.community.client.security.manager.SecurityManagerSoapDelegate;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
import org.astrogrid.community.common.exception.CommunityPolicyException;
import org.astrogrid.community.common.exception.CommunitySecurityException;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.policy.data.AccountData;
import org.astrogrid.community.common.policy.data.GroupData;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.ResourceData;
import org.astrogrid.community.resolver.CommunityAccountResolver ;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver;
import org.astrogrid.community.resolver.security.manager.SecurityManagerResolver;

/**
 * A utility to load Community data from an XML file.
 *  
 */
public class CommunityMultLoadStressTest extends TestCase {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommunityLoadStressTest.class);

    /**
     * Public constructor.
     * 
     * @param policyManager
     *            A reference to the PolicyManagerDelegate to upload the data
     *            to.
     * @param securityManager
     *            A reference to the SecurityManagerDelegate to upload the data
     *            to.
     * @throws MalformedURLException
     *  
     */
    public CommunityMultLoadStressTest()
            throws MalformedURLException {

        //String communityPolicyManagerUrl = SimpleConfig.getProperty("org.astrogrid.community.policymanager.url");
        //String communitySecurityManagerUrl = SimpleConfig.getProperty("org.astrogrid.community.securitymanager.url");
        //logger.debug("Creating delegate for PolicyManager at " + communityPolicyManagerUrl);
        //logger.debug("Creating delegate for SecurityManager at " + communitySecurityManagerUrl);

        //policyManager = new PolicyManagerSoapDelegate(new URL(communityPolicyManagerUrl));
        //securityManager = new SecurityManagerSoapDelegate(new URL(communitySecurityManagerUrl));
        try {
            PolicyManagerResolver pmr = new PolicyManagerResolver();
            ResourceData[] communityServices = pmr.resolve();
            
            SecurityManagerResolver smr = new SecurityManagerResolver();
            policyManager = pmr.resolve(((ResourceData)communityServices[0]).getIvorn());
            securityManager = smr.resolve(((ResourceData)communityServices[0]).getIvorn());
        }catch(Exception e) {
            e.printStackTrace();            
            policyManager = null;
            securityManager = null;
        }

    }

    /**
     * Our PolicyManagerDelegate.
     *  
     */
    private PolicyManagerDelegate policyManager;

    /**
     * Our SecurityManagerDelegate.
     *  
     */
    private SecurityManagerDelegate securityManager;

    private static final int MAXACCOUNTS = 100;

    private List accounts;

    private Map passwords;

    /**
     * Initialise accounts for upload
     */
    public void setUp() {
        String communityIdent = SimpleConfig.getProperty("org.astrogrid.community.ident");
        logger.info("Initialising " + MAXACCOUNTS + " accounts for community " + communityIdent);
        accounts = new ArrayList();
        passwords = new HashMap();
        for (int i = 0; i < MAXACCOUNTS; ++i) {
            String ident = "ivo://" + communityIdent + "/stresstestuser" + i;
            AccountData account = new AccountData(ident);
            account.setDescription("A Test User");
            account.setDisplayName("Mr Test User");
            account.setEmailAddress("jdt@roe.ac.uk");
            account.setHomeSpace(null);
            accounts.add(account);
            passwords.put(ident, "qwerty" + i);
        }
    }

    /**
     * GetAccounts
     *  
     */
    public void getAccountsFromManager() throws RegistryException, CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException {

        logger.debug("upload() - CommunityLoadStressTest.upload()");

        //
        // Check for null manager.
        if (null == this.policyManager) {
            throw new CommunityServiceException("PolicyManager not configured");
        }

        this.policyManager.getLocalAccounts();
    }
    
    /**
     * GetAccounts
     *  
     */
    public void getSingleAccount() throws RegistryException, CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException {

        logger.debug("upload() - CommunityLoadStressTest.upload()");

        //
        // Check for null manager.
        if (null == this.policyManager) {
            throw new CommunityServiceException("PolicyManager not configured");
        }

        //
        // Process the Community Accounts.
        Iterator iter;
        iter = getAccounts().iterator();
        AccountData account = (AccountData) iter.next();        
        this.policyManager.getAccount(account.getIdent());
    }

    
    /**
     * GetAccounts
     *  
     */
    public void getGroupsFromManager() throws RegistryException, CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException {

        logger.debug("upload() - CommunityLoadStressTest.upload()");

        //
        // Check for null manager.
        if (null == this.policyManager) {
            throw new CommunityServiceException("PolicyManager not configured");
        }

        this.policyManager.getLocalGroups();
    }
    
    /**
     * GetAccounts
     *  
     */
    public void getSingleGroup() throws RegistryException, CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException {

        logger.debug("upload() - CommunityLoadStressTest.upload()");

        //
        // Check for null manager.
        if (null == this.policyManager) {
            throw new CommunityServiceException("PolicyManager not configured");
        }

        //
        // Process the Community Accounts.
        Iterator iter;
        iter = getAccounts().iterator();
        AccountData account = (AccountData) iter.next();        
        this.policyManager.getGroup(account.getIdent());
    }            

    /**
     * @return
     */
    private List getAccounts() {
        return accounts;
    }

    /**
     * @return
     */
    private Map getPasswords() {
        return passwords;
    }

    /**
     * Can we get accounts with high threads
     * 
     * @throws RegistryException
     * @throws CommunityPolicyException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     *  
     */
    public void testGetAccounts() throws CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException, RegistryException {
        getAccountsFromManager();
    }
    
    /**
     * Can we get accounts with high threads
     * 
     * @throws RegistryException
     * @throws CommunityPolicyException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     *  
     */
    public void testGetGroups() throws CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException, RegistryException {
        getGroupsFromManager();
    }
    
    /**
     * Can we get accounts with high threads
     * 
     * @throws RegistryException
     * @throws CommunityPolicyException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     *  
     */
    public void testGetAccount() throws CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException, RegistryException {
        getSingleAccount();
    }

    /**
     * Can we get accounts with high threads
     * 
     * @throws RegistryException
     * @throws CommunityPolicyException
     * @throws CommunityIdentifierException
     * @throws CommunitySecurityException
     * @throws CommunityServiceException
     *  
     */
    public void testGetGroup() throws CommunityServiceException, CommunitySecurityException,
            CommunityIdentifierException, CommunityPolicyException, RegistryException {
        getSingleGroup();
    }    
}