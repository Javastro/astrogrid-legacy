/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityEndpointResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityEndpointResolver.java,v $
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.2  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 *   Revision 1.1.2.1  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.exception.CommunityException ;

import org.astrogrid.registry.RegistryException;

/**
 * This is just a local wrapper for the RegistryDelegate.
 * Handles creating the registry delegate to resolve Ivorn(s).
 * @see org.astrogrid.store.Ivorn
 * @see org.astrogrid.registry.client.RegistryDelegateFactory
 * @see org.astrogrid.registry.client.query.RegistryService
 *
 */
public class CommunityEndpointResolver
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Our reference to the AstroGrid config.
     *
     */
    protected static Config conf = SimpleConfig.getSingleton() ;

    /**
     * Public constructor, using default registry delegate.
     *
     */
    public CommunityEndpointResolver()
        {
        //
        // Initialise our default registry delegate.
        this.registry = factory.createQuery() ;
        }

    /**
     * Public constructor, with a specific registry delegate.
     * @param registry The endpoint address for our registry delegate.
     *
     */
    public CommunityEndpointResolver(URL registry)
        {
        //
        // Initialise our registry delegate.
        this.registry = factory.createQuery(registry) ;
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
     * Resolve an Ivorn into a service endpoint.
	 * @param ivorn An Ivorn containing a Community authority.
	 * @param type  The type of service we want.
     * @return The endpoint address for the service.
     *
     */
    public URL resolve(Ivorn ivorn, String type)
        throws RegistryException, MalformedURLException
        {
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityEndpointResolver.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		if (DEBUG_FLAG) System.out.println("  Type  : " + type)  ;
        //
        // Parse the ivorn and resolve it.
        return this.resolve(
            new CommunityIvornParser(ivorn),
            type
            ) ;
        }

	/**
	 * Resolve data from a CommunityIvornParser.
	 * @param parser A CommunityIvornParser containing the Community authority.
	 * @return A new Ivorn for the corresponding VoSpace, or null if none found.
	 * @TODO Better Exceptions for error reporting.
	 *
	 */
    public URL resolve(CommunityIvornParser parser, String type)
        throws RegistryException, MalformedURLException
        {
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityEndpointResolver.resolve()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
		if (DEBUG_FLAG) System.out.println("  Type  : " + type)  ;
		//
		// Check for null param.
		if (null == parser) { throw new IllegalArgumentException("Null parser") ; }
        if (null == type)   { throw new IllegalArgumentException("Null type")   ; }
        //
        // Create our service Ivorn.
		Ivorn ivorn  = parser.getServiceIvorn(type) ;
		//
		// If we got a service Ivorn.
		if (null != ivorn)
			{
			if (DEBUG_FLAG) System.out.println("PASS : Got service Ivorn")  ;
			if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn)  ;
			//
			// Lookup the service in the registry.
			String endpoint = registry.getEndPointByIdentifier(ivorn.toString()) ;
			//
			// If we found an entry in the Registry.
			if (null != endpoint)
				{
				if (DEBUG_FLAG) System.out.println("PASS : Got service endpoint")  ;
				if (DEBUG_FLAG) System.out.println("  Endpoint : " + endpoint)  ;
				//
				// Convert it into an endpoint URL.
				return new URL(endpoint) ;
				}
			//
			// If we didn't get a service endpoint.
			else {
				if (DEBUG_FLAG) System.out.println("FAIL : Null service endpoint")  ;
				return null ;
				}
			}
		//
		// If we didn't get a service Ivorn.
		else {
			if (DEBUG_FLAG) System.out.println("FAIL : Null service Ivorn")  ;
			return null ;
			}
		}
    }

