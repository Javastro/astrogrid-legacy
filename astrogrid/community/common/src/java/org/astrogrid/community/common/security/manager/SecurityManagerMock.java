/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/security/manager/SecurityManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/05 17:19:59 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SecurityManagerMock.java,v $
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.3  2004/03/05 14:03:23  dave
 *   Added first client side SOAP test - SecurityServiceSoapDelegateTestCase
 *
 *   Revision 1.1.2.2  2004/03/04 08:57:10  dave
 *   Started work on the install xdocs.
 *   Started work on the Security delegates.
 *
 *   Revision 1.1.2.1  2004/03/02 15:29:35  dave
 *   Working round Castor problem with PasswordData - objects remain in database cache
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.security.manager ;

import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implemetation for our SecurityManager service.
 *
 */
public class SecurityManagerMock
	extends CommunityServiceMock
	implements SecurityManager
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
	public SecurityManagerMock()
		{
		super() ;
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerMock()") ;
		}

	/**
	 * Set an account password.
	 *
	 */
	public boolean setPassword(String ident, String value)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("SecurityManagerMock.setPassword()") ;
		if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
		if (DEBUG_FLAG) System.out.println("  Value : " + value) ;
		//
		// Always return true.
		return true ;
		}

    }
