/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/CommunityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerImpl.java,v $
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.54.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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

import org.astrogrid.community.server.service.CommunityServiceImpl ;
import org.astrogrid.community.server.database.configuration.DatabaseConfiguration ;

public class CommunityManagerImpl
    extends CommunityServiceImpl
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
     * Public constructor, using a parent service.
     *
     */
    public CommunityManagerImpl(CommunityServiceImpl parent)
        {
        super(parent) ;
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
