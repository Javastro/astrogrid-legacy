/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/delegate/Attic/FileManagerDelegateNodeTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegateNodeTest.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.2  2005/01/26 12:24:21  dave
 *   Removed type from add(), replaced with addNode() and addFile() ...
 *
 *   Revision 1.1.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.3.2.5  2005/01/21 13:07:37  dave
 *   Added exportURL to the node API ...
 *
 *   Revision 1.3.2.4  2005/01/21 12:18:54  dave
 *   Changed input() and output() to exportStream() and importStream() ...
 *
 *   Revision 1.3.2.3  2005/01/19 12:55:35  dave
 *   Added sleep to date tests to avoid race condition ...
 *
 *   Revision 1.3.2.2  2005/01/19 11:39:28  dave
 *   Added combined create and modify date ...
 *
 *   Revision 1.3.2.1  2005/01/17 13:04:42  dave
 *   Added client node test for FileStore dates ...
 *
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2.4.1  2005/01/07 12:18:00  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 *   Revision 1.2  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.19  2004/12/14 13:52:16  dave
 *   Added data location copy test ....
 *
 *   Revision 1.1.2.18  2004/12/14 11:49:50  dave
 *   Added data location copy test ....
 *
 *   Revision 1.1.2.17  2004/12/14 11:30:45  dave
 *   Added node data copy test ....
 *
 *   Revision 1.1.2.16  2004/12/14 11:08:12  dave
 *   Added tests for node copy ...
 *
 *   Revision 1.1.2.15  2004/12/14 10:51:24  dave
 *   Added initial test for node copy ...
 *
 *   Revision 1.1.2.14  2004/12/14 10:48:38  dave
 *   Added initial test for node copy ...
 *
 *   Revision 1.1.2.13  2004/12/14 10:32:18  dave
 *   Added copy to node API ....
 *
 *   Revision 1.1.2.12  2004/12/11 01:57:35  dave
 *   Added automatic trigger to node update() on OutputStream close() ....
 *
 *   Revision 1.1.2.11  2004/12/10 05:21:25  dave
 *   Added node and iterator to client API ...
 *
 *   Revision 1.1.2.10  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.1.2.9  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.8  2004/12/04 05:22:21  dave
 *   Fixed null parent mistake ...
 *
 *   Revision 1.1.2.7  2004/12/03 14:32:47  dave
 *   Added test to node API
 *
 *   Revision 1.1.2.6  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.5  2004/11/29 18:05:07  dave
 *   Refactored methods names ....
 *   Added stubs for delete, copy and move.
 *
 *   Revision 1.1.2.4  2004/11/26 04:22:24  dave
 *   Added SOAP delegate node test ...
 *   Added node export test ..
 *
 *   Revision 1.1.2.3  2004/11/25 13:41:14  dave
 *   Added export stream handling to node ...
 *
 *   Revision 1.1.2.2  2004/11/24 19:23:29  dave
 *   Started to add input and output streams to node ...
 *
 *   Revision 1.1.2.1  2004/11/24 16:15:08  dave
 *   Added node functions to client ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client.delegate;

import java.net.URL;

import java.util.Date;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.transfer.TransferUtil;

import org.astrogrid.filemanager.common.BaseTest;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException ;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;

import org.astrogrid.filemanager.client.FileManagerNode;

/**
 * A generic test for the FileManagerNode API.
 *
 */
