/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerTest.java,v $
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.20  2004/11/17 07:56:33  dave
 *   Added server mock and webapp build scripts ...
 *
 *   Revision 1.1.2.19  2004/11/16 03:25:37  dave
 *   Updated API to use full ivorn rather than ident ...
 *
 *   Revision 1.1.2.18  2004/11/13 01:39:03  dave
 *   Modifications to support the new client API ...
 *
 *   Revision 1.1.2.17  2004/11/11 17:53:10  dave
 *   Removed Node interface from the server side ....
 *
 *   Revision 1.1.2.16  2004/11/11 16:36:19  dave
 *   Changed getChildren to retunr array of names ...
 *
 *   Revision 1.1.2.15  2004/11/11 15:41:44  dave
 *   Renamed importInitEx and exportInitEx back to the original names ...
 *
 *   Revision 1.1.2.14  2004/11/11 15:30:37  dave
 *   Moving manager API to property[] rather than Node.
 *
 *   Revision 1.1.2.13  2004/11/10 18:32:57  dave
 *   Moved getAccount API to use properties ...
 *
 *   Revision 1.1.2.12  2004/11/10 17:00:11  dave
 *   Moving the manager API towards property based rather than node based ...
 *
 *   Revision 1.1.2.11  2004/11/06 20:03:17  dave
 *   Implemented ImportInit and ExportInit using properties
 *
 *   Revision 1.1.2.10  2004/11/05 05:58:31  dave
 *   Refactored the properties handling in importInitEx() ..
 *
 *   Revision 1.1.2.9  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.8  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.7  2004/11/02 15:05:13  dave
 *   Added old path tests back in ...
 *
 *   Revision 1.1.2.6  2004/11/01 16:23:22  dave
 *   Started integrating import and export with FileStore ...
 *
 *   Revision 1.1.2.5  2004/10/19 14:52:36  dave
 *   Refactored container and file into just node.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import java.net.URL ;

import java.io.InputStream ;
import java.io.OutputStream ;
import java.io.ByteArrayOutputStream ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.filestore.common.FileStoreInputStream ;
import org.astrogrid.filestore.common.FileStoreOutputStream ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;

import org.astrogrid.filestore.common.transfer.TransferUtil ;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory ;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException ;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;
import org.astrogrid.filemanager.common.exception.FileManagerPropertiesException ;

/**
 * A JUnit test case for the file manager service.
 *
 */
