/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/delegate/NodeDelegateTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: NodeDelegateTest.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.5  2005/03/01 23:41:14  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.4  2005/03/01 15:07:28  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.3  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.1.2.2  2005/02/25 12:33:27  nw
 *   finished transactional store
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.2.4.3  2005/02/11 17:16:03  nw
 *   knock on effect of renaming and making IvornFactory static
 *
 *   Revision 1.2.4.2  2005/02/11 14:28:05  nw
 *   refactored, split out candidate classes.
 *
 *   Revision 1.2.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.4.2.2  2005/01/19 12:55:34  dave
 *   Added sleep to date tests to avoid race condition ...
 *
 *   Revision 1.4.2.1  2005/01/15 08:25:20  dave
 *   Added file create and modify dates to manager and client API ...
 *
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.3.4.1  2005/01/07 12:17:59  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.14  2004/12/14 11:49:50  dave
 *   Added data location copy test ....
 *
 *   Revision 1.1.2.13  2004/12/13 10:07:15  dave
 *   Fixed copy tests for delegate ...
 *
 *   Revision 1.1.2.12  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.11  2004/12/08 17:54:54  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.10  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.9  2004/12/03 13:27:52  dave
 *   Core of internal move is in place ....
 *
 *   Revision 1.1.2.8  2004/11/29 18:05:07  dave
 *   Refactored methods names ....
 *   Added stubs for delete, copy and move.
 *
 *   Revision 1.1.2.7  2004/11/26 04:22:24  dave
 *   Added SOAP delegate node test ...
 *   Added node export test ..
 *
 *   Revision 1.1.2.6  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
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
package org.astrogrid.filemanager.client.delegate;

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeMetadata;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.NodeIvorn;
import org.astrogrid.filemanager.common.NodeName;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filemanager.common.NodeTypes;
import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.FileStoreOutputStream;
import org.astrogrid.filestore.common.transfer.TransferUtil;
import org.astrogrid.io.Piper;

import org.apache.axis.types.URI;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * A generic test for the FileManager delegate API.
 */
public abstract class NodeDelegateTest extends BaseTest {
    private static final NodeName TOAD = new NodeName("toad");
    private static final NodeName NEWT = new NodeName("newt");
    private static final NodeName FROG_NODE_NAME = new NodeName("frog");



    /**
     * Set up our test.
     *  
     */
    public void setUp() throws Exception {
        super.setUp();
        delegate= createDelegate();
        
    }
    
    protected abstract NodeDelegate createDelegate() throws Exception; 

    /**
     * Our target FileManager delegate
     *  
     */
    protected NodeDelegate delegate;
    /**
     * Check we created our delegate.
     *  
     */
    public void testCreateDelegate() throws Exception {
        assertNotNull(this.delegate);
    }

    /**
     * Check that we get the right exception for a null account.
     *  
     */
    public void testAddAccountNull() throws Exception {
        try {
            delegate.addAccount(null);
        } catch (RemoteException e) {
            return;
        }
        fail("Expected RemotetException");
    }

    /**
     * Check that we can create an account node.
     *  
     */
    public void testAddAccount() throws Exception {
        assertNotNull(delegate.addAccount(accountIdent));
    }

    /**
     * Check that we can't create a duplicate account.
     *  
     */
    public void testAddAccountDuplicate() throws Exception {
        assertNotNull(delegate.addAccount(accountIdent));
        try {
            delegate.addAccount(accountIdent);
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check that an account node has an ivorn.
     *  
     */
    public void testAddAccountIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Check the node ivorn.
        assertNotNull(home.getIvorn());
    }
    
    /**
     * Check that an account node has the magic attribute set.
     *  
     */
    public void testAddAccountAttribute() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Check the node ivorn.
        assertNotNull(home.getMetadata().getAttributes());
        assertNotNull(home.getMetadata().getAttributes().get(NodeMetadata.HOME_SPACE));
    }

