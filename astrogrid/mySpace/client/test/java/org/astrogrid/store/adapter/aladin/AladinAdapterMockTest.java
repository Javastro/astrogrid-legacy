/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/test/java/org/astrogrid/store/adapter/aladin/Attic/AladinAdapterMockTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/28 10:24:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterMockTest.java,v $
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
package org.astrogrid.store.adapter.aladin ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.security.service.SecurityServiceMock ;

/**
 * A JUnit test for the Aladin adapter.
 *
 */
public class AladinAdapterMockTest
	extends AladinAdapterTestBase
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
		AladinAdapterMock mock = new AladinAdapterMock() ;
		//
		// Set our target adapter.
		this.setTestAdapter(mock) ;
		//
		// Create our test account identifier.
		// This uses currentTimeMillis to make the identifier unique between tests.
		Ivorn ivorn = CommunityAccountIvornFactory.createLocal("frog") ;
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