public class FileManagerTest
	extends BaseTest
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerTest.class);

	/**
	 * Internal reference to our target service.
	 *
	 */
	protected FileManager target ;

	/**
	 * Check that we can create our target service.
	 *
	 */
	public void testTargetNotNull()
		throws Exception
		{
		assertNotNull(
			target
			) ;
		}

	/**
	 * Check that we get the right exception for a null account name.
	 *
	 */
	public void testAddAccountNull()
		throws Exception
		{
		try {
			target.addAccount(
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
			target.addAccount(
				accountName
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
			target.addAccount(
				accountName
				)
			) ;
		try {
			target.addAccount(
				accountName
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
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Check the node ivorn.
		assertNotNull(
			home.getManagerResourceIvorn()
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
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Check the node ident.
		assertNotNull(
			home.getManagerResourceIdent()
			) ;
		}

	/**
	 * Check that an account node has a service Ivorn.
	 *
	 */
	public void testAddAccountService()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Check the node ivorn.
		assertNotNull(
			home.getManagerServiceIvorn()
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
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Check the node name.
		assertEquals(
			"home",
			home.getManagerResourceName()
			) ;
		}

	/**
	 * Check that we get the right Exception for a null account.
	 *
	 */
	public void testGetAccountNull()
		throws Exception
		{
		try {
			target.getAccount(
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
	 * Check that we get the right Exception for an unknown account.
	 *
	 */
	public void testGetAccountUnknown()
		throws Exception
		{
		try {
			target.getAccount(
				unknownName
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we can find an account node.
	 *
	 */
	public void testGetAccount()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Get the account home.
		FileManagerProperties test = new FileManagerProperties(
			target.getAccount(
				accountName
				)
			);
		//
		// Check we got the properties back.
		assertNotNull(
			test
			) ;
		}

	/**
	 * Check that an account node has the right name.
	 *
	 */
	public void testGetAccountName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Get the account home.
		FileManagerProperties test = new FileManagerProperties(
			target.getAccount(
				accountName
				)
			);
		//
		// Check the node names are the same.
		assertEquals(
			home.getManagerResourceName(),
			test.getManagerResourceName()
			) ;
		}

	/**
	 * Check that an account node has the right ivorn.
	 *
	 */
	public void testGetAccountIvorn()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Get the account home.
		FileManagerProperties test = new FileManagerProperties(
			target.getAccount(
				accountName
				)
			);
		//
		// Check the node ivorns are the same.
		assertEquals(
			home.getManagerResourceIvorn().toString(),
			test.getManagerResourceIvorn().toString()
			) ;
		}

	/**
	 * Check that an account node has the right ident.
	 *
	 */
	public void testGetAccountIdent()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Get the account home.
		FileManagerProperties test = new FileManagerProperties(
			target.getAccount(
				accountName
				)
			);
		//
		// Check the node idents are the same.
		assertEquals(
			home.getManagerResourceIdent(),
			test.getManagerResourceIdent()
			) ;
		}

	/**
	 * Check that an account node has the right type.
	 *
	 */
	public void testGetAccountType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Get the account home.
		FileManagerProperties test = new FileManagerProperties(
			target.getAccount(
				accountName
				)
			);
		//
		// Check the node type.
		assertEquals(
			FileManagerProperties.CONTAINER_NODE_TYPE,
			test.getManagerResourceType()
			) ;
		}

	/**
	 * Check that we get the right exception for a null parent.
	 *
	 */
	public void testAddNodeNullParent()
		throws Exception
		{
		//
		// Try adding a node with no parent.
		try {
			target.addNode(
				null,
				"name",
				FileManagerProperties.CONTAINER_NODE_TYPE
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
	public void testAddNodeNullName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Try adding a node with no name.
		try {
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				null,
				FileManagerProperties.CONTAINER_NODE_TYPE
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}

	/**
	 * Check that we get the right exception for a null type.
	 *
	 */
	public void testAddNodeNullType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Try adding a node with no type.
		try {
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
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
	public void testAddNodeUnknownParent()
		throws Exception
		{
		try {
			target.addNode(
				"ivo://unknown#unknown",
				"name",
				FileManagerProperties.CONTAINER_NODE_TYPE
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we get the right exception for an unknown type.
	 * @todo This one fails ....
	 *
	public void testAddNodeUnknownType()
		throws Exception
		{
		Node root = target.addAccount(
			accountName
			) ;
		try {
			target.addNode(
				root.getIdent(),
				"name",
				"unknown"
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}
	 */

	/**
	 * Check that we get the right exception for an invalid name.
	 * @todo This one fails ....
	 *
	public void testAddNodeInvalidName()
		throws Exception
		{
		Node root = target.addAccount(
			accountName
			) ;
		try {
			target.addNode(
				root.getIdent(),
				"name/name",
				FileManagerProperties.CONTAINER_NODE_TYPE
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}
	 */

	/**
	 * Check that we can add a child node.
	 *
	 */
	public void testAddContainerChild()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a container node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		//
		// Check we got a new node.
		assertNotNull(
			frog
			) ;
		}

	/**
	 * Check that we can add a child node.
	 *
	 */
	public void testAddChildData()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a data node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Check we got a new node.
		assertNotNull(
			frog
			) ;
		}

	/**
	 * Check that we can't add a child to a data node.
	 *
	 */
	public void testAddDataChild()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a data node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Try adding a child node.
		try {
			target.addNode(
				frog.getManagerResourceIvorn().toString(),
				"toad",
				FileManagerProperties.DATA_NODE_TYPE
				);
			}
		catch (UnsupportedOperationException ouch)
			{
			return ;
			}
		fail("Expected UnsupportedOperationException") ;
		}

	/**
	 * Check that we get the right exception for a null ident.
	 *
	 */
	public void testGetNodeNull()
		throws Exception
		{
		try {
			target.getNode(
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
	 * Check that we get the right exception for an unknown node.
	 *
	 */
	public void testGetNodeUnknown()
		throws Exception
		{
		try {
			target.getNode(
				"ivo://unknown#unknown"
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we can get a node by identifier.
	 *
	 */
	public void testGetNodeIvorn()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a data node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Get the node properties.
		FileManagerProperties toad = new FileManagerProperties(
			target.getNode(
				frog.getManagerResourceIvorn().toString()
				)
			);
		//
		// Check the node ivorn.
		assertEquals(
			frog.getManagerResourceIvorn().toString(),
			toad.getManagerResourceIvorn().toString()
			) ;
		}

	/**
	 * Check that a node has the right name.
	 *
	 */
	public void testGetNodeName()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a data node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Get the node properties.
		FileManagerProperties toad = new FileManagerProperties(
			target.getNode(
				frog.getManagerResourceIvorn().toString()
				)
			);
		//
		// Check the node names are the same.
		assertEquals(
			frog.getManagerResourceName(),
			toad.getManagerResourceName()
			) ;
		}

	/**
	 * Check that a node has the right identifier.
	 *
	 */
	public void testGetNodeIdent()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a data node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Get the node properties.
		FileManagerProperties toad = new FileManagerProperties(
			target.getNode(
				frog.getManagerResourceIvorn().toString()
				)
			);
		//
		// Check the node idents are the same.
		assertEquals(
			frog.getManagerResourceIdent(),
			toad.getManagerResourceIdent()
			) ;
		}

	/**
	 * Check that a node has the right type.
	 *
	 */
	public void testGetNodeType()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a data node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Get the node properties.
		FileManagerProperties toad = new FileManagerProperties(
			target.getNode(
				frog.getManagerResourceIvorn().toString()
				)
			);
		assertEquals(
			FileManagerProperties.DATA_NODE_TYPE,
			toad.getManagerResourceType()
			) ;
		}

	/**
	 * Check that we get the right exception for a null root.
	 *
	 */
	public void testGetPathNullRoot()
		throws Exception
		{
		//
		// Try getting a node with no parent.
		try {
			target.getChild(
				null,
				"path"
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}

	/**
	 * Check that we get the right exception for a null path.
	 *
	 */
	public void testGetPathNullPath()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Try getting a data node with no name.
		try {
			target.getChild(
				home.getManagerResourceIvorn().toString(),
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
	public void testGetPathUnknownParent()
		throws Exception
		{
		//
		// Try getting a node with an unknown parent.
		try {
			target.getChild(
				"ivo://unknown#unknown",
				"path"
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}

	/**
	 * Check that we get the right exception for an unknown path.
	 *
	 */
	public void testGetPathUnknownPath()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Try getting an unknown child.
		try {
			target.getChild(
				home.getManagerResourceIvorn().toString(),
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
	 * Check that we can get a node by path.
	 *
	 */
	public void testGetPath()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add some nodes.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		FileManagerProperties toad = new FileManagerProperties(
			target.addNode(
				frog.getManagerResourceIvorn().toString(),
				"toad",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		FileManagerProperties newt = new FileManagerProperties(
			target.addNode(
				toad.getManagerResourceIvorn().toString(),
				"newt",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		//
		// Get a nested child node.
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"frog/toad/newt"
				)
			) ;
		}

	/**
	 * Check that we get the right exception for a null ident.
	 *
	 */
	public void testGetChildrenNullParent()
		throws Exception
		{
		//
		// Try getting the children for a null parent.
		try {
			target.getChildren(
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
			target.getChildren(
				"ivo://unknown#unknown"
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
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Get the (empty) list of child nodes.
		String[] array = target.getChildren(
			home.getManagerResourceIvorn().toString()
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
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add some nodes.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		FileManagerProperties toad = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"toad",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		FileManagerProperties newt = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"newt",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		//
		// Get the list of child nodes.
		String[] array = target.getChildren(
			home.getManagerResourceIvorn().toString()
			) ;
		//
		// Check the list contains three nodes.
		assertEquals(
			3,
			array.length
			) ;
		}

//
// Check we get the right names ....
//

//
// Check we can get at all of the child nodes ....
//

	/**
	 * Check that we get an empty list for a data node.
	 *
	 */
	public void testGetDataChildrenEmpty()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add a data node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Get the (empty) list of child nodes.
		String[] array = target.getChildren(
			frog.getManagerResourceIvorn().toString()
			) ;
		//
		// Check the list is empty.
		assertEquals(
			0,
			array.length
			) ;
		}

	/**
	 * Check that we can get a nested container by path.
	 *
	 */
	public void testGetContainerByPath()
		throws Exception
		{
		//
		// Create the account home.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Add some nested nodes.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		FileManagerProperties toad = new FileManagerProperties(
			target.addNode(
				frog.getManagerResourceIvorn().toString(),
				"toad",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		FileManagerProperties newt = new FileManagerProperties(
			target.addNode(
				toad.getManagerResourceIvorn().toString(),
				"newt",
				FileManagerProperties.CONTAINER_NODE_TYPE
				)
			);
		//
		// Check we can access the nodes path.
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"frog"
				)
			) ;
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"frog/toad"
				)
			) ;
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"frog/toad/newt"
				)
			) ;

		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"/frog"
				)
			) ;
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"/frog/toad"
				)
			) ;
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"/frog/toad/newt"
				)
			) ;

		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"//frog"
				)
			) ;
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"//frog//toad"
				)
			) ;
		assertNotNull(
			target.getChild(
				home.getManagerResourceIvorn().toString(),
				"//frog//toad//newt"
				)
			) ;
		}

	/**
	 * Check that we can get the right list of child nodes.
	 *
	public void testContainerChildNodeNames()
		throws Exception
		{
		target.addAccount(
			accountName
			) ;
		Container root = target.getAccount(
			accountName
			) ;
		Container frog = root.addContainer(
			"frog"
			) ;
		Container toad = root.addContainer(
			"toad"
			) ;
		Container newt = root.addContainer(
			"newt"
			) ;
		//
		// Check the collection has the right values.
		int found = 0 ;
		Iterator iter = root.getChildNodes().iterator() ;
		while (iter.hasNext())
			{
			Node node = (Node) iter.next() ;
			if (node.getName().equals(frog.getName()))
				{
				assertEquals(
					frog.getIdent(),
					node.getIdent()
					) ;
				found++ ;
				}
			if (node.getName().equals(toad.getName()))
				{
				assertEquals(
					toad.getIdent(),
					node.getIdent()
					) ;
				found++ ;
				}
			if (node.getName().equals(newt.getName()))
				{
				assertEquals(
					newt.getIdent(),
					node.getIdent()
					) ;
				found++ ;
				}
			}
		assertEquals(
			3,
			found
			) ;
		}
	 */

	/**
	 * Check we get the right exception for an invalid path.
	 *
	public void testInvalidPathOne()
		throws Exception
		{
		target.addAccount(
			accountName
			) ;
		Container root = target.getAccount(
			accountName
			) ;
		try {
			root.getChild("/") ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}
	 */

	/**
	 * Check we get the right exception for an invalid path.
	 *
	public void testInvalidPathTwo()
		throws Exception
		{
		target.addAccount(
			accountName
			) ;
		Container root = target.getAccount(
			accountName
			) ;
		try {
			root.getChild("//") ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}
	 */

	/**
	 * Check we can access a container by path (with a *).
	 * Need to check how the explorer uses this ....
	 *
	 */

	/**
	 * Check we get the right exception for a null identifier.
	 *
	public void testImportInitNullIdent()
		throws Exception
		{
		try {
			target.importInit(
				null
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}
	 */

	/**
	 * Check we get the right exception for unknown node.
	 *
	public void testImportInitUnknownNode()
		throws Exception
		{
		try {
			target.importInit(
				"unknown"
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}
	 */

	/**
	 * Check we get the right exception for a container node.
	 *
	public void testImportInitContainer()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		//
		// Try to import data into the node.
		try {
			target.importInit(
				root.getIdent()
				) ;
			}
		catch (UnsupportedOperationException ouch)
			{
			return ;
			}
		fail("Expected UnsupportedOperationException") ;
		}
	 */

	/**
	 * Check we get a transfer properties for an import.
	 *
	public void testImportInitProperties()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		Node data = target.addNode(
			root.getIdent(),
			"data",
			FileManagerProperties.DATA_NODE_TYPE
			) ;
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				data.getIdent()
				) ;
		//
		// Check we got some import properties.
		assertNotNull(
			importTransfer
			) ;
		}
	 */

	/**
	 * Check the transfer properties contains a location.
	 *
	public void testImportInitLocation()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		Node data = target.addNode(
			root.getIdent(),
			"data",
			FileManagerProperties.DATA_NODE_TYPE
			) ;
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				data.getIdent()
				) ;
		//
		// Check the transfer contains a location.
		assertNotNull(
			importTransfer.getLocation()
			);
		}
	 */

	/**
	 * Check we can send data to the transfer location.
	 *
	public void testImportInitWrite()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		Node data = target.addNode(
			root.getIdent(),
			"data",
			FileManagerProperties.DATA_NODE_TYPE
			) ;
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				data.getIdent()
				) ;
		//
		// Open an output stream.
		FileStoreOutputStream importStream = new FileStoreOutputStream(
			importTransfer.getLocation()
			) ;
		//
		// Open our stream.
		importStream.open() ;
		//
		// Transfer our data.
		importStream.write(
			TEST_BYTES
			) ;
		//
		// Close our stream.
		importStream.close() ;
		}
	 */

	/**
	 * Check we get the right exception for a null identifier.
	 *
	public void testExportInitNullIdent()
		throws Exception
		{
		try {
			target.exportInit(
				null
				) ;
			}
		catch (IllegalArgumentException ouch)
			{
			return ;
			}
		fail("Expected IllegalArgumentException") ;
		}
	 */

	/**
	 * Check we get the right exception for unknown node.
	 *
	public void testExportInitUnknownNode()
		throws Exception
		{
		try {
			target.exportInit(
				"unknown"
				) ;
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException") ;
		}
	 */

	/**
	 * Check we get the right exception for a container node.
	 *
	public void testExportInitContainer()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		//
		// Try to export data from the node.
		try {
			target.exportInit(
				root.getIdent()
				) ;
			}
		catch (UnsupportedOperationException ouch)
			{
			return ;
			}
		fail("Expected UnsupportedOperationException") ;
		}
	 */

	/**
	 * Check we get the right exception for an empty node.
	 *
	public void testExportInitEmptyNode()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		Node data = target.addNode(
			root.getIdent(),
			"data",
			FileManagerProperties.DATA_NODE_TYPE
			) ;
		//
		// Try to export data from the node.
		try {
			target.exportInit(
				data.getIdent()
				) ;
			}
		catch (FileManagerServiceException ouch)
			{
			return ;
			}
		fail("Expected FileManagerServiceException") ;
		}
	 */

	/**
	 * Check we get a transfer properties for an export.
	 *
	public void testExportInitProperties()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		Node data = target.addNode(
			root.getIdent(),
			"data",
			FileManagerProperties.DATA_NODE_TYPE
			) ;
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				data.getIdent()
				) ;
		//
		// Open an output stream.
		FileStoreOutputStream importStream = new FileStoreOutputStream(
			importTransfer.getLocation()
			) ;
		//
		// Open our stream.
		importStream.open() ;
		//
		// Transfer our data.
		importStream.write(
			TEST_BYTES
			) ;
		//
		// Close our stream.
		importStream.close() ;
		//
		// Call our manager to initiate the export.
		TransferProperties exportTransfer =
			target.exportInit(
				data.getIdent()
				) ;
		//
		// Check we got some export properties.
		assertNotNull(
			exportTransfer
			);
		}
	 */

	/**
	 * Check the export properties contains a URL.
	 *
	public void testExportInitUrl()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		Node data = target.addNode(
			root.getIdent(),
			"data",
			FileManagerProperties.DATA_NODE_TYPE
			) ;
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				data.getIdent()
				) ;
		//
		// Open an output stream.
		FileStoreOutputStream importStream = new FileStoreOutputStream(
			importTransfer.getLocation()
			) ;
		//
		// Open our stream.
		importStream.open() ;
		//
		// Transfer our data.
		importStream.write(
			TEST_BYTES
			) ;
		//
		// Close our stream.
		importStream.close() ;
		//
		// Call our manager to initiate the export.
		TransferProperties exportTransfer =
			target.exportInit(
				data.getIdent()
				) ;
		//
		// Check we got a location URL.
		assertNotNull(
			exportTransfer.getLocation()
			);
		}
	 */

	/**
	 * Check we can read our data back from the URL.
	 *
	public void testExportInitRead()
		throws Exception
		{
		//
		// Create our test account.
		target.addAccount(
			accountName
			) ;
		//
		// Create our target node.
		Node root = target.getAccount(
			accountName
			) ;
		Node data = target.addNode(
			root.getIdent(),
			"data",
			FileManagerProperties.DATA_NODE_TYPE
			) ;
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				data.getIdent()
				) ;
		//
		// Open an output stream.
		FileStoreOutputStream importStream = new FileStoreOutputStream(
			importTransfer.getLocation()
			) ;
		//
		// Open our stream.
		importStream.open() ;
		//
		// Transfer our data.
		importStream.write(
			TEST_BYTES
			) ;
		//
		// Close our stream.
		importStream.close() ;
		//
		// Call our manager to initiate the export.
		TransferProperties exportTransfer =
			target.exportInit(
				data.getIdent()
				) ;
		//
		// Get an input stream to the file.
		FileStoreInputStream exportStream = new FileStoreInputStream(
			exportTransfer.getLocation()
			);
		//
		// Open our input stream.
		exportStream.open() ;
		//
		// Create our buffer stream.
		ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
		//
		// Read our test bytes.
		TransferUtil util = new TransferUtil(
			exportStream,
			buffer
			);
		util.transfer();
		//
		// Close our input stream.
		exportStream.close() ;
		//
		// Check we got the right data back.
		assertEquals(
			TEST_BYTES,
			buffer.toByteArray()
			);
		}
	 */

	/**
	 * Check we get the right Exception for null properties.
	 *
	 */
	public void testImportInitNullProperties()
		throws Exception
		{
		try {
			target.importInit(
				null
				);
			}
		catch (FileManagerPropertiesException ouch)
			{
			return ;
			}
		fail("Expected FileManagerPropertiesException");
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
			target.importInit(
				request.toArray()
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we get the right Exception for an unknown node.
	 *
	 */
	public void testImportInitUnknownIvorn()
		throws Exception
		{
		//
		// Create the ivorn factory.
		FileManagerIvornFactory factory = new FileManagerIvornFactory() ;
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Create a new resource ivorn.
		request.setManagerResourceIvorn(
			factory.ident(
				target.getIdentifier()
				)
			);
		//
		// Call our service.
		try {
			target.importInit(
				request.toArray()
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we can initiate an import into a known node.
	 *
	 */
	public void testImportInitValidNode()
		throws Exception
		{
		//
		// Create our test account.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Create our target node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the target node ivorn.
		request.setManagerResourceIvorn(
			frog.getManagerResourceIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				request.toArray()
				) ;
		//
		// Check we got a response.
		assertNotNull(
			importTransfer
			);
		}

	/**
	 * Check we can transfer data into an existing node.
	 *
	 */
	public void testImportInitWriteData()
		throws Exception
		{
		//
		// Create our test account.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Create our target node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Set the target node ivorn.
		request.setManagerResourceIvorn(
			frog.getManagerResourceIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				request.toArray()
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
	public void testExportNullProperties()
		throws Exception
		{
		try {
			target.exportInit(
				null
				);
			}
		catch (FileManagerPropertiesException ouch)
			{
			return ;
			}
		fail("Expected FileManagerPropertiesException");
		}

	/**
	 * Check we get the right Exception for a null ivorn.
	 *
	 */
	public void testExportNullIvorn()
		throws Exception
		{
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Call our service.
		try {
			target.exportInit(
				request.toArray()
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we get the right Exception for an unknown node.
	 *
	 */
	public void testExportUnknownIvorn()
		throws Exception
		{
		//
		// Create the ivorn factory.
		FileManagerIvornFactory factory = new FileManagerIvornFactory() ;
		//
		// Create our request properties.
		FileManagerProperties request = new FileManagerProperties() ;
		//
		// Create a new resource ivorn.
		request.setManagerResourceIvorn(
			factory.ident(
				target.getIdentifier()
				)
			);
		//
		// Call our service.
		try {
			target.exportInit(
				request.toArray()
				);
			}
		catch (NodeNotFoundException ouch)
			{
			return ;
			}
		fail("Expected NodeNotFoundException");
		}

	/**
	 * Check we can initiate an export from a known node.
	 *
	 */
	public void testExportValidNode()
		throws Exception
		{
		//
		// Create our test account.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Create our target node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Create our request properties.
		FileManagerProperties importRequest = new FileManagerProperties() ;
		//
		// Set the target node ivorn.
		importRequest.setManagerResourceIvorn(
			frog.getManagerResourceIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				importRequest.toArray()
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
		// Create our request properties.
		FileManagerProperties exportRequest = new FileManagerProperties() ;
		//
		// Set the target node ivorn.
		exportRequest.setManagerResourceIvorn(
			frog.getManagerResourceIvorn()
			);
		//
		// Call our manager to initiate the export.
		TransferProperties exportTransfer =
			target.exportInit(
				exportRequest.toArray()
				) ;
		//
		// Check we got a response.
		assertNotNull(
			exportTransfer
			);
		}

	/**
	 * Check we get the right data back.
	 *
	 */
	public void testExportRead()
		throws Exception
		{
		//
		// Create our test account.
		FileManagerProperties home = new FileManagerProperties(
			target.addAccount(
				accountName
				)
			);
		//
		// Create our target node.
		FileManagerProperties frog = new FileManagerProperties(
			target.addNode(
				home.getManagerResourceIvorn().toString(),
				"frog",
				FileManagerProperties.DATA_NODE_TYPE
				)
			);
		//
		// Create our request properties.
		FileManagerProperties importRequest = new FileManagerProperties() ;
		//
		// Set the target node ivorn.
		importRequest.setManagerResourceIvorn(
			frog.getManagerResourceIvorn()
			);
		//
		// Call our manager to initiate the import.
		TransferProperties importTransfer =
			target.importInit(
				importRequest.toArray()
				) ;
		//
		// Create an output stream to the import location.
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
		// Create our request properties.
		FileManagerProperties exportRequest = new FileManagerProperties() ;
		//
		// Set the target node ivorn.
		exportRequest.setManagerResourceIvorn(
			frog.getManagerResourceIvorn()
			);
		//
		// Call our manager to initiate the export.
		TransferProperties exportTransfer =
			target.exportInit(
				exportRequest.toArray()
				) ;
		//
		// Get an input stream to the export location.
		FileStoreInputStream exportStream = new FileStoreInputStream(
			exportTransfer.getLocation()
			);
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

