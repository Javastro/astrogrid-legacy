/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/community/integration/CommunityPermissionTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/05/09 15:10:17 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityPermissionTest.java,v $
 *   Revision 1.3  2005/05/09 15:10:17  clq2
 *   Kevin's commits
 *
 *   Revision 1.2.70.1  2005/04/29 07:30:45  KevinBenson
 *   Added some stress test and fixed this small bug with the community querying on relationships
 *
 *   Revision 1.2  2004/11/22 14:48:37  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.1.2.1  2004/11/08 22:18:38  KevinBenson
 *   The new integration tests for community and also the ability to split out community
 *   into 2 communities now in our auto-integration.
 *
 *   Revision 1.3  2004/04/21 13:09:45  dave
 *   Added check for Account ident.
 *
 *   Revision 1.2  2004/04/21 12:50:23  dave
 *   Added check for Account ident.
 *
 *   Revision 1.1  2004/04/21 03:42:57  dave
 *   Added initial Community test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.integration ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.common.ivorn.MockIvorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.policy.data.GroupData ;
import org.astrogrid.community.common.policy.data.GroupMemberData ;
import org.astrogrid.community.common.policy.data.PolicyPermission ;
import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;

import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver;
import org.astrogrid.community.resolver.CommunityAccountResolver ;
//import org.astrogrid.registry.client.query.ResourceData;

/**
 * Test case for the CommunityPermissionTest
 * Unit tests for the communmity tests almost all methods out with the exception of 
 * tesing out Permissions in dealing with an external group which is what this integration
 * tests does. And also GroupMember dealing with external accounts
 *
 */
public class CommunityPermissionTest
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
    public void testPermissionTransactionsFromCommunities()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolverTest.testResolveFrog()") ;
        //
        // Create our target Ivorn.
        Ivorn ivorn = CommunityAccountIvornFactory.createLocal(
            "frog/public#qwertyuiop.xml"
            ) ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        //
        // Create our resolver.
        CommunityAccountResolver resolver = new CommunityAccountResolver() ;
        //
        // Ask our resolver for the account data.
        AccountData found = resolver.resolve(ivorn) ;
        
        if (DEBUG_FLAG) System.out.println("  Found : " + ivorn) ;
        assertNotNull(
            "Failed to find account",
            found) ;
        
        
        PolicyManagerResolver pmr = new PolicyManagerResolver();
        org.astrogrid.registry.client.query.ResourceData[] communityServices = pmr.resolve();
        assertTrue(communityServices.length > 1);
        
        PolicyManagerDelegate pmd = pmr.resolve(((org.astrogrid.registry.client.query.ResourceData)communityServices[0]).getIvorn());
        Object []groups = pmd.getLocalGroups();
        assertTrue(groups.length > 0);
        assertNotNull("Null group",groups[0]);
        
        GroupData gd = (GroupData)groups[0];
        GroupData sameGroup = (GroupData)pmd.getGroup(gd.getIdent());
        assertNotNull("Null group (same)" , sameGroup);
        
        ResourceData rd = pmd.addResource();
        
        
        PolicyManagerDelegate pmdTwo = pmr.resolve(((org.astrogrid.registry.client.query.ResourceData)communityServices[1]).getIvorn());
        Object []groupsTwo = pmd.getLocalGroups();
        assertTrue(groupsTwo.length > 0);
        assertNotNull("Null group",groupsTwo[0]);
        
        ResourceData rdTwo = pmdTwo.addResource();
        
        GroupData gdTwo = (GroupData)groupsTwo[0];        
        GroupData sameGroupTwo = pmd.getGroup(gdTwo.getIdent());
        assertNotNull("Null group (same)" , sameGroupTwo);
        
        //Because the addResource is always getting another unique
        //resource you are assured never to have to deal with 
        //duplicates.
        PolicyPermission pp = 
            pmd.addPermission(rd.getIdent(),gdTwo.getIdent(),"test-action");
        assertNotNull("Null policypermission" , pp);
        PolicyPermission ppTwo = 
            pmdTwo.addPermission(rdTwo.getIdent(),gd.getIdent(),"test-action");
        assertNotNull("Null policypermissionTwo" , ppTwo);
        
        
     }
}
