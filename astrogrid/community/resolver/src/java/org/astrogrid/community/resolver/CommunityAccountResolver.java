/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityAccountResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountResolver.java,v $
 *   Revision 1.6  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.5.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.policy.data.AccountData ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;

import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver ;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.community.common.exception.CommunityPolicyException ;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

/**
 * A utility to resolve Ivorn identifiers into Community Account data.
 *
 */
public class CommunityAccountResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityAccountResolver.class);

    /**
     * Public constructor, using the default Registry service.
     *
     */
    public CommunityAccountResolver()
        {
        resolver = new PolicyManagerResolver() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public CommunityAccountResolver(URL registry)
        {
        resolver = new PolicyManagerResolver(registry) ;
        }

    /**
     * Our PolicyManager resolver.
     *
     */
    private PolicyManagerResolver resolver ;

    /**
     * Resolve a Community identifier into an AccountData.
     * @param ivorn The Community Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityPolicyException If the Community cannot locate the Account.
     * @throws CommunityServiceException If an error occurs in the Community service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public AccountData resolve(Ivorn ivorn)
        throws CommunityServiceException,
            CommunityIdentifierException,
            CommunityPolicyException,
            CommunityResolverException,
            RegistryException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityAccountResolver.resolve()") ;
        log.debug("  Ivorn : " + ivorn) ;
        //
        // Check for null param.
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
     * Resolve a Community identifier into an AccountData.
     * @param parser The Community Account identifier.
     * @return An AccountData for the Account.
     * @throws CommunityPolicyException If the Community cannot locate the Account.
     * @throws CommunityServiceException If an error occurs in the Community service.
     * @throws CommunityIdentifierException If the identifier is not valid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    protected AccountData resolve(CommunityIvornParser parser)
        throws CommunityServiceException,
            CommunityIdentifierException,
            CommunityPolicyException,
            CommunityResolverException,
            RegistryException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityAccountResolver.resolve()") ;
        log.debug("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
        //
        // Check for null param.
        if (null == parser)
            {
            throw new CommunityIdentifierException(
                "Null identifier"
                ) ;
            }
        //
        // Resolve the ivorn into a PolicyManagerDelegate.
        PolicyManagerDelegate delegate = resolver.resolve(parser) ;
        //
        // Ask the PolicyManagerDelegate for the AccountData.
        return delegate.getAccount(
            parser.getAccountIdent()
            ) ;
        }
    }
