/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/delegate/Attic/FileManagerDelegateTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegateTest.java,v $
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
package org.astrogrid.filemanager.client.delegate ;

import java.io.ByteArrayOutputStream;

import java.util.Date ;
import java.util.List ;
import java.util.Iterator ;

import org.astrogrid.store.Ivorn ;

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
import org.astrogrid.filemanager.common.exception.FileManagerPropertiesException;

import org.astrogrid.filemanager.client.FileManagerNode;

/**
 * A generic test for the FileManager delegate API.
 *
 */
public class FileManagerDelegateTest
    extends BaseTest
    {
    /**
     * A set of ivorn identifiers for our target file stores.
     *
     */
    protected Ivorn[] filestores ;

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
            home.ivorn()
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
            home.ivorn()
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
            home.name()
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
            delegate.add(
                null,
                "frog",
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
            delegate.add(
                home,
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Check the node ivorn.
        assertNotNull(
            node.ivorn()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Check the node name.
        assertEquals(
            "frog",
            node.name()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
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
            delegate.add(
                null,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
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
            delegate.add(
                home,
                null,
                FileManagerProperties.DATA_NODE_TYPE
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check the node ivorn.
        assertNotNull(
            node.ivorn()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check the node name.
        assertEquals(
            "frog",
            node.name()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
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
     * Check that the file has the right location.
     *
     */
    public void testAddFileLocation()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check the node location.
        assertNotNull(
            node.location()
            );
        assertEquals(
            filestores[0].toString(),
            node.location().toString()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        assertNotNull(
            delegate.getNode(
                frog.ivorn()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        FileManagerNode toad =
            delegate.getNode(
                frog.ivorn()
                ) ;
        //
        // Check the node ivorn.
        assertEquals(
            frog.ivorn().toString(),
            toad.ivorn().toString()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        FileManagerNode toad =
            delegate.getNode(
                frog.ivorn()
                ) ;
        //
        // Check the node name.
        assertEquals(
            "frog",
            toad.name()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        FileManagerNode toad =
            delegate.getNode(
                frog.ivorn()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        assertNotNull(
            delegate.getNode(
                frog.ivorn()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        FileManagerNode toad =
            delegate.getNode(
                frog.ivorn()
                ) ;
        //
        // Check the node ivorn.
        assertEquals(
            frog.ivorn().toString(),
            toad.ivorn().toString()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        FileManagerNode toad =
            delegate.getNode(
                frog.ivorn()
                ) ;
        //
        // Check the node name.
        assertEquals(
            "frog",
            toad.name()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check we can get the node.
        FileManagerNode toad =
            delegate.getNode(
                frog.ivorn()
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
    public void testGetChildNullIvorn()
        throws Exception
        {
        try {
            delegate.getChild(
                (Ivorn) null,
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
     * Check that we get the right Exception for a null parent.
     *
     */
    public void testGetChildNullNode()
        throws Exception
        {
        try {
            delegate.getChild(
                (FileManagerNode) null,
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        try {
            delegate.getChild(
                home.ivorn(),
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        try {
            delegate.getChild(
                home.ivorn(),
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check we can find the file.
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Add a container node.
        FileManagerNode toad =
            delegate.add(
                frog,
                "toad",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Add a file node.
        FileManagerNode newt =
            delegate.add(
                toad,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Check we can find the file(s).
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "frog"
                )
            ) ;
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "frog/toad"
                )
            ) ;
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "frog/toad/newt"
                )
            ) ;

        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "/frog"
                )
            ) ;
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "/frog/toad"
                )
            ) ;
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "/frog/toad/newt"
                )
            ) ;

        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "//frog"
                )
            ) ;
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "//frog//toad"
                )
            ) ;
        assertNotNull(
            delegate.getChild(
                home.ivorn(),
                "//frog//toad//newt"
                )
            ) ;
        }

    /**
     * Check we can get a nested node by Ivorn.
     *
     */
    public void testGetNestedIvorn()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Add a container node.
        FileManagerNode toad =
            delegate.add(
                frog,
                "toad",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Add a file node.
        FileManagerNode newt =
            delegate.add(
                toad,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our ivorn factory.
        FileManagerIvornFactory factory = new FileManagerIvornFactory();
        //
        // Check we can find the file(s).
        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "frog"
                    )
                )
            ) ;
        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "frog/toad"
                    )
                )
            ) ;
        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "frog/toad/newt"
                    )
                )
            ) ;

        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "/frog"
                    )
                )
            ) ;
        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "/frog/toad"
                    )
                )
            ) ;
        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "/frog/toad/newt"
                    )
                )
            ) ;

        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "//frog"
                    )
                )
            ) ;
        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "//frog//toad"
                    )
                )
            ) ;
        assertNotNull(
            delegate.getNode(
                factory.ivorn(
                    home.ivorn(),
                    "//frog//toad//newt"
                    )
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
        List list = delegate.getChildren(
            home.ivorn()
            ) ;
        //
        // Check the list is empty.
        assertEquals(
            0,
            list.size()
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
            delegate.add(
                home,
                "frog",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Add a container node.
        FileManagerNode toad =
            delegate.add(
                home,
                "toad",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Add a file node.
        FileManagerNode newt =
            delegate.add(
                home,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Get the list of child nodes.
        List list = delegate.getChildren(
            home.ivorn()
            ) ;
        //
        // Check the list contains three nodes.
        assertEquals(
            3,
            list.size()
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
            delegate.add(
                home,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Get the (empty) list of child nodes.
        List list = delegate.getChildren(
            newt.ivorn()
            ) ;
        //
        // Check the list is empty.
        assertEquals(
            0,
            list.size()
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
            delegate.add(
                home,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        request.setManagerResourceIvorn(
            newt.ivorn()
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
     * Check an import transfer has a URL.
     *
     */
    public void testImportInitURL()
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
            delegate.add(
                home,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        request.setManagerResourceIvorn(
            newt.ivorn()
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
     * Check we can write data to the import URL.
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
            delegate.add(
                home,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        request.setManagerResourceIvorn(
            newt.ivorn()
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
            delegate.add(
                home,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        request.setManagerResourceIvorn(
            newt.ivorn()
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
        // Create an input stream from the target.
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

    /**
     * Check we can refresh the properties after an import. 
     *
     */
    public void testImportRefreshSize()
        throws Exception
        {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add a data node.
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Update the node properties.
        FileManagerProperties toad = 
            delegate.refresh(
                frog
                );
        //
        // Check the data size.
        assertEquals(
            0,
            toad.getContentSize()
            );
        //
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        request.setManagerResourceIvorn(
            frog.ivorn()
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
        // Refresh the node properties.
        FileManagerProperties newt = 
            delegate.refresh(
                frog
                );
        //
        // Check the data size.
        assertEquals(
            TEST_BYTES.length,
            newt.getContentSize()
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
            delegate.move(
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
     * Test we can change the node name.
     *
     */
    public void testMoveName()
        throws Exception
        {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add a data node.
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Check the node name.
        assertEquals(
            "frog",
            frog.name()
            );
        //
        // Execute the move.
        FileManagerProperties result =
            delegate.move(
                request
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our data node(s).
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        FileManagerNode toad = 
            delegate.add(
                home,
                "toad",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Execute the move.
        try {
            delegate.move(
                request
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our data node(s).
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Add our container node.
        FileManagerNode newt = 
            delegate.add(
                home,
                "newt",
                FileManagerProperties.CONTAINER_NODE_TYPE
                );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerParentIvorn(
            newt.ivorn()
            );
        //
        // Execute the move.
        FileManagerProperties result = 
            delegate.move(
                request
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
            newt.ivorn().toString(),
            result.getManagerParentIvorn().toString()
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our data node(s).
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Add our container node.
        FileManagerNode newt = 
            delegate.add(
                home,
                "newt",
                FileManagerProperties.CONTAINER_NODE_TYPE
                );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        request.setManagerParentIvorn(
            newt.ivorn()
            );
        //
        // Execute the move.
        FileManagerProperties result = 
            delegate.move(
                request
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
            newt.ivorn().toString(),
            result.getManagerParentIvorn().toString()
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our container node.
        FileManagerNode newt = 
            delegate.add(
                home,
                "newt",
                FileManagerProperties.CONTAINER_NODE_TYPE
                );
        //
        // Add our data node(s).
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        FileManagerNode toad = 
            delegate.add(
                newt,
                "toad",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Create our move properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerParentIvorn(
            newt.ivorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Execute the move.
        try {
            delegate.move(
                request
                );
            }
        catch (DuplicateNodeException ouch)
            {
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Test we can change the data location of an empty node.
     *
     */
    public void testMoveLocationEmpty()
        throws Exception
        {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add a data node.
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Check the data location.
        assertNotNull(
            frog.location()
            );
        assertEquals(
            filestores[0].toString(),
            frog.location().toString()
            );
        //
        // Create our request properties.
        FileManagerProperties request = new FileManagerProperties();
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerLocationIvorn(
            filestores[1]
            );
        //
        // Execute the move.
        FileManagerProperties result =
            delegate.move(
                request
                );
        //
        // Refresh the node properties.
        frog.refresh();
        //
        // Check the data location.
        assertNotNull(
            frog.location()
            );
        assertEquals(
            filestores[1].toString(),
            frog.location().toString()
            );
        }

    /**
     * Test we can change the data location of a node with data.
     *
     */
    public void testMoveLocationData()
        throws Exception
        {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add a data node.
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Check the data location.
        assertNotNull(
            frog.location()
            );
        assertEquals(
            filestores[0].toString(),
            frog.location().toString()
            );

        //
        // Create our import request properties.
        FileManagerProperties importRequest = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        importRequest.setManagerResourceIvorn(
            frog.ivorn()
            );
        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            delegate.importInit(
                importRequest
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
        // Create our transfer request properties.
        FileManagerProperties moveRequest = new FileManagerProperties();
        moveRequest.setManagerResourceIvorn(
            frog.ivorn()
            );
        moveRequest.setManagerLocationIvorn(
            filestores[1]
            );
        //
        // Execute the move.
        FileManagerProperties result =
            delegate.move(
                moveRequest
                );

        //
        // Create our export request properties.
        FileManagerProperties exportRequest = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        exportRequest.setManagerResourceIvorn(
            frog.ivorn()
            );
        //
        // Call our manager to initiate the export.
        TransferProperties exportTransfer =
            delegate.exportInit(
                exportRequest
                ) ;
        //
        // Create an input stream from the target.
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

    /**
     * Check we get the right list of child nodes.
     *
     */
    public void testGetChildrenIvorns()
        throws Exception
        {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add some data nodes.
        FileManagerNode frog = 
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                );
        FileManagerNode toad = 
            delegate.add(
                home,
                "toad",
                FileManagerProperties.DATA_NODE_TYPE
                );
        FileManagerNode newt = 
            delegate.add(
                home,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                );
        //
        // Get the list of child nodes.
        List list = delegate.getChildren(
            home.ivorn()
            ) ;
        //
        // Check the list contains the right nodes.
        int     totalFound = 0 ;
        boolean frogFound  = false ;
        boolean toadFound  = false ;
        boolean newtFound  = false ;
        Iterator iter = list.iterator();
        while(iter.hasNext())
            {
            Ivorn ivorn = (Ivorn) iter.next();
            if (ivorn.toString().equals(frog.ivorn().toString()))
                {
                totalFound++;
                frogFound = true ;
                }
            if (ivorn.toString().equals(toad.ivorn().toString()))
                {
                totalFound++;
                toadFound = true ;
                }
            if (ivorn.toString().equals(newt.ivorn().toString()))
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

    /**
     * Check we get the right Exception for a null request.
     *
     */
    public void testCopyNullRequest()
        throws Exception
        {
        try {
            delegate.copy(
                null
                );
            }
        catch (IllegalArgumentException ouch)
            {
            return ;
            }
        fail("Expected IllegalArgumentException") ;
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Create our request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.ivorn()
            );
        request.setManagerResourceName(
            "frog"
            );
        try {
            delegate.copy(
                request
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Create our request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.ivorn()
            );
        request.setManagerResourceIvorn(
            "ivo://unknown#unknown"
            );
        request.setManagerResourceName(
            "toad"
            );
        try {
            delegate.copy(
                request
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our data node.
        FileManagerNode frog =
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our copy request properties.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerParentIvorn(
            home.ivorn()
            );
        request.setManagerResourceName(
            "frog"
            );
        try {
            delegate.copy(
                request
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our source node.
        FileManagerNode frog =
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Add our target container.
        FileManagerNode toad =
            delegate.add(
                home,
                "toad",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
        //
        // Add our duplicate node.
        FileManagerNode newt =
            delegate.add(
                toad,
                "newt",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our copy request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerParentIvorn(
            toad.ivorn()
            );
        request.setManagerResourceName(
            "newt"
            );
        try {
            delegate.copy(
                request
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our source node.
        FileManagerNode frog =
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.ivorn()
            );
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerResourceName(
            "toad"
            );

        //
        // Create our copy.
        FileManagerNode toad =
            delegate.copy(
                request
                );
        //
        // Check we got the right name.
        assertEquals(
            "toad",
            toad.name()
            );
        //
        // Check we got the right parent.
        assertEquals(
            home.ivorn(),
            toad.parent().ivorn()
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
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our source node.
        FileManagerNode frog =
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
        //
        // Create our import request properties.
        FileManagerProperties importRequest = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        importRequest.setManagerResourceIvorn(
            frog.ivorn()
            );
        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            delegate.importInit(
                importRequest
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
        // Create our copy request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.ivorn()
            );
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        //
        // Create our copy.
        FileManagerNode toad =
            delegate.copy(
                request
                );

        //
        // Create our export request properties.
        FileManagerProperties exportRequest = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        exportRequest.setManagerResourceIvorn(
            toad.ivorn()
            );
        //
        // Call our manager to initiate the export.
        TransferProperties exportTransfer =
            delegate.exportInit(
                exportRequest
                ) ;
        //
        // Create an input stream from the target.
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

    /**
     * Check we can copy a data node to another location.
     *
     */
    public void testCopyNodeLocation()
        throws Exception
        {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our source node.
        FileManagerNode frog =
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;

        //
        // Create our import request properties.
        FileManagerProperties importRequest = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        importRequest.setManagerResourceIvorn(
            frog.ivorn()
            );
        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            delegate.importInit(
                importRequest
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
        // Check the node location.
        assertEquals(
            filestores[0],
            frog.location()
            );

        //
        // Create our copy request.
        FileManagerProperties request = new FileManagerProperties() ;
        request.setManagerParentIvorn(
            home.ivorn()
            );
        request.setManagerResourceIvorn(
            frog.ivorn()
            );
        request.setManagerResourceName(
            "toad"
            );
        request.setManagerLocationIvorn(
            filestores[1]
            );

        //
        // Create our copy.
System.out.println("");
System.out.println("------------------");
System.out.println("");
        FileManagerNode toad =
            delegate.copy(
                request
                );
System.out.println("");
System.out.println("------------------");
System.out.println("");

        //
        // Create our export request properties.
        FileManagerProperties exportRequest = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        exportRequest.setManagerResourceIvorn(
            toad.ivorn()
            );
        //
        // Call our manager to initiate the export.
        TransferProperties exportTransfer =
            delegate.exportInit(
                exportRequest
                ) ;
        //
        // Create an input stream from the target.
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

        //
        // Check the node location.
        assertEquals(
            filestores[1],
            toad.location()
            );
        }

    /**
     * Check that the file create and modify dates are set.
     *
     */
    public void testImportDates()
        throws Exception
        {
        //
        // Create the account home.
        FileManagerNode home = delegate.addAccount(
            accountIvorn
            ) ;
        //
        // Add our source node.
        FileManagerNode frog =
            delegate.add(
                home,
                "frog",
                FileManagerProperties.DATA_NODE_TYPE
                ) ;
		//
		// Get the initial file dates.
		Date initialFileCreateDate = frog.getFileCreateDate();
		Date initialFileModifyDate = frog.getFileModifyDate();
		//
		// Pause for a bit ....
		Thread.sleep(1000);
        //
        // Create our import request properties.
        FileManagerProperties importRequest = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        importRequest.setManagerResourceIvorn(
            frog.ivorn()
            );
        //
        // Call our manager to initiate the import.
        TransferProperties importTransfer =
            delegate.importInit(
                importRequest
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
		frog.refresh();
		//
		// Get the updated dates.
		Date updatedFileCreateDate = frog.getFileCreateDate();
		Date updatedFileModifyDate = frog.getFileModifyDate();
		//
		// Check the first set of dates are null.
		assertNull(
			initialFileCreateDate
			);
		assertNull(
			initialFileModifyDate
			);
		//
		// Check the final dates are not null.
		assertNotNull(
			updatedFileCreateDate
			);
		assertNotNull(
			updatedFileModifyDate
			);
		//
		// Check the modified date is after the create date.
		assertTrue(
			updatedFileModifyDate.after(
				updatedFileCreateDate
				)
			);
        }

    }


