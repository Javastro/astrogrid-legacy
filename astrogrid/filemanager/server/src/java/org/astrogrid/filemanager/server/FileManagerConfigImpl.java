/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/FileManagerConfigImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerConfigImpl.java,v $
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
 *   Revision 1.1.2.3  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.2  2004/11/17 19:33:17  dave
 *   Fixed imports ...
 *
 *   Revision 1.1.2.1  2004/11/17 19:05:17  dave
 *   Added manager config ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.server ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;

import org.astrogrid.filemanager.common.FileManagerConfig ;
import org.astrogrid.filemanager.common.exception.FileManagerServiceException ;

/**
 * Server implementation of the FileManager configuration.
 * This uses the default AstroGrid configuration to get the properties.
 *
 */
public class FileManagerConfigImpl
    implements FileManagerConfig
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerConfigImpl.class);

    /**
     * The config property key for our service name.
     *
     */
    public static final String SERVICE_NAME = "org.astrogrid.filemanager.service.name" ;

    /**
     * Reference to our AstroGrid config.
     *
     */
    private Config config ;

    /**
     * Public constructor, using the default AstroGrid config.
     *
     */
    public FileManagerConfigImpl()
        {
        this(
            SimpleConfig.getSingleton()
            ) ;
        }

    /**
     * Public constructor, using a specific config.
     * @param config Reference to the config to use.
     *
     */
    public FileManagerConfigImpl(Config config)
        {
        this.config = config ;
        }

    /**
     * Access to the local service name.
     *
     */
    public String getServiceName()
        {
        return (String) config.getProperty(
            SERVICE_NAME
            ) ;
        }

    /**
     * Access to a config property, using the service name prefix.
     * @param name The property name (this is combined with the service name to make the full property key).
     *
     */
    public String getServiceProperty(String name)
        {
        log.debug("");
        log.debug("FileStoreConfigImpl.getServiceProperty()");
        log.debug("  Name  : " + name);
        String index = getServiceName() + "." + name ;
        log.debug("  Index : " + index);
        String value = (String) config.getProperty(
            index
            ) ;
        log.debug("  Value : " + value);
        return value ;
        }

    /**
     * Access to the local service ivorn.
     * @throws FileManagerServiceException if unable to read the property.
     *
     */
    public Ivorn getFileManagerIvorn()
        throws FileManagerServiceException
        {
        try {
            return new Ivorn(
                getServiceProperty(
                    "service.ivorn"
                    )
                ) ;
            }
        catch (Throwable ouch)
            {
            throw new FileManagerServiceException(
                "Unable to read FileManager ivorn from config.",
                ouch
                ) ;
            }
        }

    /**
     * Access to the local service ivorn.
     * @throws FileManagerServiceException if unable to read the property.
     *
     */
    public Ivorn getFileStoreIvorn()
        throws FileManagerServiceException
        {
        try {
            return new Ivorn(
                getServiceProperty(
                    "filestore.ivorn"
                    )
                ) ;
            }
        catch (Throwable ouch)
            {
            throw new FileManagerServiceException(
                "Unable to read FileStore ivorn from config.",
                ouch
                ) ;
            }
        }

    }
