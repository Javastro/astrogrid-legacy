/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/junit/org/astrogrid/community/resolver/policy/manager/Attic/RegistryTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: RegistryTestCase.java,v $
 *   Revision 1.5  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.4.32.1  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
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
        System.out.println("Get property : " + registry.conf.getProperty("org.astrogrid.local/org.astrogrid.community.common.policy.manager.PolicyManager")) ;
        //
        // Try resolving our service.
        String endPoint = registry.getEndPointByIdentifier("org.astrogrid.local/org.astrogrid.community.common.policy.manager.PolicyManager");
        System.out.println("Resolved endpoint : " + endPoint) ;
        }

    }
