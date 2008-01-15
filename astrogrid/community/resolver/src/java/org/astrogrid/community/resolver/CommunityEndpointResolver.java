package org.astrogrid.community.resolver ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URI;
import java.net.URL ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.v1_0.RegistryService ;
import org.astrogrid.registry.client.query.ResourceData ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityServiceIvornFactory ;

import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

import org.astrogrid.registry.RegistryException;

/**
 * This is a local wrapper for the RegistryDelegate resolve Ivorns 
 * into service endpoints. It will only work with a registry containing
 * VOResource 1.0; any VOResource 0.10 records in the registry will be
 * ignored.
 *
 * The community offers endpoints for several services - MyProxy,
 * Policymanager and SecurityService are the extant ones. The target service
 * can be indicated in two ways, as follows.
 *
 * Old style: pass to the resolver a Class object identifying the Java interface
 * of the desired service; this works with registrations based on VOResource
 * v0.10.
 *
 * New style: pass to the resolver, as a string, the standardID value of the
 * desired service-capability; this works with registrations based on
 * VOResource v1.0.
 *
 * Because the registry delegate searchs VOResource 1.0, it can't search
 * VOResource 0.10. Therefore, all the methods that accept Class arguments
 * are henceforth broken and will be removed soon.
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
        this.registry = factory.createQueryv1_0() ;
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
        this.registry = factory.createQueryv1_0(registry) ;
        }
 
  /**
   * Constructs a resolver using a given registry-delegate.
   *
   * @param registry The registry delegate.
   */
  public CommunityEndpointResolver(RegistryService registry) {
    this.registry = registry;
  }
  
  /**
   * Changes the registry delegate.
   * Normally, a registry delegate is set when the object is constructed.
   * This method changes the delegate later. It allows a mock delegate to
   * be injected for testing.
   *
   * @param registry The registry delegate.
   */
  public void setRegistry(RegistryService registry) {
    this.registry = registry;
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
     * @deprecated Doesn't work any more.
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
    * @deprecated Doesn't work any more.
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
     * @deprecated Doesn't work any more.
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
   * Looks up an endpoint identified by its standardID value.
   * This requires a v1.0 registry.
   */
  public URI resolveToUri(Ivorn ivorn, String standardId)
      throws CommunityIdentifierException, CommunityResolverException {
    if (ivorn == null) {
      throw new CommunityIdentifierException("Can't resolve a null IVORN.");
    }
    try {
      String endpoint = 
          registry.getEndpointByIdentifier(ivorn.toString(), standardId);
      return new URI(endpoint);
    } 
    catch (RegistryException ex) {
      throw new CommunityResolverException("Failed to resolve " + ivorn, ex);
    }
    catch (java.net.URISyntaxException ex) {
      throw new CommunityResolverException("Registry gave a malformed endpoint " +
                                           "for service " + 
                                           ivorn + 
                                           ", capability " + 
                                           standardId);
    }
  }
  
  /**
   * Looks up an endpoint identified by its standardID value.
   * This requires a v1.0 registry.
   */
  public URI resolveToUri(CommunityIvornParser parser, String standardId)
      throws CommunityIdentifierException, CommunityResolverException {
    Ivorn ivorn = parser.getIvorn();
    try {
      String endpoint = 
          registry.getEndpointByIdentifier(ivorn.toString(), standardId);
      return new URI(endpoint);
    } 
    catch (RegistryException ex) {
      throw new CommunityResolverException("Failed to resolve " + ivorn, ex);
    }
    catch (java.net.URISyntaxException ex) {
      throw new CommunityResolverException("Registry gave a malformed endpoint " +
                                           "for service " + 
                                           ivorn + 
                                           ", capability " + 
                                           standardId);
    }
  }
  
  /**
   * Looks up an endpoint identified by its standardID value.
   * This requires a v1.0 registry.
   */
  public URL resolve(CommunityIvornParser parser, String standardId)
      throws CommunityIdentifierException, CommunityResolverException {
    return resolve(parser.getIvorn(), standardId);
  }
  
  /**
   * Looks up an endpoint identified by its standardID value.
   * This requires a v1.0 registry.
   */
  public URL resolve(Ivorn ivorn, String standardId)
      throws CommunityIdentifierException, CommunityResolverException {
    URI endpoint = resolveToUri(ivorn, standardId);
    try {
      return endpoint.toURL();
    } catch (MalformedURLException ex) {
      throw new CommunityResolverException(endpoint +
                                           " is not a valid java.net.URL");
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

