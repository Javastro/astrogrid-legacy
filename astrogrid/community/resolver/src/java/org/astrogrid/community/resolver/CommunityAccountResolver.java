/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityAccountResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountResolver.java,v $
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 *   Revision 1.1.2.2  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.1.2.1  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import java.net.URL ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.exception.CommunityException ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;

import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver ;

import org.astrogrid.registry.RegistryException;

/**
 * A utility to resolve Ivorn identifiers into Community Account data.
 *
 */
public class CommunityAccountResolver
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor, using default registry delegate.
     *
     */
    public CommunityAccountResolver()
        {
		//
		// Initialise our default resolver.
		resolver = new PolicyManagerResolver() ;
        }

    /**
     * Public constructor, with a specific registry delegate.
     * @param registry The endpoint address for our registry delegate.
     *
     */
    public CommunityAccountResolver(URL registry)
        {
		//
		// Initialise our resolver with the url.
		resolver = new PolicyManagerResolver(registry) ;
        }

	/**
	 * Our PolicyManager resolver.
	 *
	 */
	private PolicyManagerResolver resolver ;

	/**
	 * Resolve an Ivorn into an AccountData.
	 * @param ivorn An Ivorn containing the Community authority and Account ident.
	 * @return A reference to an AccountData, or null if none found.
	 * @TODO Better Exceptions for error reporting.
	 *
	 */
	public AccountData resolve(Ivorn ivorn)
		throws CommunityException, RegistryException, MalformedURLException, URISyntaxException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityAccountResolver.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		//
		// Check for null ivorn.
		if (null == ivorn) { throw new IllegalArgumentException("Null ivorn") ; }
		//
		// Parse the Ivorn and then resolve the result.
		return resolve(
			new CommunityIvornParser(ivorn)
			) ;
		}

	/**
	 * Resolve data from a CommunityIvornParser into an AccountData.
	 * @param parser An CommunityIvornParser containing the Community authority and Account ident.
	 * @return A reference to an AccountData, or null if none found.
	 * @TODO Better Exceptions for error reporting.
	 *
	 */
	protected AccountData resolve(CommunityIvornParser parser)
		throws CommunityException, RegistryException, MalformedURLException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityAccountResolver.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
		//
		// Check for null parser.
		if (null == parser) { throw new IllegalArgumentException("Null parser") ; }
		//
		// Resolve the ivorn into a PolicyManagerDelegate.
		PolicyManagerDelegate delegate = resolver.resolve(parser) ;
		//
		// If we found a PolicyManagerDelegate.
		if (null != delegate)
			{
			//
			// Ask the PolicyManagerDelegate for the AccountData.
			return delegate.getAccount(parser.getAccountIdent()) ;
			}
		//
		// If didn't find a PolicyManagerDelegate.
		else {
			return null ;
			}
		}
	}
