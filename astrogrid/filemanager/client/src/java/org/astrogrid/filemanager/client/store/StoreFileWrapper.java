/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/store/Attic/StoreFileWrapper.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: StoreFileWrapper.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.4  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.1.2.3  2005/01/10 11:29:29  dave
 *   Tweaked comments for getPath() ....
 *
 *   Revision 1.1.2.2  2005/01/07 12:49:28  dave
 *   dded initial get path impl ...
 *
 *   Revision 1.1.2.1  2005/01/07 12:18:00  dave
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

import java.util.Date;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.StoreClient;

import org.astrogrid.filemanager.common.exception.NodeNotFoundException;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException;

import org.astrogrid.filemanager.client.FileManagerNode;

/**
 * A wrapper for the FileManager delegate nodes to implement the StoreFile API.
 *
 */
public class StoreFileWrapper
    implements StoreFile
    {

    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(StoreFileWrapper.class);

    /**
     * The FileManagerNode we are wrapping.
     *
     */
    private FileManagerNode node ;

    /**
     * Protected constructor.
     * @param node The FileManagerNode we are wrapping.
     *
     */
    protected StoreFileWrapper(FileManagerNode node)
        {
        this.node = node ;
        }

    /**
     * Return the full node Ivorn.
     *
     */
    public Ivorn getIvorn()
        {
        try {
            return node.ivorn() ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            return null ;
            }
        }

    /**
     * Return the file/folder/table name without path
     *
     */
    public String getName()
        {
        return node.name() ;
        }

    /**
     * Returns parent folder of this file/folder
     *
     */
    public StoreFile getParent()
        {
        return null ;
        }

    /**
     * Returns the owner of the file.
     * @todo - proper ownership stuff
     *
     */
    public String getOwner()
        {
        throw new UnsupportedOperationException(
            "Not implemented (yet)"
            );
        }

    /**
     * Returns the creation date (null if unknown).
     *
     */
    public Date getCreated()
        {
        throw new UnsupportedOperationException(
            "Not implemented (yet)"
            );
        }

    /**
     * Returns the date the file was last modified (null if unknown).
     *
     */
    public Date getModified()
        {
        throw new UnsupportedOperationException(
            "Not implemented (yet)"
            );
        }

    /**
     * Returns the size of the file in bytes (-1 if unknown)
     *
     */
    public long getSize()
        {
        return node.size();
        }

    /**
     * Returns the mime type (null if unknown)
     *
     */
    public String getMimeType()
        {
        throw new UnsupportedOperationException(
            "Not implemented (yet)"
            );
        }

    /**
     * Returns true if this is a container that can hold other files/folders.
     *
     */
    public boolean isFolder()
        {
        return node.isContainer() ;
        }

    /**
     * Returns true if this is a self-contained file.  For example, a database
     * table might be represented as a StoreFile but it is not a file.
     */
    public boolean isFile()
        {
        return node.isFile() ;
        }

    /**
     * Lists children files if this is a container - returns null otherwise.
     *
     */
    public StoreFile[] listFiles()
        {
        throw new UnsupportedOperationException(
            "Not implemented (yet)"
            );
        }

    /**
     * Returns the path to this file on the server, including the filename.
     * @todo This may not be what they expect.
     *
     */
    public String getPath()
        {
        try {
            return node.ivorn().getFragment() ;
            }
        catch (FileManagerIdentifierException ouch)
            {
            return null ;
            }
        }

    /**
     * Returns true if this represents the same file as the given one, within this server.
     * This won't check for references from different stores tothe same file
     * Compares the ivorns of the two nodes.
     *
     */
    public boolean equals(StoreFile file)
        {
        if (file instanceof StoreFileWrapper)
            {
            StoreFileWrapper that = (StoreFileWrapper) file ;
            return this.getIvorn().toString().equals(
                that.getIvorn().toString()
                ) ;
            }
        else {
            return false ;
            }
        }

    /**
     * Should always override hash code as well.
     * @return The hash code of the ivorn (as a string).
     *
     */
    public int hashCode()
        {
        return this.getIvorn().toString().hashCode();
        }
    }

