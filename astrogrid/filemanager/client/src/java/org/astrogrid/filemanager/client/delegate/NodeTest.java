/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/delegate/NodeTest.java,v $</cvs:source>
 * <cvs:author>$Author: gtr $</cvs:author>
 * <cvs:date>$Date: 2008/08/21 09:01:27 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: NodeTest.java,v $
 *   Revision 1.3  2008/08/21 09:01:27  gtr
 *   Branch fm-gtr-2815 is merged. This fixes BZ2815.
 *
 *   Revision 1.2.100.1  2008/08/20 11:46:13  gtr
 *   I replaced enum as a varuable name with en so as to be compatible with Java 5.
 *
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.4  2005/03/01 23:41:14  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.3  2005/03/01 15:07:28  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.2  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.1.2.2  2005/02/11 20:02:49  nw
 *   introduced state pattern into CoreNodeImpl
 *
 *   Revision 1.1.2.1  2005/02/11 14:28:05  nw
 *   refactored, split out candidate classes.
 *
 *   Revision 1.2.4.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
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
 *   Changed input() and output() to readStream() and writeContent() ...
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

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.filemanager.common.DuplicateNodeFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;
import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.file.FileProperties;
import org.astrogrid.filestore.common.transfer.TransferUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

/**
 * A generic test for the FileManagerNode API.
 *  @modified nww renamed from FileManagerDelegateNodeTest
 */
public abstract class NodeTest extends BaseTest {

    /**
     * Our target FileManager delegate
     *  
     */
    protected NodeDelegate delegate;

    /**
     * Set up our test.
     *  
     */
    public void setUp() throws Exception {
        //
        // Setup our account identifiers.
        super.setUp();  
        delegate = createDelegate();
      
    }
    
    // to be implemented by concrete test cases.
    protected abstract NodeDelegate createDelegate() throws Exception;

    /**
     * Check we created our delegate.
     *  
     */
    public void testCreateDelegate() throws Exception {
        assertNotNull(this.delegate);
    }

    /**
     * Create an account.
     *  
     */
    protected FileManagerNode account() throws Exception {
        return delegate.addAccount(accountIdent);
    }

    /**
     * Check that we can create a new account.
     *  
     */
    public void testCreateAccount() throws Exception {
        assertNotNull(this.account());
    }

    /**
     * Check that we get the right Exception for a null name.
     *  
     */
    public void testAddContainerNullName() throws Exception {
        FileManagerNode home = account();
        try {
            home.addFolder(null);
        } catch (IllegalArgumentException ouch) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }



    /**
     * Check that we can add a container.
     *  
     */
    public void testAddContainer() throws Exception {
        FileManagerNode home = account();
        assertNotNull(home.addFolder("frog"));
    }

    /**
     * Check that we get the right Exception for a duplicate.
     *  
     */
    public void testAddContainerDuplicate() throws Exception {
        FileManagerNode home = account();
        home.addFolder("frog");
        try {
            home.addFolder("frog");
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check that a container has the right name.
     *  
     */
    public void testAddContainerName() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        assertEquals("frog", frog.getName());
    }

    /**
     * Check that a container has the right type.
     *  
     */
    public void testAddContainerType() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        assertTrue(frog.isFolder());
        assertFalse(frog.isFile());
        // treenode interface method.
        assertFalse(frog.isLeaf());
        assertTrue(frog.getAllowsChildren());
    }

