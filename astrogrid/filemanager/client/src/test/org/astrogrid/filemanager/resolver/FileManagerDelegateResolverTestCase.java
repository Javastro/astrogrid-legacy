/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/resolver/Attic/FileManagerDelegateResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegateResolverTestCase.java,v $
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

import org.astrogrid.filemanager.client.FileManagerDelegate ;
import org.astrogrid.filemanager.client.FileManagerSoapDelegate ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

/**
 * A JUnit test case for the service resolver.
 *
 */
public class FileManagerDelegateResolverTestCase
	extends TestCase
	{

	/**
	 * Test properties prefix.
	 *
	 */
	public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filemanager.test" ;

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
			new FileManagerDelegateResolverImpl(
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
			new FileManagerDelegateResolverImpl()
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
			FileManagerDelegateResolver resolver = new FileManagerDelegateResolverImpl() ;
			resolver.resolve((Ivorn)null) ;
			}
		catch (FileManagerIdentifierException ouch)
			{
			return ;
			}
		fail("Expected FileManagerIdentifierException") ;
		}

	/**
	 * Check we get the right exception for an unknown ivorn.
	 *
	 */
	public void testResolveUnknown()
		throws Exception
		{
		try {
			FileManagerDelegateResolver resolver = new FileManagerDelegateResolverImpl() ;
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
	 * Check we get a delegate for a valid ivorn.
	 *
	 */
	public void testResolveValid()
		throws Exception
		{
		FileManagerDelegateResolver resolver = new FileManagerDelegateResolverImpl() ;
		FileManagerDelegate delegate = 
			resolver.resolve(
				new Ivorn(
					getTestProperty(
						"resolver.ivorn"
						)
					)
				) ;
		assertTrue(
			"Didn't get a true SOAP delegate",
			(delegate instanceof FileManagerSoapDelegate)
			) ;
		assertEquals(
			"Wrong service identifier",
			getTestProperty(
				"service.ivorn"
				),
			delegate.getServiceIvorn().toString()
			) ;
		}
	}
