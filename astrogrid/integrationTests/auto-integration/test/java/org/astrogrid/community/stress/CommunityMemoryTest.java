/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/community/stress/CommunityMemoryTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/09/09 01:19:50 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityMemoryTest.java,v $
 *   Revision 1.2  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.1.2.1  2004/09/07 13:10:24  dave
 *   Moved initial memory test into separate package.
 *
 *   Revision 1.1.2.6  2004/09/07 05:18:04  dave
 * </cvs:log>
 *
 */
package org.astrogrid.community.stress ;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.community.common.service.data.ServiceStatusData ;

import org.astrogrid.community.common.ivorn.CommunityIvornParser ;
import org.astrogrid.community.common.ivorn.CommunityAccountIvornFactory ;

import org.astrogrid.community.common.policy.manager.PolicyManager ;
import org.astrogrid.community.client.policy.manager.PolicyManagerDelegate ;
import org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver ;

/**
 * Test case to track memory usage.
 *
 */
public class CommunityMemoryTest
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Our valid ivorn.
	 *
	 */
	private Ivorn valid ;

	/**
	 * Our unknown ivorn.
	 *
	 */
	private Ivorn unknown ;

	/**
	 * Our target resolver.
	 *
	 */
	private PolicyManagerResolver resolver ;

	/**
	 * Our target service.
	 *
	 */
	private PolicyManagerDelegate service ;

	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		super.setUp() ;
        //
        // Create our valid Ivorn.
        valid = CommunityAccountIvornFactory.createLocal(
            "frog/public#qwertyuiop.xml"
            ) ;
        //
        // Create our unknown Ivorn.
        unknown = CommunityAccountIvornFactory.createLocal(
            "unknown/public#qwertyuiop.xml"
            ) ;
        //
        // Create our resolver.
        resolver = new PolicyManagerResolver() ;
		}

    /**
     * Test the system memory ....
     *
     */
    public void testSystemMemory()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("CommunityMemoryTest.testSystemMemory()") ;
		//
		// Check we can call the service status a few times ....
		for (int i = 0 ; i < 100 ; i++)
			{
			if (DEBUG_FLAG) System.out.println("") ;
			if (DEBUG_FLAG) System.out.println("----") ;
			//
			// Get the local (client) runtime.
			Runtime local = Runtime.getRuntime() ;
			//
			// Print out the client memory.
			if (DEBUG_FLAG) System.out.println("  Client free  memory : " + String.valueOf(local.freeMemory()))  ;
			if (DEBUG_FLAG) System.out.println("  Client total memory : " + String.valueOf(local.totalMemory())) ;
			//
			// Record the start time.
			long start = System.currentTimeMillis() ;
			//
			// Resolve our valid service delegate.
			service = resolver.resolve(valid) ;
			//
			// Request the service status.
			ServiceStatusData status = service.getServiceStatus() ;
			//
			// Check the end time.
			long done = System.currentTimeMillis() ;
			long time = done - start ;
			//
			// Print out the service values.
			if (DEBUG_FLAG) System.out.println("  Server free  memory : " + String.valueOf(status.getFreeMemory()))  ;
			if (DEBUG_FLAG) System.out.println("  Server total memory : " + String.valueOf(status.getTotalMemory())) ;
			if (DEBUG_FLAG) System.out.println("  Server used  memory : " + String.valueOf(status.getTotalMemory() - status.getFreeMemory())) ;
			if (DEBUG_FLAG) System.out.println("  Request time : " + String.valueOf(done - start)) ;
			}
        }
    }
