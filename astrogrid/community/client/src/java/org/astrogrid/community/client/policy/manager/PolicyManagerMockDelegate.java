/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerMockDelegate.java,v $
 *   Revision 1.4  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.3.12.1  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.exception.CommunityException ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.common.policy.manager.PolicyManagerMock ;

/**
 * Mock delegate for our PolicyManager service.
 *
 */
public class PolicyManagerMockDelegate
    extends PolicyManagerCoreDelegate
    implements PolicyManagerDelegate
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, no identifier.
     * Creates a default mock delegate, without an identifier.
     *
     */
    public PolicyManagerMockDelegate()
        {
        super() ;
        //
        // Set our PolicyManager service.
        this.setPolicyManager(
            new PolicyManagerMock()
            ) ;
        }

    /**
     * Public constructor.
     * @param ivorn The identifier for the delegate.
     *
     */
    public PolicyManagerMockDelegate(Ivorn ivorn)
        {
        super() ;
        //
        // Set our PolicyManager service.
        this.setPolicyManager(
            getManager(ivorn)
            ) ;
        }

	/**
	 * Our map of delegates, indexed by identifier.
	 *
	 */
	private static Map map = new HashMap() ;

	/**
	 * Get a PolicyManager for an Ivorn identifier.
	 * @param ivorn The identifier for the PolicyManager.
	 * @return A reference to a PolicyManager, or null if not found in our map.
	 *
	 */
	protected static PolicyManager getManager(Ivorn ivorn)
		{
		return getManager(
			new CommunityIvornParser(ivorn)
			) ;
		}

	/**
	 * Get a PolicyManager for an Ivorn identifier.
	 * @param parser The identifier for the PolicyManager.
	 * @return A reference to a PolicyManager, or null if not found in our map.
	 *
	 */
	protected static PolicyManager getManager(CommunityIvornParser parser)
		{
		return (PolicyManager) map.get(parser.getCommunityIdent()) ;
		}

	/**
	 * Add a new PolicyManager for an identifier.
	 * This will replace any existing PolicyManager for the identifier.
	 * This enables a JUnit test to reset the PolicyManager for an identifier.
	 * @param ivorn The identifier for the PolicyManager.
	 * @return A reference to a new PolicyManager.
	 *
	 */
	public static PolicyManager addManager(Ivorn ivorn)
		{
		return addManager(
			new CommunityIvornParser(ivorn)
			) ;
		}

	/**
	 * Add a new PolicyManager for an identifier.
	 * This will replace any existing PolicyManager for the identifier.
	 * This enables a JUnit test to reset the PolicyManager for an identifier.
	 * @param parser The identifier for the PolicyManager.
	 *
	 */
	public static PolicyManager addManager(CommunityIvornParser parser)
		{
		PolicyManager manager = new PolicyManagerMock() ;
		map.put(
			parser.getCommunityIdent(),
			manager
			) ;
		return manager ;
		}
    }
