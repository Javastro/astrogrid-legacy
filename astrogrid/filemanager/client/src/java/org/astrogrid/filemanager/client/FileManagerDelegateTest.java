/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/Attic/FileManagerDelegateTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegateTest.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.5  2004/11/17 07:56:33  dave
 *   Added server mock and webapp build scripts ...
 *
 *   Revision 1.1.2.4  2004/11/16 07:56:08  dave
 *   Added last set of tests for delegate ....
 *
 *   Revision 1.1.2.3  2004/11/16 06:22:20  dave
 *   Added tests for child nodes and paths ...
 *
 *   Revision 1.1.2.2  2004/11/16 03:26:14  dave
 *   Added initial tests for adding accounts, containers and files ...
 *
 *   Revision 1.1.2.1  2004/11/13 01:41:26  dave
 *   Created initial client API ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import java.io.ByteArrayOutputStream;

import org.astrogrid.store.Ivorn ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.filestore.common.file.FileProperty;
import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.FileStoreOutputStream;
import org.astrogrid.filestore.common.transfer.TransferUtil;
import org.astrogrid.filestore.common.transfer.TransferProperties;

import org.astrogrid.filemanager.common.BaseTest;
import org.astrogrid.filemanager.common.FileManagerProperties;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;

/**
 * A generic test for the FileManager delegate API.
 *
 */
