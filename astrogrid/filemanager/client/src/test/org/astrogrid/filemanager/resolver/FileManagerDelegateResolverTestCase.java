/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/resolver/Attic/FileManagerDelegateResolverTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegateResolverTestCase.java,v $
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.2  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
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

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerConfigMock;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filemanager.client.FileManagerDelegate ;
import org.astrogrid.filemanager.client.FileManagerSoapDelegate ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock ;

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
		//
		// Setup our filestore identifiers.
		Ivorn[] filestores = new Ivorn[]
			{
			new Ivorn("ivo://filestore/alpha"),
			new Ivorn("ivo://filestore/beta")
			} ;
		//
		// Setup the default manager config.
		FileManagerMock.defaultConfig = 
			new FileManagerConfigMock(
				new Ivorn(
					getTestProperty(
						"service.ivorn"
						)
					),
				filestores[0]
				);
		//
		// Initialise our filestore resolver.
		FileStoreDelegateResolverMock resolver = new FileStoreDelegateResolverMock() ;
		//
		// Register our test filestore(s).
		resolver.register(
			new FileStoreMockDelegate(
				filestores[0]
				)
			);
		resolver.register(
			new FileStoreMockDelegate(
				filestores[1]
				)
			);
		//
		// Setup the default resolver.
		FileManagerMock.defaultResolver = resolver ;
		//
		// Setup the default manager factory.
		FileManagerMock.defaultFactory = new FileManagerIvornFactory();
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
						"service.ivorn"
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
