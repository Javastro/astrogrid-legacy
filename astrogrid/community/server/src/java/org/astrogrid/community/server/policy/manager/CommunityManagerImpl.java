/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/java/org/astrogrid/community/server/policy/manager/Attic/CommunityManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/15 10:24:24 $</cvs:date>
 * <cvs:version>$Revision: 1.9 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerImpl.java,v $
 *   Revision 1.9  2005/02/15 10:24:24  jdt
 *   Merged  community_pah_910
 *
 *   Revision 1.8.44.1  2005/02/07 16:03:37  pah
 *   updated log messages to make it explicit in the logs when login-logout occurs.
 *
 *   Revision 1.8  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.7.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityManagerImpl.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.addCommunity()") ;
        log.debug("  name : " + name) ;

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
            log.info("created community ="+community);
            }
        //
        // If we already have an object with that ident.
// TODO
// The only reason to treat this differently is that we might one day report it differently to the client.
        catch (DuplicateIdentityException ouch)
            {
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("DuplicateIdentityException in addCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("Exception in addCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
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
        log.debug("----\"----") ;
        return community ;
        }

    /**
     * Request an Community details.
     *
     */
    public CommunityData getCommunity(String ident)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.getCommunity()") ;
        log.debug("  ident : " + ident) ;

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
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("ObjectNotFoundException in getCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("Exception in getCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // Close our connection.
        finally
            {
            closeConnection(database) ;
            }

        log.debug("----\"----") ;
        return community ;
        }

    /**
     * Update an Community details.
     *
     */
    public CommunityData setCommunity(CommunityData community)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.setCommunity()") ;
        log.debug("  Community") ;
        log.debug("    ident   : " + community.getIdent()) ;
        log.debug("    desc    : " + community.getDescription()) ;
        log.debug("    service : " + community.getServiceUrl()) ;
        log.debug("    manager : " + community.getManagerUrl()) ;

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
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("ObjectNotFoundException in setCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("Exception in setCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            //
            // Set the response to null.
            community = null ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // Close our connection.
        finally
            {
            closeConnection(database) ;
            }

        log.debug("----\"----") ;
        return community ;
        }

    /**
     * Request a list of Communitys.
     *
     */
    public Object[] getCommunityList()
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.getCommunityList()") ;

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
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("Exception in getCommunityList()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            array = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // Close our connection.
        finally
            {
            closeConnection(database) ;
            }
        log.debug("----\"----") ;
        return array ;
        }

    /**
     * Delete an Community.
     *
     */
    public CommunityData delCommunity(String ident)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.delCommunity()") ;
        log.debug("  ident : " + ident) ;

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
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("ObjectNotFoundException in delCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // If anything else went bang.
        catch (Exception ouch)
            {
            log.debug("") ;
            log.debug("  ----") ;
            log.debug("Exception in delCommunity()") ;
            log.debug("  Exception : " + ouch) ;
            log.debug("  Message   : " + ouch.getMessage()) ;
            //
            // Set the response to null.
            community = null ;
            //
            // Cancel the database transaction.
            rollbackTransaction(database) ;
            log.debug("  ----") ;
            log.debug("") ;
            }
        //
        // Close our connection
        finally
            {
            closeConnection(database) ;
            }
        log.debug("----\"----") ;

        return community ;
        }

    /**
     * Get a PolicyManager for a remote community.
     *
     */
    public PolicyManager getPolicyManager(String name)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.getPolicyManager()") ;
        log.debug("  name : " + name) ;
        //
        // Get the CommunityData.
        CommunityData community = this.getCommunity(name) ;
        //
        // If we found the CommunityData.
        if (null != community)
            {
            log.debug("PASS : Found CommunityData") ;
            return getPolicyManager(community) ;
            }
        //
        // If we didn't find the CommunityData.
        else {
            log.debug("FAIL : Unknown CommunityData") ;
            return null ;
            }
        }

    /**
     * Get a PolicyManager for a remote community.
     *
     */
    public PolicyManager getPolicyManager(CommunityData community)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.getPolicyManager()") ;
        log.debug("  ident : " + community.getIdent()) ;
        PolicyManager manager = null ;
        //
        // If we have a manager URL.
        if (null != community.getManagerUrl())
            {
            log.debug("PASS : Found manager URL " + community.getManagerUrl()) ;
            //
            // Try creating our manager.
            try {
                PolicyManagerService locator = new PolicyManagerServiceLocator() ;
                manager = locator.getPolicyManager(new URL(community.getManagerUrl())) ;
                }
            catch (Exception ouch)
                {
                log.debug("Exception when creating remote manager.") ;
                log.debug("  Exception : " + ouch) ;
                log.debug("  Message   : " + ouch.getMessage()) ;
                }
            log.debug("PASS : Created manager ...") ;
            }
        //
        // If we don't have a manager URL
        else {
            log.debug("FAIL : NULL manager URL") ;
            }
        log.debug("----\"----") ;
        return manager ;
        }

    /**
     * Get a PolicyService for a remote community.
     *
     */
    public PolicyService getPolicyService(String name)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.getPolicyService()") ;
        log.debug("  name : " + name) ;
        //
        // Get the CommunityData.
        CommunityData community = this.getCommunity(name) ;
        //
        // If we found the CommunityData.
        if (null != community)
            {
            log.debug("PASS : Found CommunityData") ;
            return getPolicyService(community) ;
            }
        //
        // If we didn't find the CommunityData.
        else {
            log.debug("FAIL : Unknown CommunityData") ;
            return null ;
            }
        }

    /**
     * Get a PolicyService for a remote community.
     *
     */
    public PolicyService getPolicyService(CommunityData community)
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerImpl.getPolicyService()") ;
        log.debug("  ident : " + community.getIdent()) ;
        PolicyService service = null ;
        //
        // If we have a service URL.
        if (null != community.getServiceUrl())
            {
            log.debug("PASS : Found service URL " + community.getServiceUrl()) ;
            //
            // Try creating our service.
            try {
                PolicyServiceService locator = new PolicyServiceServiceLocator() ;
                service = locator.getPolicyService(new URL(community.getServiceUrl())) ;
                }
            catch (Exception ouch)
                {
                log.debug("Exception when creating remote service.") ;
                log.debug("  Exception : " + ouch) ;
                log.debug("  Message   : " + ouch.getMessage()) ;
                }
            log.debug("PASS : Created service ...") ;
            }
        //
        // If we don't have a service URL
        else {
            log.debug("FAIL : NULL service URL") ;
            }
        log.debug("----\"----") ;
        return service ;
        }
    }
