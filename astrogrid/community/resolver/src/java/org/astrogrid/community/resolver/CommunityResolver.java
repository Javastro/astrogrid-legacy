/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/Attic/CommunityResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityResolver.java,v $
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
package org.astrogrid.community.resolver ;

import java.net.URL ;
import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.registry.RegistryException;

/**
 * Base class for our delegate resolvers.
 * Handles creating the registry delegate to resolve Ivorn(s).
 * @see org.astrogrid.store.Ivorn
 * @see org.astrogrid.registry.client.RegistryDelegateFactory
 * @see org.astrogrid.registry.client.query.RegistryService
 *
 */
public class CommunityResolver
	{

	/**
	 * Our reference to the AstroGrid config.
	 *
	 */
	protected static Config conf = SimpleConfig.getSingleton() ;

	/**
	 * Public constructor, using default registry delegate.
	 *
	 */
	public CommunityResolver()
		{
		//
		// Initialise our default registry delegate.
		this.registry = factory.createQuery() ;
		}

	/**
	 * Public constructor, with a specific registry delegate.
	 * @param url The endpoint address for our registry delegate.
	 *
	 */
	public CommunityResolver(URL url)
		{
		//
		// Initialise our registry delegate.
		this.registry = factory.createQuery(url) ;
		}

	/**
	 * Our Registry delegate factory.
	 *
	 */
	private static RegistryDelegateFactory factory = new RegistryDelegateFactory() ;

	/**
	 * Our Registry delegate.
	 *
	 */
	private RegistryService registry ;

	/**
	 * Resolve a string ident into a service endpoint.
	 *
	 */
	public String resolveEndpoint(String ident)
		throws RegistryException
		{
		//
		// Check for null ident.
		if (null == ident) { throw new IllegalArgumentException("Null identifier") ; }
		//
		// Lookup the ident in the registry.
		return registry.getEndPointByIdentifier(ident) ;
		}

	/**
	 * Resolve an Ivorn into a service endpoint.
	 *
	 */
	public String resolveEndpoint(Ivorn ident)
		throws RegistryException
		{
		//
		// Check for null ident.
		if (null == ident) { throw new IllegalArgumentException("Null identifier") ; }
		//
		// Lookup the ident in the registry.
		return registry.getEndPointByIdentifier(ident.toRegistryString()) ;
		}
	}

