/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/FileStoreConfigImpl.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:04 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreConfigImpl.java,v $
 *   Revision 1.4  2005/01/28 10:44:04  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.3.8.1  2005/01/17 17:19:21  dave
 *   Fixed bug in FileManagerImpl test (missing '/' in repository path on Unix) ...
 *   Changed tabs to spaces ..
 *
 *   Revision 1.3  2004/12/13 00:51:56  jdt
 *   merge from FLS_JDT_861
 *
 *   Revision 1.2.2.1  2004/12/09 11:54:53  jdt
 *   Made file:// windows-friendly.
 *
 *   Revision 1.2  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/17 19:06:30  dave
 *   Updated server configuration ...
 *
 *   Revision 1.1.2.1  2004/10/19 14:56:16  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 *   Revision 1.3  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.2.22.2  2004/08/09 10:16:28  dave
 *   Added resource URL to the properties.
 *
 *   Revision 1.2.22.1  2004/08/06 22:25:06  dave
 *   Refactored bits and broke a few tests ...
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.1  2004/07/08 07:31:30  dave
 *   Added container impl and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.server ;

import java.io.File ;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.MalformedURLException ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;
import org.astrogrid.config.PropertyNotFoundException ;

import org.astrogrid.filestore.common.ivorn.FileStoreIvornFactory ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;

import org.astrogrid.filestore.common.FileStoreConfig ;

/**
 * Configuration implementation using the AstroGrid Config.
 *
 */
public class FileStoreConfigImpl
    implements FileStoreConfig
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreConfigImpl.class);

    /**
     * The config property key for our service name.
     *
     */
    public static final String SERVICE_NAME = "org.astrogrid.filestore.service.name" ;

    /**
     * Reference to our AstroGrid config.
     *
     */
    private Config config ;

    /**
     * Public constructor, using the default AstroGrid config.
     *
     */
    public FileStoreConfigImpl()
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
    public FileStoreConfigImpl(Config config)
        {
        this.config = config ;
        }

    /**
     * Access to the local service name.
     *
     */
    public String getServiceName()
        {
        String name = (String) config.getProperty(
            SERVICE_NAME
            ) ;
        return name ;
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
     * @throws FileStoreServiceException if unable to read the property.
     *
     */
    public Ivorn getServiceIvorn()
        throws FileStoreServiceException
        {
        log.debug("");
        log.debug("FileStoreConfigImpl.getServiceIvorn()");
        try {
            return new Ivorn(
                getServiceProperty(
                    "service.ivorn"
                    )
                ) ;
            }
        catch (Throwable ouch)
            {
            log.error("Unable to read service ivorn from config", ouch);
            throw new FileStoreServiceException(
                "Unable to read service ivorn from config.",
                ouch
                ) ;
            }
        }

    /**
     * The local service URL.
     * @throws FileStoreServiceException if unable to read the property.
     *
     */
    public URL getServiceUrl()
        throws FileStoreServiceException
        {
        log.debug("");
        log.debug("FileStoreConfigImpl.getServiceUrl()");
        try {
            return new URL(
                getServiceProperty(
                    "service.url"
                    )
                ) ;
            }
        catch (Throwable ouch)
            {
            throw new FileStoreServiceException(
                "Unable to generate service URL.",
                ouch
                ) ;
            }
        }

    /**
     * Generate a resource ivorn.
     * @param ident - the resource identifier.
     * @throws FileStoreServiceException if unable to read the property.
     *
     */
    public Ivorn getResourceIvorn(String ident)
        throws FileStoreServiceException
        {
        log.debug("");
        log.debug("FileStoreConfigImpl.getResourceIvorn()");
        log.debug("  Ident : " + ident);
        try {
            return FileStoreIvornFactory.createIvorn(
                getServiceProperty(
                    "service.ivorn"
                    ),
                ident
                ) ;
            }
        catch (Throwable ouch)
            {
            throw new FileStoreServiceException(
                "Unable to generate resource ivorn.",
                ouch
                ) ;
            }
        }

    /**
     * Generate a resource URL.
     * @param ident - the resource identifier.
     * @throws FileStoreServiceException if unable to read the property.
     *
     */
    public URL getResourceUrl(String ident)
        throws FileStoreServiceException
        {
        log.debug("");
        log.debug("FileStoreConfigImpl.getResourceUrl()");
        log.debug("  Ident : " + ident);
        try {
            return new URL(
                getServiceProperty(
                    "service.url"
                    )
                + "/" + ident
                ) ;
            }
        catch (Throwable ouch)
            {
            throw new FileStoreServiceException(
                "Unable to generate resource URL.",
                ouch
                ) ;
            }
        }

    /**
     * The repository data directory.
     * @throws FileStoreServiceException if unable to read the property.
     *
     */
    public File getDataDirectory()
        throws FileStoreServiceException
        {
        log.debug("");
        log.debug("FileStoreConfigImpl.getDataDirectory()");
        try {
            return new File(
                getServiceProperty(
                    "repository"
                    )
                ) ;
            }
        catch (Throwable ouch)
            {
            throw new FileStoreServiceException(
                "Unable to read data directory from config.",
                ouch
                ) ;
            }
        }

    /**
     * The repository info directory.
     * @throws FileStoreServiceException if unable to read the property.
     *
     */
    public File getInfoDirectory()
        throws FileStoreServiceException
        {
        log.debug("");
        log.debug("FileStoreConfigImpl.getInfoDirectory()");
        try {
            return new File(
                getServiceProperty(
                    "repository"
                    )
                ) ;
            }
        catch (Throwable ouch)
            {
            throw new FileStoreServiceException(
                "Unable to read info directory from config.",
                ouch
                ) ;
            }
        }
    }



