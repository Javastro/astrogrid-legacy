/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/Attic/FileManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerImpl.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:28:46  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2.4.1  2005/01/10 15:36:28  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 *   Revision 1.2  2004/11/25 00:20:30  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.1  2004/11/17 07:56:33  dave
 *   Added server mock and webapp build scripts ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.server ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerConfig;
import org.astrogrid.filemanager.common.FileManagerStore;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.resolver.FileStoreDelegateResolver;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverImpl;

/**
 * The public interface for a file manager service.
 *
 */
public class FileManagerImpl
    extends FileManagerMock
    implements FileManager
    {

    /**
     * Public constructor, using the default configuration, identifier factory and resolver.
     *
     */
    public FileManagerImpl()
        {
        this(
            new FileManagerConfigImpl(),
            new FileManagerStoreImpl(),
            new FileManagerIvornFactory(),
            new FileStoreDelegateResolverImpl()
            );
        }

    /**
     * Public constructor, using a custom configuration, identifier factory and resolver.
     * @param config The local file manager configuration.
     * @param store The local file manager store.
     * @param factory A factory for creating resource identifiers.
     * @param resolver A resolver to locate filestores.
     *
     */
    public FileManagerImpl(
        FileManagerConfig config,
        FileManagerStore store,
        FileManagerIvornFactory factory,
        FileStoreDelegateResolver resolver
        )
        {
        super(
            config,
            store,
            factory,
            resolver
            );
        }

    }
