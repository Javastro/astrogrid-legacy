/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/store/Attic/StoreClientWrapper.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: StoreClientWrapper.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.2  2005/01/26 12:24:22  dave
 *   Removed type from add(), replaced with addNode() and addFile() ...
 *
 *   Revision 1.1.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.2.2.2  2005/01/21 13:07:37  dave
 *   Added exportURL to the node API ...
 *
 *   Revision 1.2.2.1  2005/01/21 12:18:54  dave
 *   Changed input() and output() to exportStream() and importStream() ...
 *
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.4  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.1.2.3  2005/01/07 12:49:28  dave
 *   dded initial get path impl ...
 *
 *   Revision 1.1.2.2  2005/01/07 12:18:00  dave
 *   Added StoreClientWrapperTest
 *   Added StoreFileWrapper
 *
 *   Revision 1.1.2.1  2004/12/22 07:38:36  dave
 *   Started to move towards StoreClient API ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client.store;

import java.net.URL;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.User;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.StoreClient;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;
import org.astrogrid.filemanager.common.exception.NodeNotFoundException;
import org.astrogrid.filemanager.common.exception.DuplicateNodeException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;

import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.delegate.FileManagerDelegate;

/**
 * A wrapper for the FileManager delegate to implement the StoreClient API.
 *
 */