    /**
     * Check that an account node has an ident.
     *  
     */
    public void testAddAccountIdent() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Check the node ident.
        assertNotNull(home.getMetadata().getNodeIvorn());
    }

    /**
     * Check that an account node has the right name.
     *  
     */
    public void testAddAccountName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Check the node name.
        assertNotNull(home.getName());
    }

    /**
     * Check that an account node is a container.
     *  
     */
    public void testAddAccountType() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Check the node type.
        assertTrue(home.isFolder());
        assertFalse(home.isFile());
    }

    /**
     * Check that we get the right exception for a null account.
     *  
     */
    public void testGetAccountNull() throws Exception {
        try {
            delegate.getAccount(null);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemoteException");
    }

    /**
     * Check that we get the right exception for an unknown account.
     *  
     */
    public void testGetAccountUnknown() throws Exception {
        try {
            delegate.getAccount(unknownIvorn);
        } catch (NodeNotFoundFault ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }

    /**
     * Check that we get the right exception for a null parent.
     *  
     */
    public void testAddContainerNullParent() throws Exception {
        try {
            delegate.addNode(null, FROG_NODE_NAME,
                    NodeTypes.FOLDER);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemoteException");
    }

    /**
     * Check that we get the right exception for a null name.
     *  
     */
    public void testAddContainerNullName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Try adding a node with no name.
        try {
            delegate.addNode(home.getMetadata().getNodeIvorn(), null, NodeTypes.FOLDER);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemoteException");
    }

    /**
     * Check that we can create a container node.
     *  
     */
    public void testAddContainer() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        assertNotNull(delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER));
    }

    /**
     * Check that we we get the right Exception for a duplicate container.
     *  
     */
    public void testAddDuplicateContainer() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Try adding the same container twice.
        try {
            delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                    NodeTypes.FOLDER);
            delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                    NodeTypes.FOLDER);
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check that the container has a valid node  ivorn.
     *  
     */
    public void testAddContainerNodeIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check the node ivorn.
        assertNotNull(node.getMetadata().getNodeIvorn());
        assertFalse(node.getIvorn().toString().equals(node.getMetadata().getNodeIvorn().toString()));
        assertSame(node,delegate.getNode(node.getMetadata().getNodeIvorn()));

    }
    
    /**
     * Check that the container has a valid ivorn.
     *  
     */
    public void testAddContainerIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check the node ivorn.
        assertNotNull(node.getIvorn());
    }    

    /**
     * Check that the container has the right name.
     *  
     */
    public void testAddContainerName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check the node name.
        assertEquals(FROG_NODE_NAME.toString(), node.getName());
    }

    /**
     * Check that the container has the right type.
     *  
     */
    public void testAddContainerType() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check the node type.
        assertTrue(node.isFolder());
        assertFalse(node.isFile());
    }

    /**
     * Check that we get the right exception for a null parent.
     *  
     */
    public void testAddFileNullParent() throws Exception {
        try {
            delegate.addNode(null, FROG_NODE_NAME, NodeTypes.FILE);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemoteException");
    }

    /**
     * Check that we get the right exception for a null name.
     *  
     */
    public void testAddFileNullName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Try adding a file with no name.
        try {
            delegate.addNode(home.getMetadata().getNodeIvorn(), null, NodeTypes.FILE);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemotetException");
    }

    /**
     * Check that we can create a file node.
     *  
     */
    public void testAddFile() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        assertNotNull(delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE));
    }

    /**
     * Check that we we get the right Exception for a duplicate file.
     *  
     */
    public void testAddDuplicateFile() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Try adding the same file twice.
        try {
            delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME, NodeTypes.FILE);
            delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME, NodeTypes.FILE);
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check that the file has a valid ivorn.
     *  
     */
    public void testAddFileIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check the node ivorn.
        assertNotNull(node.getIvorn());
    }
    
    /**
     * Check that the file has a valid node ivorn.
     *  
     */
    public void testAddFileNodeIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check the node ivorn.
        assertNotNull(node.getMetadata().getNodeIvorn());
        assertFalse(node.getIvorn().toString().equals(node.getMetadata().getNodeIvorn().toString()));        
        assertSame(node,delegate.getNode(node.getMetadata().getNodeIvorn()));
    }


    /**
     * Check that the file has the right name.
     *  
     */
    public void testAddFileName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check the node name.
        assertEquals(FROG_NODE_NAME.toString(), node.getName());
    }

    /**
     * Check that the file has the right type.
     *  
     */
    public void testAddFileType() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check the node type.
        assertFalse(node.isFolder());
        assertTrue(node.isFile());
    }

    /**
     * Check that the file has the right location.
     *  
     */
    public void testAddFileLocation() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode node = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check the node location.
        assertNotNull(node.getMetadata().getContentLocation());
        assertEquals(filestores[0].toString(), node.getMetadata().getContentLocation().toString());
    }

    /**
     * Check we can get the right Exception for a null ident.
     *  
     */
    public void testGetNodeNull() throws Exception {
        //
        // Try requesting a node with no ivorn.
        try {
            delegate.getNode(null);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemoteException");
    }

    /**
     * Check we get the right Exception for an unknown node.
     *  
     */
    public void testGetNodeUnknown() throws Exception {
        //
        // Try requesting an unknown node.
        try {
            delegate.getNode(new NodeIvorn("ivo://unknown"));
        } catch (NodeNotFoundFault ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }


    /**
     * Check we can get a container node.
     *  
     */
    public void testGetContainerNode() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check we can get the node.
        assertNotNull(delegate.getNode(frog.getMetadata().getNodeIvorn()));
    }

    /**
     * Check the node has the right node ivorn.
     *  
     */
    public void testGetContainerNodeIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node ivorn.
        assertEquals(frog.getMetadata().getNodeIvorn(), toad.getMetadata().getNodeIvorn());
  
    }
    
    /**
     * Check the node has the right ivorn.
     *  
     */
    public void testGetContainerIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node ivorn.
        assertEquals(frog.getIvorn(), toad.getIvorn());
    }

    /**
     * Check the node has the right name.
     *  
     */
    public void testGetContainerNodeName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node name.
        assertEquals(FROG_NODE_NAME.toString(), toad.getName());
    }

    /**
     * Check the node has the right type.
     *  
     */
    public void testGetContainerNodeType() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a container node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FOLDER);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node type.
        assertTrue(toad.isFolder());
        assertFalse(toad.isFile());
    }

    /**
     * Check we can get a file node.
     *  
     */
    public void testGetFileNode() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check we can get the node.
        assertNotNull(delegate.getNode(frog.getMetadata().getNodeIvorn()));
    }

    /**
     * Check the node has the right node ivorn.
     *  
     */
    public void testGetFileNodeIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node ivorn.
        assertEquals(frog.getMetadata().getNodeIvorn(), toad.getMetadata().getNodeIvorn());
    }


    /**
     * Check the node has the right ivorn.
     *  
     */
    public void testGetFileIvorn() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node ivorn.
        assertEquals(frog.getIvorn(), toad.getIvorn());
    }

    /**
     * Check the node has the right name.
     *  
     */
    public void testGetFileNodeName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node name.
        assertEquals(FROG_NODE_NAME.toString(), toad.getName());
    }

    /**
     * Check the node has the right type.
     *  
     */
    public void testGetFileNodeType() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check we can get the node.
        FileManagerNode toad = delegate.getNode(frog.getMetadata().getNodeIvorn());
        //
        // Check the node type.
        assertFalse(toad.isFolder());
        assertTrue(toad.isFile());
    }



    /**
     * Check we get the right Exception for a null ivorn.
     *  
     */
    public void testImportInitNullIvorn() throws Exception {

        try {
            delegate.writeContent(null);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }

    /**
     * Check we get the right Exception for an unknow ivorn.
     *  
     */
    public void testImportInitUnknownIvorn() throws Exception {

        try {
            delegate.writeContent(new NodeIvorn("ivo://wibble"));
        } catch (NodeNotFoundFault ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }

    /**
     * Check we can initiate an import into a node.
     *  
     */
    public void testImportInitTransferURL() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode newt = delegate.addNode(home.getMetadata().getNodeIvorn(), NEWT,
                NodeTypes.FILE);

        URL importTransfer = new URL(delegate.writeContent(newt.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Check we got a response.
        assertNotNull(importTransfer);
    }


    /**
     * Check we can write data to the import URL.
     *  
     */
    public void testImportInitWrite() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode newt = delegate.addNode(home.getMetadata().getNodeIvorn(), NEWT,
                NodeTypes.FILE);

        // Call our manager to initiate the import.
        URL url = new URL(delegate.writeContent(newt.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an output stream to the target.
        FileStoreOutputStream importStream = new FileStoreOutputStream(url);
        //
        // Transfer our data.
        importStream.open();
        importStream.write(TEST_BYTES);
        importStream.close();
    }

    /**
     * Check we get the right Exception for null properties.
     *  
     */
    public void testExportInitNullProperties() throws Exception {
        try {
            delegate.writeContent(null);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemoteException");
    }

    /**
     * Check we get the right Exception for an unknow ivorn.
     *  
     */
    public void testExportInitUnknownIvorn() throws Exception {

        try {
            delegate.writeContent(new NodeIvorn("ivo://wibble"));
        } catch (NodeNotFoundFault ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }

    /**
     * Check we can read and write data to a node.
     *  
     */
    public void testExportInitUnknownRead() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a file node.
        FileManagerNode newt = delegate.addNode(home.getMetadata().getNodeIvorn(), NEWT,
                NodeTypes.FILE);

        //
        // Call our manager to initiate the import.
        URL url = new URL(delegate.writeContent(newt.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an output stream to the target.
        FileStoreOutputStream importStream = new FileStoreOutputStream(url);
        //
        // Transfer our data.
        importStream.open();
        importStream.write(TEST_BYTES);
        importStream.close();

        //
        // Call our manager to initiate the export.
        URL exportTransfer = new URL(delegate.readContent(newt.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an input stream from the target.
        FileStoreInputStream exportStream = new FileStoreInputStream(exportTransfer);

        //
        // Create our buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the export location.
        exportStream.open();
        new TransferUtil(exportStream, buffer).transfer();
        exportStream.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }

    /**
     * Check we can refresh the properties after an import.
     *  
     */
    public void testImportRefreshSize() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a data node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //

        assertEquals(new Long(0), frog.getMetadata().getSize());
        URL url = new URL(delegate.writeContent(frog.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an output stream to the target.
        FileStoreOutputStream importStream = new FileStoreOutputStream(url);
        //
        // Transfer our data.
        importStream.open();
        importStream.write(TEST_BYTES);
        importStream.close();
        // notify that a write has happended.
        delegate.transferCompleted(frog.getMetadata().getNodeIvorn());
        assertEquals(new Long( TEST_BYTES.length), frog.getMetadata().getSize());
    }

 
    /**
     * Test we can change the node name.
     *  
     */
    public void testMoveName() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a data node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);

        // Check the node name.
        assertEquals(FROG_NODE_NAME.toString(), frog.getName());
        //
        // Execute the move.
        delegate.move(frog.getMetadata().getNodeIvorn(),null,TOAD,null);
        // get the node again.
        FileManagerNode frog1 = delegate.getNode(frog.getMetadata().getNodeIvorn());
        // Check the node name.
        assertEquals(TOAD.toString(), frog1.getName());
    }

    /**
     * Check we get the right Exception for a duplicate name.
     *  
     */
    public void testMoveNameDuplicate() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a data node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        FileManagerNode toad = delegate.addNode(home.getMetadata().getNodeIvorn(), TOAD,
                NodeTypes.FILE);        

        // Check the node name.
        assertEquals(FROG_NODE_NAME.toString(), frog.getName());
        //
        // Execute the move.
        try {
        delegate.move(frog.getMetadata().getNodeIvorn(),null,TOAD,null);
    } catch (DuplicateNodeFault ouch) {
        return;
    }
    fail("Expected DuplicateNodeFault");        
    }

    /**
     * Test we can change the node parent.
     *  
     */
    public void testMoveParent() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our data node(s).
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Add our container node.
        FileManagerNode newt = delegate.addNode(home.getMetadata().getNodeIvorn(), NEWT,
                NodeTypes.FOLDER);

        // Execute the move.
        delegate.move(frog.getMetadata().getNodeIvorn(),newt.getMetadata().getNodeIvorn(),null,null);
        // get the node again.
        FileManagerNode frog1 = delegate.getNode(frog.getMetadata().getNodeIvorn());        
        // Check the node name.
        assertEquals(FROG_NODE_NAME.toString(), frog.getName());
        //
        // Check the node parent.
        assertEquals(newt, frog1.getParentNode());               
    }

    /**
     * Test we can change the node name and parent.
     *  
     */
    public void testMoveNameParent() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our data node(s).
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Add our container node.
        FileManagerNode newt = delegate.addNode(home.getMetadata().getNodeIvorn(), NEWT,
                NodeTypes.FOLDER);

        // Execute the move.
        delegate.move(frog.getMetadata().getNodeIvorn(),newt.getMetadata().getNodeIvorn(),TOAD,null);
        // get the node again.
        FileManagerNode frog1 = delegate.getNode(frog.getMetadata().getNodeIvorn());        
        // Check the node name.
        assertEquals(TOAD.toString(), frog.getName());
        //
        // Check the node parent.
        assertEquals(newt, frog1.getParentNode());            
    }

    /**
     * Check we get the right Exception for a duplicate name.
     *  
     */
    public void testMoveParentDuplicate() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our container node.
        FileManagerNode newt = delegate.addNode(home.getMetadata().getNodeIvorn(), NEWT,
                NodeTypes.FOLDER);
        //
        // Add our data node(s).
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        FileManagerNode toad = delegate.addNode(newt.getMetadata().getNodeIvorn(),TOAD,
                NodeTypes.FILE);

        try {
            delegate.move(frog.getMetadata().getNodeIvorn(),newt.getMetadata().getNodeIvorn(),TOAD,null);
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Test we can change the data location of an empty node.
     *  
     */
    public void testMoveLocationEmpty() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a data node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check the data location.
        assertNotNull(frog.getMetadata().getContentLocation());
        assertEquals(filestores[0].toString(), frog.getMetadata().getContentLocation().toString());

        // Execute the move.
        delegate.move(frog.getMetadata().getNodeIvorn(),null,null,new URI(filestores[1].toString()));
        //
        FileManagerNode frog1 = delegate.getNode(frog.getMetadata().getNodeIvorn());     
        // Check the data location.
        assertNotNull(frog1.getMetadata().getContentLocation());
        assertEquals(filestores[1].toString(), frog.getMetadata().getContentLocation().toString());
    }

    /**
     * Test we can change the data location of a node with data.
     *  
     */
    public void testMoveLocationData() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add a data node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Check the data location.
        assertNotNull(frog.getMetadata().getContentLocation());
        assertEquals(filestores[0].toString(), frog.getMetadata().getContentLocation().toString());

        URL url = new URL (delegate.writeContent(frog.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an output stream to the target.
        FileStoreOutputStream importStream = new FileStoreOutputStream(url);
        //
        // Transfer our data.
        importStream.open();
        importStream.write(TEST_BYTES);
        importStream.close();


        // Execute the move.
        delegate.move(frog.getMetadata().getNodeIvorn(),null,null,new URI(filestores[1].toString()));
        //
        FileManagerNode frog1 = delegate.getNode(frog.getMetadata().getNodeIvorn());     
        // Check the data location.
        assertNotNull(frog1.getMetadata().getContentLocation());
        assertEquals(filestores[1].toString(), frog.getMetadata().getContentLocation().toString());
        //

        // Call our manager to initiate the export.
        URL url1 = new URL(delegate.readContent(frog1.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an input stream from the target.
        FileStoreInputStream exportStream = new FileStoreInputStream(url1);
        //
        // Create our buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the export location.
        exportStream.open();
        Piper.pipe(exportStream,buffer);
        exportStream.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }




    /**
     * Check we get the right Exception for an unknown resource.
     *  
     */
    public void testCopyUnknownResource() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        try {
            delegate.copy(new NodeIvorn("ivo://unknown#unknown"),home.getMetadata().getNodeIvorn(),null,null);
        } catch (NodeNotFoundFault ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }

    /**
     * Check we get the right Exception for the same resource.
     *  
     */
    public void testCopyDuplicateSame() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our data node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        try {
            delegate.copy(frog.getMetadata().getNodeIvorn(),home.getMetadata().getNodeIvorn(),FROG_NODE_NAME,null);
        } catch (RemoteException ouch) {
            return;
        }
        fail("Expected RemoteException");
    }

    /**
     * Check we get the right Exception for a duplicate node.
     *  
     */
    public void testCopyDuplicateNode() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our source node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Add our target container.
        FileManagerNode toad = delegate.addNode(home.getMetadata().getNodeIvorn(), TOAD,
                NodeTypes.FOLDER);
        //
        // Add our duplicate node.
        FileManagerNode newt = delegate.addNode(toad.getMetadata().getNodeIvorn(), NEWT,
                NodeTypes.FILE);
        try {
            delegate.copy(frog.getMetadata().getNodeIvorn(),toad.getMetadata().getNodeIvorn(),NEWT,null);
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check we can copy an empty node.
     *  
     */
    public void testCopyEmptyNode() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our source node.
        NodeIvorn homeNodeIvorn = home.getMetadata().getNodeIvorn();
        FileManagerNode frog = delegate.addNode(homeNodeIvorn, FROG_NODE_NAME,
                NodeTypes.FILE);


        FileManagerNode toad = delegate.copy(frog.getMetadata().getNodeIvorn(),homeNodeIvorn,TOAD,null);
        //
        // Check we got the right name.
        assertEquals(TOAD.toString(), toad.getName());
        //
        // Check we got the right parent.
        assertEquals(homeNodeIvorn, toad.getParentNode().getMetadata().getNodeIvorn());
    }

    /**
     * Check we can copy a node with data.
     *  
     */
    public void testCopyNodeData() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our source node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        // Call our manager to initiate the import.
        URL url = new URL(delegate.writeContent(frog.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an output stream to the target.
        FileStoreOutputStream importStream = new FileStoreOutputStream(url);
        //
        // Transfer our data.
        importStream.open();
        importStream.write(TEST_BYTES);
        importStream.close();



        FileManagerNode toad = delegate.copy(frog.getMetadata().getNodeIvorn(),home.getMetadata().getNodeIvorn(),TOAD,null);

        // Call our manager to initiate the export.
        URL url1 = new URL(delegate.readContent(frog.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an input stream from the target.
        FileStoreInputStream exportStream = new FileStoreInputStream(url1);
        //
        // Create our buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the export location.
        exportStream.open();
        Piper.pipe(exportStream,buffer);
        exportStream.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }

    /**
     * Check we can copy a data node to another location.
     *  
     */
    public void testCopyNodeLocation() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our source node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        // Call our manager to initiate the import.
        URL url = new URL(delegate.writeContent(frog.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an output stream to the target.
        FileStoreOutputStream importStream = new FileStoreOutputStream(url);
        //
        // Transfer our data.
        importStream.open();
        importStream.write(TEST_BYTES);
        importStream.close();

        //
        // Check the node location.
        assertEquals(filestores[0].toString(), frog.getMetadata().getContentLocation().toString());


        FileManagerNode toad = delegate.copy(frog.getMetadata().getNodeIvorn(),home.getMetadata().getNodeIvorn(),TOAD,new URI(filestores[1].toString()));

        //
        // Check the node location.
        assertEquals(filestores[1].toString(), toad.getMetadata().getContentLocation().toString());


        // Call our manager to initiate the export.
        URL url1 = new URL(delegate.readContent(toad.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an input stream from the target.
        FileStoreInputStream exportStream = new FileStoreInputStream(url1);
        //
        // Create our buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the export location.
        exportStream.open();
        Piper.pipe(exportStream,buffer);
        exportStream.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }

    /**
     * Check that the file create and modify dates are set.
     *  
     */
    public void testImportDates() throws Exception {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(accountIdent);
        //
        // Add our source node.
        FileManagerNode frog = delegate.addNode(home.getMetadata().getNodeIvorn(), FROG_NODE_NAME,
                NodeTypes.FILE);
        //
        // Get the initial file dates.
        Calendar initialFileCreateDate = frog.getMetadata().getCreateDate();
        Calendar initialFileModifyDate = frog.getMetadata().getModifyDate();
        //
        // Pause for a bit ....
        Thread.sleep(1000);
 
        URL importTransfer = new URL(delegate.writeContent(frog.getMetadata().getNodeIvorn()).getUri().toString());
        //
        // Create an output stream to the target.
        FileStoreOutputStream importStream = new FileStoreOutputStream(importTransfer);
        //
        // Transfer our data.
        importStream.open();
        importStream.write(TEST_BYTES);
        importStream.close();
        //
        // Refresh the node properties.
        frog.transferCompleted();
        //
        // Get the updated dates.
        Calendar updatedFileCreateDate = frog.getMetadata().getCreateDate();
        Calendar updatedFileModifyDate = frog.getMetadata().getModifyDate();
        //
        // Check the first set of dates are notnull.
        assertNotNull(initialFileCreateDate);
        assertNotNull(initialFileModifyDate);
        //
        // Check the final dates are not null.
        assertNotNull(updatedFileCreateDate);
        assertNotNull(updatedFileModifyDate);
        //
        // Check the modified date is after the create date.
        assertTrue(updatedFileModifyDate.after(updatedFileCreateDate));
    }

}

