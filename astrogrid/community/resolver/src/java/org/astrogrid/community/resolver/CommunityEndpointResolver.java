/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityEndpointResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.7 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityEndpointResolver.java,v $
 *   Revision 1.7  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.6.32.2  2004/06/17 15:17:30  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.6.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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
import org.astrogrid.community.common.ivorn.CommunityServiceIvornFactory ;

import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

import org.astrogrid.registry.RegistryException;

/**
 * This is a local wrapper for the RegistryDelegate resolve Ivorns into service endpoints.
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
     * Public constructor, using the default Registry service.
     *
     */
    public CommunityEndpointResolver()
        {
        //
        // Initialise our default registry delegate.
        this.registry = factory.createQuery() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
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
     * @param ivorn An Ivorn containing a Community identifier.
     * @param type  The Java class of the service interface we want.
     * @return The endpoint address for the service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public URL resolve(Ivorn ivorn, Class type)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityEndpointResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
        if (DEBUG_FLAG) System.out.println("  Type  : " + type)  ;
        //
        // Check for null ivorn.
        if (null == ivorn)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check for null type.
        if (null == type)
            {
            throw new CommunityIdentifierException(
                "Null service type"
                ) ;
            }
        //
        // Parse the ivorn and resolve it.
        return this.resolve(
            new CommunityIvornParser(ivorn),
            type
            ) ;
        }

    /**
     * Resolve data from a CommunityIvornParser.
     * @param parser A CommunityIvornParser containing the Community identifier.
     * @param type   The Java class of the service interface we want.
     * @return The endpoint address for the service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     * @todo relies on ivorn.getPath()
     *
     */
    public URL resolve(CommunityIvornParser parser, Class type)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityEndpointResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
        if (DEBUG_FLAG) System.out.println("  Type  : " + type)  ;
        //
        // Check for null param.
        if (null == parser)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Check for null type.
        if (null == type)
            {
            throw new CommunityIdentifierException(
                "Null service type"
                ) ;
            }
        //
        // Check for null community.
        if (null == parser.getCommunityIdent())
            {
            throw new CommunityIdentifierException(
                "Null community identifier"
                ) ;
            }
        //
        // Create our service Ivorn.
        Ivorn ivorn  = CommunityServiceIvornFactory.createIvorn(
            parser.getCommunityIdent(),
            type
            ) ;
        if (DEBUG_FLAG) System.out.println("PASS : Got service Ivorn")  ;
        if (DEBUG_FLAG) System.out.println("  Ivorn    : " + ivorn)  ;
        //
        // Lookup the service in the registry.
        String endpoint = null ;
        try {
            endpoint = registry.getEndPointByIdentifier(
                ivorn.getPath()
                ) ;
            }
        catch (Throwable ouch)
            {
            if (DEBUG_FLAG) System.out.println("FAIL : Registry lookup failed")  ;
            if (DEBUG_FLAG) System.out.println("  Exception : " + ouch)  ;
ouch.printStackTrace() ;
            throw new CommunityResolverException(
                "Registry lookup failed",
                ouch
                ) ;
            }
        //
        // If we found an entry in the Registry.
        if (null != endpoint)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Got service endpoint")  ;
            if (DEBUG_FLAG) System.out.println("  Endpoint : " + endpoint)  ;
            //
            // Convert it into an endpoint URL.
            try {
                return new URL(endpoint) ;
                }
            //
            // Report the problem in a Exception.
            catch (MalformedURLException ouch)
                {
                throw new CommunityResolverException(
                    "Unable to parse Registry response into endpoint URL",
                    ivorn
                    ) ;
                }
            }
        //
        // If we didn't get a service endpoint.
        else {
            //
            // Report the problem in a Exception.
            throw new CommunityResolverException(
                "Registry returned null endpoint address for ivorn",
                ivorn
                ) ;
            }
        }
    }

