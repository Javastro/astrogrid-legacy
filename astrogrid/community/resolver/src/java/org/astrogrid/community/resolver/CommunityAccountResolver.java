/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityAccountResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountResolver.java,v $
 *   Revision 1.4  2004/03/30 01:40:03  dave
 *   Merged development branch, dave-dev-200403242058, into HEAD
 *
 *   Revision 1.3.6.1  2004/03/28 09:11:43  dave
 *   Convert tabs to spaces
 *
 *   Revision 1.3  2004/03/19 14:43:15  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.2.2.3  2004/03/19 04:51:46  dave
 *   Updated resolver with new Exceptions
 *
 *   Revision 1.2.2.2  2004/03/17 01:08:48  dave
 *   Added AccountNotFoundException
 *   Added DuplicateAccountException
 *   Added InvalidIdentifierException
 *
 *   Revision 1.2.2.1  2004/03/16 19:51:17  dave
 *   Added Exception reporting to resolvers
 *
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 *   Revision 1.1.2.2  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.1.2.1  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
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
