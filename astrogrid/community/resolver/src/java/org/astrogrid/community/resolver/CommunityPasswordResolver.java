/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/java/org/astrogrid/community/resolver/CommunityPasswordResolver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityPasswordResolver.java,v $
 *   Revision 1.2  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/23 15:51:46  dave
 *   Added CommunityTokenResolver and CommunityPasswordResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver ;

import java.net.URL ;
import org.astrogrid.store.Ivorn ;

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
 * A utility to resolve passwords into tokens.
 *
 */
public class CommunityPasswordResolver
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
    public CommunityPasswordResolver()
        {
		resolver = new SecurityServiceResolver() ;
        }

    /**
     * Public constructor, for a specific Registry service.
     * @param registry The endpoint address for our RegistryDelegate.
     *
     */
    public CommunityPasswordResolver(URL registry)
        {
		resolver = new SecurityServiceResolver(registry) ;
        }

	/**
	 * Our SecurityServiceResolver resolver.
	 *
	 */
	private SecurityServiceResolver resolver ;

    /**
     * Check an Account password.
     * @param account  The account ident.
     * @param password The account password.
     * @return A valid SecurityToken if the ident and password are valid.
     * @throws CommunitySecurityException If the security check fails.
     * @throws CommunityServiceException If there is an internal error in service.
     * @throws CommunityIdentifierException If the account identifier is invalid.
     * @throws CommunityResolverException If the Community is unable to resolve the identifier.
     * @throws RegistryException If the Registry is unable to resolve the identifier.
     *
     */
    public SecurityToken checkPassword(String account, String password)
        throws RegistryException, CommunityResolverException, CommunityServiceException, CommunitySecurityException, CommunityIdentifierException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("CommunityPasswordResolver.checkPassword()") ;
		if (DEBUG_FLAG) System.out.println("  Account : " + account) ;
		//
		// Check for null param.
		if (null == account)
			{
			throw new CommunityIdentifierException(
				"Null account"
				) ;
			}
		//
		// Parse the account ident into an ivorn.
		CommunityIvornParser parser = new CommunityIvornParser(account) ;
		//
		// Resolve the ivorn into a SecurityServiceDelegate.
		SecurityServiceDelegate delegate = resolver.resolve(parser) ;
		//
		// Ask the SecurityServiceDelegate to check the token.
		return delegate.checkPassword(
			account,
			password
			) ;
		}
	}