public class FileManagerDelegateTest
	extends BaseTest
	{

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(FileManagerDelegateTest.class);

	/**
	 * Our identifier factory.
	 *
	 */
	FileManagerIvornFactory factory ;

	/**
	 * Set up our test.
	 *
	 */
	public void setUp()
		throws Exception
		{
		super.setUp();
		//
		// Create our ivorn factory.
		factory = new FileManagerIvornFactory() ;
		}

	/**
	 * Our target FileManager delegate
	 *
	 */
	protected FileManagerCoreDelegate delegate;

	/**
	 * Check we created our delegate.
	 *
	 */
	public void testCreateDelegate()
		throws Exception
		{
		assertNotNull(
			this.delegate
			);
		}

	/**
	 * Check that we get the right exception for a null account.
	 *
	 */
	public void testAddAccountNull()
		throws Exception
		{
		try {
			delegate.addAccount(
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
	 * Check that we can create an account node.
	 *
	 */
	public void testAddAccount()
		throws Exception
		{
		assertNotNull(
			delegate.addAccount(
				accountIvorn
				)
			) ;
		}

	/**
	 * Check that we can't create a duplicate account.
	 *
	 */
	public void testAddAccountDuplicate()
		throws Exception
		{
		assertNotNull(
			delegate.addAccount(
				accountIvorn
				)
			) ;
		try {
			delegate.addAccount(
				accountIvorn
				) ;
			}
		catch (DuplicateNodeException ouch)
			{
			return ;
			}
		fail("Expected DuplicateNodeException") ;
		}

	/**
	 * Check that an account node has an ivorn.
	 *
	 */
	public void testAddAccountIvorn()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Check the node ivorn.
		assertNotNull(
			home.getIvorn()
			) ;
		}

	/**
	 * Check that an account node has an ident.
	 *
	 */
	public void testAddAccountIdent()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Check the node ident.
		assertNotNull(
			home.getIvorn()
			) ;
		}

	/**
	 * Check that an account node has the right name.
	 *
	 */
	public void testAddAccountName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Check the node name.
		assertNotNull(
			home.getName()
			) ;
		}

	/**
	 * Check that an account node is a container.
	 *
	 */
	public void testAddAccountType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Check the node type.
		assertTrue(
			home.isContainer()
			) ;
		assertFalse(
			home.isFile()
			) ;
		}

	/**
	 * Check that we get the right exception for a null account.
	 *
	 */
	public void testGetAccountNull()
		throws Exception
		{
		try {
			delegate.getAccount(
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
	 * Check that we get the right exception for an unknown account.
	 *
	 */
	public void testGetAccountUnknown()
		throws Exception
		{
		try {
			delegate.getAccount(
				unknownIvorn
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we get the right exception for a null parent.
	 *
	 */
	public void testAddContainerNullParent()
		throws Exception
		{
		try {
			delegate.addContainer(
				null,
				"frog"
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}

	/**
	 * Check that we get the right exception for a null name.
	 *
	 */
	public void testAddContainerNullName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Try adding a node with no name.
		try {
			delegate.addContainer(
				home,
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
	 * Check that we can create a container node.
	 *
	 */
	public void testAddContainer()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		assertNotNull(
			delegate.addContainer(
				home,
				"frog"
				)
			);
		}

	/**
	 * Check that we we get the right Exception for a duplicate container.
	 *
	 */
	public void testAddDuplicateContainer()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Try adding the same container twice.
		try {
			delegate.addContainer(
				home,
				"frog"
				) ;
			delegate.addContainer(
				home,
				"frog"
				) ;
			}
		catch (DuplicateNodeException ouch)
			{
			return ;
			}
		fail("Expected DuplicateNodeException") ;
		}

	/**
	 * Check that the container has a valid ivorn.
	 *
	 */
	public void testAddContainerIvorn()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode node =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Check the node ivorn.
		assertNotNull(
			node.getIvorn()
			);
		}

	/**
	 * Check that the container has the right name.
	 *
	 */
	public void testAddContainerName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode node =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Check the node name.
		assertEquals(
			"frog",
			node.getName()
			);
		}

	/**
	 * Check that the container has the right type.
	 *
	 */
	public void testAddContainerType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode node =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Check the node type.
		assertTrue(
			node.isContainer()
			);
		assertFalse(
			node.isFile()
			);
		}

	/**
	 * Check that we get the right exception for a null parent.
	 *
	 */
	public void testAddFileNullParent()
		throws Exception
		{
		try {
			delegate.addFile(
				null,
				"frog"
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}

	/**
	 * Check that we get the right exception for a null name.
	 *
	 */
	public void testAddFileNullName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Try adding a file with no name.
		try {
			delegate.addFile(
				home,
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
	 * Check that we can create a file node.
	 *
	 */
	public void testAddFile()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		assertNotNull(
			delegate.addFile(
				home,
				"frog"
				)
			);
		}

	/**
	 * Check that we we get the right Exception for a duplicate file.
	 *
	 */
	public void testAddDuplicateFile()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Try adding the same file twice.
		try {
			delegate.addFile(
				home,
				"frog"
				) ;
			delegate.addFile(
				home,
				"frog"
				) ;
			}
		catch (DuplicateNodeException ouch)
			{
			return ;
			}
		fail("Expected DuplicateNodeException") ;
		}

	/**
	 * Check that the file has a valid ivorn.
	 *
	 */
	public void testAddFileIvorn()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode node =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check the node ivorn.
		assertNotNull(
			node.getIvorn()
			);
		}

	/**
	 * Check that the file has the right name.
	 *
	 */
	public void testAddFileName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode node =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check the node name.
		assertEquals(
			"frog",
			node.getName()
			);
		}

	/**
	 * Check that the file has the right type.
	 *
	 */
	public void testAddFileType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode node =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check the node type.
		assertFalse(
			node.isContainer()
			);
		assertTrue(
			node.isFile()
			);
		}

	/**
	 * Check we can get the right Exception for a null ident.
	 *
	 */
	public void testGetNodeNull()
		throws Exception
		{
		//
		// Try requesting a node with no ivorn.
		try {
			delegate.getNode(
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
	 * Check we get the right Exception for an unknown node.
	 *
	 */
	public void testGetNodeUnknown()
		throws Exception
		{
		//
		// Try requesting an unknown node.
		try {
			delegate.getNode(
				factory.ivorn(
					delegate.getServiceIvorn()
					)
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check we can get a container node.
	 *
	 */
	public void testGetContainerNode()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode frog =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		assertNotNull(
			delegate.getNode(
				frog.getIvorn()
				)
			);
		}

	/**
	 * Check the node has the right ivorn.
	 *
	 */
	public void testGetContainerNodeIvorn()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode frog =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		FileManagerNode toad =
			delegate.getNode(
				frog.getIvorn()
				) ;
		//
		// Check the node ivorn.
		assertEquals(
			frog.getIvorn().toString(),
			toad.getIvorn().toString()
			);
		}

	/**
	 * Check the node has the right name.
	 *
	 */
	public void testGetContainerNodeName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode frog =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		FileManagerNode toad =
			delegate.getNode(
				frog.getIvorn()
				) ;
		//
		// Check the node name.
		assertEquals(
			"frog",
			toad.getName()
			);
		}

	/**
	 * Check the node has the right type.
	 *
	 */
	public void testGetContainerNodeType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode frog =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		FileManagerNode toad =
			delegate.getNode(
				frog.getIvorn()
				) ;
		//
		// Check the node type.
		assertTrue(
			toad.isContainer()
			);
		assertFalse(
			toad.isFile()
			);
		}

	/**
	 * Check we can get a file node.
	 *
	 */
	public void testGetFileNode()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode frog =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		assertNotNull(
			delegate.getNode(
				frog.getIvorn()
				)
			);
		}

	/**
	 * Check the node has the right ivorn.
	 *
	 */
	public void testGetFileNodeIvorn()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode frog =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		FileManagerNode toad =
			delegate.getNode(
				frog.getIvorn()
				) ;
		//
		// Check the node ivorn.
		assertEquals(
			frog.getIvorn().toString(),
			toad.getIvorn().toString()
			);
		}

	/**
	 * Check the node has the right name.
	 *
	 */
	public void testGetFileNodeName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode frog =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		FileManagerNode toad =
			delegate.getNode(
				frog.getIvorn()
				) ;
		//
		// Check the node name.
		assertEquals(
			"frog",
			toad.getName()
			);
		}

	/**
	 * Check the node has the right type.
	 *
	 */
	public void testGetFileNodeType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode frog =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check we can get the node.
		FileManagerNode toad =
			delegate.getNode(
				frog.getIvorn()
				) ;
		//
		// Check the node type.
		assertFalse(
			toad.isContainer()
			);
		assertTrue(
			toad.isFile()
			);
		}

	/**
	 * Check that we get the right Exception for a null parent.
	 *
	 */
	public void testGetChildNullParent()
		throws Exception
		{
		try {
			delegate.getChild(
				null,
				"frog"
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}

	/**
	 * Check that we get the right Exception for an unknown parent.
	 *
	 */
	public void testGetChildUnknownParent()
		throws Exception
		{
		try {
			delegate.getChild(
				factory.ivorn(
					delegate.getServiceIvorn()
					),
				"frog"
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we get the right Exception for a null path.
	 *
	 */
	public void testGetChildNullPath()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode frog =
			delegate.addFile(
				home,
				"frog"
				) ;
		try {
			delegate.getChild(
				home.getIvorn(),
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
	 * Check that we get the right Exception for an unknown path.
	 *
	 */
	public void testGetChildUnknownPath()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode frog =
			delegate.addFile(
				home,
				"frog"
				) ;
		try {
			delegate.getChild(
				home.getIvorn(),
				"unknown"
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we can get a file by path.
	 *
	 */
	public void testGetChild()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode frog =
			delegate.addFile(
				home,
				"frog"
				) ;
		//
		// Check we can find the file.
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"frog"
				)
			) ;
		}

//
// ... Ivorn, Name and type ...
//

	/**
	 * Check that we can get file by path.
	 *
	 */
	public void testGetNestedChild()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode frog =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Add a container node.
		FileManagerNode toad =
			delegate.addContainer(
				frog,
				"toad"
				) ;
		//
		// Add a file node.
		FileManagerNode newt =
			delegate.addFile(
				toad,
				"newt"
				) ;
		//
		// Check we can find the file(s).
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"frog"
				)
			) ;
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"frog/toad"
				)
			) ;
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"frog/toad/newt"
				)
			) ;

		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"/frog"
				)
			) ;
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"/frog/toad"
				)
			) ;
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"/frog/toad/newt"
				)
			) ;

		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"//frog"
				)
			) ;
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"//frog//toad"
				)
			) ;
		assertNotNull(
			delegate.getChild(
				home.getIvorn(),
				"//frog//toad//newt"
				)
			) ;
		}

