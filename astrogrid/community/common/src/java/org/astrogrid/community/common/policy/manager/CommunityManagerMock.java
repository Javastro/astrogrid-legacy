/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/CommunityManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerMock.java,v $
 *   Revision 1.6  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.5.2.1  2004/03/21 06:41:41  dave
 *   Refactored to include Exception handling.
 *
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.2.1  2004/03/18 13:41:19  dave
 *   Added Exception handling to AccountManager
 *
 *   Revision 1.4  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.3.12.1  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
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
package org.astrogrid.community.common.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.exception.CommunityPolicyException     ;
import org.astrogrid.community.common.exception.CommunityServiceException    ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

/**
 * Mock implementation of our CommunityManager service.
 *
 */
public class CommunityManagerMock
    extends CommunityServiceMock
    implements CommunityManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public CommunityManagerMock()
        {
        super() ;
        }

    /**
     * Our hash table of values.
     *
     */
    private Map map = new HashMap() ;

    /**
     * Add a new Community, given the Account ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is already in the database.
     *
     */
    public CommunityData addCommunity(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerMock.addCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check if we already have an existing community.
        if (map.containsKey(ident))
            {
            throw new CommunityPolicyException(
                "Duplicate account",
                ident
                ) ;
            }
        //
        // Create a new community.
        CommunityData community = new CommunityData(ident) ;
        //
        // Add it to our map.
        map.put(community.getIdent(), community) ;
        //
        // Return the new Community.
        return community ;
        }

    /**
     * Request a Community details, given the Community ident.
     * @param  ident The Community identifier.
     * @return A CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public CommunityData getCommunity(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerMock.getCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Lookup the Account in our map.
        CommunityData community = (CommunityData) map.get(ident) ;
        //
        // If we found a Community.
        if (null != community)
            {
            return community ;
            }
        //
        // If we didn't find a Community.
        else {
            throw new CommunityPolicyException(
                "Community not found",
                ident
                ) ;
            }
        }

    /**
     * Update a Community.
     * @param  update The new CommunityData to update.
     * @return A new CommunityData for the Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public CommunityData setCommunity(CommunityData update)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerMock.setCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ((null != update) ? update.getIdent() : null)) ;
        //
        // Check for null data.
        if (null == update)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check for null ident.
        if (null == update.getIdent())
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Lookup the Community in our map.
        CommunityData found = this.getCommunity(update.getIdent()) ;
        //
        // Replace the existing Community with the new data.
        map.put(found.getIdent(), update) ;
        //
        // Return the new data.
        return update ;
        }

    /**
     * Delete a Community.
     * @param  ident The Community identifier.
     * @return The CommunityData for the old Community.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityPolicyException If the identifier is not in the database.
     *
     */
    public CommunityData delCommunity(String ident)
        throws CommunityIdentifierException, CommunityPolicyException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerMock.delCommunity()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Try to find the Community.
        CommunityData community = this.getCommunity(ident) ;
        //
        // Remove the Community from our map.
        map.remove(community.getIdent()) ;
        //
        // Return the old Community.
        return community ;
        }

    /**
     * Request a list of Communities.
     * @return An array of CommunityData objects.
     *
     */
    public Object[] getCommunityList()
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityManagerMock.getCommunityList()") ;
        return map.values().toArray() ;
        }
    }
