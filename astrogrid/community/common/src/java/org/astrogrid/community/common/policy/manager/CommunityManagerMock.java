/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/CommunityManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerMock.java,v $
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
 *   Revision 1.6.36.2  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.36.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.CommunityData ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityManagerMock.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerMock.addCommunity()") ;
        log.debug("  Ident : " + ident) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerMock.getCommunity()") ;
        log.debug("  Ident : " + ident) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerMock.setCommunity()") ;
        log.debug("  Ident : " + ((null != update) ? update.getIdent() : null)) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerMock.delCommunity()") ;
        log.debug("  Ident : " + ident) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityManagerMock.getCommunityList()") ;
        return map.values().toArray() ;
        }
    }
