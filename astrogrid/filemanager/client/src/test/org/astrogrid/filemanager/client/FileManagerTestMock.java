/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/client/Attic/FileManagerTestMock.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerTestMock.java,v $
 *   Revision 1.2  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.1.2.2  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.1.2.1  2005/01/10 15:36:27  dave
 *   Refactored store into a separate interface and mock impl ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client;

import org.astrogrid.filestore.resolver.FileStoreDelegateResolver ;

import org.astrogrid.filemanager.common.FileManagerMock ;
import org.astrogrid.filemanager.common.FileManagerStore ;
import org.astrogrid.filemanager.common.FileManagerConfig ;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory ;

/**
 * A wrapper for the FileManagerMock to enable JUnit tests to setup with a no-param constructor for Axis SOAP tests.
 *
 */
public class FileManagerTestMock
    extends FileManagerMock
    {

    /**
     * The default test config.
     * This is used by the no-param constructor, enabling the JUnit tests to setup the service environment for Axis.
     *
     */
    public static FileManagerConfig config ;

    /**
     * The default data store.
     * This is used by the no-param constructor, enabling the JUnit tests to setup the service environment for Axis.
     *
     */
    public static FileManagerStore store ;

    /**
     * The default store resolver.
     * This is used by the no-param constructor, enabling the JUnit tests to setup the service environment for Axis.
     *
     */
    public static FileStoreDelegateResolver resolver ;

    /**
     * The default ivorn factory.
     * This is used by the no-param constructor, enabling the JUnit tests to setup the service environment for Axis.
     *
     */
    public static FileManagerIvornFactory factory ;

    /**
     * Public constructor, using the default configuration, store, identifier factory and resolver.
     *
     */
    public FileManagerTestMock()
        {
        super(
            config,
            store,
            factory,
            resolver
            );
        }

    }
