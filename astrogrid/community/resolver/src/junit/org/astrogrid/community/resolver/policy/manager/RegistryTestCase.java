/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/policy/manager/Attic/RegistryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: RegistryTestCase.java,v $
 *   Revision 1.2  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/13 16:08:09  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.resolver.policy.manager ;

import junit.framework.TestCase ;

import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

/**
 * Simple test case for the RegistryDelegate.
 *
 */
public class RegistryTestCase
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

	/**
	 * Test the registry delegate.
	 *
	 */
	public void testRegistryDelegate()
		throws Exception
		{
		System.out.println("") ;
		System.out.println("----\"----") ;
		System.out.println("RegistryTestCase.testRegistryDelegate()") ;

		String cacheDir = System.getProperty("org.astrogrid.registry.cache.url") ;
		System.out.println("Property 'cacheDir' : " + cacheDir) ;

		RegistryService registry = RegistryDelegateFactory.createQuery() ;
		System.out.println("Got registry") ;

		registry.conf.setProperty("vm05.astrogrid.org/MyspaceManager", (cacheDir +"/Myspace.xml")) ;
		System.out.println("Set property") ;

		System.out.println("Get property : " + registry.conf.getProperty("vm05.astrogrid.org/MyspaceManager")) ;
		System.out.println("Get DOM      : " + registry.conf.getDom("vm05.astrogrid.org/MyspaceManager")) ;

		String endPoint = registry.getEndPointByIdentifier("vm05.astrogrid.org/MyspaceManager");
		System.out.println("Endpoint : " + endPoint) ;

		}

	}
