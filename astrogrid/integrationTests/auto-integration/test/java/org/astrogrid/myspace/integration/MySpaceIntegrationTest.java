/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/myspace/integration/Attic/MySpaceIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: MySpaceIntegrationTest.java,v $
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.1.2.36  2004/08/09 14:44:55  dave
 *   Fixed 404 check in tests ...
 *
 *   Revision 1.1.2.35  2004/08/09 14:37:29  dave
 *   Fixed 404 check in tests ...
 *
 *   Revision 1.1.2.34  2004/08/09 14:15:32  dave
 *   Fixed null path in copy test
 *
 *   Revision 1.1.2.33  2004/08/09 13:54:35  dave
 *   Fixed typo in test code ....
 *
 *   Revision 1.1.2.32  2004/08/09 13:21:56  dave
 *   Fixed typo in test code ....
 *
 *   Revision 1.1.2.31  2004/08/09 13:13:06  dave
 *   Fixed typo in test code ....
 *
 *   Revision 1.1.2.30  2004/08/09 13:03:03  dave
 *   Fixed typo in test code ....
 *
 *   Revision 1.1.2.29  2004/08/09 12:38:33  dave
 *   Added URL contents check to tests
 *
 *   Revision 1.1.2.28  2004/08/09 11:48:21  dave
 *   Added modify to copy test
 *
 *   Revision 1.1.2.27  2004/08/06 13:42:55  dave
 *   Added copy test
 *
 *   Revision 1.1.2.26  2004/08/05 18:00:55  dave
 *   Replaced import Msrl
 *
 *   Revision 1.1.2.25  2004/08/05 17:57:37  dave
 *   Added exception catch
 *
 *   Revision 1.1.2.24  2004/08/05 15:15:11  dave
 *   Added delete test
 *
 *   Revision 1.1.2.23  2004/08/05 14:57:21  dave
 *   Added append bytes test
 *
 *   Revision 1.1.2.22  2004/08/05 14:54:16  dave
 *   Added append bytes test
 *
 *   Revision 1.1.2.21  2004/08/05 14:31:20  dave
 *   Added append string test
 *
 *   Revision 1.1.2.20  2004/08/05 14:27:21  dave
 *   Added append string test
 *
 *   Revision 1.1.2.19  2004/08/05 13:58:19  dave
 *   ....
 *
 *   Revision 1.1.2.18  2004/08/04 19:49:29  dave
 *   Added import jar test
 *
 *   Revision 1.1.2.17  2004/08/04 04:08:16  dave
 *   Added test for putUrl
 *
 *   Revision 1.1.2.16  2004/08/04 03:19:46  dave
 *   Added initial hook for get URL test
 *
 *   Revision 1.1.2.15  2004/08/04 02:47:34  dave
 *   Fixed typo
 *
 *   Revision 1.1.2.14  2004/08/04 02:43:21  dave
 *   Extended to test import and export bytes
 *
 *   Revision 1.1.2.13  2004/08/03 20:25:07  dave
 *   Fixed bug in the tests
 *
 *   Revision 1.1.2.12  2004/08/03 18:45:22  dave
 *   Updated comments in tests
 *
 *   Revision 1.1.2.11  2004/08/03 16:08:52  dave
 *   Fixed typo in tests
 *
 *   Revision 1.1.2.10  2004/08/03 15:26:19  dave
 *   Fixed typo
 *
 *   Revision 1.1.2.9  2004/08/03 15:20:43  dave
 *   Reafctored user names in test
 *
 *   Revision 1.1.2.8  2004/08/03 14:38:38  dave
 *   Fixed equals param in test
 *
 *   Revision 1.1.2.7  2004/08/03 14:35:15  dave
 *   Restored some of the tests
 *
 *   Revision 1.1.2.6  2004/08/03 14:30:32  dave
 *   Restored some of the tests
 *
 *   Revision 1.1.2.5  2004/08/02 23:52:42  dave
 *   Updated myspace database config
 *
 *   Revision 1.1.2.4  2004/08/02 17:21:44  dave
 *   Added Exception
 *
 *   Revision 1.1.2.3  2004/08/02 17:19:29  dave
 *   Added details to JUnit tests
 *
 *   Revision 1.1.2.2  2004/08/02 17:15:43  dave
 *   Added details to JUnit tests
 *
 *   Revision 1.1.2.1  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 * </cvs:log>
 *
 */
package org.astrogrid.myspace.integration ;

import java.net.URL ;
import java.net.HttpURLConnection ;

