/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/junit/org/astrogrid/filestore/resolver/FileStoreEndpointResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreEndpointResolverTestCase.java,v $
 *   Revision 1.4  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.3.8.1  2004/07/28 03:00:17  dave
 *   Refactored resolver constructors and added mock ivorn
 *
 *   Revision 1.3  2004/07/23 15:17:30  dave
 *   Merged development branch, dave-dev-200407231013, into HEAD
 *
 *   Revision 1.2.2.1  2004/07/23 15:04:46  dave
 *   Added delegate resolver and tests
 *
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/23 08:35:12  dave
 *   Added properties for local registry (incomplete)
 *
 *   Revision 1.1.2.1  2004/07/23 04:29:27  dave
 *   Initial resolver test
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.resolver ;

import java.net.URL ;

import junit.framework.TestCase ;

import org.apache.axis.client.Call ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

/**
 * A JUnit test case for the service resolver.
 *
 */
public class FileStoreEndpointResolverTestCase
	extends TestCase
	{

	/**
	 * Test properties prefix.
	 *
	 */
	public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filestore.test" ;

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Initialise the Axis 'local:' URL protocol.
        Call.initialize() ;
        }

	/**
	 * Helper method to get a local property.
	 *
	 */
	public String getTestProperty(String name)
		{
		return System.getProperty(TEST_PROPERTY_PREFIX + "." + name) ;
		}

	/**
	 * Check we get the right Exception for a null registry.
	 *
	 */
	public void testNullRegistry()
		throws Exception
		{
		try {
			new FileStoreEndpointResolver(
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
			new FileStoreEndpointResolver()
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
			FileStoreEndpointResolver resolver = new FileStoreEndpointResolver() ;
			resolver.resolve((Ivorn)null) ;
			}
		catch (FileStoreIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileStoreIdentifierException") ;
		}

	/**
	 * Check we get the right exception for an unknown ivorn.
	 *
	 */
	public void testResolveUnknown()
		throws Exception
		{
		try {
			FileStoreEndpointResolver resolver = new FileStoreEndpointResolver() ;
			resolver.resolve(
				new Ivorn("ivo://unknown")
				) ;
			}
		catch (FileStoreResolverException ouch)
			{
			return ;
			}
		fail("Expected FileStoreResolverException") ;
		}

	/**
	 * Check we get the right URL for a valid ivorn.
	 *
	 */
	public void testResolveValid()
		throws Exception
		{
		FileStoreEndpointResolver resolver = new FileStoreEndpointResolver() ;
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
