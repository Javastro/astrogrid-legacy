/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/community/integration/CommunityGroupTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/22 14:48:37 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityGroupTest.java,v $
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
import org.astrogrid.community.common.policy.manager.PolicyManager ;

import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;

import org.astrogrid.community.resolver.CommunityAccountResolver ;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver;
import org.astrogrid.registry.client.query.ServiceData;

/**
 * Test case for the CommunityGroupTest
 * Unit tests for the communmity tests almost all methods out with the exception of 
 * tesing out GroupMembers in dealing with an external accounts which is what this integration
 * tests does.  And also Permissions dealing with external groups.
 * 
 *
 */
public class CommunityGroupTest
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
    public void testGroupTransactionsFromCommunities()
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
        ServiceData[] communityServices = pmr.resolve();
        assertTrue(communityServices.length > 1);
        
        PolicyManagerDelegate pmd = pmr.resolve(((ServiceData)communityServices[0]).getIvorn());
        Object []groups = pmd.getLocalGroups();
        assertTrue(groups.length > 0);
        assertNotNull("Null group",groups[0]);
        
        Object []accounts = pmd.getLocalAccounts();
        assertTrue(accounts.length > 0);
        assertNotNull("Null account ",accounts[0]);
        
        GroupData gd = null;
        
        for(int i = 0;i < groups.length;i++) {
            if(GroupData.MULTI_TYPE.equals(((GroupData)groups[i]).getType()))
                    gd = (GroupData)groups[i];
        }
        //System.out.println("found groupdata = " + gd.getIdent());
        AccountData ad = (AccountData)accounts[0];
        //System.out.println("found accountdata = " + ad.getIdent());
        
        AccountData sameAccount = pmd.getAccount(ad.getIdent());
        assertNotNull("Null account (same)" , sameAccount);
        //System.out.println("found sameaccountdata = " + sameAccount.getIdent());
        
        GroupData sameGroup = pmd.getGroup(gd.getIdent());
        assertNotNull("Null group (same)" , sameGroup);
        //System.out.println("found samegroupdata = " + sameGroup.getIdent());
        
        PolicyManagerDelegate pmdTwo = pmr.resolve(((ServiceData)communityServices[1]).getIvorn());
        Object []groupsTwo = pmdTwo.getLocalGroups();
        assertTrue(groupsTwo.length > 0);
        assertNotNull("Null group",groupsTwo[0]);
        
        Object []accountsTwo = pmdTwo.getLocalAccounts();
        assertTrue(accountsTwo.length > 0);
        assertNotNull("Null account ",accountsTwo[0]);
        
        GroupData gdTwo = null;
        for(int i = 0;i < groupsTwo.length;i++) {
            if(GroupData.MULTI_TYPE.equals(((GroupData)groupsTwo[i]).getType()))
                    gdTwo = (GroupData)groupsTwo[i];
        }

        AccountData adTwo = (AccountData)accountsTwo[0];
        
        AccountData sameAccountTwo = pmdTwo.getAccount(adTwo.getIdent());
        assertNotNull("Null account (same)" , sameAccountTwo);
        
        GroupData sameGroupTwo = pmdTwo.getGroup(gdTwo.getIdent());
        assertNotNull("Null group (same)" , sameGroupTwo);
        
        
        
        //System.out.println("the adTwo ident = " + adTwo.getIdent());
        //System.out.println("the gd ident = " + gd.getIdent());
        try {
            GroupMemberData gmdCheck = pmd.getGroupMember(adTwo.getIdent(),gd.getIdent());
            pmd.delGroupMember(adTwo.getIdent(),gd.getIdent());
        }catch(Exception e) {  //change this to a catch CommunityPolicyExceptin
            System.out.println("no duplicate found good");
        }
        GroupMemberData gmd = pmd.addGroupMember(adTwo.getIdent(),gd.getIdent());
        assertNotNull("Null groupmemberdata" , gmd);
        //GroupMemberData gmdTwo = pmdTwo.addGroupMember(ad.getIdent(),gdTwo.getIdent());
        //assertNotNull("Null groupmemberdataTwo" , gmdTwo);
     }
}