//
// ... Ivorn, Name and type ...
//

//
// ... Check we can't add a child to a file.
//

	/**
	 * Check that we get the right exception for a null parent.
	 *
	 */
	public void testGetChildrenNullParent()
		throws Exception
		{
		try {
			delegate.getChildren(
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
	 * Check that we get the right exception for an unknown parent.
	 *
	 */
	public void testGetChildrenUnknownParent()
		throws Exception
		{
		//
		// Try getting the children for an unknown parent.
		try {
			delegate.getChildren(
				factory.ivorn(
					delegate.getServiceIvorn()
					)
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we get an empty list of nodes.
	 *
	 */
	public void testGetChildrenEmpty()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Get the (empty) list of child nodes.
		String[] array = delegate.getChildren(
			home.getIvorn()
			) ;
		//
		// Check the list is empty.
		assertEquals(
			0,
			array.length
			) ;
		}

	/**
	 * Check that we can get a list of child nodes.
	 *
	 */
	public void testGetChildren()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a container node.
		FileManagerNode frog =
			delegate.addContainer(
				home,
				"frog"
				) ;
		//
		// Add a container node.
		FileManagerNode toad =
			delegate.addContainer(
				home,
				"toad"
				) ;
		//
		// Add a file node.
		FileManagerNode newt =
			delegate.addFile(
				home,
				"newt"
				) ;
		//
		// Get the list of child nodes.
		String[] array = delegate.getChildren(
			home.getIvorn()
			) ;
		//
		// Check the list contains three nodes.
		assertEquals(
			3,
			array.length
			) ;
		}

	/**
	 * Check that we get an empty list of nodes.
	 *
	 */
	public void testGetDataChildrenEmpty()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home = delegate.addAccount(
			accountIvorn
			) ;
		//
		// Add a file node.
		FileManagerNode newt =
			delegate.addFile(
				home,
				"newt"
				) ;
		//
		// Get the (empty) list of child nodes.
		String[] array = delegate.getChildren(
			newt.getIvorn()
			) ;
		//
		// Check the list is empty.
		assertEquals(
			0,
			array.length
			) ;
		}

//
// Check we get the right children ...
//

	/**
	 * Check we get the right Exception for null properties.
	 *
	 */
	public void testImportInitNullProperties()
		throws Exception
		{
		try {
			delegate.importInit(
				null
				);
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException");
		}

	/**
	 * Check we get the right Exception for a null ivorn.
	 *
	 */
	public void testImportInitNullIvorn()
		throws Exception
		{
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Call our service.
		try {
			delegate.importInit(
				request
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we get the right Exception for an unknow ivorn.
	 *
	 */
	public void testImportInitUnknownIvorn()
		throws Exception
		{
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the resource ivorn.
		request.setManagerResourceIvorn(
			factory.ivorn(
				delegate.getServiceIvorn()
				)
			);
		//
		// Call our service.
		try {
			delegate.importInit(
				request
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we can initiate an import into a node.
	 *
	 */
	public void testImportInitTransfer()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home =
			delegate.addAccount(
				accountIvorn
				) ;
		//
		// Add a file node.
		FileManagerNode newt =
			delegate.addFile(
				home,
				"newt"
				) ;
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the resource ivorn.
		request.setManagerResourceIvorn(
			newt.getIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			delegate.importInit(
				request
				) ;
		//
		// Check we got a response.
		assertNotNull(
			importTransfer
			);
		}

	/**
	 * Check an import transfer has a location.
	 *
	 */
	public void testImportInitLocation()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home =
			delegate.addAccount(
				accountIvorn
				) ;
		//
		// Add a file node.
		FileManagerNode newt =
			delegate.addFile(
				home,
				"newt"
				) ;
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the resource ivorn.
		request.setManagerResourceIvorn(
			newt.getIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			delegate.importInit(
				request
				) ;
		//
		// Check we got a location.
		assertNotNull(
			importTransfer.getLocation()
			);
		}

	/**
	 * Check we can write data to the import location.
	 *
	 */
	public void testImportInitWrite()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home =
			delegate.addAccount(
				accountIvorn
				) ;
		//
		// Add a file node.
		FileManagerNode newt =
			delegate.addFile(
				home,
				"newt"
				) ;
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the resource ivorn.
		request.setManagerResourceIvorn(
			newt.getIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			delegate.importInit(
				request
				) ;
		//
		// Create an output stream to the target.
		FileStoreOutputStream importStream = new FileStoreOutputStream(
			importTransfer.getLocation()
			) ;
		//
		// Transfer our data.
		importStream.open() ;
		importStream.write(
			TEST_BYTES
			) ;
		importStream.close() ;
		}

	/**
	 * Check we get the right Exception for null properties.
	 *
	 */
	public void testExportInitNullProperties()
		throws Exception
		{
		try {
			delegate.importInit(
				null
				);
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException");
		}

	/**
	 * Check we get the right Exception for a null ivorn.
	 *
	 */
	public void testExportInitNullIvorn()
		throws Exception
		{
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Call our service.
		try {
			delegate.importInit(
				request
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we get the right Exception for an unknow ivorn.
	 *
	 */
	public void testExportInitUnknownIvorn()
		throws Exception
		{
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the resource ivorn.
		request.setManagerResourceIvorn(
			factory.ivorn(
				delegate.getServiceIvorn()
				)
			);
		//
		// Call our service.
		try {
			delegate.importInit(
				request
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we can read and write data to a node.
	 *
	 */
	public void testExportInitUnknownRead()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerNode home =
			delegate.addAccount(
				accountIvorn
				) ;
		//
		// Add a file node.
		FileManagerNode newt =
			delegate.addFile(
				home,
				"newt"
				) ;
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the resource ivorn.
		request.setManagerResourceIvorn(
			newt.getIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			delegate.importInit(
				request
				) ;
		//
		// Create an output stream to the target.
		FileStoreOutputStream importStream = new FileStoreOutputStream(
			importTransfer.getLocation()
			) ;
		//
		// Transfer our data.
		importStream.open() ;
		importStream.write(
			TEST_BYTES
			) ;
		importStream.close() ;

		//
		// Call our manager to initiate the export.
		TransferProperties exportTransfer =
			delegate.exportInit(
				request
				) ;
		//
		// Create an output stream to the target.
		FileStoreInputStream exportStream = new FileStoreInputStream(
			exportTransfer.getLocation()
			) ;

		//
		// Create our buffer stream.
		ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
		//
		// Read the data from the export location.
		exportStream.open();
		new TransferUtil(
			exportStream,
			buffer
			).transfer();
		exportStream.close();
		//
		// Check we got the right data back.
		assertEquals(
			TEST_BYTES,
			buffer.toByteArray()
			);

		}


	}

