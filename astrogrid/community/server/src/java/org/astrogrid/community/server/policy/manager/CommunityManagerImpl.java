/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/CommunityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/02/12 06:56:46 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerImpl.java,v $
 *   Revision 1.3  2004/02/12 06:56:46  dave
 *   Merged development branch, dave-dev-200401131047, into HEAD
 *
 *   Revision 1.2.4.5  2004/02/06 13:49:09  dave
 *   Moved CommunityManagerBase into server.common.CommunityServer.
 *   Moved getServiceStatus into server.common.CommunityServer.
 *   Moved JUnit tests to match.
 *
 *   Revision 1.2.4.4  2004/01/27 19:18:16  dave
 *   Removed unused imports listed in PMD report
 *
 *   Revision 1.2.4.3  2004/01/27 18:55:08  dave
 *   Removed unused imports listed in PMD report
 *
 *   Revision 1.2.4.2  2004/01/27 03:54:28  dave
 *   Changed database name to database config in CommunityManagerBase
 *
 *   Revision 1.2.4.1  2004/01/26 23:23:23  dave
 *   Changed CommunityManagerImpl to use the new DatabaseManager.
 *   Moved rollback and close into CommunityManagerBase.
 *
 *   Revision 1.2  2004/01/07 10:45:45  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.8  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.7  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.6  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.5  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.4  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.3  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.2  2003/09/08 11:01:35  KevinBenson
 *   A check in of the Authentication authenticateToken roughdraft and some changes to the groudata and community data
 *   along with an AdministrationDelegate
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.server.policy.manager ;

//import java.rmi.RemoteException ;

import java.net.URL ;

import java.util.Vector ;
import java.util.Collection ;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;
import org.exolab.castor.jdo.DuplicateIdentityException ;

import org.astrogrid.community.common.policy.data.CommunityData ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.common.policy.manager.PolicyManagerService ;
import org.astrogrid.community.common.policy.manager.PolicyManagerServiceLocator ;

import org.astrogrid.community.common.policy.service.PolicyService ;
import org.astrogrid.community.common.policy.service.PolicyServiceService ;
import org.astrogrid.community.common.policy.service.PolicyServiceServiceLocator ;

import org.astrogrid.community.common.policy.manager.CommunityManager ;

import org.astrogrid.community.server.common.CommunityServer ;
import org.astrogrid.community.server.database.DatabaseConfiguration ;

public class CommunityManagerImpl
	extends CommunityServer
    implements CommunityManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    protected static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using default database configuration.
     *
     */
    public CommunityManagerImpl()
        {
		super() ;
        }

    /**
     * Public constructor, using specific database configuration.
     *
     */
    public CommunityManagerImpl(DatabaseConfiguration config)
        {
		super(config) ;
        }

    /**
     * Create a new Community.
     *
     */
    public CommunityData addCommunity(String name)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.addCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  name : " + name) ;

        // TODO
        // Check that the name is valid.
        //

        //
        // Create the new community.
		CommunityData community = new CommunityData() ;
		community.setIdent(name) ;
        //
        // Set the default endpoint urls.
