/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerTest.java,v $
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.6  2005/01/12 14:58:18  dave
 *   Removed delete test ..
 *
 *   Revision 1.3.4.5  2005/01/12 14:33:48  dave
 *   Removed System.out.println debug ...
 *
 *   Revision 1.3.4.4  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3.4.3  2005/01/07 12:18:00  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 *   Revision 1.3.4.2  2004/12/24 02:42:45  dave
 *   Changed delete to use ivorn ...
 *
 *   Revision 1.3.4.1  2004/12/24 02:05:05  dave
 *   Refactored exception handling, removing IdentifierException from the public API ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.40  2004/12/16 13:12:12  dave
 *   Comment for the delete test ...
 *
 *   Revision 1.1.2.39  2004/12/14 17:29:40  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.38  2004/12/14 17:23:36  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.37  2004/12/14 16:36:54  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.36  2004/12/14 16:25:44  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.35  2004/12/14 16:02:10  dave
 *   Added delete from filemanager ....
 *
 *   Revision 1.1.2.34  2004/12/14 15:34:46  dave
 *   Added 'not there' test for delete ....
 *
 *   Revision 1.1.2.33  2004/12/14 14:44:46  dave
 *   Added test for copy empty location ...
 *
 *   Revision 1.1.2.32  2004/12/14 14:26:16  dave
 *   Added delete to the server API ....
 *
 *   Revision 1.1.2.31  2004/12/14 14:21:58  dave
 *   Added delete to the server API ....
 *
 *   Revision 1.1.2.30  2004/12/11 21:19:53  dave
 *   Added copy accross remote filestore(s) ...
 *
 *   Revision 1.1.2.29  2004/12/11 05:59:18  dave
 *   Added internal copy for nodes ...
 *   Added local copy for data ...
 *
 *   Revision 1.1.2.28  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.27  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.26  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.25  2004/12/06 13:29:02  dave
 *   Added initial code for move location ....
 *
 *   Revision 1.1.2.24  2004/12/04 05:22:21  dave
 *   Fixed null parent mistake ...
 *
 *   Revision 1.1.2.23  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.22  2004/11/26 05:07:17  dave
 *   Fixed node tests for integration ...
 *
 *   Revision 1.1.2.21  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
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
import java.io.FileNotFoundException ;