import java.io.InputStream ;
import java.io.IOException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;

import org.astrogrid.store.delegate.StoreFile ;
import org.astrogrid.store.delegate.StoreException ;
import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate ;

import org.astrogrid.community.User;

import junit.framework.TestCase;

/**
 * A JUnit test case for the myspace service.
 *
 */
public class MySpaceIntegrationTest
	extends TestCase
	{


	/**
	 * A test string.
	 * "A short test string ...."
	 *
	 */
	public static final String TEST_STRING = "A short test string ...." ;

	/**
	 * A test string.
	 * " plus a bit more ...."
	 *
	 */
	public static final String EXTRA_STRING = " plus a bit more ...." ;

	/**
	 * A test byte array.
	 * "A short byte array ...."
	 *
	 */
	public static final byte[] TEST_BYTES = {
		0x41,
		0x20,
		0x73,
		0x68,
		0x6f,
		0x72,
		0x74,
		0x20,
		0x62,
		0x79,
		0x74,
		0x65,
		0x20,
		0x61,
		0x72,
		0x72,
		0x61,
		0x79,
		0x20,
		0x2e,
		0x2e,
		0x2e,
		0x2e
		} ;

	/**
	 * A test byte array.
	 * " plus a few more ...."
	 *
	 */
	public static final byte[] EXTRA_BYTES = {
		0x20,
		0x70,
		0x6c,
		0x75,
		0x73,
		0x20,
		0x61,
		0x20,
		0x66,
		0x65,
		0x77,
		0x20,
		0x6d,
		0x6f,
		0x72,
		0x65,
		0x20,
		0x2e,
		0x2e,
		0x2e,
		0x2e
		} ;

	/**
	 * Static counter of tests,
	 * used to generate a new user name for each test.
	 *
	 */
	private static int tests = 0 ;

    /**
     * Setup our test (increments the test count).
     *
     */
    public void setUp()
        throws Exception
        {
		this.tests++ ;
		}

	/**
	 * Helper method to create a user name.
	 * Creates a string based on the test count.
	 * e.g. integration-test-4.
	 *
	 */
	public String getUserName()
		{
		//
		// Generate the user name.
		return "integration-test-" + String.valueOf(tests) ;
		}

	/**
	 * Helper method to create a user object.
	 * Creates a User object with the current user name.
	 *
	 */
	public User getUserObject()
		{
		//
		// Generate the user object.
		return new User(
			this.getUserName(),
			"community",
			"group",
			"token"
			) ;
		}

	/**
	 * Helper method to create a user path.
	 * Creates a metadata path based on the current user name.
	 * e.g '/integration-test-4/path/file'
	 *
	 */
	public String getUserPath(String path)
		{
		return new StringBuffer()
			.append("/")
			.append(
				this.getUserName()
				)
			.append("/")
			.append(path)
			.toString()
			;
		}

	/**
	 * Our AstroGrid configuration.
	 */
	private static Config config = SimpleConfig.getSingleton();

	/**
	 * Helper method to get a config property.
	 *
	 */
	public String getConfigProperty(String name)
		{
		return (String) config.getProperty(
			name
			) ;
		}

	/**
	 * Our test property key prefix.
	 * Note, this uses the same properties as the filestore tests.
	 *
	 */
	public static final String TEST_PROPERTY_PREFIX = "org.astrogrid.filestore.test" ;

	/**
	 * Helper method to get a test property.
	 *
	 */
	public String getTestProperty(String name)
		{
		return (String) config.getProperty(
			TEST_PROPERTY_PREFIX + "." + name
			) ;
		}

	/**
	 * Test utility to compare two arrays of bytes.
	 *
	 */
	public static void assertEquals(byte[] left, byte[] right)
		{
		System.out.println("--------") ;
		assertEquals(
			"Different array length",
			left.length,
			right.length
			) ;
		for (int i = 0 ; i < left.length ; i++)
			{
			System.out.println(
				"[" + i + "] " + Integer.toHexString(left[i]) + ":" + Integer.toHexString(right[i])
				) ;
			assertEquals(
				"Wrong value for byte[" + i + "]",
				left[i],
				right[i]
				) ;
			}
		System.out.println("--------") ;
		}

	/**
	 * Test utility to compare the contents of two input streams.
	 *
	 */
	public static void assertContentsEqual(InputStream aStream, InputStream bStream)
		throws Exception
		{
		int aByte = -1 ;
		int bByte = -1 ;
		do {
			aByte = aStream.read() ;
			bByte = bStream.read() ;
			assertEquals(
				aByte,
				bByte
				) ;
			}
		while (aByte != -1) ;
		}

	/**
	 * Helper method to create a delegate.
	 *
	 */
	public MySpaceIt05Delegate delegate()
		throws Exception
		{
		//
		// Get the service endpoint from the registry.
		// Shouldn't this be resolveable from the registry ?
		String endpoint = getConfigProperty(
			"org.astrogrid.myspace.endpoint"
			) ;
		System.out.println("Endpoint : " + endpoint) ;
		//
		// Create our fake 'user'.
		User operator = new User(
			"someone",
			"somewhere",
			"group",
			"token"
			);
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = new MySpaceIt05Delegate(
			operator,
			new Msrl("myspace:" + endpoint).toString()
			);
		//
		// Configure the delegate to
		// (i) not generate test responses and
		// (ii) throw exceptions if errors are returned.
		myspace.setTest(false);
		myspace.setThrow(true);
		return myspace ;
		}

	/**
	 * Test we can create a myspace delegate.
	 *
	 */
	public void testCreateDelegate()
		throws Exception
		{
		assertNotNull(
			"Failed to create delegate.",
			delegate()
			) ;
		}

	/**
	 * Test we can create a test account.
	 *
	 */
	public void testCreateUser()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		}

	/**
	 * Test that we can import a string.
	 *
	 */
	public void testImportString()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Import some data.
		myspace.putString(
			TEST_STRING,
			this.getUserPath(
				"imported-string"
				),
			false
			);
		}

	/**
	 * Test that we can import and export a string.
	 *
	 */
	public void testExportString()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Import some data.
		myspace.putString(
			TEST_STRING,
			this.getUserPath(
				"imported-string"
				),
			false
			);
		//
		// Export the same data as a string.
		assertEquals(
			TEST_STRING,
			myspace.getString(
				this.getUserPath(
					"imported-string"
					)
				)
			) ;
		}

	/**
	 * Test that we can import a byte array.
	 *
	 */
	public void testImportBytes()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Import some data.
		myspace.putBytes(
			TEST_BYTES,
			0,
			TEST_BYTES.length,
			this.getUserPath(
				"imported-bytes"
				),
			false
			);
		}

	/**
	 * Test that we can import and export a byte array.
	 *
	 */
	public void testExportBytes()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Import some data.
		myspace.putBytes(
			TEST_BYTES,
			0,
			TEST_BYTES.length,
			this.getUserPath(
				"imported-bytes"
				),
			false
			);
		//
		// Export the same data as bytes.
		assertEquals(
			TEST_BYTES,
			myspace.getBytes(
				this.getUserPath(
					"imported-bytes"
					)
				)
			) ;
		}

	/**
	 * Test that we can import the contents of a URL.
	 *
	 */
	public void testImportHtml()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Get the URL from our config.
		URL url = new URL(
			getTestProperty(
				"data.http.html"
				)
			) ;
		//
		// Import some data.
		myspace.putUrl(
			url,
			this.getUserPath(
				"imported-bytes"
				),
			false
			);
		}

	/**
	 * Test that we can import the contents of a URL.
	 *
	 */
	public void testImportJar()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Get the URL from our config.
		URL url = new URL(
			getTestProperty(
				"data.http.jar"
				)
			) ;
		//
		// Import some data.
		myspace.putUrl(
			url,
			this.getUserPath(
				"imported-bytes"
				),
			false
			);
		}

	/**
	 * Test the contents of an imported URL.
	 *
	 */
	public void testImportedUrlContents()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Get the URL from our config.
		URL source = new URL(
			getTestProperty(
				"data.http.jar"
				)
			) ;
		//
		// Import some data.
		myspace.putUrl(
			source,
			this.getUserPath(
				"imported.jar"
				),
			false
			);
		//
		// Get the file URL from the manager.
		URL stored = myspace.getUrl(
			this.getUserPath(
				"imported.jar"
				)
			) ;
		//
		// Check the content are the same.
		assertContentsEqual(
			source.openStream(),
			stored.openStream()
			) ;
		}

	/**
	 * Test that we can import and append a string.
	 *
	 */
	public void testAppendString()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Import some data.
		myspace.putString(
			TEST_STRING,
			this.getUserPath(
				"imported-string"
				),
			false
			);
		//
		// Append some data.
		myspace.putString(
			EXTRA_STRING,
			this.getUserPath(
				"imported-string"
				),
			true
			);
		//
		// Export the same data as a string.
		assertEquals(
			(TEST_STRING + EXTRA_STRING),
			myspace.getString(
				this.getUserPath(
					"imported-string"
					)
				)
			) ;
		}

	/**
	 * Test that we can import and append a byte array.
	 *
	 */
	public void testAppendBytes()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Import some data.
		myspace.putBytes(
			TEST_BYTES,
			0,
			TEST_BYTES.length,
			this.getUserPath(
				"imported-string"
				),
			false
			);
		//
		// Append some data.
		myspace.putBytes(
			EXTRA_BYTES,
			0,
			EXTRA_BYTES.length,
			this.getUserPath(
				"imported-string"
				),
			true
			);
		//
		// Export the data as a string.
		assertEquals(
			(new String(TEST_BYTES) + new String(EXTRA_BYTES)),
			myspace.getString(
				this.getUserPath(
					"imported-string"
					)
				)
			) ;
		}

	/**
	 * Test that we can copy a file.
	 *
	 */
	public void testCopyLocal()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Create the source and destination paths.
		String source = this.getUserPath(
			"imported string"
			) ;
		String destination = this.getUserPath(
			"copy of string"
			) ;
		String endpoint = getConfigProperty(
			"org.astrogrid.myspace.endpoint"
			) ;
		Msrl msrl = new Msrl(
			"myspace:" + endpoint
			) ;
		Agsl agsl = new Agsl(
			msrl,
			destination
			);
		//
		// Import some data.
		myspace.putString(
			TEST_STRING,
			source,
			false
			);
		//
		// Copy the data within the manager.
		myspace.copy(
			source,
			agsl
			);
		//
		// Check the copy has the same data.
		assertEquals(
			TEST_STRING,
			myspace.getString(
				this.getUserPath(
					"copy of string"
					)
				)
			) ;
		//
		// Modify the copy.
		myspace.putString(
			EXTRA_STRING,
			this.getUserPath(
				"copy of string"
				),
			true
			);
		//
		// Check the copy has changed
		assertEquals(
			(TEST_STRING + EXTRA_STRING),
			myspace.getString(
				this.getUserPath(
					"copy of string"
					)
				)
			) ;
		//
		// Check the original is unchanged
		assertEquals(
			TEST_STRING,
			myspace.getString(
				this.getUserPath(
					"imported string"
					)
				)
			) ;
		//
		// Delete the original.
		myspace.delete(
			this.getUserPath(
				"imported string"
				)
			) ;
		//
		// Check the copy is ok.
		assertEquals(
			(TEST_STRING + EXTRA_STRING),
			myspace.getString(
				this.getUserPath(
					"copy of string"
					)
				)
			) ;
		}

	/**
	 * Test that we can delete a file.
	 *
	 */
	public void testDelete()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Import some data.
		myspace.putString(
			TEST_STRING,
			this.getUserPath(
				"imported-string"
				),
			false
			);
		//
		// Delete the file.
		myspace.delete(
			this.getUserPath(
				"imported-string"
				)
			) ;
		//
		// Try to export the data.
		try {
			myspace.getString(
				this.getUserPath(
					"imported-string"
					)
				) ;
			}
		catch(StoreException ouch)
			{
			return ;
			}
		fail("") ;
		}

	/**
	 * Test accessing the URL for a deleted file won't work.
	 *
	 */
	public void testDeletedUrlFails()
		throws Exception
		{
		//
		// Create our delegate.
		MySpaceIt05Delegate myspace = delegate() ;
		//
		// Create the user space.
		myspace.createUser(
			this.getUserObject()
			);
		//
		// Get the URL from our config.
		URL source = new URL(
			getTestProperty(
				"data.http.jar"
				)
			) ;
		//
		// Import some data.
		myspace.putUrl(
			source,
			this.getUserPath(
				"imported.jar"
				),
			false
			);
		//
		// Get the file URL from the manager.
		URL stored = myspace.getUrl(
			this.getUserPath(
				"imported.jar"
				)
			) ;
		//
		// Delete the file.
		myspace.delete(
			this.getUserPath(
				"imported.jar"
				)
			) ;
		//
		// Check that accessing the URL returns the right error.
		HttpURLConnection connection = (HttpURLConnection) stored.openConnection() ;
		assertEquals(
			404,
			connection.getResponseCode()
			) ;
		}

//
// What happens is we overwrite an existing file ?
// Currently, Manager will just import a new file.
// Leaving the old data in place on the filestore.
//



	}