//
// TODO
// Replace these with constants, or just remove them altogether.
        community.setServiceUrl("http://" + name + ":8080/axis/services/PolicyService") ;
        community.setManagerUrl("http://" + name + ":8080/axis/services/PolicyManager") ;
        //
        // Try performing our transaction.
        Database database = null ;
        try {
			//
			// Open a connection to our database.
			database = this.getDatabase() ;
			//
			// If we got a database connection.
			if (null != database)
				{
	            //
	            // Begin a new database transaction.
	            database.begin();
	            //
	            // Try creating the community in the database.
	            database.create(community);
				//
				// Commit the transaction.
				database.commit() ;
				}
            }
        //
        // If we already have an object with that ident.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
        catch (DuplicateIdentityException ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("DuplicateIdentityException in addCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("Exception in addCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // Close our connection.
        finally
            {
			closeConnection(database) ;
            }
        // TODO
        // Need to return something to the client.
        // Possible a new DataObject ... CommunityResult ?
        // Something to indicate success or failure and the reasons why ....
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return community ;
        }

    /**
     * Request an Community details.
     *
     */
    public CommunityData getCommunity(String ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

        CommunityData community = null ;
        Database database = null ;
        try {
			//
			// Open a connection to our database.
			database = this.getDatabase() ;
			//
			// If we got a database connection.
			if (null != database)
				{
	            //
	            // Begin a new database transaction.
	            database.begin();
	            //
	            // Load the Community from the database.
	            community = (CommunityData) database.load(CommunityData.class, ident) ;
				//
				// Commit the transaction.
				database.commit() ;
				}
            }
        //
        // If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
        catch (ObjectNotFoundException ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in getCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("Exception in getCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // Close our connection.
        finally
            {
			closeConnection(database) ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return community ;
        }

    /**
     * Update an Community details.
     *
     */
    public CommunityData setCommunity(CommunityData community)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.setCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

        //
        // Try update the database.
        Database database = null ;
        try {
			//
			// Open a connection to our database.
			database = this.getDatabase() ;
			//
			// If we got a database connection.
			if (null != database)
				{
	            //
	            // Begin a new database transaction.
	            database.begin();
	            //
	            // Load the Community from the database.
	            CommunityData data = (CommunityData) database.load(CommunityData.class, community.getIdent()) ;
	            //
	            // Update the community data.
	            data.setManagerUrl(community.getManagerUrl()) ;
	            data.setServiceUrl(community.getServiceUrl()) ;
	            data.setDescription(community.getDescription()) ;
				//
				// Commit the transaction.
				database.commit() ;
				}
            }
        //
        // If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
        catch (ObjectNotFoundException ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in setCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("Exception in setCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            //
            // Set the response to null.
            community = null ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // Close our connection.
        finally
            {
			closeConnection(database) ;
            }

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return community ;
        }

    /**
     * Request a list of Communitys.
     *
     */
    public Object[] getCommunityList()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getCommunityList()") ;

        //
        // Try QUERY the database.
        Object[] array = null ;
        Database database = null ;
        try {
			//
			// Open a connection to our database.
			database = this.getDatabase() ;
			//
			// If we got a database connection.
			if (null != database)
				{
	            //
	            // Begin a new database transaction.
	            database.begin();
	            //
	            // Create our OQL query.
	            OQLQuery query = database.getOQLQuery(
	                "SELECT communitys FROM org.astrogrid.community.policy.data.CommunityData communitys"
	                );
	            //
	            // Execute our query.
	            QueryResults results = query.execute();
	            //
	            // Transfer our results to a vector.
	            Collection collection = new Vector() ;
	            while (results.hasMore())
	                {
	                collection.add(results.next()) ;
	                }
	            // 
	            // Convert it into an array.
	            array = collection.toArray() ;
				//
				// Commit the transaction.
				database.commit() ;
				}
            }
        //
        // If anything went bang.
        catch (Exception ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("Exception in getCommunityList()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            array = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // Close our connection.
        finally
            {
			closeConnection(database) ;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return array ;
        }

    /**
     * Delete an Community.
     *
     */
    public CommunityData delCommunity(String ident)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.delCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + ident) ;

        CommunityData community = null ;
        //
        // Try update the database.
        Database database = null ;
        try {
			//
			// Open a connection to our database.
			database = this.getDatabase() ;
			//
			// If we got a database connection.
			if (null != database)
				{
	            //
	            // Begin a new database transaction.
	            database.begin();
	            //
	            // Load the Community from the database.
	            community = (CommunityData) database.load(CommunityData.class, ident) ;
	            //
	            // Delete the community.
	            database.remove(community) ;
				//
				// Commit the transaction.
				database.commit() ;
				}
			//
			// If we didn't get a database connection.
			else {
				//
				// Set the response to null.
				community = null ;
				}
            }
        //
        // If we couldn't find the object.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
        catch (ObjectNotFoundException ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("ObjectNotFoundException in delCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            if (DEBUG_FLAG) System.out.println("") ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("Exception in delCommunity()") ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
			//
			// Cancel the database transaction.
			rollbackTransaction(database) ;
            if (DEBUG_FLAG) System.out.println("  ----") ;
            if (DEBUG_FLAG) System.out.println("") ;
            }
        //
        // Close our connection
        finally
            {
			closeConnection(database) ;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;

        return community ;
        }

    /**
     * Get a PolicyManager for a remote community.
     *
     */
    public PolicyManager getPolicyManager(String name)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyManager()") ;
        if (DEBUG_FLAG) System.out.println("  name : " + name) ;
        //
        // Get the CommunityData.
        CommunityData community = this.getCommunity(name) ;
        //
        // If we found the CommunityData.
        if (null != community)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Found CommunityData") ;
            return getPolicyManager(community) ;
            }
        //
        // If we didn't find the CommunityData.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Unknown CommunityData") ;
            return null ;
            }
        }

    /**
     * Get a PolicyManager for a remote community.
     *
     */
    public PolicyManager getPolicyManager(CommunityData community)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyManager()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + community.getIdent()) ;
        PolicyManager manager = null ;
        //
        // If we have a manager URL.
        if (null != community.getManagerUrl())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Found manager URL " + community.getManagerUrl()) ;
            //
            // Try creating our manager.
            try {
                PolicyManagerService locator = new PolicyManagerServiceLocator() ;
                manager = locator.getPolicyManager(new URL(community.getManagerUrl())) ;
                }
            catch (Exception ouch)
                {
                if (DEBUG_FLAG) System.out.println("Exception when creating remote manager.") ;
                if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
                if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
                }
            if (DEBUG_FLAG) System.out.println("PASS : Created manager ...") ;
            }
        //
        // If we don't have a manager URL
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : NULL manager URL") ;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return manager ;
        }

    /**
     * Get a PolicyService for a remote community.
     *
     */
    public PolicyService getPolicyService(String name)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyService()") ;
        if (DEBUG_FLAG) System.out.println("  name : " + name) ;
        //
        // Get the CommunityData.
        CommunityData community = this.getCommunity(name) ;
        //
        // If we found the CommunityData.
        if (null != community)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Found CommunityData") ;
            return getPolicyService(community) ;
            }
        //
        // If we didn't find the CommunityData.
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : Unknown CommunityData") ;
            return null ;
            }
        }

    /**
     * Get a PolicyService for a remote community.
     *
     */
    public PolicyService getPolicyService(CommunityData community)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerImpl.getPolicyService()") ;
        if (DEBUG_FLAG) System.out.println("  ident : " + community.getIdent()) ;
        PolicyService service = null ;
        //
        // If we have a service URL.
        if (null != community.getServiceUrl())
            {
            if (DEBUG_FLAG) System.out.println("PASS : Found service URL " + community.getServiceUrl()) ;
            //
            // Try creating our service.
            try {
                PolicyServiceService locator = new PolicyServiceServiceLocator() ;
                service = locator.getPolicyService(new URL(community.getServiceUrl())) ;
                }
            catch (Exception ouch)
                {
                if (DEBUG_FLAG) System.out.println("Exception when creating remote service.") ;
                if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
                if (DEBUG_FLAG) System.out.println("  Message   : " + ouch.getMessage()) ;
                }
            if (DEBUG_FLAG) System.out.println("PASS : Created service ...") ;
            }
        //
        // If we don't have a service URL
        else {
            if (DEBUG_FLAG) System.out.println("FAIL : NULL service URL") ;
            }
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        return service ;
        }
    }
