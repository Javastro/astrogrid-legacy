/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/delegate/Attic/FileManagerMockDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerMockDelegate.java,v $
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/22 07:54:16  dave
 *   Refactored delegate into a separate package ....
 *
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2.4.1  2005/01/10 15:36:27  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.1  2004/11/13 01:41:26  dave
 *   Created initial client API ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client.delegate ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerStore;
import org.astrogrid.filemanager.common.FileManagerConfig;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.resolver.FileStoreDelegateResolver ;

/**
 * The core implementation for the FileManager delegate.
 *
 */
public class FileManagerMockDelegate
    extends FileManagerCoreDelegate
    implements FileManagerDelegate
    {
    /**
     * Our debug logger.
     *
     */
    protected static Log log = LogFactory.getLog(FileManagerMockDelegate.class);

    /**
     * Public constructor.
     * Creates a new mock service with the parameters passed in.
     * @param config   The local file manager configuration.
     * @param store    The local file manager store.
     * @param factory  A factory for creating resource identifiers.
     * @param resolver A resolver to locate filestores.
     *
     */
    public FileManagerMockDelegate(
        FileManagerConfig config,
        FileManagerStore store,
        FileManagerIvornFactory factory,
        FileStoreDelegateResolver resolver
        )
        {
        super(
            new FileManagerMock(
                config,
                store,
                factory,
                resolver
                )
            );
        }
    }
