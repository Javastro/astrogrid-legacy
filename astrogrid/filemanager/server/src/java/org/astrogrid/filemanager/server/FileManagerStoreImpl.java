/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/Attic/FileManagerStoreImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerStoreImpl.java,v $
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.2  2005/01/12 14:28:46  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.1  2005/01/10 15:36:28  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.server ;

import org.astrogrid.filemanager.common.FileManagerStore;
import org.astrogrid.filemanager.common.FileManagerStoreMock;

/**
 * A database backed implementation of the store interface.
 * Current implementation just extends the HashTable backed mock.
 *
 */
public class FileManagerStoreImpl
    extends FileManagerStoreMock
    implements FileManagerStore
    {
    /**
     * Public constructor.
     *
     */
    public FileManagerStoreImpl()
        {
        super();
        }
    }
