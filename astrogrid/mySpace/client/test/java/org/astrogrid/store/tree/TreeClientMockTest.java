/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/test/java/org/astrogrid/store/tree/TreeClientMockTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2004/11/17 16:22:53 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: TreeClientMockTest.java,v $
 *   Revision 1.2  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.1.2.1  2004/11/16 17:29:16  nw
 *   added tests for new treeclient interface to myspace.
 *
 *   Revision 1.4  2004/11/11 17:50:42  clq2
 *   Noel's aladin stuff
 *
 *   Revision 1.3.6.1  2004/11/11 13:11:01  nw
 *   modifed way that the user ivorn is created
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
 *   Revision 1.1.2.5  2004/09/27 22:46:53  dave
 *   Added AdapterFile interface, with input and output stream API.
 *
 *   Revision 1.1.2.4  2004/09/23 12:21:31  dave
 *   Added mock security service and login test ...
 *
 *   Revision 1.1.2.3  2004/09/23 10:12:19  dave
 *   Added config properties for JUnit tests ....
 *   Added test for null password.
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

import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory;
import org.astrogrid.store.Ivorn;

/**
 * A JUnit test for the Aladin adapter.
 *
 */
public class TreeClientMockTest
	extends TreeClientTest
	{

	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		//
		// Create our test accounts.
		super.setUp() ;
		//
		// Create our target adapter.
		TreeClientMock mock = new TreeClientMock() ;
		//
		// Set our target adapter.
		this.setTestAdapter(mock) ;
		//
		// Create our test account identifier.
		// This uses currentTimeMillis to make the identifier unique between tests.
//		Ivorn ivorn = CommunityAccountIvornFactory.createLocal("frog") ;
        //@modified NWW - creating a mock means you don't need to have keys set up.
        Ivorn ivorn = CommunityAccountIvornFactory.createMock("org.astrogrid.localhost","frog");
		//
		// Setup the adapter account.
		mock.setTestAccount(ivorn) ;
		mock.setTestPassword("qwerty") ;
		//
		// Setup our test account.
		this.setTestAccount(ivorn) ;
		this.setTestPassword("qwerty") ;
		//
		// Setup our test container name.
		this.initContainerName() ;
		}

	}