    /**
     * Check that a container has an Ivorn.
     *  
     */
    public void testAddContainerIvorn() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        assertNotNull(frog.getMetadata().getNodeIvorn());
    }

    /**
     * Check that a container has a parent.
     *  
     */
    public void testAddContainerParent() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        assertNotNull(frog.getParentNode());
        assertNotNull(frog.getParent());
    }

    /**
     * Check that we get the right Exception for a null name.
     *  
     */
    public void testAddChildContainerNullName() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        try {
            frog.addFolder(null);
        } catch (IllegalArgumentException ouch) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }


    /**
     * Check that we can add a nested container.
     *  
     */
    public void testAddChildContainer() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        assertNotNull(frog.addFolder("toad"));
    }

    /**
     * Check that we get the right Exception for a duplicate.
     *  
     */
    public void testAddChildContainerDuplicate() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        frog.addFolder("toad");
        try {
            frog.addFolder("toad");
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check that a child container has the right name.
     *  
     */
    public void testAddChildContainerName() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        assertEquals("toad", toad.getName());
    }

    /**
     * Check that a child container has the right type.
     *  
     */
    public void testAddChildContainerType() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        assertTrue(toad.isFolder());
        assertFalse(toad.isFile());
        assertFalse(toad.isLeaf());
        assertTrue(toad.getAllowsChildren());
    }

    /**
     * Check that a child container has an Ivorn.
     *  
     */
    public void testAddChildContainerIvorn() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        assertNotNull(toad.getMetadata().getNodeIvorn());
    }

    /**
     * Check that we get the right Exception for a null name.
     *  
     */
    public void testAddFileNullName() throws Exception {
        FileManagerNode home = account();
        try {
            home.addFile(null);
        } catch (IllegalArgumentException ouch) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }



    /**
     * Check that we can add a file.
     *  
     */
    public void testAddFile() throws Exception {
        FileManagerNode home = account();
        assertNotNull(home.addFile("frog"));
    }

    /**
     * Check that we get the right Exception for a duplicate.
     *  
     */
    public void testAddFileDuplicate() throws Exception {
        FileManagerNode home = account();
        home.addFile("frog");
        try {
            home.addFile("frog");
        } catch (DuplicateNodeFault ouch) {
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check that a file has the right name.
     *  
     */
    public void testAddFileName() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertEquals("frog", frog.getName());
    }

    /**
     * Check that a file has the right type.
     *  
     */
    public void testAddFileType() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertFalse(frog.isFolder());
        assertTrue(frog.isFile());
        // test for equivalent treenode method.
        assertTrue(frog.isLeaf());
        assertFalse(frog.getAllowsChildren());
    }

    /**
     * Check that a file has an Ivorn.
     *  
     */
    public void testAddFileIvorn() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertNotNull(frog.getIvorn());
    }
    /**
     * Check that a file has a node ivorn;
     *  
     */
    public void testAddFileNodeIvorn() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertNotNull(frog.getMetadata().getNodeIvorn());
        assertFalse(frog.getIvorn().toString().equals(frog.getMetadata().getNodeIvorn().toString()));
    }    

    /**
     * Check that a file has a parent.
     *  
     */
    public void testAddFileParent() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertNotNull(frog.getParentNode());
        assertNotNull(frog.getParent());
    }

    public void testAddFileAttributes() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertNotNull(frog.getMetadata().getAttributes());        
    }
    
    /**
     * Check that we get the right Exception for a null name.
     *  
     */
    public void testGetChildNull() throws Exception {
        FileManagerNode home = account();
        try {
            home.getChild(null);
        } catch (IllegalArgumentException ouch) {
            return;
        }
        fail("Expected IllegalArgumentException");
    }

    /**
     * Check that we get the right Exception for an unknown name.
     *  
     */
    public void testGetChildUnknown() throws Exception {
        FileManagerNode home = account();
        try {
            home.getChild("frog");
        } catch (NodeNotFoundFault ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }

    /**
     * Check that we can find a child node.
     *  
     */
    public void testGetChildValid() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        assertNotNull(home.getChild("frog"));
    }

    /** @todo don't want to support nested nodes as a primitive - makes things more confusing.
     * instead will add helper methods to build a hierarcy for the user, as needed.
     * Check we can add nested nodes.
     *  
     */
    /*
    public void testAddNested() throws Exception {
        Node home = account();
        Node frog = home.addContainer("frog");
        Node toad = frog.addContainer("toad");
        Node newt = toad.addContainer("newt");
        assertNotNull(home.getChild("frog"));
        assertNotNull(home.getChild("frog/toad"));
        assertNotNull(home.getChild("frog/toad/newt"));
    }*/

    /**
     * Check we can get a node by ivorn.
     *  
     */
    public void testGetNode() throws Exception {
        FileManagerNode home = account();
        assertNotNull(delegate.getNode(home.getMetadata().getNodeIvorn()));
    }

    /**
     * Check we can get a (nested) node by ivorn.
     *  
     */
    public void testGetNestedNode() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        FileManagerNode newt = toad.addFolder("newt");
        assertNotNull(delegate.getNode(newt.getMetadata().getNodeIvorn()));
    }

    /**
     * Check that we get the right Exception using a file as a container.
     *  
     */
    public void testFileAddFile() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        try {
            frog.addFile("toad");
        } catch (UnsupportedOperationException ouch) {
            return;
        }
        fail("Expected UnsupportedOperationException");
    }

    /**
     * Check that we get the right Exception using a file as a container.
     *  
     */
    public void testFileAddContainer() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        try {
            frog.addFolder("toad");
        } catch (UnsupportedOperationException ouch) {
            return;
        }
        fail("Expected UnsupportedOperationException");
    }

    /**
     * Check that we get the right Exception using a file as a container.
     *  
     */
    public void testFileGetChild() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        try {
            frog.getChild("frog");
        } catch (UnsupportedOperationException ouch) {
            return;
        }
        fail("Expected UnsupportedOperationException");
    }
    

    /**
     * Check that we get the right Exception using a file as a container.
     *  
     */
    public void testFileGetChildCount() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertEquals(0,frog.getChildCount());
    }    

    /**
     * Check that we get the right Exception using a container as a file.
     *  
     */
    public void testOutputStreamContainer() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        try {
            frog.writeContent();
        } catch (UnsupportedOperationException ouch) {
            return;
        }
        fail("Expected UnsupportedOperationException");
    }

    /**
     * Check that we can get an output stream for a file.
     *  
     */
    public void testOutputStream() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertNotNull(frog.writeContent());
    }

    /**
     * Check that we can send data to a file.
     *  
     */
    public void testOutputStreamWrite() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
    }

    /**
     * Check that we can get an output URL for a file. Check that we can write
     * to an output URL for a file.
     *  
     */

    /**
     * Check that we get the right Exception using a container as a file.
     *  
     */
    public void testInputStreamContainer() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        try {
            frog.readContent();
        } catch (UnsupportedOperationException ouch) {
            return;
        }
        fail("Expected UnsupportedOperationException");
    }

    /**
     * Check that we can get the right Exception for an empty file.
     * 
     * @todo This should be DataNotFoundException ....
     *  
     */
    public void testInputStreamEmpty() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        try {
            frog.readContent();
        } catch (NodeNotFoundFault ouch) {
            return;
        }
        fail("Expected NodeNotFoundFault");
    }

    /**
     * Check that we can get an input stream for a file.
     *  
     */
    public void testInputStream() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
        //
        // Check we can get an input stream.
        assertNotNull(frog.readContent());
    }

    /**
     * Check that we can read data from a file.
     *  
     */
    public void testInputStreamRead() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
        //
        // Open an input stream from our file.
        InputStream input = frog.readContent();
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the file into the buffer.
        new TransferUtil(input, buffer).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }

    /**
     * Check that an empty file still has a location.
     *  
     */
    public void testEmptyFileLocation() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertEquals(filestores[0].toString(), frog.getMetadata().getContentLocation().toString());
    }

    /**
     * Check that a file with data has a location.
     *  
     */
    public void testFileLocationNotNull() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
        //
        // Refresh the node data.
        // (no longer required if the stream is closed)
        //frog.refresh() ;
        //
        // Check the file location
        assertEquals(filestores[0].toString(), frog.getMetadata().getContentLocation().toString());
    }

    /**
     * Check we can rename a file.
     *  
     */
    public void testMoveFileName() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertNotNull(home);
        assertNotNull(home.getMetadata().getNodeIvorn());
        assertNotNull(frog);
        assertNotNull(frog.getParentNode());
        assertNotNull(frog.getParentNode().getMetadata().getNodeIvorn());
        assertEquals(home.getMetadata().getNodeIvorn(), frog.getParentNode().getMetadata().getNodeIvorn());
        frog.move("toad", null, null);
        assertEquals("toad", frog.getName());
        assertEquals(home.getMetadata().getNodeIvorn(), frog.getParentNode().getMetadata().getNodeIvorn());
    }

    /**
     * Check we get the right Exception for a duplicate name.
     *  
     */
    public void testMoveFileNameDuplicate() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        FileManagerNode toad = home.addFile("toad");
        try {
            frog.move("toad", null, null);
        } catch (DuplicateNodeFault ouch) {
            frog.refresh();
            assertEquals("frog", frog.getName());
            assertEquals(home.getIvorn(), frog.getParentNode().getIvorn());
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check we can rename a container.
     *  
     */
    public void testMoveContainerName() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        frog.move("toad", null, null);
        assertEquals("toad", frog.getName());
        assertEquals(home.getIvorn(), frog.getParentNode().getIvorn());
    }

    /**
     * Check that we can still find a child node.
     * 
     * From : home | \- frog | \- toad To: home | \- fish | \- toad
     *  
     */
    public void testMoveContainerNameChild() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        assertNotNull(home.getChild("frog").getChild("toad"));
        frog.move("fish", null, null);
        assertEquals("fish", frog.getName());
        assertNotNull(home.getChild("fish").getChild("toad"));
    }

    /**
     * Check that we get the right Exception for a duplicate name.
     * 
     * From : home | +- frog | | | \- toad | \- fish
     * 
     * To: home | +- fish | | | \- toad | \- fish
     *  
     */
    public void testMoveContainerNameDuplicate() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = frog.addFolder("toad");
        FileManagerNode fish = home.addFolder("fish");
        assertNotNull(home.getChild("frog").getChild("toad"));
        try {
            frog.move("fish", null, null);
        } catch (DuplicateNodeFault ouch) {
            assertEquals("frog", frog.getName());
            assertNotNull(home.getChild("frog").getChild("toad"));
            return;
        }
        fail("Expected DuplicateNodeFault");
    }

    /**
     * Check that we can change the location of an empty file.
     *  
     */
    public void testMoveEmptyFileLocation() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        assertEquals(filestores[0].toString(), frog.getMetadata().getContentLocation().toString());
        frog.move(null, null, filestores[1]);
        assertEquals(filestores[1].toString(), frog.getMetadata().getContentLocation().toString());
    }

    /**
     * Check that we can change the location of a file with data.
     *  
     */
    public void testMoveDataFileLocation() throws Exception {
        //
        // Create the data node.
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");

        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();

        //
        // Check the data location.
        assertEquals(filestores[0].toString(), frog.getMetadata().getContentLocation().toString());
        //
        // Move the file.
        frog.move(null, null, filestores[1]);
        //
        // Check the data location.
        assertEquals(filestores[1].toString(), frog.getMetadata().getContentLocation().toString());

        //
        // Open an input stream from our file.
        InputStream input = frog.readContent();
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the file into the buffer.
        new TransferUtil(input, buffer).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());

    }

    /**
     * Check we get the right properties after an import.
     *  
     */
    public void testImportRefreshSize() throws Exception {
        //
        // Create the data node.
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");

        //
        // Refresh the node properties.
        frog.refresh();
        //
        // Check the data size.
        assertEquals(0, frog.getMetadata().getSize().longValue());

        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();

        //
        // Refresh the node properties.
        // (no loger required if the stream is closed)
        //frog.refresh();
        //
        // Check the data size.
        assertEquals(TEST_BYTES.length, frog.getMetadata().getSize().longValue());
    }
    
    /** check that we're getting mimetype information back after a refresh */
    public void testImportRefreshMimeType() throws Exception {
        //
        // Create the data node.
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog.vot");

        //
        // Refresh the node properties.
        frog.refresh();
        //
        // Check the data size.
        Map origAttribs = frog.getMetadata().getAttributes();
        assertNotNull(origAttribs);
        assertFalse(origAttribs.containsKey(FileProperties.MIME_TYPE_PROPERTY)); // no data, so should't have this key.

        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();

        //
        // Refresh the node properties.
        // (no loger required if the stream is closed)
        //frog.refresh();
        //
        // Check the data size.
        Map newAttribs = frog.getMetadata().getAttributes();
        assertNotNull(newAttribs);
        assertNotNull(newAttribs.get(FileProperties.MIME_TYPE_PROPERTY));
        assertEquals(FileProperties.MIME_TYPE_VOTABLE,newAttribs.get(FileProperties.MIME_TYPE_PROPERTY));
        
    }    

    /**
     * Check we get the right child nodes for an empty node.
     *  
     */
    public void testChildrenEmpty() throws Exception {
        //
        // Create our test node(s).
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        //
        // Get an iterator for the child nodes.
        NodeIterator iter = frog.iterator();
        //
        // Check the iterator contains no nodes.
        assertFalse(iter.hasNext());
        
        // do the same with the tree-node enumeration.
        Enumeration en = frog.children();
        assertFalse(en.hasMoreElements());
        
        assertEquals(0,frog.getChildCount());
    }

    /**
     * Check we get the right child nodes.
     *  
     */
    public void testChildrenValid() throws Exception {
        //
        // Create the data node(s).
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = home.addFile("toad");
        FileManagerNode newt = home.addFolder("newt");
        FileManagerNode fish = newt.addFile("fish");
        //
        // Get an iterator for the child nodes.
        NodeIterator iter = home.iterator();
        //
        // Check the iterator contains the right nodes.
        int totalFound = 0;
        boolean frogFound = false;
        boolean toadFound = false;
        boolean newtFound = false;
        while (iter.hasNext()) {
            FileManagerNode node = iter.nextNode();
            if (node.getMetadata().getNodeIvorn().equals(frog.getMetadata().getNodeIvorn())) {
                totalFound++;
                frogFound = true;
            }
            if (node.getMetadata().getNodeIvorn().equals(toad.getMetadata().getNodeIvorn())) {
                totalFound++;
                toadFound = true;
            }
            if (node.getMetadata().getNodeIvorn().equals(newt.getMetadata().getNodeIvorn())) {
                totalFound++;
                newtFound = true;
            }
        }
        assertEquals(3, totalFound);
        assertTrue(frogFound);
        assertTrue(toadFound);
        assertTrue(newtFound);
        
        // so the same with the treenode enumeration.
        Enumeration en = home.children();
        // reset counters.
        totalFound = 0;
        frogFound = false;
        toadFound = false;
        newtFound = false;
        while (en.hasMoreElements()) {
            FileManagerNode node = (FileManagerNode)en.nextElement();
            if (node.getMetadata().getNodeIvorn().equals(frog.getMetadata().getNodeIvorn())) {
                totalFound++;
                frogFound = true;
            }
            if (node.getMetadata().getNodeIvorn().equals(toad.getMetadata().getNodeIvorn())) {
                totalFound++;
                toadFound = true;
            }
            if (node.getMetadata().getNodeIvorn().equals(newt.getMetadata().getNodeIvorn())) {
                totalFound++;
                newtFound = true;
            }
        }
        assertEquals(3, totalFound);
        assertTrue(frogFound);
        assertTrue(toadFound);
        assertTrue(newtFound);   
        
        // now do the same with the treenode getChildAt methods..
        assertEquals(3,home.getChildCount());
        frogFound = false;
        toadFound = false;
        newtFound = false;        
        for (int i = 0; i < home.getChildCount(); i++) {
            FileManagerNode node = (FileManagerNode)home.getChildAt(i);
            assertNotNull(node);
            assertEquals(i,home.getIndex(node));
            if (node.getMetadata().getNodeIvorn().equals(frog.getMetadata().getNodeIvorn())) {
                frogFound = true;
            }
            if (node.getMetadata().getNodeIvorn().equals(toad.getMetadata().getNodeIvorn())) {
                toadFound = true;
            }
            if (node.getMetadata().getNodeIvorn().equals(newt.getMetadata().getNodeIvorn())) {
                newtFound = true;
            }            
        }
    }

    /**
     * Check we can copy an empty data node.
     *  
     */
    public void testCopyName() throws Exception {
        //
        // Create the data node(s).
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = home.addFile("toad");
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy("newt", null, null);
        //
        // Check we got a copy.
        assertNotNull(copy);
        //
        // Check our copy has the right name.
        assertEquals("newt", copy.getName());
        //
        // Check our copy has the right parent.
        assertEquals(home.getIvorn(), copy.getParentNode().getIvorn());
    }

    /**
     * Check we can create a copy in a different container.
     *  
     */
    public void testCopyParent() throws Exception {
        //
        // Create the data node(s).
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = home.addFile("toad");
        //
        // Create the target container.
        FileManagerNode newt = home.addFolder("newt");
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy(null, newt, null);
        //
        // Check we got a copy.
        assertNotNull(copy);
        //
        // Check our copy has the right name.
        assertEquals("toad", copy.getName());
        //
        // Check our copy has the right parent.
        assertEquals(newt.getIvorn(), copy.getParentNode().getIvorn());
    }

    /**
     * Check we can copy a node with data.
     *  
     */
    public void testCopyData() throws Exception {
        //
        // Create the data node(s).
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = home.addFile("toad");
        //
        // Open an output stream to our file.
        OutputStream output = toad.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy("newt", null, null);
        //
        // Open an input stream from our file.
        InputStream input = copy.readContent();
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the file into the buffer.
        new TransferUtil(input, buffer).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }

    /**
     * Check we can create a copy at a different location.
     *  
     */
    public void testCopyDataLocation() throws Exception {
        //
        // Create the data node(s).
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = home.addFile("toad");
        //
        // Open an output stream to our file.
        OutputStream output = toad.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();

        //
        // Check the data location.
        assertEquals(filestores[0].toString(), toad.getMetadata().getContentLocation().toString());
        //
        // Create a copy of toad.
        FileManagerNode copy = toad.copy("newt", null, filestores[1]);
        //
        // Check the data location.
        assertEquals(filestores[1].toString(), copy.getMetadata().getContentLocation().toString());
        //
        // Open an input stream from our file.
        InputStream input = copy.readContent();
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the file into the buffer.
        new TransferUtil(input, buffer).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }



    /*
     * Check the modified dates.
     *  
     */
    public void testDates() throws Exception {
        //
        // Create the data node(s).
        FileManagerNode home = account();
        FileManagerNode frog = home.addFolder("frog");
        FileManagerNode toad = home.addFile("toad");
        //
        // Check the dates are not null.
        assertNotNull(toad.getMetadata().getCreateDate());
        assertNotNull(toad.getMetadata().getModifyDate());
        //
        // Pause for a bit ....
        Thread.sleep(1000);
        //
        // Open an output stream to our file.
        OutputStream output = toad.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
        //
        // Refresh the node properties.
        toad.refresh();
        //
        // Check the dates are valid.
        assertNotNull(toad.getMetadata().getCreateDate());
        assertNotNull(toad.getMetadata().getModifyDate());
        //
        // Check the modified date is after the create date.
        assertTrue(toad.getMetadata().getModifyDate().after(toad.getMetadata().getCreateDate()));
        //
        // Check we get the original create date.
        assertEquals(toad.getMetadata().getCreateDate(), toad.getMetadata().getCreateDate());
    }

    /**
     * Check we can get an export URL to access node data.
     *  
     */
    public void testExportUrlValid() throws Exception {
        FileManagerNode home = account();
        FileManagerNode frog = home.addFile("frog");
        //
        // Open an output stream to our file.
        OutputStream output = frog.writeContent();
        //
        // Send it some test data.
        output.write(TEST_BYTES);
        output.close();
        //
        // Get a URL to access our data.
        URL url = frog.contentURL();
        assertNotNull(url);
        //
        // Open an input stream to the URL.
        FileStoreInputStream input = new FileStoreInputStream(url);
        input.open();
        //
        // Create a buffer stream.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        //
        // Read the data from the file into the buffer.
        new TransferUtil(input, buffer).transfer();
        input.close();
        //
        // Check we got the right data back.
        assertEquals(TEST_BYTES, buffer.toByteArray());
    }

}

