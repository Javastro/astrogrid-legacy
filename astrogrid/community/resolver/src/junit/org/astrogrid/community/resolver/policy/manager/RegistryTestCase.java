/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/policy/manager/Attic/RegistryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/23 16:34:08 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: RegistryTestCase.java,v $
 *   Revision 1.3  2004/03/23 16:34:08  dave
 *   Merged development branch, dave-dev-200403191458, into HEAD
 *
 *   Revision 1.2.4.2  2004/03/23 11:01:44  dave
 *   Removed old test data.
 *
 *   Revision 1.2.4.1  2004/03/23 10:48:32  dave
 *   Registry configuration files and PolicyManagerResolver tests.
 *
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

import org.apache.axis.client.Call ;

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
	public void testLocalDelegate()
		throws Exception
		{
		System.out.println("") ;
		System.out.println("----\"----") ;
		System.out.println("RegistryTestCase.testLocalDelegate()") ;
		//
		// Create our registry delegate.
		RegistryService registry = RegistryDelegateFactory.createQuery() ;
		System.out.println("Got registry") ;
		System.out.println("Get property : " + registry.conf.getProperty("org.astrogrid.local.community/org.astrogrid.community.common.policy.manager.PolicyManager")) ;
		//
		// Try resolving our service.
		String endPoint = registry.getEndPointByIdentifier("org.astrogrid.local.community/org.astrogrid.community.common.policy.manager.PolicyManager");
		System.out.println("Resolved endpoint : " + endPoint) ;
		}

	}
