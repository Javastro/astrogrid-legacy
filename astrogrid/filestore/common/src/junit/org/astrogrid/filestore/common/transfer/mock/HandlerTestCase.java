/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/junit/org/astrogrid/filestore/common/transfer/mock/HandlerTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: HandlerTestCase.java,v $
 *   Revision 1.2  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.transfer.mock ;

import java.net.URL ;
import java.net.URLConnection ;

import java.io.InputStream ;
import java.io.OutputStream ;

import java.io.FileNotFoundException ;

import junit.framework.TestCase ;

/**
 * A Junit test for our mock URL handler.
 *
 */
public class HandlerTestCase
	extends TestCase
	{
	/**
	 * Setup our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		Handler.register() ;
		}

	/**
	 * Test we can create a mock URL.
	 *
	 */
	public void testCreateMockUrl()
		throws Exception
		{
		assertNotNull(
			new URL("mock://albert")
			) ;
		}

	/**
	 * Test we can open a connection to a mock URL.
	 *
	 */
	public void testConnectMockUrl()
		throws Exception
		{
		URL url = new URL("mock://albert") ;
		assertNotNull(
			url.openConnection()
			);
		}

	/**
	 * Test we get a null input stream.
	 *
	 */
	public void testNullInputStream()
		throws Exception
		{
		URL url = new URL("mock://albert") ;
		URLConnection conn = url.openConnection() ;
		try {
			conn.getInputStream() ;
			}
		catch (FileNotFoundException ouch)
			{
			return ;
			}
		fail("Expected FileNotFoundException");
		}

	/**
	 * Test we get a null output stream.
	 *
	 */
	public void testNullOutputStream()
		throws Exception
		{
		URL url = new URL("mock://albert") ;
		URLConnection conn = url.openConnection() ;
		try {
			conn.getOutputStream() ;
			}
		catch (FileNotFoundException ouch)
			{
			return ;
			}
		fail("Expected FileNotFoundException");
		}

	/**
	 * A test flag to indicate something has been done.
	 *
	 */
	protected boolean done = false ;

	/**
	 * Test we can register a connector.
	 *
	 */
	public void testRegisterConnector()
		throws Exception
		{
		//
		// Create our mock URL.
		URL url = new URL("mock://albert") ;
		//
		// Create our connector.
		Connector connector = new Connector()
			{
			public InputStream getInputStream()
				{
				done = true ;
				return null ;
				}
			public OutputStream getOutputStream()
				{
				return null ;
				}
			} ;
		//
		// Register our connector.
		Handler.addConnector(
			url,
			connector
			);
		//
		// Open a connection to our URL.
		URLConnection connection = url.openConnection() ;
		//
		// Try to get the input stream.
		connection.getInputStream() ;
		//
		// Check the done flag (set by the connector).
		assertTrue(done) ;
		}
	}
