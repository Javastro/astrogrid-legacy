/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/Attic/CommunityTokenResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/16 23:18:08 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityTokenResolver.java,v $
 *   Revision 1.5  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.4.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
 *   Revision 1.4  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.3.32.2  2004/06/17 15:17:30  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.3.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;

import org.astrogrid.community.common.security.data.SecurityToken ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;

import org.astrogrid.community.client.security.service.SecurityServiceDelegate ;
import org.astrogrid.community.resolver.security.service.SecurityServiceResolver ;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.community.common.exception.CommunityServiceException ;
import org.astrogrid.community.common.exception.CommunitySecurityException ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;
import org.astrogrid.community.resolver.exception.CommunityResolverException ;

/**
 * A utility to resolve security tokens.
 *
 */
public class CommunityTokenResolver
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(CommunityTokenResolver.class);

    /**
     * Public constructor, using the default Registry service.
     *
     */
    public CommunityTokenResolver()
        {
        resolver = new SecurityServiceResolver() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public CommunityTokenResolver(URL registry)
        {
        resolver = new SecurityServiceResolver(registry) ;
        }

    /**
     * Our SecurityServiceResolver resolver.
     *
     */
    private SecurityServiceResolver resolver ;

    /**
     * Validate a SecurityToken.
     * Validates a token, and creates a new token issued to the same account.
     * @param token The token to validate.
     * @return A new SecurityToken if the original was valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public SecurityToken checkToken(SecurityToken token)
        throws RegistryException, CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityTokenResolver.checkToken()") ;
        log.debug("  Token : " + token) ;
        //
        // Check for null param.
        if (null == token)
            {
            throw new CommunityIdentifierException(
                "Null token"
                ) ;
            }
        //
        // Check for null value.
        if (null == token.getToken())
            {
            throw new CommunityIdentifierException(
                "Null token value"
                ) ;
            }
        //
        // Parse the token value into an ivorn.
        CommunityIvornParser parser = new CommunityIvornParser(token.getToken()) ;
        //
        // Resolve the ivorn into a SecurityServiceDelegate.
        SecurityServiceDelegate delegate = resolver.resolve(parser) ;
        //
        // Ask the SecurityServiceDelegate to check the token.
        return delegate.checkToken(
            token
            ) ;
        }

    /**
     * Split a SecurityToken.
     * Validates a token, and then creates a new set of tokens issued to the same account.
     * @param token The token to validate.
     * @param count The number of new tokens required.
     * @return An array of new SecurityToken(s).
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the token is invalid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public Object[] splitToken(SecurityToken token, int count)
        throws RegistryException, CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("CommunityTokenResolver.splitToken()") ;
        log.debug("  Token : " + token) ;
        //
        // Check for null param.
        if (null == token)
            {
            throw new CommunityIdentifierException(
                "Null token"
                ) ;
            }
        //
        // Check for null value.
        if (null == token.getToken())
            {
            throw new CommunityIdentifierException(
                "Null token value"
                ) ;
            }
        //
        // Parse the token value into an ivorn.
        CommunityIvornParser parser = new CommunityIvornParser(token.getToken()) ;
        //
        // Resolve the ivorn into a SecurityServiceDelegate.
        SecurityServiceDelegate delegate = resolver.resolve(parser) ;
        //
        // Ask the SecurityServiceDelegate to split the token.
        return delegate.splitToken(
            token,
            count
            ) ;
        }
    }
