/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityAccountSpaceResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityAccountSpaceResolver.java,v $
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
 *   Revision 1.1.2.2  2004/03/15 06:53:50  dave
 *   Removed redundant import.
 *
 *   Revision 1.1.2.1  2004/03/15 06:52:08  dave
 *   Refactored PolicyManagerMockDelegate to use ivorn identifiers.
 *   Refactored CommunityAccountResolver to just handle AccountData.
 *   Added CommunityAccountSpaceResolver to resolve home space ivorn.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import java.net.URL ;
import java.net.URISyntaxException ;

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
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

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
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
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
    protected Ivorn resolve(CommunityIvornParser parser)
        throws CommunityServiceException,
            CommunityIdentifierException,
            CommunityPolicyException,
            CommunityResolverException,
            RegistryException
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityAccountSpaceResolver.resolve()") ;
        if (DEBUG_FLAG) System.out.println("  Ivorn : " + ((null != parser) ? parser.getIvorn() : null)) ;
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
        AccountData account = resolver.resolve(parser) ;
        if (DEBUG_FLAG) System.out.println("PASS : Got Account.") ;
        if (DEBUG_FLAG) System.out.println("Account : " + account.getIdent()) ;
        //
        // Get the Account home space Ivorn.
        String home = account.getHomeSpace() ;
        //
        // If the Account has a home space.
        if (null != home)
            {
            if (DEBUG_FLAG) System.out.println("PASS : Got Account home.") ;
            if (DEBUG_FLAG) System.out.println("Building new Ivorn based on Account home") ;
            if (DEBUG_FLAG) System.out.println("  Home : " + home) ;
            if (DEBUG_FLAG) System.out.println("  Path : " + parser.getRemainder()) ;
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
