/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/policy/manager/Attic/PolicyManagerResolverImpl.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerResolverImpl.java,v $
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/12 13:44:43  dave
 *   Moved MockIvornFactory to MockIvorn
 *
 *   Revision 1.1.2.2  2004/03/12 00:46:25  dave
 *   Added CommunityIvornFactory and CommunityIvornParser.
 *   Added MockIvorn (to be moved to common project).
 *
 *   Revision 1.1.2.1  2004/03/10 17:29:21  dave
 *   Added initial resolver classes.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.manager ;

import java.net.URL ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.exception.CommunityException ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerMockDelegate ;
import org.astrogrid.community.client.policy.manager.PolicyManagerSoapDelegate ;

import org.astrogrid.community.resolver.CommunityResolver ;

import org.astrogrid.registry.RegistryException;

/**
 * Implementation of our PolicyManagerResolver interface.
 *
 */
public class PolicyManagerResolverImpl
	extends CommunityResolver
	implements PolicyManagerResolver
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
	public PolicyManagerResolverImpl()
		{
		super() ;
		}

	/**
	 * Public constructor, with a specific registry delegate.
	 * @param url The endpoint address for our registry delegate.
	 *
	 */
	public PolicyManagerResolverImpl(URL url)
		{
		super(url) ;
		}

	/**
	 * Resolve a Ivorn identifier into a PolicyManagerDelegate.
	 * If the Ivorn is null, this will return null.
	 * If the Ivorn matches the MOCK_IVORN, this will return a PolicyManagerMockDelegate.
	 * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
	 * This may involve a WebService call to a remote Resistry service.
	 * @param ident The IvoIdentifier.
	 * @return A reference to a PolicyManagerDelegate, or null if the service cannot be located.
	 * @see Ivorn
	 *
	 */
	public PolicyManagerDelegate resolve(Ivorn ivorn)
		throws CommunityException, RegistryException, MalformedURLException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerResolverImpl.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != ivorn) ? ivorn : null)) ;
		//
		// Check for null ivorn.
		if (null == ivorn) { throw new IllegalArgumentException("Null ivorn") ; }
		//
		// Parse the ivorn and resolve it.
		return this.resolve(
			new CommunityIvornParser(ivorn)
			) ;
		}

	/**
	 * Resolve a CommunityIvorn identifier into a PolicyManagerDelegate.
	 * If the Ivorn is null, this will return null.
	 * If the Ivorn matches the MOCK_IVORN, this will return a PolicyManagerMockDelegate.
	 * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
	 * This may involve a WebService call to a remote Resistry service.
	 * @param ident The IvoIdentifier.
	 * @return A reference to a PolicyManagerDelegate, or null if the service cannot be located.
	 * @see Ivorn
	 *
	 */
	public PolicyManagerDelegate resolve(CommunityIvornParser parser)
		throws CommunityException, RegistryException, MalformedURLException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("PolicyManagerResolverImpl.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Parser : " + ((null != parser) ? parser : null)) ;
		//
		// Check for null param.
		if (null == parser) { throw new IllegalArgumentException("Null parser") ; }
		//
		// Check for a mock ivorn.
		if (parser.isMock())
			{
			//
			// Return a new mock delegate.
			return new PolicyManagerMockDelegate() ;
			}
		//
		// If the ident is real.
		else {
			//
			// Lookup the community ident in the registry.
			String string = this.resolveEndpoint(parser.getCommunityIdent()) ;
			//
			// If we got an endpoint string.
			if (null != string)
				{
				//
				// Try creating a SOAP delegate.
				return new PolicyManagerSoapDelegate(string) ;
				}
			//
			// If we didn't get an endpoint string.
			else {
				//
				// Throw an Exception ?
				// Just return null for now.
				return null ;
				}
			}
		}

	/**
	 * Resolve a WebService endpoint into a PolicyManagerDelegate.
	 * @param url A vaild endpoint URL.
	 * @return A reference to a PolicyManagerDelegate
	 *
	 */
	public PolicyManagerDelegate resolve(URL url)
		throws CommunityException
		{
		//
		// Check for null ident.
		if (null == url) { throw new IllegalArgumentException("Null url") ; }
		return new PolicyManagerSoapDelegate(url) ;
		}

	/**
	 * Resolve a string identifier into a PolicyManagerDelegate.
	 * @param ident A vaild endpoint URL or Ivorn.
	 * @return A reference to a PolicyManagerDelegate
	 * @see Ivorn
	 *
	 */
	public PolicyManagerDelegate resolve(String string)
		throws CommunityException, RegistryException, MalformedURLException, URISyntaxException
		{
		//
		// Check for null string.
		if (null == string) { throw new IllegalArgumentException("Null identifier") ; }
		//
		// Check for a Ivorn.
		if (string.startsWith(Ivorn.SCHEME))
			{
			return this.resolve(new Ivorn(string)) ;
			}
		//
		// Assume the string is an endpoint URL.
		return new PolicyManagerSoapDelegate(new URL(string)) ;
		}
	}