public class StoreClientWrapper
    implements StoreClient
    {

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(StoreClientWrapper.class);

    /**
     * Our Ivorn factory.
     *
     */
    private FileManagerIvornFactory factory = new FileManagerIvornFactory();

    /**
     * The FileManager delegate this wrapper is for.
     *
     */
    private FileManagerDelegate delegate ;

    /**
     * Public constructor.
     *
     */
    public StoreClientWrapper(FileManagerDelegate delegate)
        {
        if (null == delegate)
            {
            throw new IllegalArgumentException(
                "Null FileManager delegate"
                );
            }
        this.delegate = delegate ;
        }

    /**
     * The service ivorn.
     *
     */
    private Ivorn ivorn ;

    /**
     * Get the service ivorn.
     *
     */
    public Ivorn getIvorn()
        {
        log.debug("StoreClientWrapper.getIvorn()");
        if (null == this.ivorn)
            {
            try {
                this.ivorn = delegate.getServiceIvorn();
                }
            catch(FileManagerServiceException ouch)
                {
                log.debug("Failed to get service Ivorn");
                }
            }
        log.debug("  Ivorn : " + this.ivorn.toString());
        return this.ivorn ;
        }

    /**
     * The current user ** Not used **.
     *
     */
    private User user ;

    /**
     * Returns the user of this delegate.
     * Not supported, throws UnsupportedOperationException.
     * @throws UnsupportedOperationException
     *
     */
    public User getOperator()
        {
        log.warn("StoreClientWrapper.getOperator()");
        return this.user ;
        }

    /**
     * Returns the Agsl to the service this client is connected to.
     * @return An Agsl based on the FileManager Ivorn.
     * @todo Do something sensible if it can't get the FileManager Ivorn.
     *
     */
    public Agsl getEndpoint()
        {
        log.debug("StoreClientWrapper.getEndpoint()");
        return new Agsl(
            new Msrl(
                this.getIvorn(),
                null
                )
            );
        }

    /**
     * Returns the Agsl for the given source path. This should be sufficient
     * that we can reach the file for writing to as well; if this requires a
     * delegate, then the agsl should include the delegate endpoint (eg myspace)
     * rather than a read-only URL
     * @param path The path of the file to access.
     *
     */
    public Agsl getAgsl(String path)
        throws IOException
        {
        log.debug("StoreClientWrapper.getAgsl()");
        try {
            return new Agsl(
                new Msrl(
                    factory.ivorn(
                        this.getIvorn(),
                        path
                        ),
                    null
                    )
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new IOException(
                "Unable to parse ivorn address"
                );
            }
        }

    /**
     * Returns a tree representation of the files that match the expression.
     * Not supported.
     *
     */
    public StoreFile getFiles(String filter)
        throws IOException
        {
        throw new UnsupportedOperationException(
            "Wildcard path is not supported"
            );
        }

    /**
     * Returns the StoreFile representation of the file at the given path.
     *
     */
    public StoreFile getFile(String path)
        throws IOException
        {
        log.debug("StoreClientWrapper.getFile()");
        log.debug("  Path : " + path);
        try {
            Ivorn ivorn = factory.ivorn(
                this.getIvorn(),
                path
                );
            log.debug("  Ivorn : " + ivorn.toString());
            return new StoreFileWrapper(
                delegate.getNode(
                    ivorn
                    )
                );
            }
        catch (NodeNotFoundException ouch)
            {
            throw new FileNotFoundException(
                ouch.getMessage()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        catch (FileManagerServiceException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        }

    /**
     * Get the parent from a path.
     * @param path The path to parse.
     * @return The parent path.
     *
     */
    protected String getFilePath(String path)
        {
        int index  = path.lastIndexOf('/');
        //
        // If there is no path separator.
        if (index == -1)
            {
            return null ;
            }
        //
        // If the '/' is at the start
        if (index == 0)
            {
            return null ;
            }
        //
        // Return the parent path.
        return path.substring(
            0,
            index
            );
        }

    /**
     * Get the name from a path.
     * @param path The path to parse.
     * @return The filename.
     *
     */
    protected String getFileName(String path)
        {
        //
        // If the path separator is at the end.
        if (path.endsWith("/"))
            {
            return null ;
            }
        //
        // If there is no path separator.
        int index = path.lastIndexOf('/');
        if (index == -1)
            {
            return path ;
            }
        //
        // Return the file name.
        return path.substring(
            index + 1
            );
        }

    /**
     * Create a new container.
     * @param target The path of the new folder.
     *
     */
    public void newFolder(String target)
        throws IOException
        {
        log.debug("StoreClientWrapper.newFolder(String)");
        log.debug("  Path  : " + target);
        //
        // Separate the path into path and name.
        String name = getFileName(target);
        String path = getFilePath(target);
        if (null == path)
            {
            throw new FileNotFoundException(
                "Unable to parse file path"
                );
            }
        if (null == name)
            {
            throw new FileNotFoundException(
                "Unable to parse file name"
                );
            }
        try {
            //
            // Generate the parent ivorn.
            Ivorn ivorn = factory.ivorn(
                this.getIvorn(),
                path
                );
            log.debug("  Ivorn : " + ivorn.toString());
            //
            // Find the parent node.
            FileManagerNode parent = delegate.getNode(
                ivorn
                );
            //
            // Check it is a container.
            if (parent.isContainer() == false)
                {
                throw new FileNotFoundException(
                    "Parent node is not a container"
                    );
                }
            //
            // Create the new node.
            parent.addNode(
                name
                );
            }
        catch (NodeNotFoundException ouch)
            {
            throw new FileNotFoundException(
                ouch.getMessage()
                );
            }
        catch (DuplicateNodeException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        catch (FileManagerServiceException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        }

    /**
     * Create a new file.
     * @param string The path of the new file.
     *
     */
    public FileManagerNode newFile(String target)
        throws IOException
        {
        log.debug("StoreClientWrapper.newFile(String)");
        log.debug("  Path  : " + target);
        //
        // Separate the path into path and name.
        String name = getFileName(target);
        String path = getFilePath(target);
        if (null == path)
            {
            throw new FileNotFoundException(
                "Unable to parse file path"
                );
            }
        if (null == name)
            {
            throw new FileNotFoundException(
                "Unable to parse file name"
                );
            }
        try {
            //
            // Generate the parent ivorn.
            Ivorn ivorn = factory.ivorn(
                this.getIvorn(),
                path
                );
            log.debug("  Ivorn : " + ivorn.toString());
            //
            // Find the parent node.
            FileManagerNode parent = delegate.getNode(
                ivorn
                );
            //
            // Check it is a container.
            if (parent.isContainer() == false)
                {
                throw new FileNotFoundException(
                    "Parent node is not a container"
                    );
                }
            //
            // Create the new node.
            return parent.addFile(
                name
                );
            }
        catch (NodeNotFoundException ouch)
            {
            throw new FileNotFoundException(
                ouch.getMessage()
                );
            }
        catch (DuplicateNodeException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        catch (FileManagerServiceException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        }

    /**
     * Create a new node.
     * @param target The node path.
     * @param type   The node type.
     *
    protected FileManagerNode addNode(String target, String type)
        throws IOException
        {
        log.debug("StoreClientWrapper.addNode(String, String)");
        }
     */

    /**
     * Puts the given byte buffer from offset of length bytes, to the given target.
     * @param bytes  The byte array to store.
     * @param offset The starting offset within the array.
     * @param length The the number of bytes to transfer from the array.
     * @param path   The path of the file to store the data.
     * @param append Not supported.
     *
     */
    public void putBytes(byte[] bytes, int offset, int length, String path, boolean append)
        throws IOException
        {
        if (append)
            {
            throw new UnsupportedOperationException(
                "Append is not supported"
                );
            }
        //
        // Open an output stream to the target node.
        OutputStream stream = putStream(
            path,
            append
            );
        //
        // Transfer the data.
        stream.write(
            bytes,
            offset,
            length
            );
        //
        // Close the stream.
        stream.close();
        }

    /**
     * Puts the given string into the given location.
     * @deprecated - use putBytes() or stream to putStream()
     * @param contents The string to store.
     * @param path   The path of the file to store the data.
     * @param append Not supported.
     *
     */
    public void putString(String contents, String path, boolean append)
        throws IOException
        {
        if (append)
            {
            throw new UnsupportedOperationException(
                "Append is not supported"
                );
            }
        //
        // Open an output stream to the target node.
        OutputStream stream = putStream(
            path,
            append
            );
        //
        // Wrap it in a writer.
        OutputStreamWriter writer = new OutputStreamWriter(
            stream
            );
        //
        // Transfer the data.
        writer.write(
            contents
            );
        //
        // Close the stream.
        writer.close();
        }

    /**
     * Copies the contents of the file at the given source url to the given location.
     * @param source The source URL of the data to transfer.
     * @param path   The path of the file to store the data.
     * @param append Not supported.
     *
     */
    public void putUrl(URL source, String path, boolean append)
        throws IOException
        {
        if (append)
            {
            throw new UnsupportedOperationException(
                "Append is not supported"
                );
            }
        throw new UnsupportedOperationException(
            "Put URL is not supported (yet)"
            );
        }

    /**
     * Streaming output - returns a stream that can be used to output to the given location.
     * @param path The path of the file to send the data to.
     * @param flag Append flag (not supported).
     *
     */
    public OutputStream putStream(String path, boolean flag)
        throws IOException
        {
        log.debug("StoreClientWrapper.putStream(String, boolean)");
        log.debug("  Path : " + path);
        log.debug("  Flag : " + flag);
        if (flag)
            {
            throw new UnsupportedOperationException(
                "Append is not supported"
                );
            }
        try {
            //
            // Create the node ivorn.
            Ivorn ivorn = factory.ivorn(
                this.getIvorn(),
                path
                );
            log.debug("  Ivorn : " + ivorn.toString());
            //
            // Try to find the target node.
            FileManagerNode node = null ;
            try {
                node = delegate.getNode(
                    ivorn
                    );
                }
            //
            // Create the node if it does not exist.
            catch (NodeNotFoundException ouch)
                {
                node = this.newFile(
                    path
                    );
                }
            //
            // Return an output stream to the node.
            return node.importStream();
            }
        catch (NodeNotFoundException ouch)
            {
            throw new FileNotFoundException(
                ouch.getMessage()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        catch (FileManagerServiceException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        }

    /**
     * Gets a file's contents as a stream.
     * @param path  The path of the file to read the data from.
     *
     */
    public InputStream getStream(String path)
        throws IOException
        {
        log.debug("StoreClientWrapper.getStream(String)");
        log.debug("  Path : " + path);
        try {
            //
            // Create the node ivorn.
            Ivorn ivorn = factory.ivorn(
                this.getIvorn(),
                path
                );
            log.debug("  Ivorn : " + ivorn.toString());
            //
            // Try to find the target node.
            FileManagerNode node = 
                delegate.getNode(
                    ivorn
                    );
            //
            // Return an output stream to the node.
            return node.exportStream();
            }
        catch (NodeNotFoundException ouch)
            {
            throw new FileNotFoundException(
                ouch.getMessage()
                );
            }
        catch (FileManagerIdentifierException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        catch (FileManagerServiceException ouch)
            {
            throw new IOException(
                ouch.getMessage()
                );
            }
        }

    /**
     * Gets the url to the given source file.
     * @deprecated? don't think we should always publish files as URLs... mch
     * @param path The path of the file to access.
     *
     */
    public URL getUrl(String path)
        throws IOException
        {
        log.debug("StoreClientWrapper.getUrl(String)");
        log.debug("  Path : " + path);
        throw new UnsupportedOperationException(
            "Get URL is not supported (yet)"
            );
        }

    /**
     * Delete a file.
     * @param path The path of the file to delete.
     *
     */
    public void delete(String path)
        throws IOException
        {
        throw new UnsupportedOperationException(
            "Delete not implemented (yet)"
            );
        }

    /**
     * Copy a file to a target Agsl.
     * @param path   The path of the data to copy.
     * @param target The destination to copy the data to.
     *
     */
    public void copy(String path, Agsl target)
        throws IOException
        {
        throw new UnsupportedOperationException(
            "Copy not implemented (yet)"
            );
        }

    /**
     * Copy a file from a source Agsl
     * @param source The Agsl of the data to copy.
     * @param path   The path of the file to copy the data to.
     *
     */
    public void copy(Agsl source, String path)
        throws IOException
        {
        throw new UnsupportedOperationException(
            "Copy not implemented (yet)"
            );
        }

    /**
     * Move a file to a target Agsl.
     * @param path   The path of the data to move.
     * @param target The destination to move the data to.
     *
     */
    public void move(String path, Agsl target)
        throws IOException
        {
        throw new UnsupportedOperationException(
            "Move not implemented (yet)"
            );
        }

    /**
     * Move a file from a source Agsl.
     * @param source The source of the data to move.
     * @param path   The destination to move the data to.
     *
     */
    public void move(Agsl source, String path)
        throws IOException
        {
        throw new UnsupportedOperationException(
            "Move not implemented (yet)"
            );
        }



    }

