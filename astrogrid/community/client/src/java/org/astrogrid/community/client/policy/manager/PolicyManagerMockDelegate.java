/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/PolicyManagerMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerMockDelegate.java,v $
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.2.2  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.common.policy.manager.PolicyManagerMock ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

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
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    public PolicyManagerMockDelegate(Ivorn ivorn)
        throws CommunityIdentifierException
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
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    protected static PolicyManager getManager(Ivorn ivorn)
        throws CommunityIdentifierException
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
     * @throws CommunityIdentifierException If the identifier is not valid.
     *
     */
    public static PolicyManager addManager(Ivorn ivorn)
        throws CommunityIdentifierException
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
