/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/java/org/astrogrid/community/install/loader/CommunityLoader.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityLoader.java,v $
 *   Revision 1.3  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.2.4.2  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.2.4.1  2004/03/28 02:00:55  dave
 *   Added database management tasks.
 *
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/20 06:54:11  dave
 *   Added addAccount(AccountData) to PolicyManager et al.
 *   Added XML loader for AccountData.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.install.loader ;

import java.net.URL ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import java.io.IOException ;


import java.util.HashMap ;
import java.util.Iterator ;
import java.util.Collection ;
import java.util.Enumeration ;

import org.xml.sax.InputSource;

import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.CastorException ;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.RegistryException;

import org.astrogrid.community.common.ivorn.CommunityServiceIvornFactory ;

import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.security.manager.SecurityManagerDelegate ;

 /**
  * A utility to load Community data from an XML file.
  *
  */
public class CommunityLoader
    {
    /**
     * Switch for our debug statements.
     * @todo Refactor to use the common logging.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * The default name for our XML mapping file.
     *
     */
    private static final String XML_MAPPING_NAME = "/org/astrogrid/community/install/loader/mapping.xml" ;

    /**
     * Public constructor.
     * @param policyManager   A reference to the PolicyManagerDelegate to upload the data to.
     * @param securityManager A reference to the SecurityManagerDelegate to upload the data to.
     *
     */
    public CommunityLoader(PolicyManagerDelegate policyManager, SecurityManagerDelegate securityManager)
        {
        this.policyManager   = policyManager ;
        this.securityManager = securityManager ;
        }

    /**
     * Our PolicyManagerDelegate.
     *
     */
    private PolicyManagerDelegate policyManager ;

    /**
     * Our SecurityManagerDelegate.
     *
     */
    private SecurityManagerDelegate securityManager ;

    /**
     * Our Community data.
     *
     */
    private CommunityLoaderData data ;

    /**
     * Load our Community data from our source.
     *
     */
    public void load(URL source)
        throws IOException, CommunityServiceException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityLoader.load()") ;
        //
        // Check for null param.
        if (null == source)
            {
            throw new CommunityServiceException(
                "Null Community data URL"
                ) ;
            }
        //
        // Try parsing the data source.
        try {
            //
            // Locate our XML mapping resource.
            URL map = this.getClass().getResource(
                XML_MAPPING_NAME
                ) ;
            if (null == map)
                {
                throw new CommunityServiceException(
                    "Unable to locate XML mapping"
                    ) ;
                }
            //
            // Create our XML mapping.
            Mapping mapping = new Mapping();
            //
            // Load our XML mapping resource.
            mapping.loadMapping(map) ;
            if (DEBUG_FLAG) System.out.println("PASS : Got mapping") ;
            //
            // Create our marshaller.
            Unmarshaller marshaller = new Unmarshaller(mapping);
            if (DEBUG_FLAG) System.out.println("PASS : Got marshaller") ;
            //
            // Load the Community data.
            this.data = (CommunityLoaderData) marshaller.unmarshal(
                new InputSource(
                    source.openStream()
                    )
                ) ;
            if (DEBUG_FLAG) System.out.println("PASS : Got data") ;
            }
        catch (CastorException ouch)
            {
            if (DEBUG_FLAG) System.out.println("----\"----") ;
            if (DEBUG_FLAG) System.out.println("Castor Exception") ;
            if (DEBUG_FLAG) System.out.println(ouch) ;
            throw new CommunityServiceException(
                "Error occurred loading Community data",
                ouch
                ) ;
            }
        catch (MappingException ouch)
            {
            if (DEBUG_FLAG) System.out.println("----\"----") ;
            if (DEBUG_FLAG) System.out.println("Mapping Exception") ;
            if (DEBUG_FLAG) System.out.println(ouch) ;
            throw new CommunityServiceException(
                "Error occurred loading Community data",
                ouch
                ) ;
            }
        }

    /**
     * Upload our Community data.
     *
     */
    public void upload()
        throws
            RegistryException,
            CommunityServiceException,
            CommunitySecurityException,
            CommunityIdentifierException,
            CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityLoader.upload()") ;
        //
        // Check for null data.
        if (null == data)
            {
            throw new CommunityServiceException(
                "No community data loaded"
                ) ;
            }
        //
        // Check for null manager.
        if (null == this.policyManager)
            {
            throw new CommunityServiceException(
                "PolicyManager not configured"
                ) ;
            }
        //
        // Check for null manager.
        if (null == this.securityManager)
            {
            throw new CommunityServiceException(
                "SecurityManager not configured"
                ) ;
            }
        //
        // Process the Community Accounts.
        Iterator iter ;
        iter = data.getAccounts().iterator() ;
        while (iter.hasNext())
            {
            AccountData account = (AccountData) iter.next() ;
            if (DEBUG_FLAG) System.out.println("----") ;
            if (DEBUG_FLAG) System.out.println("Account : " + account.getIdent()) ;
            this.policyManager.addAccount(
                account
                ) ;
            }
        //
        // Process the Community Groups.
        iter = data.getGroups().iterator() ;
        while (iter.hasNext())
            {
            GroupData group = (GroupData) iter.next() ;
            if (DEBUG_FLAG) System.out.println("----") ;
            if (DEBUG_FLAG) System.out.println("Group : " + group.getIdent()) ;
            this.policyManager.addGroup(
                group
                ) ;
            }
        //
        // Process the Community Passwords.
        HashMap passwords = data.getPasswords() ;
        iter = passwords.keySet().iterator() ;
        while (iter.hasNext())
            {
            String account  = (String) iter.next() ;
            String password = (String) passwords.get(account) ;
            if (DEBUG_FLAG) System.out.println("----") ;
            if (DEBUG_FLAG) System.out.println("Account  : " + account) ;
            if (DEBUG_FLAG) System.out.println("Password : " + password) ;
            this.securityManager.setPassword(
                account,
                password
                ) ;
            }
        }
    }

