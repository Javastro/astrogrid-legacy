/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityAccountSpaceResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountSpaceResolver.java,v $
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/15 06:53:50  dave
 *   Removed redundant import.
 *
 *   Revision 1.1.2.1  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
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

import org.astrogrid.registry.RegistryException;

/**
 * A utility to resolve an Ivorn identifier for a Community Account into an Ivorn for the Account home space..
 *
 */
public class CommunityAccountSpaceResolver
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
    public CommunityAccountSpaceResolver()
        {
		//
		// Initialise our default resolver.
		resolver = new CommunityAccountResolver() ;
        }

    /**
     * Public constructor, with a specific registry delegate.
     * @param registry The endpoint address for our registry delegate.
     *
     */
    public CommunityAccountSpaceResolver(URL registry)
        {
		//
		// Initialise our resolver with the url.
		resolver = new CommunityAccountResolver(registry) ;
        }

	/**
	 * Our CommunityAccountResolver resolver.
	 *
	 */
	private CommunityAccountResolver resolver ;

	/**
	 * Resolve an Ivorn into the corresponding VoSpace Ivorn.
	 *
	 */
	public Ivorn resolve(Ivorn ivorn)
		throws CommunityException, RegistryException, MalformedURLException, URISyntaxException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceResolver.resolve()") ;
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
	 * Resolve data from a CommunityIvornParser.
	 * @param parser A CommunityIvornParser containing the Community authority and Account ident.
	 * @return A new Ivorn for the corresponding VoSpace, or null if none found.
	 * @TODO Better Exceptions for error reporting.
	 *
	 */
	protected Ivorn resolve(CommunityIvornParser parser)
		throws CommunityException, RegistryException, MalformedURLException, URISyntaxException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceResolver.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
		//
		// Check for null parser.
		if (null == parser) { throw new IllegalArgumentException("Null parser") ; }
		//
		// Resolve the ivorn into an AccountData.
		AccountData account = resolver.resolve(parser) ;
		//
		// If we found an Account.
		if (null != account)
			{
			if (DEBUG_FLAG) System.out.println("PASS : Got Account.") ;
			if (DEBUG_FLAG) System.out.println("Account : " + account.getIdent()) ;
			//
			// Get the Account home space Ivorn.
			String home = account.getHomeSpace() ;
			//
			// If the Account has a home space.
			if (null != home)
				{
				if (DEBUG_FLAG) System.out.println("PASS : Got Account home.") ;
				if (DEBUG_FLAG) System.out.println("Building new Ivorn based on Account home") ;
				if (DEBUG_FLAG) System.out.println("  Home : " + home) ;
				if (DEBUG_FLAG) System.out.println("  Path : " + parser.getRemainder()) ;
				//
				// Create a new Ivorn based on the home.
				Ivorn ivorn = new Ivorn(
					(home + parser.getRemainder())
					) ;
				if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
				return ivorn ;
				}
			//
			// If the Account does not have a home space.
			else {
				if (DEBUG_FLAG) System.out.println("FAIL : Null Account home.") ;
				return null ;
				}
			}
		//
		// If we didn't find the Account.
		else {
			if (DEBUG_FLAG) System.out.println("FAIL : Null Account.") ;
			return null ;
			}
		}
	}
