/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/policy/manager/PolicyManagerResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyManagerResolver.java,v $
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/12 00:46:25  dave
 *   Added CommunityIvornFactory and CommunityIvornParser.
 *   Added MockIvornFactory (to be moved to common project).
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

import org.astrogrid.registry.RegistryException;

/**
 * Public interface for a PolicyManagerResolver.
 *
 */
public interface PolicyManagerResolver
	{

	/**
	 * Resolve a Ivorn identifier into a PolicyManagerDelegate.
	 * If the Ivorn is null, this will return null.
	 * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
	 * This may involve a WebService call to a remote Resistry service.
	 * @param ident The IvoIdentifier.
	 * @return A reference to a PolicyManagerDelegate, or null if the service cannot be located.
	 * @see Ivorn
	 *
	 */
	public PolicyManagerDelegate resolve(Ivorn ivorn)
		throws CommunityException, RegistryException, MalformedURLException ;

	/**
	 * Resolve a CommunityIvorn identifier into a PolicyManagerDelegate.
	 * If the Ivorn is null, this will return null.
	 * If the Ivorn is a valid identifier, then this will lookup the identifier in the registry.
	 * This may involve a WebService call to a remote Resistry service.
	 * @param ident The IvoIdentifier.
	 * @return A reference to a PolicyManagerDelegate, or null if the service cannot be located.
	 * @see Ivorn
	 *
	 */
	public PolicyManagerDelegate resolve(CommunityIvornParser parser)
		throws CommunityException, RegistryException, MalformedURLException ;

	/**
	 * Resolve a WebService endpoint into a PolicyManagerDelegate.
	 * @param url A vaild endpoint URL.
	 * @return A reference to a PolicyManagerDelegate
	 *
	 */
	public PolicyManagerDelegate resolve(URL url)
		throws CommunityException ;

	/**
	 * Resolve a string identifier into a PolicyManagerDelegate.
	 * @param ident A vaild endpoint URL or Ivorn.
	 * @return A reference to a PolicyManagerDelegate
	 * @see Ivorn
	 *
	 */
	public PolicyManagerDelegate resolve(String string)
		throws CommunityException, RegistryException, MalformedURLException, URISyntaxException ;

	}
