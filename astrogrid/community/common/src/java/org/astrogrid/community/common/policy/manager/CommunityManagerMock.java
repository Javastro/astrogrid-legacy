/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/CommunityManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityManagerMock.java,v $
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
	 * Our hash table of accounts.
	 *
	 */
	private Map map = new HashMap() ;

    /**
     * Create a new Community.
     *
     */
    public CommunityData addCommunity(String ident)
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			// TODO
			// Throw an exception ?
			//
			// Return null for now.
			return null ;
			}
		//
		// Check if we already have an existing community.
		if (null != this.getCommunity(ident))
			{
			// TODO
			// Throw a duplicate exception ?
			//
			// Return null for now.
			return null ;
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
	 * Request an Community details.
	 *
	 */
	public CommunityData getCommunity(String ident)
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			// TODO
			// Throw an exception ?
			//
			// Return null for now.
			return null ;
			}
		//
		// Lookup the Community in our map.
		return (CommunityData) map.get(ident) ;
		}

	/**
	 * Update an Community details.
	 *
	 */
	public CommunityData setCommunity(CommunityData community)
		{
		//
		// Check for null param.
		if (null == community)
			{
			//
			// Throw an exception ?
			//
			// Return null for now.
			return null ;
			}
		//
		// If we don't have an existing community.
		if (null == this.getCommunity(community.getIdent()))
			{
			//
			// Throw an exception ?
			//
			// Return null for now.
			return null ;
			}
		//
		// Replace the existing Community with the new data.
		map.put(community.getIdent(), community) ;
		//
		// Return the new Community.
		return community ;
		}

	/**
	 * Delete an Community.
	 *
	 */
	public CommunityData delCommunity(String ident)
		{
		//
		// Check for null ident.
		if (null == ident)
			{
			//
			// Throw an exception ?
			//
			// Return null for now.
			return null ;
			}
		//
		// Try to find the Community.
		CommunityData community = this.getCommunity(ident) ;
		//
		// If we didn't find a Community.
		if (null == community)
			{
			//
			// Throw an exception ?
			//
			// Return null for now.
			return null ;
			}
		//
		// Remove the Community from our map.
		map.remove(community.getIdent()) ;
		//
		// Return the old Community.
		return community ;
		}

    /**
     * Request a list of Communitys.
     *
     */
    public Object[] getCommunityList()
		{
		return map.values().toArray() ;
		}
    }
