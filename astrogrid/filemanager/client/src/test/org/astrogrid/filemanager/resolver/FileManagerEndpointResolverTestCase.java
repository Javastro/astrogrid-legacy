/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/resolver/Attic/FileManagerEndpointResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:02 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerEndpointResolverTestCase.java,v $
 *   Revision 1.4  2005/01/28 10:44:02  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.3.2.1  2005/01/25 08:01:16  dave
 *   Added tests for FileManagerClientFactory ....
 *
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2.4.1  2004/12/22 07:38:36  dave
 *   Started to move towards StoreClient API ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/18 16:06:11  dave
 *   Added delegate resolver and tests ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.resolver ;

import java.net.URL ;

import junit.framework.TestCase ;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filemanager.common.BaseTest ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

/**
 * A JUnit test case for the service resolver.
 *
 */
public class FileManagerEndpointResolverTestCase
    extends BaseTest
    {

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
		//
		// Initialise our base class.
		super.setUp();
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        }

    /**
     * Check we get the right Exception for a null registry.
     *
     */
    public void testNullRegistry()
        throws Exception
        {
        try {
            new FileManagerEndpointResolverImpl(
                (RegistryService) null
                ) ;
            }
        catch (IllegalArgumentException ouch)
            {
            return ;
            }
        fail("Expected IllegalArgumentException") ;
        }

    /**
     * Check we can create a resolver with the default registry.
     *
     */
    public void testDefaultRegistry()
        throws Exception
        {
        assertNotNull(
            "Failed to create resolver",
            new FileManagerEndpointResolverImpl()
            ) ;
        }

    /**
     * Check we get the right exception for a null ivorn.
     *
     */
    public void testResolveNull()
        throws Exception
        {
        try {
            FileManagerEndpointResolver resolver = new FileManagerEndpointResolverImpl() ;
            resolver.resolve(
                (Ivorn) null
                ) ;
            }
        catch (IllegalArgumentException ouch)
            {
            return ;
            }
        fail("Expected IllegalArgumentException") ;
        }

    /**
     * Check we get the right exception for an unknown ivorn.
     *
     */
    public void testResolveUnknown()
        throws Exception
        {
        try {
            FileManagerEndpointResolver resolver = new FileManagerEndpointResolverImpl() ;
            resolver.resolve(
                new Ivorn("ivo://unknown")
                ) ;
            }
        catch (FileManagerResolverException ouch)
            {
            return ;
            }
        fail("Expected FileManagerResolverException") ;
        }

    /**
     * Check we get the right URL for a valid ivorn.
     *
     */
    public void testResolveValid()
        throws Exception
        {
        FileManagerEndpointResolver resolver = new FileManagerEndpointResolverImpl() ;
        assertEquals(
            "Wrong endpoint",
            new URL(
                getTestProperty(
                    "resolver.endpoint"
                    )
                ),
            resolver.resolve(
                new Ivorn(
                    getTestProperty(
                        "resolver.ivorn"
                        )
                    )
                )
            ) ;
        }
    }
