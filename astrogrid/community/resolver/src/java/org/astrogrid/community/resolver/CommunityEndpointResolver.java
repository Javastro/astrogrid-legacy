/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityEndpointResolver.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2006/04/06 17:44:25 $</cvs:date>
 * <cvs:version>$Revision: 1.18 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityEndpointResolver.java,v $
 *   Revision 1.18  2006/04/06 17:44:25  clq2
 *   wb-gtr-1537.
 *
 *   Revision 1.17.54.2  2006/03/02 19:25:08  gtr
 *   Various fixes after tests with the workbench. Login in via my Proxy now works.
 *
 *   Revision 1.17.54.1  2006/02/28 14:48:35  gtr
 *   This supports access to cryptographic credentials via MyProxy.
 *
 *   Revision 1.17  2005/05/09 15:10:15  clq2
 *   Kevin's commits
 *
 *   Revision 1.16.24.1  2005/04/29 07:53:53  KevinBenson
 *   small changes to use this ResourceData object from the registry instead of this SErviceData which is what it was called before
 *
 *   Revision 1.16  2005/02/18 19:48:31  clq2
 *   Reg_KMB_913 again merging again.
 *
 *   Revision 1.13.8.1  2005/02/11 16:46:17  KevinBenson
 *   no more context.xml used here.  And made it where communityendpointresolver used a full ivorn.
 *
 *   Revision 1.13  2005/01/07 14:14:25  jdt
 *   merged from Reg_KMB_787
 *
 *   Revision 1.12.12.1  2004/12/11 11:53:16  KevinBenson
 *   modifications to the jsps, also merged in several pieces of the validtion stuff
 *
 *   Revision 1.12  2004/11/04 18:00:02  jdt
 *   Restored following fixes to auto-integration
 *   Merged in Reg_KMB_546 and Reg_KMB_603 and Comm_KMB_583
 *
 *   Revision 1.10  2004/11/02 21:47:39  jdt
 *   Merge of Comm_KMB_583
 *
 *   Revision 1.9.20.1  2004/10/26 06:10:39  KevinBenson
 *   sprucing up admin interface and getting it where it grabs communities and accounts from external communities
 *
 *   Revision 1.9  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.8.42.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.8  2004/08/03 13:55:22  KevinBenson
 *   small change to not print out the stacktrace of an exception
 *
 *   Revision 1.7.34.1  2004/08/03 06:55:17  KevinBenson
 *   small change to not print out the exception
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URI;
import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;
import org.astrogrid.registry.client.query.ResourceData ;

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
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityEndpointResolver.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityEndpointResolver.resolve()") ;
        log.debug("  Ivorn : " + ivorn) ;
        log.debug("  Type  : " + type)  ;
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
    
    public Ivorn getIvornForService(Ivorn ivorn, Class type) throws CommunityIdentifierException {
        CommunityIvornParser cip = new CommunityIvornParser(ivorn);
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
        if (null == cip.getCommunityIdent())
            {
            throw new CommunityIdentifierException(
                "Null community identifier"
                ) ;
            }
        //
        // Create our service Ivorn.
        Ivorn returnIvorn  = CommunityServiceIvornFactory.createIvorn(
            cip.getCommunityIdent(),
            type
            ) ;
        return returnIvorn;
    }

   /**
    * Resolve data from a CommunityIvornParser.
    * @param parser A CommunityIvornParser containing the Community identifier.
    * @param type   The Java class of the service interface we want.
    * @return The endpoint address for the service.
    * @throws CommunityIdentifierException If the identifier is not valid.
    * @throws CommunityResolverException If the Community is unable to resolve the identifier.
    * @throws RegistryException If the Registry is unable to resolve the identifier.
    */
   public URL resolve(CommunityIvornParser parser, Class type)
       throws RegistryException, 
              CommunityIdentifierException, 
              CommunityResolverException {
     // Resolve to a java.net.URI first, and then
     // convert that to a java.net.URL. The subordinate
     // method handles the probably errors and returns the
     // right kind of exceptions, so they are not caught.
     // The URI-to-URL connvertion can go wrong if Java
     // doesn't understand the scheme, and that error is
     // caught here.
     try {
       return this.resolveToUri(parser, type).toURL();
     }
     catch (MalformedURLException ouch) {
       throw new CommunityResolverException(
           "IVOID " + 
           parser.getIvorn() +
           " was resolved to a service-endpoint URI, " +
           "but that URI cannot be converted to a URL " +
           "because Java has no handler for the scheme of the URI.");
     }
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
    public URI resolveToUri(CommunityIvornParser parser, Class type)
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityEndpointResolver.resolveToUri()") ;
        log.debug("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
        log.debug("  Type  : " + type)  ;
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
        log.debug("PASS : Got service Ivorn")  ;
        log.debug("  Ivorn    : " + ivorn)  ;
        //
        // Lookup the service in the registry.
        String endpoint = null ;
        try {
            endpoint = registry.getEndPointByIdentifier(
                ivorn
                ) ;
            }
        catch (Throwable ouch)
            {
            log.debug("FAIL : Registry lookup failed")  ;
            //log.debug("  Exception : " + ouch)  ;
            //ouch.printStackTrace() ;
            throw new CommunityResolverException(
                "Registry lookup failed",
                ouch
                ) ;
            }
        //
        // If we found an entry in the Registry.
        if (null != endpoint)
            {
            log.debug("PASS : Got service endpoint")  ;
            log.debug("  Endpoint : " + endpoint)  ;
            //
            // Convert it into an endpoint URL.
            try {
                return new URI(endpoint) ;
                }
            //
            // Report the problem in a Exception.
            catch (Exception ouch)
                {
                throw new CommunityResolverException(
                    "Unable to parse Registry response into endpoint URI",
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
    public ResourceData[] resolve()
        throws RegistryException, CommunityIdentifierException, CommunityResolverException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityEndpointResolver.resolve()") ;

        //
        // Lookup the service in the registry.
        ResourceData []rd = null ;
        try {
            rd = registry.getResourceDataByRelationship(
                    new Ivorn("ivo://org.astrogrid/CommunityServerKind")
                ) ;
            }
        catch (Throwable ouch)
            {
            log.debug("FAIL : Registry lookup failed")  ;
            //log.debug("  Exception : " + ouch)  ;
            //ouch.printStackTrace() ;
            throw new CommunityResolverException(
                "Registry lookup failed",
                ouch
                ) ;
            }
        //
        // If we found an entry in the Registry.
        if (null != rd && rd.length > 0)
            {
            log.debug("PASS : Got service endpoints")  ;
            //
            // Convert it into an endpoint URL.
            return rd;
            }
        //
        // If we didn't get a service endpoint.
        else {
            //
            // Report the problem in a Exception.
            throw new CommunityResolverException(
                "Registry returned no endpoints address for communities"
                ) ;
            }
        }
    }

