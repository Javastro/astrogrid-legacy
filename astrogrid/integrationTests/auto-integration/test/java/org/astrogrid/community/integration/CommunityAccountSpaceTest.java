/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/community/integration/CommunityAccountSpaceTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/18 23:02:41 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountSpaceTest.java,v $
 *   Revision 1.2  2005/03/18 23:02:41  clq2
 *   dave-dev-200503150513
 *
 *   Revision 1.1.2.1  2005/03/17 04:55:13  dave
 *   Updated Community to use FileManager to register account space.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.integration ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
//import org.astrogrid.common.ivorn.MockIvorn ;

//import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;
import org.astrogrid.community.resolver.CommunityAccountResolver ;


import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;

//import org.astrogrid.community.common.policy.manager.PolicyManager ;

//import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
//import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;


/**
 * Test case for the Community account home space.
 *
 */
public class CommunityAccountSpaceTest
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Test that we can resolve a real address.
     *
     */
    public void testResolveFrog()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceTest.testResolveFrog()") ;
        //
        // Create our target Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
            "frog"
            ) ;
        if (DEBUG_FLAG) System.out.println("Got ivorn : " + ivorn.toString()) ;
        //
        // Create our resolver.
        CommunityAccountResolver resolver = new CommunityAccountResolver() ;
        //
        // Ask our resolver for the account data.
        AccountData account = resolver.resolve(
            ivorn
            ) ;
        if (DEBUG_FLAG) System.out.println("Got account data") ;
        if (DEBUG_FLAG) System.out.println("  Account ident : " + account.getIdent()) ;
        if (DEBUG_FLAG) System.out.println("  Account home  : " + account.getHomeSpace()) ;
        //
        // Get the home space ivorn.
        Ivorn home = new Ivorn(
            account.getHomeSpace()
            );
        if (DEBUG_FLAG) System.out.println("Got account home") ;
        //
        // Create our client factory.
        FileManagerClientFactory factory = new FileManagerClientFactory();
        //
        // Create an anon client.
        FileManagerClient client = factory.login();
        //
        // Get our home node.
        FileManagerNode node = client.node(
            home
            );
        if (DEBUG_FLAG) System.out.println("Got account node") ;
        assertNotNull(
            node
            );
        }
    }
