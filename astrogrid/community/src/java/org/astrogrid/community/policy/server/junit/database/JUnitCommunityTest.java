/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/junit/database/Attic/JUnitCommunityTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitCommunityTest.java,v $
 *   Revision 1.4  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.3  2003/09/13 02:18:52  dave
 *   Extended the jConfig configuration code.
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server.junit.database ;

import junit.framework.TestCase ;

import org.astrogrid.community.policy.server.DatabaseManager ;
import org.astrogrid.community.policy.server.DatabaseManagerImpl ;

import org.astrogrid.community.policy.data.CommunityData ;

import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.QueryResults;
import org.exolab.castor.jdo.ObjectNotFoundException ;

import org.exolab.castor.util.Logger;
import org.exolab.castor.persist.spi.Complex ;
import org.exolab.castor.mapping.Mapping;

import java.io.PrintWriter;

import java.util.Iterator ;
import java.util.Collection ;

/**
 *
 * JUnit test for accessing Community objects using Castor JDO.
 *
 */
public class JUnitCommunityTest
    extends TestCase
    {
    /**
     * Our test community ident.
     *
     */
    private static final String TEST_COMMUNITY_IDENT = "server.database" ;

    /**
     * Our test community service url.
     *
     */
    private static final String TEST_COMMUNITY_SERVICE_URL = "http://localhost:8080/axis/services/PolicyService" ;

    /**
     * Our test community manager url.
     *
     */
    private static final String TEST_COMMUNITY_MANAGER_URL = "http://localhost:8080/axis/services/PolicyManager" ;

    /**
     * Our test community description.
     *
     */
    private static final String TEST_COMMUNITY_DESC = "JUnit test community" ;

    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Switch for our assert statements.
     *
     */
    private static final boolean ASSERT_FLAG = false ;

    /**
     * Our database manager.
     *
     */
    private DatabaseManager manager ;

    /**
     * Our database connection.
     *
     */
    private Database database ;

    /**
     * Setup our tests.
     *
     */
    protected void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("setUp()") ;

        //
        // Initialise our DatabaseManager.
        manager = new DatabaseManagerImpl() ;
        //
        // Initialise our database.
        database = manager.getDatabase() ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can create an Community object.
     *
     */
    public void testCreateCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testCreateCommunity()") ;

        //
        // Create the Community object.
        CommunityData community = new CommunityData(TEST_COMMUNITY_IDENT, TEST_COMMUNITY_DESC) ;
        community.setServiceUrl(TEST_COMMUNITY_SERVICE_URL) ;
        community.setManagerUrl(TEST_COMMUNITY_MANAGER_URL) ;

        //
        // Begin a new database transaction.
        database.begin();

        //
        // Try creating the Community in the database.
        database.create(community);

        //
        // Commit our transaction.
        database.commit() ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can load an Community object.
     *
     */
    public void testLoadCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadCommunity()") ;

        //
        // Begin a new database transaction.
        database.begin();
        //
        // Load the Community from the database.
        CommunityData community = (CommunityData) database.load(CommunityData.class, TEST_COMMUNITY_IDENT) ;
        assertNotNull("Null community data", community) ;
        //
        // Commit our transaction.
        database.commit() ;

        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident   : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc    : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can modify an Community object.
     *
     */
    public void testModifyCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testModifyCommunity()") ;

        //
        // Begin a new database transaction.
        database.begin();
        //
        // Load the Community from the database.
        CommunityData community = (CommunityData) database.load(CommunityData.class, TEST_COMMUNITY_IDENT) ;
        //
        // Update the community data.
        community.setDescription("Modified description") ;
        //
        // Commit our transaction.
        database.commit() ;

        if (DEBUG_FLAG) System.out.println("  Community") ;
        if (DEBUG_FLAG) System.out.println("    ident : " + community.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("    desc  : " + community.getDescription()) ;
        if (DEBUG_FLAG) System.out.println("    service : " + community.getServiceUrl()) ;
        if (DEBUG_FLAG) System.out.println("    manager : " + community.getManagerUrl()) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Check we can delete an Community object.
     *
     */
    public void testDeleteCommunity()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testDeleteCommunity()") ;

        //
        // Begin a new database transaction.
        database.begin();
        //
        // Load the Community from the database.
        CommunityData community = (CommunityData) database.load(CommunityData.class, TEST_COMMUNITY_IDENT) ;
        //
        // Delete the community.
        database.remove(community) ;
        //
        // Commit our transaction.
        database.commit() ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }
    }