public class FileManagerDelegateNodeTest
    extends BaseTest
    {

    /**
     * A set of ivorn identifiers for our target file stores.
     *
     */
    protected Ivorn[] filestores ;

    /**
     * Our target FileManager delegate
     *
     */
    protected FileManagerDelegate delegate;

    /**
     * Set up our test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Setup our account identifiers.
        super.setUp();
        }

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
     * Create an account.
     *
     */
    protected FileManagerNode account()
        throws Exception
        {
        return delegate.addAccount(
            accountIvorn
            ) ;
        }

    /**
     * Check that we can create a new account.
     *
     */
    public void testCreateAccount()
        throws Exception
        {
        assertNotNull(
            this.account()
            );
        }

    /**
     * Check that we get the right Exception for a null name.
     *
     */
    public void testAddContainerNullName()
        throws Exception
        {
        FileManagerNode home = account() ;
        try {
            home.addNode(
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
     * Check that we get the right Exception for a null type.
     *
     */
    public void testAddContainerNullType()
        throws Exception
        {
        FileManagerNode home = account() ;
        try {
            ((FileManagerDelegateNode)home).add(
                "frog",
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
     * Check that we can add a container.
     *
     */
    public void testAddContainer()
        throws Exception
        {
        FileManagerNode home = account() ;
        assertNotNull(
            home.addNode(
                "frog"
                )
            );
        }

    /**
     * Check that we get the right Exception for a duplicate.
     *
     */
    public void testAddContainerDuplicate()
        throws Exception
        {
        FileManagerNode home = account() ;
        home.addNode(
            "frog"
            );
        try {
            home.addNode(
                "frog"
                );
            }
        catch (DuplicateNodeException  ouch)
            {
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Check that a container has the right name.
     *
     */
    public void testAddContainerName()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        assertEquals(
            "frog",
            frog.name()
            );
        }

    /**
     * Check that a container has the right type.
     *
     */
    public void testAddContainerType()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        assertTrue(
            frog.isContainer()
            );
        assertFalse(
            frog.isFile()
            );
        }

    /**
     * Check that a container has an Ivorn.
     *
     */
    public void testAddContainerIvorn()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        assertNotNull(
            frog.ivorn()
            );
        }

    /**
     * Check that a container has a parent.
     *
     */
    public void testAddContainerParent()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        assertNotNull(
            frog.parent()
            );
        }

    /**
     * Check that we get the right Exception for a null name.
     *
     */
    public void testAddChildContainerNullName()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        try {
            frog.addNode(
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
     * Check that we get the right Exception for a null type.
     *
     */
    public void testAddChildContainerNullType()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        try {
            ((FileManagerDelegateNode)frog).add(
                "frog",
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
     * Check that we can add a nested container.
     *
     */
    public void testAddChildContainer()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        assertNotNull(
            frog.addNode(
                "toad"
                )
            );
        }

    /**
     * Check that we get the right Exception for a duplicate.
     *
     */
    public void testAddChildContainerDuplicate()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        frog.addNode(
            "toad"
            );
        try {
            frog.addNode(
                "toad"
                );
            }
        catch (DuplicateNodeException  ouch)
            {
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Check that a child container has the right name.
     *
     */
    public void testAddChildContainerName()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = frog.addNode(
            "toad"
            );
        assertEquals(
            "toad",
            toad.name()
            );
        }

    /**
     * Check that a child container has the right type.
     *
     */
    public void testAddChildContainerType()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = frog.addNode(
            "toad"
            );
        assertTrue(
            toad.isContainer()
            );
        assertFalse(
            toad.isFile()
            );
        }

    /**
     * Check that a child container has an Ivorn.
     *
     */
    public void testAddChildContainerIvorn()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = frog.addNode(
            "toad"
            );
        assertNotNull(
            toad.ivorn()
            );
        }

    /**
     * Check that we get the right Exception for a null name.
     *
     */
    public void testAddFileNullName()
        throws Exception
        {
        FileManagerNode home = account() ;
        try {
            home.addFile(
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
     * Check that we get the right Exception for a null type.
     *
     */
    public void testAddFileNullType()
        throws Exception
        {
        FileManagerNode home = account() ;
        try {
            ((FileManagerDelegateNode)home).add(
                "frog",
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
     * Check that we can add a file.
     *
     */
    public void testAddFile()
        throws Exception
        {
        FileManagerNode home = account() ;
        assertNotNull(
            home.addFile(
                "frog"
                )
            );
        }

    /**
     * Check that we get the right Exception for a duplicate.
     *
     */
    public void testAddFileDuplicate()
        throws Exception
        {
        FileManagerNode home = account() ;
        home.addFile(
            "frog"
            );
        try {
            home.addFile(
                "frog"
                );
            }
        catch (DuplicateNodeException  ouch)
            {
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Check that a file has the right name.
     *
     */
    public void testAddFileName()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertEquals(
            "frog",
            frog.name()
            );
        }

    /**
     * Check that a file has the right type.
     *
     */
    public void testAddFileType()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertFalse(
            frog.isContainer()
            );
        assertTrue(
            frog.isFile()
            );
        }

    /**
     * Check that a file has an Ivorn.
     *
     */
    public void testAddFileIvorn()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertNotNull(
            frog.ivorn()
            );
        }

    /**
     * Check that a file has a parent.
     *
     */
    public void testAddFileParent()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertNotNull(
            frog.parent()
            );
        }

    /**
     * Check that we get the right Exception for a null name.
     *
     */
    public void testGetChildNull()
        throws Exception
        {
        FileManagerNode home = account() ;
        try {
            home.child(
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
     * Check that we get the right Exception for an unknown name.
     *
     */
    public void testGetChildUnknown()
        throws Exception
        {
        FileManagerNode home = account() ;
        try {
            home.child(
                "frog"
                );
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException");
        }

    /**
     * Check that we can find a child node.
     *
     */
    public void testGetChildValid()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        assertNotNull(
            home.child(
                "frog"
                )
            );
        }

    /**
     * Check we can add nested nodes.
     *
     */
    public void testAddNested()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = frog.addNode(
            "toad"
            );
        FileManagerNode newt = toad.addNode(
            "newt"
            );
        assertNotNull(
            home.child(
                "frog"
                )
            );
        assertNotNull(
            home.child(
                "frog/toad"
                )
            );
        assertNotNull(
            home.child(
                "frog/toad/newt"
                )
            );
        }

    /**
     * Check we can get a node by ivorn.
     *
     */
    public void testGetNode()
        throws Exception
        {
        FileManagerNode home = account() ;
        assertNotNull(
            delegate.getNode(
                home.ivorn()
                )
            );
        }

    /**
     * Check we can get a (nested) node by ivorn.
     *
     */
    public void testGetNestedNode()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = frog.addNode(
            "toad"
            );
        FileManagerNode newt = toad.addNode(
            "newt"
            );
        assertNotNull(
            delegate.getNode(
                newt.ivorn()
                )
            );
        }

    /**
     * Check that we get the right Exception using a file as a container.
     *
     */
    public void testFileAddFile()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        try {
            frog.addFile(
                "toad"
                );
            }
        catch (UnsupportedOperationException  ouch)
            {
            return ;
            }
        fail("Expected UnsupportedOperationException");
        }

    /**
     * Check that we get the right Exception using a file as a container.
     *
     */
    public void testFileAddContainer()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        try {
            frog.addNode(
                "toad"
                );
            }
        catch (UnsupportedOperationException  ouch)
            {
            return ;
            }
        fail("Expected UnsupportedOperationException");
        }

    /**
     * Check that we get the right Exception using a file as a container.
     *
     */
    public void testFileGetChild()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        try {
            frog.child(
                "frog"
                );
            }
        catch (UnsupportedOperationException  ouch)
            {
            return ;
            }
        fail("Expected UnsupportedOperationException");
        }

    /**
     * Check that we get the right Exception using a container as a file.
     *
     */
    public void testOutputStreamContainer()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        try {
            frog.importStream();
            }
        catch (UnsupportedOperationException  ouch)
            {
            return ;
            }
        fail("Expected UnsupportedOperationException");
        }

    /**
     * Check that we can get an output stream for a file.
     *
     */
    public void testOutputStream()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertNotNull(
            frog.importStream()
            );
        }

    /**
     * Check that we can send data to a file.
     *
     */
    public void testOutputStreamWrite()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        //
        // Open an output stream to our file.
        OutputStream output = frog.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        }

/**
 * Check that we can get an output URL for a file.
 * Check that we can write to an output URL for a file.
 *
 */

    /**
     * Check that we get the right Exception using a container as a file.
     *
     */
    public void testInputStreamContainer()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        try {
            frog.exportStream();
            }
        catch (UnsupportedOperationException  ouch)
            {
            return ;
            }
        fail("Expected UnsupportedOperationException");
        }

    /**
     * Check that we can get the right Exception for an empty file.
     * @todo This should be DataNotFoundException ....
     *
     */
    public void testInputStreamEmpty()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        try {
            frog.exportStream();
            }
        catch (NodeNotFoundException ouch)
            {
            return ;
            }
        fail("Expected NodeNotFoundException");
        }

    /**
     * Check that we can get an input stream for a file.
     *
     */
    public void testInputStream()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        //
        // Open an output stream to our file.
        OutputStream output = frog.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Check we can get an input stream.
        assertNotNull(
            frog.exportStream()
            );
        }

    /**
     * Check that we can read data from a file.
     *
     */
    public void testInputStreamRead()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        //
        // Open an output stream to our file.
        OutputStream output = frog.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Open an input stream from our file.
        InputStream input = frog.exportStream() ;
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read the data from the file into the buffer.
        new TransferUtil(
            input,
            buffer
            ).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );
        }

    /**
     * Check that an empty file still has a location.
     *
     */
    public void testEmptyFileLocation()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertEquals(
            filestores[0],
            frog.location()
            );
        }

    /**
     * Check that a file with data has a location.
     *
     */
    public void testFileLocationNotNull()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        //
        // Open an output stream to our file.
        OutputStream output = frog.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Refresh the node data.
        // (no longer required if the stream is closed)
        //frog.refresh() ;
        //
        // Check the file location
        assertEquals(
            filestores[0],
            frog.location()
            );
        }

    /**
     * Check we can rename a file.
     *
     */
    public void testMoveFileName()
        throws Exception
        {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertNotNull(
            home
            );
        assertNotNull(
            home.ivorn()
            );
        assertNotNull(
            frog
            );
        assertNotNull(
            frog.parent()
            );
        assertNotNull(
            frog.parent().ivorn()
            );
        assertEquals(
            home.ivorn(),
            frog.parent().ivorn()
            );
        frog.move(
            "toad",
            null,
            null
            );
        assertEquals(
            "toad",
            frog.name()
            );
        assertEquals(
            home.ivorn(),
            frog.parent().ivorn()
            );
        }

    /**
     * Check we get the right Exception for a duplicate name.
     *
     */
    public void testMoveFileNameDuplicate()
        throws Exception
        {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        try {
            frog.move(
                "toad",
                null,
                null
                );
            }
        catch (DuplicateNodeException ouch)
            {
            frog.refresh();
            assertEquals(
                "frog",
                frog.name()
                );
            assertEquals(
                home.ivorn(),
                frog.parent().ivorn()
                );
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Check we can rename a container.
     *
     */
    public void testMoveContainerName()
        throws Exception
        {
        FileManagerNode home = account();
        FileManagerNode frog = home.addNode(
            "frog"
            );
        frog.move(
            "toad",
            null,
            null
            );
        assertEquals(
            "toad",
            frog.name()
            );
        assertEquals(
            home.ivorn(),
            frog.parent().ivorn()
            );
        }

    /**
     * Check that we can still find a child node.
     *
     * From :
     *  home
     *   |
     *   \- frog
     *       |
     *       \- toad
     * To:
     *  home
     *   |
     *   \- fish
     *       |
     *       \- toad
     *
     */
    public void testMoveContainerNameChild()
        throws Exception
        {
        FileManagerNode home = account();
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = frog.addNode(
            "toad"
            );
        assertNotNull(
            home.child(
                "frog/toad"
                )
            );
        frog.move(
            "fish",
            null,
            null
            );
        assertEquals(
            "fish",
            frog.name()
            );
        assertNotNull(
            home.child(
                "fish/toad"
                )
            );
        }

    /**
     * Check that we get the right Exception for a duplicate name.
     *
     * From :
     *  home
     *   |
     *   +- frog
     *   |   |
     *   |   \- toad
     *   |
     *   \- fish
     *
     * To:
     *  home
     *   |
     *   +- fish
     *   |   |
     *   |   \- toad
     *   |
     *   \- fish
     *
     */
    public void testMoveContainerNameDuplicate()
        throws Exception
        {
        FileManagerNode home = account();
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = frog.addNode(
            "toad"
            );
        FileManagerNode fish = home.addNode(
            "fish"
            );
        assertNotNull(
            home.child(
                "frog/toad"
                )
            );
        try {
            frog.move(
                "fish",
                null,
                null
                );
            }
        catch (DuplicateNodeException ouch)
            {
            assertEquals(
                "frog",
                frog.name()
                );
            assertNotNull(
                home.child(
                    "frog/toad"
                    )
                );
            return ;
            }
        fail("Expected DuplicateNodeException");
        }

    /**
     * Check that we can change the location of an empty file.
     *
     */
    public void testMoveEmptyFileLocation()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        assertEquals(
            filestores[0],
            frog.location()
            );
        frog.move(
            null,
            null,
            filestores[1]
            );
        assertEquals(
            filestores[1],
            frog.location()
            );
        }

    /**
     * Check that we can change the location of a file with data.
     *
     */
    public void testMoveDataFileLocation()
        throws Exception
        {
        //
        // Create the data node.
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );

        //
        // Open an output stream to our file.
        OutputStream output = frog.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;

        //
        // Check the data location.
        assertEquals(
            filestores[0],
            frog.location()
            );
        //
        // Move the file.
        frog.move(
            null,
            null,
            filestores[1]
            );
        //
        // Check the data location.
        assertEquals(
            filestores[1],
            frog.location()
            );

        //
        // Open an input stream from our file.
        InputStream input = frog.exportStream() ;
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read the data from the file into the buffer.
        new TransferUtil(
            input,
            buffer
            ).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );

        }

    /**
     * Check we get the right properties after an import. 
     *
     */
    public void testImportRefreshSize()
        throws Exception
        {
        //
        // Create the data node.
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );

        //
        // Refresh the node properties.
        frog.refresh();
        //
        // Check the data size.
        assertEquals(
            0,
            frog.size()
            );

        //
        // Open an output stream to our file.
        OutputStream output = frog.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;

        //
        // Refresh the node properties.
        // (no loger required if the stream is closed)
        //frog.refresh();
        //
        // Check the data size.
        assertEquals(
            TEST_BYTES.length,
            frog.size()
            );
        }

    /**
     * Check we get the right child nodes for an empty node.
     *
     */
    public void testChildrenEmpty()
        throws Exception
        {
        //
        // Create our test node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        //
        // Get an iterator for the child nodes.
        FileManagerNode.NodeIterator iter = frog.iterator();
        //
        // Check the iterator contains no nodes.
        assertFalse(
            iter.hasNext()
            );
        }

    /**
     * Check we get the right child nodes.
     *
     */
    public void testChildrenValid()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        FileManagerNode newt = home.addNode(
            "newt"
            );
        FileManagerNode fish = newt.addFile(
            "fish"
            );
        //
        // Get an iterator for the child nodes.
        FileManagerNode.NodeIterator iter = home.iterator();
        //
        // Check the iterator contains the right nodes.
        int     totalFound = 0 ;
        boolean frogFound  = false ;
        boolean toadFound  = false ;
        boolean newtFound  = false ;
        while(iter.hasNext())
            {
            FileManagerNode node = iter.next();
            if (compare(node.ivorn(), frog.ivorn()))
                {
                totalFound++;
                frogFound = true ;
                }
            if (compare(node.ivorn(), toad.ivorn()))
                {
                totalFound++;
                toadFound = true ;
                }
            if (compare(node.ivorn(), newt.ivorn()))
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
     * Check we can copy an empty data node.
     *
     */
    public void testCopyName()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy(
            "newt",
            null,
            null
            );
        //
        // Check we got a copy.
        assertNotNull(
            copy
            );
        //
        // Check our copy has the right name.
        assertEquals(
            "newt",
            copy.name()
            );
        //
        // Check our copy has the right parent.
        assertEquals(
            home.ivorn(),
            copy.parent().ivorn()
            );
        }

    /**
     * Check we can create a copy in a different container.
     *
     */
    public void testCopyParent()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        //
        // Create the target container.
        FileManagerNode newt = home.addNode(
            "newt"
            );
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy(
            null,
            newt,
            null
            );
        //
        // Check we got a copy.
        assertNotNull(
            copy
            );
        //
        // Check our copy has the right name.
        assertEquals(
            "toad",
            copy.name()
            );
        //
        // Check our copy has the right parent.
        assertEquals(
            newt.ivorn(),
            copy.parent().ivorn()
            );
        }

    /**
     * Check we can copy a node with data.
     *
     */
    public void testCopyData()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        //
        // Open an output stream to our file.
        OutputStream output = toad.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy(
            "newt",
            null,
            null
            );
        //
        // Open an input stream from our file.
        InputStream input = copy.exportStream() ;
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read the data from the file into the buffer.
        new TransferUtil(
            input,
            buffer
            ).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );
        }

    /**
     * Check we can create a copy at a different location.
     *
     */
    public void testCopyDataLocation()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        //
        // Open an output stream to our file.
        OutputStream output = toad.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;

        //
        // Check the data location.
        assertEquals(
            filestores[0],
            toad.location()
            );
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy(
            "newt",
            null,
            filestores[1]
            );
        //
        // Check the data location.
        assertEquals(
            filestores[1],
            copy.location()
            );
        //
        // Open an input stream from our file.
        InputStream input = copy.exportStream() ;
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read the data from the file into the buffer.
        new TransferUtil(
            input,
            buffer
            ).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );
        }

    /*
     * Check the file modified dates.
     *
     */
    public void testFileDates()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        //
        // Check the filestore dates are null.
        assertNull(
            toad.getFileCreateDate()
            );
        assertNull(
            toad.getFileModifyDate()
            );
        //
        // Pause for a bit ....
        Thread.sleep(1000);
        //
        // Open an output stream to our file.
        OutputStream output = toad.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Refresh the node properties.
        toad.refresh();
        //
        // Check the filestore dates are valid.
        assertNotNull(
            toad.getFileCreateDate()
            );
        assertNotNull(
            toad.getFileModifyDate()
            );
        //
        // Check the modified date is after the create date.
        assertTrue(
            toad.getFileModifyDate().after(
                toad.getFileCreateDate()
                )
            );
        }

    /*
     * Check the node modified dates.
     *
     */
    public void testNodeDates()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        //
        // Check the filestore dates are valid.
        assertNotNull(
            toad.getNodeCreateDate()
            );
        assertNotNull(
            toad.getNodeModifyDate()
            );
        //
        // Pause for a bit ....
        Thread.sleep(1000);
        //
        // Open an output stream to our file.
        OutputStream output = toad.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Refresh the node properties.
        toad.refresh();
        //
        // Check the node dates are valid.
        assertNotNull(
            toad.getNodeCreateDate()
            );
        assertNotNull(
            toad.getNodeModifyDate()
            );
        //
        // Check the modified date is after the create date.
        assertTrue(
            toad.getNodeModifyDate().after(
                toad.getNodeCreateDate()
                )
            );
        }

    /*
     * Check the modified dates.
     *
     */
    public void testDates()
        throws Exception
        {
        //
        // Create the data node(s).
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addNode(
            "frog"
            );
        FileManagerNode toad = home.addFile(
            "toad"
            );
        //
        // Check the dates are not null.
        assertNotNull(
            toad.getCreateDate()
            );
        assertNotNull(
            toad.getModifyDate()
            );
        //
        // Pause for a bit ....
        Thread.sleep(1000);
        //
        // Open an output stream to our file.
        OutputStream output = toad.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Refresh the node properties.
        toad.refresh();
        //
        // Check the dates are valid.
        assertNotNull(
            toad.getCreateDate()
            );
        assertNotNull(
            toad.getModifyDate()
            );
        //
        // Check the modified date is after the create date.
        assertTrue(
            toad.getModifyDate().after(
                toad.getCreateDate()
                )
            );
        //
        // Check we get the original create date.
        assertEquals(
            toad.getNodeCreateDate(),
            toad.getCreateDate()
            );
        }

    /**
     * Check we can get an export URL to access node data.
     *
     */
    public void testExportUrlValid()
        throws Exception
        {
        FileManagerNode home = account() ;
        FileManagerNode frog = home.addFile(
            "frog"
            );
        //
        // Open an output stream to our file.
        OutputStream output = frog.importStream() ;
        //
        // Send it some test data.
        output.write(
            TEST_BYTES
            ) ;
        output.close() ;
        //
        // Get a URL to access our data.
        URL url = frog.exportURL();
        assertNotNull(
            url
            );
        //
        // Open an input stream to the URL.
        FileStoreInputStream input = new FileStoreInputStream(
            url
            ) ;
        input.open() ;
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        //
        // Read the data from the file into the buffer.
        new TransferUtil(
            input,
            buffer
            ).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(
            TEST_BYTES,
            buffer.toByteArray()
            );
        }

    }

