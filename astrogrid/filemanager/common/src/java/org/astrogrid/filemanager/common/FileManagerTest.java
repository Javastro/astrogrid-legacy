/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/10 14:17:20 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 *
 */
package org.astrogrid.filemanager.common ;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL ;

import java.util.Date ;

import java.io.InputStream ;
import java.io.OutputStream ;
import java.io.ByteArrayOutputStream ;
import java.io.FileNotFoundException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.FileStoreInputStream ;
import org.astrogrid.filestore.common.FileStoreOutputStream ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;
import org.astrogrid.filestore.common.transfer.UrlGetTransfer ;

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
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(FileManagerTest.class);

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
        logger.info("testTargetNotNull() - start");

        assertNotNull(
            target
            ) ;

        logger.info("testTargetNotNull() - end");
        }

    /**
     * Check that we get the right exception for a null account name.
     *
     */
    public void testAddAccountNull()
        throws Exception
        {
        logger.info("testAddAccountNull() - start");

        try {
            target.addAccount(
                null
                ) ;
            }
        catch (IllegalArgumentException ouch)
            {
            logger.info("testAddAccountNull() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testAddAccountNull() - end");
        }

    /**
     * Check that we can create an account node.
     *
     */
    public void testAddAccount()
        throws Exception
        {
        logger.info("testAddAccount() - start");

        assertNotNull(
            target.addAccount(
                accountName
                )
            ) ;

        logger.info("testAddAccount() - end");
        }

    /**
     * Check that we can't create a duplicate account.
     *
     */
    public void testAddAccountDuplicate()
        throws Exception
        {
        logger.info("testAddAccountDuplicate() - start");

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
            logger.info("testAddAccountDuplicate() - end");
            return ;
            }
        fail("Expected DuplicateNodeException") ;

        logger.info("testAddAccountDuplicate() - end");
        }

    /**
     * Check that an account node has an ivorn.
     *
     */
    public void testAddAccountIvorn()
        throws Exception
        {
        logger.info("testAddAccountIvorn() - start");

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

        logger.info("testAddAccountIvorn() - end");
        }

    /**
     * Check that an account node has an ident.
     *
     */
    public void testAddAccountIdent()
        throws Exception
        {
        logger.info("testAddAccountIdent() - start");

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

        logger.info("testAddAccountIdent() - end");
        }

    /**
     * Check that an account node has a service Ivorn.
     *
     */
    public void testAddAccountService()
        throws Exception
        {
        logger.info("testAddAccountService() - start");

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

        logger.info("testAddAccountService() - end");
        }

    /**
     * Check that an account node has the right name.
     *
     */
    public void testAddAccountName()
        throws Exception
        {
        logger.info("testAddAccountName() - start");

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

        logger.info("testAddAccountName() - end");
        }

    /**
     * Check that we get the right Exception for a null account.
     *
     */
    public void testGetAccountNull()
        throws Exception
        {
        logger.info("testGetAccountNull() - start");

        try {
            target.getAccount(
                null
                ) ;
            }
        catch (IllegalArgumentException ouch)
            {
            logger.info("testGetAccountNull() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testGetAccountNull() - end");
        }

    /**
     * Check that we get the right Exception for an unknown account.
     *
     */
    public void testGetAccountUnknown()
        throws Exception
        {
        logger.info("testGetAccountUnknown() - start");

        try {
            target.getAccount(
                unknownName
                ) ;
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testGetAccountUnknown() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testGetAccountUnknown() - end");
        }

    /**
     * Check that we can find an account node.
     *
     */
    public void testGetAccount()
        throws Exception
        {
        logger.info("testGetAccount() - start");

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

        logger.info("testGetAccount() - end");
        }

    /**
     * Check that an account node has the right name.
     *
     */
    public void testGetAccountName()
        throws Exception
        {
        logger.info("testGetAccountName() - start");

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

        logger.info("testGetAccountName() - end");
        }

    /**
     * Check that an account node has the right ivorn.
     *
     */
    public void testGetAccountIvorn()
        throws Exception
        {
        logger.info("testGetAccountIvorn() - start");

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

        logger.info("testGetAccountIvorn() - end");
        }

    /**
     * Check that an account node has the right ident.
     *
     */
    public void testGetAccountIdent()
        throws Exception
        {
        logger.info("testGetAccountIdent() - start");

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

        logger.info("testGetAccountIdent() - end");
        }

    /**
     * Check that an account node has the right type.
     *
     */
    public void testGetAccountType()
        throws Exception
        {
        logger.info("testGetAccountType() - start");

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

        logger.info("testGetAccountType() - end");
        }

    /**
     * Check that we get the right exception for a null parent.
     *
     */
    public void testAddNodeNullParent()
        throws Exception
        {
        logger.info("testAddNodeNullParent() - start");

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
            logger.info("testAddNodeNullParent() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testAddNodeNullParent() - end");
        }

    /**
     * Check that we get the right exception for a null name.
     *
     */
    public void testAddNodeNullName()
        throws Exception
        {
        logger.info("testAddNodeNullName() - start");

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
            logger.info("testAddNodeNullName() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testAddNodeNullName() - end");
        }

    /**
     * Check that we get the right exception for a null type.
     *
     */
    public void testAddNodeNullType()
        throws Exception
        {
        logger.info("testAddNodeNullType() - start");

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
            logger.info("testAddNodeNullType() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testAddNodeNullType() - end");
        }

    /**
     * Check that we get the right exception for an unknown parent.
     *
     */
    public void testAddNodeUnknownParent()
        throws Exception
        {
        logger.info("testAddNodeUnknownParent() - start");

        try {
            target.addNode(
                "ivo://unknown#unknown",
                "name",
                FileManagerProperties.CONTAINER_NODE_TYPE
                ) ;
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testAddNodeUnknownParent() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testAddNodeUnknownParent() - end");
        }

    /**
     * Check that we can add a child node.
     *
     */
    public void testAddContainerChild()
        throws Exception
        {
        logger.info("testAddContainerChild() - start");

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

        logger.info("testAddContainerChild() - end");
        }

    /**
     * Check that a new container has the right name.
     *
     */
    public void testAddContainerName()
        throws Exception
        {
        logger.info("testAddContainerName() - start");

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

        logger.info("testAddContainerName() - end");
        }

    /**
     * Check that a new container has the right type.
     *
     */
    public void testAddContainerType()
        throws Exception
        {
        logger.info("testAddContainerType() - start");

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

        logger.info("testAddContainerType() - end");
        }

    /**
     * Check that a new container has the right parent.
     *
     */
    public void testAddContainerParent()
        throws Exception
        {
        logger.info("testAddContainerParent() - start");

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

        logger.info("testAddContainerParent() - end");
        }

    /**
     * Check that we can add a data node.
     *
     */
    public void testAddData()
        throws Exception
        {
        logger.info("testAddData() - start");

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

        logger.info("testAddData() - end");
        }

    /**
     * Check that a new data node has the right name.
     *
     */
    public void testAddDataName()
        throws Exception
        {
        logger.info("testAddDataName() - start");

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

        logger.info("testAddDataName() - end");
        }

    /**
     * Check that a new data node has the right type.
     *
     */
    public void testAddDataType()
        throws Exception
        {
        logger.info("testAddDataType() - start");

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

        logger.info("testAddDataType() - end");
        }

    /**
     * Check that a new data node has the right parent.
     *
     */
    public void testAddDataParent()
        throws Exception
        {
        logger.info("testAddDataParent() - start");

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

        logger.info("testAddDataParent() - end");
        }

    /**
     * Check that a new data node has the right location.
     *
     */
    public void testAddDataLocation()
        throws Exception
        {
        logger.info("testAddDataLocation() - start");

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

        logger.info("testAddDataLocation() - end");
        }

    /**
     * Check that we can't add a child to a data node.
     *
     */
    public void testAddChildToData()
        throws Exception
        {
        logger.info("testAddChildToData() - start");

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
            logger.info("testAddChildToData() - end");
            return ;
            }
        fail("Expected UnsupportedOperationException") ;

        logger.info("testAddChildToData() - end");
        }

    /**
     * Check that we get the right exception for a null ident.
     *
     */
    public void testGetNodeNull()
        throws Exception
        {
        logger.info("testGetNodeNull() - start");

        try {
            target.getNode(
                null
                ) ;
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testGetNodeNull() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Check that we get the right exception for an empty ident.
     *
     */
    public void testGetNodeNone()
        throws Exception
        {
        try {
            target.getNode(
                "ivo://unknown"
                ) ;
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Check that we get the right exception for an unknown node.
     *
     */
    public void testGetNodeUnknown()
        throws Exception
        {
        logger.info("testGetNodeUnknown() - start");

        try {
            target.getNode(
                "ivo://unknown#unknown"
                ) ;
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testGetNodeUnknown() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testGetNodeUnknown() - end");
        }

    /**
     * Check that we can get a node by identifier.
     *
     */
    public void testGetNodeIvorn()
        throws Exception
        {
        logger.info("testGetNodeIvorn() - start");

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

        logger.info("testGetNodeIvorn() - end");
        }

    /**
     * Check that a node has the right name.
     *
     */
    public void testGetNodeName()
        throws Exception
        {
        logger.info("testGetNodeName() - start");

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

        logger.info("testGetNodeName() - end");
        }

    /**
     * Check that a node has the right identifier.
     *
     */
    public void testGetNodeIdent()
        throws Exception
        {
        logger.info("testGetNodeIdent() - start");

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

        logger.info("testGetNodeIdent() - end");
        }

    /**
     * Check that a node has the right type.
     *
     */
    public void testGetNodeType()
        throws Exception
        {
        logger.info("testGetNodeType() - start");

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

        logger.info("testGetNodeType() - end");
        }

    /**
     * Check that a node has the right parent.
     *
     */
    public void testGetNodeParent()
        throws Exception
        {
        logger.info("testGetNodeParent() - start");

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

        logger.info("testGetNodeParent() - end");
        }

    /**
     * Check that we get the right exception for a null root.
     *
     */
    public void testGetPathNullRoot()
        throws Exception
        {
        logger.info("testGetPathNullRoot() - start");

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
            logger.info("testGetPathNullRoot() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testGetPathNullRoot() - end");
        }

    /**
     * Check that we get the right exception for a null path.
     *
     */
    public void testGetPathNullPath()
        throws Exception
        {
        logger.info("testGetPathNullPath() - start");

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
            logger.info("testGetPathNullPath() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testGetPathNullPath() - end");
        }

    /**
     * Check that we get the right exception for an unknown parent.
     *
     */
    public void testGetPathUnknownParent()
        throws Exception
        {
        logger.info("testGetPathUnknownParent() - start");

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
            logger.info("testGetPathUnknownParent() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testGetPathUnknownParent() - end");
        }

    /**
     * Check that we get the right exception for an unknown path.
     *
     */
    public void testGetPathUnknownPath()
        throws Exception
        {
        logger.info("testGetPathUnknownPath() - start");

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
            logger.info("testGetPathUnknownPath() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testGetPathUnknownPath() - end");
        }

    /**
     * Check that we can get a node by path.
     *
     */
    public void testGetPath()
        throws Exception
        {
        logger.info("testGetPath() - start");

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

        logger.info("testGetPath() - end");
        }

    /**
     * Check that we get the right exception for a null ident.
     *
     */
    public void testGetChildrenNullParent()
        throws Exception
        {
        logger.info("testGetChildrenNullParent() - start");

        //
        // Try getting the children for a null parent.
        try {
            target.getChildren(
                null
                ) ;
            }
        catch (IllegalArgumentException ouch)
            {
            logger.info("testGetChildrenNullParent() - end");
            return ;
            }
        fail("Expected IllegalArgumentException") ;

        logger.info("testGetChildrenNullParent() - end");
        }

    /**
     * Check that we get the right exception for an unknown parent.
     *
     */
    public void testGetChildrenUnknownParent()
        throws Exception
        {
        logger.info("testGetChildrenUnknownParent() - start");

        //
        // Try getting the children for an unknown parent.
        try {
            target.getChildren(
                "ivo://unknown#unknown"
                ) ;
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testGetChildrenUnknownParent() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testGetChildrenUnknownParent() - end");
        }

    /**
     * Check that we get an empty list of nodes.
     *
     */
    public void testGetChildrenEmpty()
        throws Exception
        {
        logger.info("testGetChildrenEmpty() - start");

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

        logger.info("testGetChildrenEmpty() - end");
        }

    /**
     * Check that we can get a list of child nodes.
     *
     */
    public void testGetChildren()
        throws Exception
        {
        logger.info("testGetChildren() - start");

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

        logger.info("testGetChildren() - end");
        }

    /**
     * Check we get the right list of child nodes.
     *
     */
    public void testGetChildrenIvorns()
        throws Exception
        {
        logger.info("testGetChildrenIvorns() - start");

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

        logger.info("testGetChildrenIvorns() - end");
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
        logger.info("testGetDataChildrenEmpty() - start");

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

        logger.info("testGetDataChildrenEmpty() - end");
        }

    /**
     * Check that we can get a nested container by path.
     *
     */
    public void testGetContainerByPath()
        throws Exception
        {
        logger.info("testGetContainerByPath() - start");

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

        logger.info("testGetContainerByPath() - end");
        }

    /**
     * Check that we can get a nested container by ivorn.
     *
     */
    public void testGetContainerByIvorn()
        throws Exception
        {
        logger.info("testGetContainerByIvorn() - start");

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

        logger.info("testGetContainerByIvorn() - end");
        }

    /**
     * Check we get the right Exception for null properties.
     *
     */
    public void testImportInitNullProperties()
        throws Exception
        {
        logger.info("testImportInitNullProperties() - start");

        try {
            target.importInit(
                null
                );
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testImportInitNullProperties() - end");
            return ;
            }
        fail("Expected NodeNotFoundException");

        logger.info("testImportInitNullProperties() - end");
        }

    /**
     * Check we get the right Exception for a null ivorn.
     *
     */
    public void testImportInitNullIvorn()
        throws Exception
        {
        logger.info("testImportInitNullIvorn() - start");

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
            logger.info("testImportInitNullIvorn() - end");
            return ;
            }
        fail("Expected NodeNotFoundException");

        logger.info("testImportInitNullIvorn() - end");
        }

    /**
     * Check we get the right Exception for an unknown node.
     *
     */
    public void testImportInitUnknownIvorn()
        throws Exception
        {
        logger.info("testImportInitUnknownIvorn() - start");

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
            logger.info("testImportInitUnknownIvorn() - end");
            return ;
            }
        fail("Expected NodeNotFoundException");

        logger.info("testImportInitUnknownIvorn() - end");
        }

    /**
     * Check we can initiate an import into a known node.
     *
     */
    public void testImportInitValidNode()
        throws Exception
        {
        logger.info("testImportInitValidNode() - start");

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

        logger.info("testImportInitValidNode() - end");
        }

    /**
     * Check we can transfer data into an existing node.
     *
     */
    public void testImportInitWriteData()
        throws Exception
        {
        logger.info("testImportInitWriteData() - start");

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

        logger.info("testImportInitWriteData() - end");
        }

    /**
     * Check we can refresh the properties after an import. 
     *
     */
    public void testImportRefreshSize()
        throws Exception
        {
        logger.info("testImportRefreshSize() - start");

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

        logger.info("testImportRefreshSize() - end");
        }

    /**
     * Check we get the right Exception for null properties.
     *
     */
    public void testExportNullProperties()
        throws Exception
        {
        logger.info("testExportNullProperties() - start");

        try {
            target.exportInit(
                null
                );
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testExportNullProperties() - end");
            return ;
            }
        fail("Expected NodeNotFoundException");

        logger.info("testExportNullProperties() - end");
        }

    /**
     * Check we get the right Exception for a null ivorn.
     *
     */
    public void testExportNullIvorn()
        throws Exception
        {
        logger.info("testExportNullIvorn() - start");

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
            logger.info("testExportNullIvorn() - end");
            return ;
            }
        fail("Expected NodeNotFoundException");

        logger.info("testExportNullIvorn() - end");
        }

    /**
     * Check we get the right Exception for an unknown node.
     *
     */
    public void testExportUnknownIvorn()
        throws Exception
        {
        logger.info("testExportUnknownIvorn() - start");

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
            logger.info("testExportUnknownIvorn() - end");
            return ;
            }
        fail("Expected NodeNotFoundException");

        logger.info("testExportUnknownIvorn() - end");
        }

    /**
     * Check we can initiate an export from a known node.
     *
     */
    public void testExportValidNode()
        throws Exception
        {
        logger.info("testExportValidNode() - start");

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

        logger.info("testExportValidNode() - end");
        }

    /**
     * Check we get the right data back.
     *
     */
    public void testExportRead()
        throws Exception
        {
        logger.info("testExportRead() - start");

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

        logger.info("testExportRead() - end");
        }

    /**
     * Test we get the right Exception for a null request.
     *
     */
    public void testMoveNullProperties()
        throws Exception
        {
        logger.info("testMoveNullProperties() - start");

        try {
            target.move(
                null
                );
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testMoveNullProperties() - end");
            return ;
            }
        fail("Expected NodeNotFoundException");

        logger.info("testMoveNullProperties() - end");
        }

    /**
     * Test we can change the node name.
     *
     */
    public void testMoveName()
        throws Exception
        {
        logger.info("testMoveName() - start");

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

        logger.info("testMoveName() - end");
        }

    /**
     * Check we get the right Exception for a duplicate name.
     *
     */
    public void testMoveNameDuplicate()
        throws Exception
        {
        logger.info("testMoveNameDuplicate() - start");

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
            logger.info("testMoveNameDuplicate() - end");
            return ;
            }
        fail("Expected DuplicateNodeException");

        logger.info("testMoveNameDuplicate() - end");
        }

    /**
     * Test we can change the node parent.
     *
     */
    public void testMoveParent()
        throws Exception
        {
        logger.info("testMoveParent() - start");

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

        logger.info("testMoveParent() - end");
        }

    /**
     * Test we can change the node name and parent.
     *
     */
    public void testMoveNameParent()
        throws Exception
        {
        logger.info("testMoveNameParent() - start");

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

        logger.info("testMoveNameParent() - end");
        }

    /**
     * Check we get the right Exception for a duplicate name.
     *
     */
    public void testMoveParentDuplicate()
        throws Exception
        {
        logger.info("testMoveParentDuplicate() - start");

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

            logger.info("testMoveParentDuplicate() - end");
            return ;
            }
        fail("Expected DuplicateNodeException");

        logger.info("testMoveParentDuplicate() - end");
        }

    /**
     * Test we can change the location of an empty node.
     *
     */
    public void testMoveEmptyLocation()
        throws Exception
        {
        logger.info("testMoveEmptyLocation() - start");

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

        logger.info("testMoveEmptyLocation() - end");
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
        logger.info("testMoveDataLocation() - start");

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

        logger.info("testMoveDataLocation() - end");
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
        logger.info("testCopyNullRequest() - start");

        try {
            target.copy(
                null
                );
            }
        catch (NodeNotFoundException ouch)
            {
            logger.info("testCopyNullRequest() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testCopyNullRequest() - end");
        }

    /**
     * Check we get the right Exception for a null resource.
     *
     */
    public void testCopyNullResource()
        throws Exception
        {
        logger.info("testCopyNullResource() - start");

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
            logger.info("testCopyNullResource() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testCopyNullResource() - end");
        }

    /**
     * Check we get the right Exception for an unknown resource.
     *
     */
    public void testCopyUnknownResource()
        throws Exception
        {
        logger.info("testCopyUnknownResource() - start");

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
            logger.info("testCopyUnknownResource() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testCopyUnknownResource() - end");
        }

    /**
     * Check we get the right Exception for the same resource.
     *
     */
    public void testCopyDuplicateSame()
        throws Exception
        {
        logger.info("testCopyDuplicateSame() - start");

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
            logger.info("testCopyDuplicateSame() - end");
            return ;
            }
        fail("Expected DuplicateNodeException") ;

        logger.info("testCopyDuplicateSame() - end");
        }

    /**
     * Check we get the right Exception for a duplicate node.
     *
     */
    public void testCopyDuplicateNode()
        throws Exception
        {
        logger.info("testCopyDuplicateNode() - start");

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
            logger.info("testCopyDuplicateNode() - end");
            return ;
            }
        fail("Expected DuplicateNodeException") ;

        logger.info("testCopyDuplicateNode() - end");
        }

    /**
     * Check we can copy an empty node.
     *
     */
    public void testCopyEmptyNode()
        throws Exception
        {
        logger.info("testCopyEmptyNode() - start");

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

        logger.info("testCopyEmptyNode() - end");
        }

    /**
     * Check we can copy a node with data.
     *
     */
    public void testCopyNodeData()
        throws Exception
        {
        logger.info("testCopyNodeData() - start");

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
        logger.info("testCopyNodeLocation() - start");

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

        logger.info("testCopyNodeLocation() - end");
        }

    /**
     * Check we can copy an empty node to a new location.
     *
     */
    public void testCopyEmptyLocation()
        throws Exception
        {
        logger.info("testCopyEmptyLocation() - start");

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

        logger.info("testCopyEmptyLocation() - end");
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
        logger.info("testDeleteEmptyData() - start");

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
            logger.info("testDeleteEmptyData() - end");
            return ;
            }
        fail("Expected NodeNotFoundException") ;

        logger.info("testDeleteEmptyData() - end");
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

    /**
     * Check that the file create and modify dates are set.
     *
     */
    public void testImportDates()
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
        // Get the initial dates.
        Date frogCreateDate = frog.getFileCreateDate();
        Date frogModifyDate = frog.getFileModifyDate();
        //
        // Refresh the node properties.
        FileManagerProperties toad = new FileManagerProperties(
            target.refresh(
                frog.getManagerResourceIvorn().toString()
                )
            );
        //
        // Get the updated dates.
        Date toadCreateDate = toad.getFileCreateDate();
        Date toadModifyDate = toad.getFileModifyDate();
        //
        // Pause for a bit ....
        Thread.sleep(1000);
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
        // Get the final dates.
        Date newtCreateDate = newt.getFileCreateDate();
        Date newtModifyDate = newt.getFileModifyDate();

        System.out.println("");
        System.out.println("Created  : " + frogCreateDate);
        System.out.println("Modified : " + frogModifyDate);

        System.out.println("");
        System.out.println("Created  : " + toadCreateDate);
        System.out.println("Modified : " + toadModifyDate);

        System.out.println("");
        System.out.println("Created  : " + newtCreateDate);
        System.out.println("Modified : " + newtModifyDate);
        //
        // Check the first set of dates are null.
        assertNull(
            frogCreateDate
            );
        assertNull(
            frogModifyDate
            );
        assertNull(
            toadCreateDate
            );
        assertNull(
            toadModifyDate
            );
        //
        // Check the final dates are not null.
        assertNotNull(
            newtCreateDate
            );
        assertNotNull(
            newtModifyDate
            );
        //
        // Check the modified date is after the create date.
        assertTrue(
            newtModifyDate.after(
                newtCreateDate
                )
            );
        }

    /**
     * Test that we get the right Exception for null request.
     *
     */
    public void testImportDataNullRequest()
        throws Exception
        {
        //
        // Try to import the data.
        try {
            target.importData(
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
     * Test that we get the right Exception for null properties.
     *
     */
    public void testImportDataNullProperties()
        throws Exception
        {
        //
        // Try to import the data.
        try {
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.file.text"
                            )
                        ),
                    (FileManagerProperties)null
                    )
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Test that we get the right Exception for empty properties.
     *
     */
    public void testImportDataEmptyProperties()
        throws Exception
        {
        //
        // Create our test properties.
        FileManagerProperties properties = new FileManagerProperties() ;
        //
        // Try to import the data.
        try {
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.file.text"
                            )
                        ),
                    properties
                    )
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Test that we get the right Exception for an unknown node.
     *
     */
    public void testImportDataUnknownNode()
        throws Exception
        {
        //
        // Create the ivorn factory.
        FileManagerIvornFactory factory = new FileManagerIvornFactory() ;
        //
        // Create our request properties.
        FileManagerProperties properties = new FileManagerProperties() ;
        //
        // Set the resource ivorn.
        properties.setManagerResourceIvorn(
            factory.ident(
                target.getIdentifier()
                )
            );
        //
        // Try to import the data.
        try {
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.file.text"
                            )
                        ),
                    properties
                    )
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException") ;
        }

    /**
     * Test that we get the right Exception for container node.
     *
     */
    public void testImportDataContainerNode()
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
                FileManagerProperties.CONTAINER_NODE_TYPE
                )
            );
        //
        // Try to import the data.
        try {
            target.importData(
                new UrlGetTransfer(
                    new URL(
                        getTestProperty(
                            "data.file.text"
                            )
                        ),
                    frog
                    )
                );
            }
        catch (FileManagerServiceException ouch)
            {
            return ;
            }
        fail("Expected FileManagerServiceException") ;
        }

    /**
     * Check that we can import data from local file.
     *
     */
    public void testImportDataFile()
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
        // Import the data.
        target.importData(
            new UrlGetTransfer(
                new URL(
                    getTestProperty(
                        "data.file.text"
                        )
                    ),
                frog
                )
            );
        }

    /**
     * Check that we get the right data from a local file.
     *
     */
    public void testImportDataFileData()
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
        // Import the data.
        target.importData(
            new UrlGetTransfer(
                new URL(
                    getTestProperty(
                        "data.file.text"
                        )
                    ),
                frog
                )
            );
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
        TransferUtil util =
            new TransferUtil(
                exportStream,
                buffer
                );
        int count = util.transfer();
        exportStream.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_STRING,
            new String(
                buffer.toByteArray()
                )
            );
        }

    /**
     * Check that we can't change the name with an import.
     *
     */
    public void testImportDataName()
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
        FileManagerProperties frog =
            new FileManagerProperties(
                target.addNode(
                    home.getManagerResourceIvorn().toString(),
                    "frog",
                    FileManagerProperties.DATA_NODE_TYPE
                    )
                );
        //
        // Modify the node name.
        frog.setManagerResourceName(
            "toad"
            );
        //
        // Import the data.
        FileManagerProperties toad = 
            new FileManagerProperties(
                target.importData(
                    new UrlGetTransfer(
                        new URL(
                            getTestProperty(
                                "data.file.text"
                                )
                            ),
                        frog
                        )
                    )
                );
        //
        // Check we still have the original name.
        assertEquals(
            "frog",
            toad.getManagerResourceName()
            );
        }

    /**
     * Check that we can't change the parent with an import.
     *
     */
    public void testImportDataParent()
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
        FileManagerProperties frog =
            new FileManagerProperties(
                target.addNode(
                    home.getManagerResourceIvorn().toString(),
                    "frog",
                    FileManagerProperties.DATA_NODE_TYPE
                    )
                );
        //
        // Create our other parent node.
        FileManagerProperties newt =
            new FileManagerProperties(
                target.addNode(
                    home.getManagerResourceIvorn().toString(),
                    "newt",
                    FileManagerProperties.CONTAINER_NODE_TYPE
                    )
                );
        //
        // Modify the node parent.
        frog.setManagerParentIvorn(
            newt.getManagerResourceIvorn()
            );
        //
        // Import the data.
        FileManagerProperties toad = 
            new FileManagerProperties(
                target.importData(
                    new UrlGetTransfer(
                        new URL(
                            getTestProperty(
                                "data.file.text"
                                )
                            ),
                        frog
                        )
                    )
                );
        //
        // Check we still have the original name.
        assertEquals(
            home.getManagerResourceIvorn(),
            toad.getManagerParentIvorn()
            );
        }

    /**
     * Check that we can't change the type with an import.
     *
     */
    public void testImportDataType()
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
        FileManagerProperties frog =
            new FileManagerProperties(
                target.addNode(
                    home.getManagerResourceIvorn().toString(),
                    "frog",
                    FileManagerProperties.DATA_NODE_TYPE
                    )
                );
        //
        // Modify the node type.
        frog.setManagerResourceType(
            FileManagerProperties.CONTAINER_NODE_TYPE
            );
        //
        // Import the data.
        FileManagerProperties toad = 
            new FileManagerProperties(
                target.importData(
                    new UrlGetTransfer(
                        new URL(
                            getTestProperty(
                                "data.file.text"
                                )
                            ),
                        frog
                        )
                    )
                );
        //
        // Check we still have the original name.
        assertEquals(
            FileManagerProperties.DATA_NODE_TYPE,
            toad.getManagerResourceType()
            );
        }
    }
