/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/junit/org/astrogrid/filestore/resolver/FileStoreEndpointResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 09:11:16 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreEndpointResolverTestCase.java,v $
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

import junit.framework.TestCase ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.store.Ivorn ;

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
				null
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
		System.out.println("=============") ;
		System.out.println("testResolveValid") ;
		Config config = SimpleConfig.getSingleton() ;
		System.out.println(
			config.getProperty(
				"org.astrogrid.test/filestore"
				)
			) ;
		FileStoreEndpointResolver resolver = new FileStoreEndpointResolver() ;
		assertEquals(
			"Wrong endpoint",
			getTestProperty(
				"resolver.service.endpoint"
				),
			resolver.resolve(
				new Ivorn(
					getTestProperty(
						"resolver.service.ivorn"
						)
					)
				)
			) ;
		System.out.println("=============") ;
		}
	}