import org.astrogrid.store.Ivorn ;

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
     * A set of ivorn identifiers for our target file stores.
     *
     */
    protected Ivorn[] filestores ;

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
            home.getManagerResourceIvorn(),
            test.getManagerResourceIvorn()
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
     * Check that a new container has the right name.
     *
     */
    public void testAddContainerName()
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
        // Check it has the right name.
        assertEquals(
            "frog",
            frog.getManagerResourceName()
            ) ;
        }

    /**
     * Check that a new container has the right type.
     *
     */
    public void testAddContainerType()
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
        // Check it has the right type.
        assertEquals(
            FileManagerProperties.CONTAINER_NODE_TYPE,
            frog.getManagerResourceType()
            ) ;
        }

    /**
     * Check that a new container has the right parent.
     *
     */
    public void testAddContainerParent()
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
        // Check it has the right type.
        assertEquals(
            home.getManagerResourceIvorn(),
            frog.getManagerParentIvorn()
            ) ;
        }

    /**
     * Check that we can add a data node.
     *
     */
    public void testAddData()
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
     * Check that a new data node has the right name.
     *
     */
    public void testAddDataName()
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
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check it has the right name.
        assertEquals(
            "frog",
            frog.getManagerResourceName()
            ) ;
        }

    /**
     * Check that a new data node has the right type.
     *
     */
    public void testAddDataType()
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
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check it has the right type.
        assertEquals(
            FileManagerProperties.DATA_NODE_TYPE,
            frog.getManagerResourceType()
            ) ;
        }

    /**
     * Check that a new data node has the right parent.
     *
     */
    public void testAddDataParent()
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
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check it has the right type.
        assertEquals(
            home.getManagerResourceIvorn(),
            frog.getManagerParentIvorn()
            ) ;
        }

    /**
     * Check that a new data node has the right location.
     *
     */
    public void testAddDataLocation()
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
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check the node location.
        assertNotNull(
            frog.getManagerLocationIvorn()
            );
        assertEquals(
            filestores[0],
            frog.getManagerLocationIvorn()
            );
        }

    /**
     * Check that we can't add a child to a data node.
     *
     */
    public void testAddChildToData()
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
     * Check that a node has the right parent.
     *
     */
    public void testGetNodeParent()
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
            home.getManagerResourceIvorn(),
            toad.getManagerParentIvorn()
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

    /**
     * Check we get the right list of child nodes.
     *
     */
    public void testGetChildrenIvorns()
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
        // Check the list contains the right nodes.
        int     totalFound = 0 ;
        boolean frogFound  = false ;
        boolean toadFound  = false ;
        boolean newtFound  = false ;
        for (int i = 0 ; i < array.length ; i++)
            {
            if (array[i].equals(frog.getManagerResourceIvorn().toString()))
                {
                totalFound++;
                frogFound = true ;
                }
            if (array[i].equals(toad.getManagerResourceIvorn().toString()))
                {
                totalFound++;
                toadFound = true ;
                }
            if (array[i].equals(newt.getManagerResourceIvorn().toString()))
                {
                totalFound++;
                newtFound = true ;
                }
            }
        assertEquals(
            3,
            totalFound
            ) ;
        assertTrue(
            frogFound
            );
        assertTrue(
            toadFound
            );
        assertTrue(
            newtFound
            );
        }

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
     * Check that we can get a nested container by ivorn.
     *
     */
    public void testGetContainerByIvorn()
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
        // Check we can access the nodes by ivorn.
        assertNotNull(
            target.getNode(
                home.getManagerResourceIvorn().toString() + "/frog"
                )
            ) ;
        assertNotNull(
            target.getNode(
                home.getManagerResourceIvorn().toString() + "/frog/toad"
                )
            ) ;
        assertNotNull(
            target.getNode(
                home.getManagerResourceIvorn().toString() + "/frog/toad/newt"
                )
            ) ;

        assertNotNull(
            target.getNode(
                home.getManagerResourceIvorn().toString() + "//frog"
                )
            ) ;
        assertNotNull(
            target.getNode(
                home.getManagerResourceIvorn().toString() + "//frog//toad"
                )
            ) ;
        assertNotNull(
            target.getNode(
                home.getManagerResourceIvorn().toString() + "//frog//toad//newt"
                )
            ) ;
        }

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
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException");
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
     * Check we can refresh the properties after an import. 
     *
     */
    public void testImportRefreshSize()
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
        // Refresh the node properties.
        FileManagerProperties toad = new FileManagerProperties(
            target.refresh(
                frog.getManagerResourceIvorn().toString()
                )
            );
        //
        // Check the data size.
        assertEquals(
            0,
            toad.getContentSize()
            );
        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            target.importInit(
                frog.toArray()
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
        // Refresh the node properties.
        FileManagerProperties newt = new FileManagerProperties(
            target.refresh(
                frog.getManagerResourceIvorn().toString()
                )
            );
        //
        // Check the data size.
        assertEquals(
            TEST_BYTES.length,
            newt.getContentSize()
            );
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
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException");
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

    /**
     * Test we get the right Exception for a null request.
     *
     */
    public void testMoveNullProperties()
        throws Exception
        {
        try {
            target.move(
                null
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException");
        }

    /**
     * Test we can change the node name.
     *
     */
    public void testMoveName()
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
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Check the node name.
        assertEquals(
            "frog",
            frog.getManagerResourceName()
            );
        //
        // Execute the move.
        FileManagerProperties result = new FileManagerProperties(
            target.move(
                request.toArray()
                )
            );
        //
        // Check the node name.
        assertEquals(
            "toad",
            result.getManagerResourceName()
            );
        }

    /**
     * Check we get the right Exception for a duplicate name.
     *
     */
    public void testMoveNameDuplicate()
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
        // Add our data node(s).
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        FileManagerProperties toad = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "toad",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Execute the move.
        try {
            target.move(
                request.toArray()
                );
            }
        catch (DuplicateNodeException ouch)
            {
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Test we can change the node parent.
     *
     */
    public void testMoveParent()
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
        // Add our data node(s).
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Add our container node.
        FileManagerProperties newt = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "newt",
                FileManagerProperties.CONTAINER_NODE_TYPE
                )
            );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerParentIvorn(
            newt.getManagerResourceIvorn()
            );
        //
        // Execute the move.
        FileManagerProperties result = new FileManagerProperties(
            target.move(
                request.toArray()
                )
            );
        //
        // Check the node name.
        assertEquals(
            "frog",
            result.getManagerResourceName()
            );
        //
        // Check the node parent.
        assertEquals(
            newt.getManagerResourceIvorn(),
            result.getManagerParentIvorn()
            );
        }

    /**
     * Test we can change the node name and parent.
     *
     */
    public void testMoveNameParent()
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
        // Add our data node(s).
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Add our container node.
        FileManagerProperties newt = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "newt",
                FileManagerProperties.CONTAINER_NODE_TYPE
                )
            );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        request.setManagerParentIvorn(
            newt.getManagerResourceIvorn()
            );
        //
        // Execute the move.
        FileManagerProperties result = new FileManagerProperties(
            target.move(
                request.toArray()
                )
            );
        //
        // Check the node name.
        assertEquals(
            "toad",
            result.getManagerResourceName()
            );
        //
        // Check the node parent.
        assertEquals(
            newt.getManagerResourceIvorn(),
            result.getManagerParentIvorn()
            );
        }

    /**
     * Check we get the right Exception for a duplicate name.
     *
     */
    public void testMoveParentDuplicate()
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
        // Add our container node.
        FileManagerProperties newt = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "newt",
                FileManagerProperties.CONTAINER_NODE_TYPE
                )
            );
        //
        // Add our data node(s).
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        FileManagerProperties toad = new FileManagerProperties(
            target.addNode(
                newt.getManagerResourceIvorn().toString(),
                "toad",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerParentIvorn(
            newt.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Execute the move.
        try {
            target.move(
                request.toArray()
                );
            }
        catch (DuplicateNodeException ouch)
            {
            FileManagerProperties test = new FileManagerProperties(
                target.getNode(
                    frog.getManagerResourceIvorn().toString()
                    )
                );
            assertEquals(
                home.getManagerResourceIvorn().toString(),
                test.getManagerParentIvorn().toString()
                );
            assertEquals(
                "frog",
                test.getManagerResourceName()
                );
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Test we can change the location of an empty node.
     *
     */
    public void testMoveEmptyLocation()
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
        // Add our data node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check the node location.
        assertNotNull(
            frog.getManagerLocationIvorn()
            );
        assertEquals(
            filestores[0],
            frog.getManagerLocationIvorn()
            );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerLocationIvorn(
            filestores[1]
            );
        //
        // Execute the move.
        FileManagerProperties result = new FileManagerProperties(
            target.move(
                request.toArray()
                )
            );
        //
        // Check the node location.
        assertNotNull(
            result.getManagerLocationIvorn()
            );
        assertEquals(
            filestores[1],
            result.getManagerLocationIvorn()
            );
        }

    /**
     * Test we can upload some data after we have changed the location.
     *
     */

    /**
     * Test that we can change the location of a node with data.
     *
     */
    public void testMoveDataLocation()
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
        // Add our data node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check the node location.
        assertNotNull(
            frog.getManagerLocationIvorn()
            );
        assertEquals(
            filestores[0],
            frog.getManagerLocationIvorn()
            );
        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            target.importInit(
                frog.toArray()
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
        // Set our target location.
        frog.setManagerLocationIvorn(
            filestores[1]
            );
        //
        // Execute the move.
        frog = new FileManagerProperties(
            target.move(
                frog.toArray()
                )
            );
        //
        // Check the node location.
        assertNotNull(
            frog.getManagerLocationIvorn()
            );
        assertEquals(
            filestores[1],
            frog.getManagerLocationIvorn()
            );

        //
        // Call our manager to initiate the export.
        TransferProperties exportTransfer =
            target.exportInit(
                frog.toArray()
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

/* TBD
 * Check that the old file has been removed ...
 *
 */

    /**
     * Check we get the right Exception for a null request.
     *
     */
    public void testCopyNullRequest()
        throws Exception
        {
        try {
            target.copy(
                null
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Check we get the right Exception for a null resource.
     *
     */
    public void testCopyNullResource()
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
        // Create our request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "frog"
            );
        try {
            target.copy(
                request.toArray()
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Check we get the right Exception for an unknown resource.
     *
     */
    public void testCopyUnknownResource()
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
        // Create our request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.getManagerResourceIvorn()
            );
        request.setManagerResourceIvorn(
            "ivo://unknown#unknown"
            );
        request.setManagerResourceName(
            "toad"
            );
        try {
            target.copy(
                request.toArray()
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Check we get the right Exception for the same resource.
     *
     */
    public void testCopyDuplicateSame()
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
        // Add our data node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        try {
            target.copy(
                frog.toArray()
                );
            }
        catch (DuplicateNodeException ouch)
            {
            return ;
            }
        fail("Expected DuplicateNodeException") ;
        }

    /**
     * Check we get the right Exception for a duplicate node.
     *
     */
    public void testCopyDuplicateNode()
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
        // Add our source node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Add our duplicate target.
        FileManagerProperties toad = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "toad",
                FileManagerProperties.CONTAINER_NODE_TYPE
                )
            );
        FileManagerProperties newt = new FileManagerProperties(
            target.addNode(
                toad.getManagerResourceIvorn().toString(),
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Create our copy request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerParentIvorn(
            toad.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "newt"
            );
        try {
            target.copy(
                request.toArray()
                );
            }
        catch (DuplicateNodeException ouch)
            {
            return ;
            }
        fail("Expected DuplicateNodeException") ;
        }

    /**
     * Check we can copy an empty node.
     *
     */
    public void testCopyEmptyNode()
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
        // Add our source node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Create our request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.getManagerResourceIvorn()
            );
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Create our copy.
        FileManagerProperties result = new FileManagerProperties(
            target.copy(
                request.toArray()
                )
            ) ;
        //
        // Check we got the right name.
        assertEquals(
            "toad",
            result.getManagerResourceName()
            );
        //
        // Check we got the right parent.
        assertEquals(
            home.getManagerResourceIvorn(),
            result.getManagerParentIvorn()
            );
        }

    /**
     * Check we can copy a node with data.
     *
     */
    public void testCopyNodeData()
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
        // Add our source node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );

        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            target.importInit(
                frog.toArray()
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
        // Create our copy request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.getManagerResourceIvorn()
            );
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Create our copy.
        FileManagerProperties toad = new FileManagerProperties(
            target.copy(
                request.toArray()
                )
            ) ;

        //
        // Call our manager to initiate the export.
        TransferProperties exportTransfer =
            target.exportInit(
                toad.toArray()
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


    /**
     * Check we can copy a data node to another location.
     *
     */
    public void testCopyNodeLocation()
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
        // Add our source node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );

        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            target.importInit(
                frog.toArray()
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
        // Check the node location.
        assertEquals(
            filestores[0],
            frog.getManagerLocationIvorn()
            );

        //
        // Create our copy request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.getManagerResourceIvorn()
            );
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        request.setManagerLocationIvorn(
            filestores[1]
            );

        //
        // Create our copy.
        FileManagerProperties toad = new FileManagerProperties(
            target.copy(
                request.toArray()
                )
            ) ;

        //
        // Call our manager to initiate the export.
        TransferProperties exportTransfer =
            target.exportInit(
                toad.toArray()
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

        //
        // Check the node location.
        assertEquals(
            filestores[1],
            toad.getManagerLocationIvorn()
            );
        }

    /**
     * Check we can copy an empty node to a new location.
     *
     */
    public void testCopyEmptyLocation()
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
        // Add our source node.
        FileManagerProperties frog = new FileManagerProperties(
            target.addNode(
                home.getManagerResourceIvorn().toString(),
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Create our request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.getManagerResourceIvorn()
            );
        request.setManagerResourceIvorn(
            frog.getManagerResourceIvorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        request.setManagerLocationIvorn(
            filestores[1]
            );
        //
        // Create our copy.
        FileManagerProperties result = new FileManagerProperties(
            target.copy(
                request.toArray()
                )
            ) ;
        //
        // Check we got the right name.
        assertEquals(
            "toad",
            result.getManagerResourceName()
            );
        //
        // Check we got the right parent.
        assertEquals(
            home.getManagerResourceIvorn(),
            result.getManagerParentIvorn()
            );
        //
        // Check we got the right location.
        assertEquals(
            filestores[1],
            result.getManagerLocationIvorn()
            );
        }


//
// Check we can add something to the copy.
// Check the original is unchanged.
// Check the copy has the new data.
// ** Needs append ...
//

//
// Check we can delete the original.
// ** Needs delete ...
//

//
// Check we can copy a node to a new filestore.
// ** Needs implementing ...
//

    /**
     * Check that we can delete an empty node.
     *
     */
    public void testDeleteEmptyData()
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
        // Add a data node.
        FileManagerProperties toad = new FileManagerProperties(
            target.addNode(
                frog.getManagerResourceIvorn().toString(),
                "toad",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check we can get the node details.
        assertNotNull(
            target.getNode(
                toad.getManagerResourceIvorn().toString()
                )
            );
        //
        // Try deleting the node.
        target.delete(
            toad.getManagerResourceIvorn().toString()
            );
        //
        // Check the node isn't there any more.
        try {
            target.getNode(
                toad.getManagerResourceIvorn().toString()
                ) ;
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Check that we can delete a data node.
     * This can fail on the mock implementation because the service does not let go of the data.
     *
    public void testDeleteVaildData()
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
        // Add a data node.
        FileManagerProperties toad = new FileManagerProperties(
            target.addNode(
                frog.getManagerResourceIvorn().toString(),
                "toad",
                FileManagerProperties.DATA_NODE_TYPE
                )
            );
        //
        // Check we can get the node details.
        assertNotNull(
            target.getNode(
                toad.getManagerResourceIvorn().toString()
                )
            );
        //
        // Call our manager to initiate an import.
//
// BUG ... this was 'frog' and it didn't cause an error.
//
        TransferProperties importTransfer =
            target.importInit(
                toad.toArray()
                ) ;
        //
        // Create an output stream to the import location.
        FileStoreOutputStream importStream = new FileStoreOutputStream(
            importTransfer.getLocation()
            ) ;
        //
        // Transfer some data into the node.
        importStream.open() ;
        importStream.write(
            TEST_BYTES
            ) ;
        importStream.close() ;

        //
        // Call our manager to initiate an export.
        TransferProperties exportTransfer =
            target.exportInit(
                toad.toArray()
                ) ;

        //
        // Try deleting the node.
        target.delete(
            toad.getManagerResourceIvorn().toString()
            );
//
// This don't work on the mock implementation.
// The current mock service does not disconnect the URL handler when a node is deleted.
//
        //
        // Check the data isn't there any more.
        try {
            //
            // Get an input stream to the export location.
            FileStoreInputStream exportStream = new FileStoreInputStream(
                exportTransfer.getLocation()
                );
            //
            // Try opening the stream.
            exportStream.open();
            }
        catch (FileNotFoundException ouch)
            {
            return ;
            }
        fail("Expected FileNotFoundException") ;
        }
     */
    }

