package org.astrogrid.community.resolver ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.URISyntaxException ;
import org.astrogrid.registry.client.query.v1_0.RegistryService;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.policy.data.AccountData ;
import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.registry.RegistryException;

import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.resolver.exception.CommunityResolverException ;

/**
 * A utility to resolve an Ivorn identifier for a Community Account into an Ivorn for the Account home space..
 *
 */
public class CommunityAccountSpaceResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityAccountSpaceResolver.class);

    /**
     * Public constructor, using the default Registry service.
     *
     */
    public CommunityAccountSpaceResolver()
        {
        //
        // Initialise our default resolver.
        resolver = new CommunityAccountResolver() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public CommunityAccountSpaceResolver(URL registry)
        {
        //
        // Initialise our resolver with the url.
        resolver = new CommunityAccountResolver(registry) ;
        }

  /**
   * Constructs a resolver using a given registry-delegate.
   *
   * @param registry The registry delegate.
   */
  public CommunityAccountSpaceResolver(RegistryService registry) {
    resolver = new CommunityAccountResolver(registry);
  }
  
    /**
     * Our CommunityAccountResolver resolver.
     *
     */
    private CommunityAccountResolver resolver ;

    /**
     * Resolve a Community Account identifier into the corresponding VoSpace identifier.
     * @param ivorn The Community Account identifier.
     * @return A new Ivorn for the corresponding VoSpace.
     * @throws CommunityPolicyException If the Community cannot locate the Account.
     * @throws CommunityServiceException If an error occurs in the Community service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public Ivorn resolve(Ivorn ivorn)
        throws CommunityServiceException,
            CommunityIdentifierException,
            CommunityPolicyException,
            CommunityResolverException,
            RegistryException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityAccountSpaceResolver.resolve()") ;
        log.debug("  Ivorn : " + ivorn) ;
        //
        // Check for null ivorn.
        if (null == ivorn)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Parse the Ivorn and then resolve the result.
        return resolve(
            new CommunityIvornParser(ivorn)
            ) ;
        }

    /**
     * Resolve a Community Account identifier into the corresponding VoSpace identifier.
     * @param parser The Community Account identifier.
     * @return A new Ivorn for the corresponding VoSpace.
     * @throws CommunityPolicyException If the Community cannot locate the Account.
     * @throws CommunityServiceException If an error occurs in the Community service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public Ivorn resolve(CommunityIvornParser parser)
        throws CommunityServiceException,
            CommunityIdentifierException,
            CommunityPolicyException,
            CommunityResolverException,
            RegistryException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityAccountSpaceResolver.resolve()") ;
        log.debug("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
        //
        // Check for null parser.
        if (null == parser)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Resolve the ivorn into an AccountData.
        AccountData account = resolver.resolve(parser.getIvorn()) ;
        log.debug("CommunityAccountSpaceResolver.resolve()") ;
        log.debug("Got Account") ;
        log.debug("Account : " + account.getIdent()) ;
        log.debug("Home    : " + account.getHomeSpace()) ;
        //
        // Get the Account home space Ivorn.
        String home = account.getHomeSpace() ;
        //
        // If the Account has a home space.
        if (null != home)
            {
            log.debug("PASS : Got Account home.") ;
            log.debug("Building new Ivorn based on Account home") ;
            log.debug("  Home : " + home) ;
            log.debug("  Path : " + parser.getRemainder()) ;
            //
            // Create a new Ivorn from the home address.
            try {
                return new Ivorn(
                    (home + parser.getRemainder())
                    ) ;
                }
            catch (URISyntaxException ouch)
                {
                throw new CommunityIdentifierException(
                    "Unable to parse Account home into an Ivorn",
                    ouch
                    ) ;
                }
            }
        //
        // If the Account does not have a home space.
        else {
            throw new CommunityResolverException(
                "Account does not have a home space defined",
                parser.getIvorn()
                ) ;
            }
        }
    }
