/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerConfig.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerConfig.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.1  2004/10/21 21:08:52  dave
 *   Added config interface and mock implementation.
 *   Partial implementation of data import into FileStore via direct URL transfer.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;

/**
 * Public interface for the FileManager configuration.
 *
 */
public interface FileManagerConfig
    {

    /**
     * Get the manager ivorn.
     * @throws FileManagerServiceException If unable to read the configuration.
     *
     */
    public Ivorn getFileManagerIvorn()
        throws FileManagerServiceException ;

    /**
     * Get the default FileStore Ivorn.
     * @throws FileManagerServiceException If unable to read the configuration.
     *
     */
    public Ivorn getFileStoreIvorn()
        throws FileManagerServiceException ;

    }
