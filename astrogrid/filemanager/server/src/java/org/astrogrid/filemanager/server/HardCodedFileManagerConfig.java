/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/HardCodedFileManagerConfig.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: HardCodedFileManagerConfig.java,v $
 *   Revision 1.2  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/03/01 23:43:36  nw
 *   split code inito client and server projoects again.
 *
 *   Revision 1.1.2.1  2005/03/01 15:07:33  nw
 *   close to finished now.
 *
 *   Revision 1.1.2.2  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.1.2.1  2005/02/25 12:33:27  nw
 *   finished transactional store
 *
 *   Revision 1.1.2.1  2005/02/18 15:50:14  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.1.2.2  2005/02/11 20:04:24  nw
 *   started refactoring
 *
 *   Revision 1.1.2.1  2005/02/11 17:14:13  nw
 *   moved mock implementations of the server nto a separate package.
 *
 *   Revision 1.1.2.2  2005/02/11 14:29:03  nw
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
 *   Revision 1.1.2.3  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.2  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.1  2004/10/21 21:08:52  dave
 *   Added config interface and mock implementation.
 *   Partial implementation of data import into FileStore via direct URL transfer.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.server;

import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Mock implementation of the FileManager configuration.
 *  
 */
public class HardCodedFileManagerConfig implements FileManagerConfig {

    /**
     * Our manager ivorn.
     *  
     */
    private final Ivorn filemanager;

    /**
     * Our default FileStore Ivorn.
     *  
     */
    private final URI filestore;
    
    private final File basedir;


    private HardCodedFileManagerConfig(Ivorn filemanager, URI filestore) {
        this.filemanager = filemanager;
        this.filestore = filestore;
        try {
        basedir= File.createTempFile("filemanager-test-basedir",null);
        basedir.delete();
        basedir.mkdir();
        basedir.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException("Could not create basedir",e);
        }
    }
    
    public HardCodedFileManagerConfig() {
        this(FILEMANAGER,FILESTORE_1_URI);
    }
    
    public static Ivorn FILEMANAGER;

    public static URI FILESTORE_1_URI;
    static {
        try {
        FILEMANAGER  = new Ivorn(BaseTest.FILEMANAGER_IVORN_STRING);
        FILESTORE_1_URI = new URI(BaseTest.FILESTORE_1.toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Could not initialize statics",e);        
        } catch (MalformedURIException e) {
            throw new RuntimeException("Could not initialize statics",e);
        }
    }
    /**
     * Get the manager ivorn.
     * 
     * @throws FileManagerServiceException
     *             If unable to read the configuration.
     *  
     */
    public Ivorn getFileManagerIvorn() {
        return this.filemanager;
    }

    /**
     * Get the default FileStore Ivorn.
     * 
     * @throws FileManagerServiceException
     *  
     */
    public URI getDefaultStorageServiceURI() {
        return this.filestore;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FileManagerConfigMock:");
        buffer.append(" filemanager: ");
        buffer.append(filemanager);
        buffer.append(" filestore: ");
        buffer.append(filestore);
        buffer.append(" basedir: ");
        buffer.append(basedir);
        buffer.append("]");
        return buffer.toString();
    }

    /** returns a new temporary directory.
     * @see org.astrogrid.filemanager.server.FileManagerConfig#getBaseDir()
     */
    public File getBaseDir() {
        return basedir;
    }
}