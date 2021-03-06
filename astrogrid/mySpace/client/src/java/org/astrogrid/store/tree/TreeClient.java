/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/tree/TreeClient.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2004/11/17 16:22:53 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: TreeClient.java,v $
 *   Revision 1.2  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.1.2.2  2004/11/16 17:27:58  nw
 *   tidied imports
 *
 *   Revision 1.1.2.1  2004/11/16 16:47:28  nw
 *   copied aladinAdapter interfaces into a neutrally-named package.
 *   deprecated original interfaces.
 *   javadoc
 *
 *   Revision 1.3  2004/10/05 15:39:29  dave
 *   Merged changes to AladinAdapter ...
 *
 *   Revision 1.2.4.1  2004/10/05 15:30:44  dave
 *   Moved test base from test to src tree ....
 *   Added MimeTypeUtil
 *   Added getMimeType to the adapter API
 *   Added logout to the adapter API
 *
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
 *
 *   Revision 1.1.2.5  2004/09/24 01:36:18  dave
 *   Refactored File as Node and Container ...
 *
 *   Revision 1.1.2.4  2004/09/23 16:32:01  dave
 *   Added better Exception handling ....
 *   Added initial mock container ....
 *   Added initial root container tests ...
 *
 *   Revision 1.1.2.3  2004/09/23 12:21:31  dave
 *   Added mock security service and login test ...
 *
 *   Revision 1.1.2.2  2004/09/23 09:18:13  dave
 *   Renamed AbstractTest to TestBase to exclude it from batch test ....
 *   Added first test for null account ....
 *
 *   Revision 1.1.2.1  2004/09/22 16:47:37  dave
 *   Added initial classes and tests for AladinAdapter.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.store.tree;

import org.astrogrid.community.common.security.data.SecurityToken;
import org.astrogrid.store.Ivorn;

/**
 * An adapter to enable Aladin to access files in AstroGrid MySpace.
 *
 */
public interface TreeClient
	{

	/**
	 * Login to the AstroGrid community.
	 * @param ivorn    The 'ivo://...' identifier for the AstroGrid account.
	 * @param password The account password.
     * @throws TreeClientLoginException   If the login fails.
     * @throws TreeClientServiceException If unable to handle the request.
	 *
	 */
	public void login(Ivorn ivorn, String password)
		throws TreeClientLoginException, TreeClientServiceException ;

	/**
	 * Logout from the AstroGrid community.
     * @throws TreeClientServiceException If unable to handle the request.
	 *
	 */
	public void logout()
		throws TreeClientServiceException ;

	/**
	 * Access to the current security token.
	 * @return The current security token, or null if not logged in.
	 *
	 */
	public SecurityToken getToken() ;

	/**
	 * Get the root node of the account home space.
	 * @return An AladinAdapterContainer representing the root of the account myspace.
     * @throws TreeClientSecurityException If the adapter is not logged in.
     * @throws TreeClientServiceException  If unable to handle the request.
	 *
	 */
	public Container getRoot()
		throws TreeClientSecurityException, TreeClientServiceException ;

	}
