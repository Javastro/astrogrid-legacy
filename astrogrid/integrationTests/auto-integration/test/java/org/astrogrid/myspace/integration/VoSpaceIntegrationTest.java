/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/myspace/integration/Attic/VoSpaceIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/06 12:50:12 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: VoSpaceIntegrationTest.java,v $
 *   Revision 1.2  2004/09/06 12:50:12  dave
 *   Added VoSpace integration test.
 *
 *   Revision 1.1.2.5  2004/09/03 15:13:12  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.4  2004/09/03 14:29:48  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.3  2004/09/03 14:22:22  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.2  2004/09/03 13:58:01  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.1  2004/09/03 13:41:12  dave
 *   Adde VoSPace test ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.myspace.integration ;

import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;

import org.astrogrid.integration.AbstractTestForIntegration;

/**
 * JUnit test for the VoSpace client interface to MySpace.
 *
 */
public class VoSpaceIntegrationTest
	extends AbstractTestForIntegration
	{

	/**
	 * Reference to our target VoSpace.
	 *
	 */
	protected VoSpaceClient vospace ;

	/**
	 * Public constructor.
	 * @param name
	 *
	 */
	public VoSpaceIntegrationTest(String name)
		{
		super(name);
		}

	/**
	 * Set up our test.
	 * Creates our target VoSpaceClient.
	 *
	 */
	public void setUp()
		throws Exception
		{
		super.setUp();
System.out.println("") ;
System.out.println("VoSpaceIntegrationTest.setUp()") ;
System.out.println(" userIvorn    : " + userIvorn.toString()) ;
System.out.println(" mySpaceIvorn : " + mySpaceIvorn.toString()) ;

		vospace = new VoSpaceClient(user) ;
		}

	/**
	 * Test that we can create a VoSpaceClient.
	 *
	 */
	public void testCreateClient()
		throws Exception
		{
		assertNotNull(
			vospace
			) ;
		}

	/**
	 * Test that we can create an output stream.
	 *
	 */
	public void testCreateStream()
		throws Exception
		{
		Ivorn target = createIVORN("/frog.txt") ;
		assertNotNull(
			vospace.putStream(
				target
				)
			) ;
		}
	}
