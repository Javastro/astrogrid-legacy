/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/store/tree/TreeClientTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: TreeClientTest.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.3  2005/03/01 23:41:12  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.2  2005/03/01 15:07:31  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.1  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.2  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.1.2.2  2004/11/16 17:27:58  nw
 *   tidied imports
 *
 *   Revision 1.1.2.1  2004/11/16 16:47:28  nw
 *   copied aladinAdapter interfaces into a neutrally-named package.
 *   deprecated original interfaces.
 *   javadoc
 *
 *   Revision 1.3  2004/11/11 17:50:42  clq2
 *   Noel's aladin stuff
 *
 *   Revision 1.2.6.1  2004/11/11 13:12:36  nw
 *   added some further checking of the root container
 *
 *   Revision 1.2  2004/10/05 15:39:29  dave
 *   Merged changes to AladinAdapter ...
 *
 *   Revision 1.1.2.1  2004/10/05 15:30:44  dave
 *   Moved test base from test to src tree ....
 *   Added MimeTypeUtil
 *   Added getMimeType to the adapter API
 *   Added logout to the adapter API
 *
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
 *
 *   Revision 1.1.2.7  2004/09/27 22:46:53  dave
 *   Added AdapterFile interface, with input and output stream API.
 *
 *   Revision 1.1.2.6  2004/09/24 01:36:18  dave
 *   Refactored File as Node and Container ...
 *
 *   Revision 1.1.2.5  2004/09/24 01:12:09  dave
 *   Added initial test for child nodes.
 *
 *   Revision 1.1.2.4  2004/09/23 16:32:02  dave
 *   Added better Exception handling ....
 *   Added initial mock container ....
 *   Added initial root container tests ...
 *
 *   Revision 1.1.2.3  2004/09/23 12:21:31  dave
 *   Added mock security service and login test ...
 *
 *   Revision 1.1.2.2  2004/09/23 10:12:19  dave
 *   Added config properties for JUnit tests ....
 *   Added test for null password.
 *
 *   Revision 1.1.2.1  2004/09/23 09:18:13  dave
 *   Renamed AbstractTest to TestBase to exclude it from batch test ....
 *   Added first test for null account ....
 *
 *   Revision 1.1.2.1  2004/09/22 16:47:37  dave
 *   Added initial classes and tests for AladinAdapter.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.store.tree;

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.util.MimeTypeUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * A JUnit test for the Aladin adapter.
 *
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 */
public class TreeClientTest
	extends BaseTest
	{


	/**
	 * A test string.
	 * "A short test string ...."
	 *
	 */
	public static final String TEST_STRING = "A short test string ...." ;

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
	 * Our target adapter.
	 *
	 */
	protected TreeClient adapter ;

	/**
	 * Setup our test adapter.
	 *
	 */
	protected void setTestAdapter(TreeClient adapter)
		{
		this.adapter = adapter ;
		}

	/**
	 * Our test account.
	 *
	 */
	protected Ivorn account ;

	/**
	 * Setup our test account.
	 *
	 */
	public void setTestAccount(Ivorn ivorn)
		{
		this.account = ivorn ;
		}

	/**
	 * Our test password.
	 *
	 */
	protected String password ;

	/**
	 * Setup our test password.
	 *
	 */
	public void setTestPassword(String pass)
		{
		this.password = pass ;
		}

	/**
	 * Our target container name.
	 *
	 */
	protected String container ;

	/**
	 * Setup our container name.
	 *
	 */
	public void setContainerName(String name)
		{
		this.container = name ;
		}

	/**
	 * Get our container name.
	 *
	 */
	public String getContainerName()
		{
		return this.container ;
		}

	/**
	 * Create our container name.
	 * This uses the current timestamp to create a unique container name for each test.
	 *
	 */
	public void initContainerName()
		{
		this.setContainerName(
			"aladin-" + 
			String.valueOf(
				System.currentTimeMillis()
				)
			) ;
		}

	/**
	 * Check we have an adapter.
	 *
	 */
	public void testAdapterNotNull()
		throws Exception
		{
		assertNotNull(
			adapter
			) ;
		}

	/**
	 * Check we get the right Exception for a null account.
	 *
	 */
	public void testLoginNullAccount()
		throws Exception
		{
		try {
			adapter.login(
				null,
				password
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}

	/**
	 * Check we get the right Exception for a null password.
	 *
	 */
	public void testLoginNullPassword()
		throws Exception
		{
		try {
			adapter.login(
				account,
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
	 * Check we get the right Exception for the wrong password
	 *
	 */
	public void testLoginWrongPassword()
		throws Exception
		{
		try {
			adapter.login(
				account,
				(password + "WRONG")
				) ;
			}
		catch (TreeClientLoginException ouch)
			{
			return ;
			}
		fail("Expected AladinAdapterLoginException") ;
		}

	/**
	 * Check we can login with the right password.
	 *
	 */
	public void testLoginValidPassword()
		throws Exception
		{
		//
		// Check we are not logged in.
		assertNull(
			adapter.getToken()
			) ;
		//
		// Login using our account and password.
		adapter.login(
			account,
			password
			) ;
		//
		// Check we are logged in.
		assertNotNull(
			adapter.getToken()
			) ;
		}

	/**
	 * Check we get the right exception if we are not logged in.
	 *
	 */
	public void testGetRootFails()
		throws Exception
		{
		try {
			adapter.getRoot() ;
			}
		catch (TreeClientSecurityException ouch)
			{
			return ;
			}
		fail("Expected AladinAdapterSecurityException") ;
		}

	/**
	 * Check we get the a root node if we are logged in.
	 *
	 */
	public void testGetRoot()
		throws Exception
		{
		//
		// Login using our account and password.
		adapter.login(
			account,
			password
			) ;
		//
		// Check we have a root node.
		Container root = adapter.getRoot();
        assertNotNull(
			root
			) ;
        assertTrue(root.isContainer());
		}

	/**
	 * Check the root node has the default 'workflow' node.
	 *
	 */
	public void testWorkflowNode()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root container.
		Container root = adapter.getRoot() ;
		//
		// Check the child nodes for the expected defaults.
		Iterator iter = root.getChildNodes().iterator() ; 
		while (iter.hasNext())
			{
			Node next = (Node) iter.next() ;
			//
			// Check for a 'workflow' node.
			if ("workflow".equals(next.getName()))
				{
				return ;
				}
			}
		fail("Expected to find workflow container") ;
		}

	/**
	 * Check we can add a container.
	 *
	 */
	public void testAddContainer()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		assertNotNull(
			root.addContainer(
				this.getContainerName()
				)
			) ;
		}

	/**
	 * Check we can find our a container.
	 *
	 */
	public void testFindContainer()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		root.addContainer(
			this.getContainerName()
			) ;
		//
		// Get an iterator for the child nodes.
		Node found = null ;
		Iterator iter = root.getChildNodes().iterator() ; 
		while (iter.hasNext())
			{
			Node next = (Node) iter.next() ;
			//
			// Check for a matching node.
			if (this.getContainerName().equals(next.getName()))
				{
				return ;
				}
			}
		fail("Expected to find aladin container") ;
		}

	/*
	 * Check we get the right exception for a duplicate container.
	 *
	 */
	public void testDuplicateContainer()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		root.addContainer(
			this.getContainerName()
			) ;
		//
		// Try creating the same container again.
		try {
			root.addContainer(
				this.getContainerName()
				) ;
			}
		catch (TreeClientDuplicateException ouch)
			{
			return ;
			}
		fail("Expected AladinAdapterDuplicateException") ;
		}

	/**
	 * Check we can add a file.
	 *
	 */
	public void testAddFile()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create a new (empty) file.
		assertNotNull(
			node.addFile(
				"data.txt"
				)
			) ;
		}

	/*
	 * Check we get the right exception for a duplicate file.
	 *
	 */
	public void testDuplicateFile()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		node.addFile(
			"results.txt"
			) ;
		//
		// Try creating the same file again.
		try {
			node.addFile(
				"results.txt"
				) ;
			}
		catch (TreeClientDuplicateException ouch)
			{
			return ;
			}
		fail("Expected AladinAdapterDuplicateException") ;
		}

	/**
	 * Check a new file appears in the list of child nodes.
	 *
	 */
	public void testFindFile()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		node.addFile(
			"results.txt"
			) ;
		//
		// Get an iterator for the child nodes.
		Node found = null ;
		Iterator iter = node.getChildNodes().iterator() ; 
		while (iter.hasNext())
			{
			Node next = (Node) iter.next() ;
			//
			// Check for a matching node.
			if ("results.txt".equals(next.getName()))
				{
				return ;
				}
			}
		fail("Expected to find results.txt") ;
		}

	/**
	 * Check we can get an OutputStream for a file.
	 *
	 */
	public void testGetOutputStream()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.txt"
			) ;
		//
		// Get an OutputStream for the file.
		assertNotNull(
			file.getOutputStream()
			) ;
		}

	/**
	 * Check we can transfer some data to the stream.
	 *
	 */
	public void testImportData()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.txt"
			) ;
		//
		// Get an OutputStream for the file.
		OutputStream stream = file.getOutputStream() ;
		//
		// Transfer some data to the stream.
		stream.write(
			TEST_BYTES
			) ;
		//
		// Close the stream.
		stream.close() ;
		}

	/**
	 * Check we can get an InputStream for a file.
	 *
	 */
	public void testGetInputStream()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.txt"
			) ;
		//
		// Get an InputStream for the file.
		assertNotNull(
			file.getInputStream()
			) ;
		}

	/**
	 * Check we can transfer some data from the stream.
	 *
	 */
	public void testImportExportData()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.txt"
			) ;
		//
		// Get an OutputStream for the file.
		OutputStream output = file.getOutputStream() ;
		//
		// Transfer some data to the stream.
		output.write(
			TEST_BYTES
			) ;
		//
		// Close the stream.
		output.close() ;
		//
		// Get an InputStream for the file.
		InputStream input = file.getInputStream() ;
		//
		// Read some data from the stream.
		byte[] data = new byte[TEST_BYTES.length] ;
		input.read(data) ;
		//
		// Check we get the same data back ...
		for (int i = 0 ; i < TEST_BYTES.length ; i++)
			{
			assertEquals(
				TEST_BYTES[i],
				data[i]
				) ;
			}
		}

	/**
	 * Check we get the right mime type for an unknown type.
	 *
	 */
	public void testGetMimeUnknown()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.unknown"
			) ;
		//
		// Check the mime type.
		assertNull(
			file.getMimeType()
			) ;
		}

	/**
	 * Check we get the right mime type for an xml file.
	 *
	 */
	public void testGetMimeXml()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.xml"
			) ;
		//
		// Check the mime type.
		assertEquals(
			MimeTypeUtil.MIME_TYPE_XML,
			file.getMimeType()
			) ;
		}

	/**
	 * Check we get the right mime type for an votable file.
	 *
	 */
	public void testGetMimeVot()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.vot"
			) ;
		//
		// Check the mime type.
		assertEquals(
			MimeTypeUtil.MIME_TYPE_VOTABLE,
			file.getMimeType()
			) ;
		}

	/**
	 * Check we get the right mime type for an votable file.
	 *
	 */
	public void testGetMimeVotable()
		throws Exception
		{
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Get the root node.
		Container root = adapter.getRoot() ;
		//
		// Create our new container.
		Container node = root.addContainer(
			this.getContainerName()
			) ;
		//
		// Create our new file.
		File file = node.addFile(
			"results.votable"
			) ;
		//
		// Check the mime type.
		assertEquals(
			MimeTypeUtil.MIME_TYPE_VOTABLE,
			file.getMimeType()
			) ;
		}

	/**
	 * Check we get the right exception if we logout.
	 *
	 */
	public void testLogout()
		throws Exception
		{
		//
		// Check we get an exception if we are not logged in.
		try {
			adapter.getRoot() ;
			}
		catch (TreeClientSecurityException ouch)
			{
			return ;
			}
		fail("Expected AladinAdapterSecurityException") ;
		//
		// Login.
		adapter.login(
			account,
			password
			) ;
		//
		// Check we can get the root node.
		assertNotNull(
			adapter.getRoot()
			) ;
		//
		// Logout.
		adapter.logout() ;
		//
		// Check we get an exception having logged out.
		try {
			adapter.getRoot() ;
			}
		catch (TreeClientSecurityException ouch)
			{
			return ;
			}
		fail("Expected AladinAdapterSecurityException") ;
		}


    protected void setUp() throws Exception {
        super.setUp();
    }
	}
