/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/FileManagerConfig.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerConfig.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/03/01 23:43:36  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.1  2005/03/01 15:07:34  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.4  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.1.2.3  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.1.2.2  2005/02/11 14:29:02  nw
 *   in the middle of refactoring these.
 *
 *   Revision 1.1.2.1  2005/02/10 18:01:06  jdt
 *   Moved common into client.
 *
 *   Revision 1.3.8.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
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
package org.astrogrid.filemanager.server;

import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI;

import java.io.File;

/**
 * Public interface for the FileManager configuration.
 *  
 */
public interface FileManagerConfig {

    /**
     * Get the manager ivorn.

     *  
     */
    public Ivorn getFileManagerIvorn() throws FileManagerFault;

    /**
     * Get the URI for the default storage service for this filemanager (probably an ivorn to a filestore).

     *  
     */
    public URI getDefaultStorageServiceURI() throws FileManagerFault;
    
    /** get a file location where persistent data is to be stored */
    public File getBaseDir() ;

}