/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/FileManagerConfigImpl.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerConfigImpl.java,v $
 *   Revision 1.4  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.3.8.5  2005/03/01 23:43:37  nw
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
 *   Revision 1.3.8.3  2005/02/18 15:50:15  nw
 *   lots of changes.
 *   introduced new schema-driven soap binding, got soap-based unit tests
 *   working again (still some failures)
 *
 *   Revision 1.3.8.2  2005/02/11 14:31:36  nw
 *   changes due to refactoring other classes
 *
 *   Revision 1.3.8.1  2005/02/10 16:23:14  nw
 *   formatted code
 *
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
package org.astrogrid.filemanager.server;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.store.Ivorn;

import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Server implementation of the FileManager configuration. This uses the default
 * AstroGrid configuration to get the properties.
 *  
 */
public class FileManagerConfigImpl implements FileManagerConfig {
    /**
     * Our debug logger.
     *  
     */
    private static Log log = LogFactory.getLog(FileManagerConfigImpl.class);

    /**
     * The config property key for our service name.
     *  
     */
    public static final String SERVICE_NAME = "org.astrogrid.filemanager.service.name";

    /**
     * Reference to our AstroGrid config.
     *  
     */
    private Config config;

    /**
     * Public constructor, using the default AstroGrid config.
     *  
     */
    public FileManagerConfigImpl() {
        this(SimpleConfig.getSingleton());
    }

    /**
     * Public constructor, using a specific config.
     * 
     * @param config
     *            Reference to the config to use.
     *  
     */
    public FileManagerConfigImpl(Config config) {
        this.config = config;
    }

    /**
     * Access to the local service name.
     *  
     */
    public String getServiceName() {
        return (String) config.getProperty(SERVICE_NAME);
    }

    /**
     * Access to a config property, using the service name prefix.
     * 
     * @param name
     *            The property name (this is combined with the service name to
     *            make the full property key).
     *  
     */
    public String getServiceProperty(String name) {
        String index = getServiceName() + "." + name;
        String value = (String) config.getProperty(index);
        return value;
    }

    /**
     * Access to the local service ivorn.
     * @throws URISyntaxException
     * 
     * @throws FileManagerServiceException
     *             if unable to read the property.
     *  
     */
    public Ivorn getFileManagerIvorn() throws FileManagerFault  {   
        try {
            return new Ivorn(getServiceProperty("service.ivorn"));
        } catch (URISyntaxException e) {
            throw new FileManagerFault(e.getMessage());
        }
        
    }

    /**
     * Access to the local service ivorn.
     * @throws URISyntaxException
     * 
     * @throws FileManagerServiceException
     *             if unable to read the property.
     *  
     */
    public URI getDefaultStorageServiceURI() throws FileManagerFault  {
        try {
            return new URI(getServiceProperty("filestore.ivorn"));
        } catch (MalformedURIException e) {
            throw new FileManagerFault(e.getMessage());
        }            
    }

    /**
     * @see org.astrogrid.filemanager.server.FileManagerConfig#getBaseDir()
     */
    public File getBaseDir() {
        return new File(getServiceProperty("basedir"));
    }
    
    

}