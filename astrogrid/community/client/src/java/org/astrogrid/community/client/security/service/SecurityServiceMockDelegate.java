/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/security/service/SecurityServiceMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityServiceMockDelegate.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 08:57:10  dave
 *   Started work on the install xdocs.
 *   Started work on the Security delegates.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.security.service ;


import org.astrogrid.community.common.security.service.SecurityService ;
import org.astrogrid.community.common.security.service.SecurityServiceMock ;

/**
 * Mock delegate for our SecurityService service.
 *
 */
public class SecurityServiceMockDelegate
	extends SecurityServiceCoreDelegate
	implements SecurityServiceDelegate
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	private static boolean DEBUG_FLAG = true ;

	/**
	 * Public constructor.
	 *
	 */
	public SecurityServiceMockDelegate()
		{
		super() ;
		//
		// Set our SecurityService service.
		this.setSecurityService(
			new SecurityServiceMock()
			) ;
		}
	}
