/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/servlet/FileStoreServlet.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:19 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreServlet.java,v $
 *   Revision 1.5  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.4.10.3  2004/10/27 11:59:43  dave
 *   Added sevlet debug ...
 *
 *   Revision 1.4.10.2  2004/10/27 10:56:31  dave
 *   Changed inport init to save the details, and simplified tests for debug
 *
 *   Revision 1.4.10.1  2004/10/19 14:56:16  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 *   Revision 1.4  2004/09/17 16:46:14  dave
 *   Fixed servlet deployment in FileStore ...
 *   Changed tabs to spaces in source code ...
 *
 *   Revision 1.3.4.2  2004/09/17 15:10:32  dave
 *   Uncommented the call to servlet deploy in the build script.
 *   Replaced tabs with spaces in source code.
 *
 *   Revision 1.3.4.1  2004/09/17 14:51:41  dave
 *   Added debug to servlet ....
 *
 *   Revision 1.3  2004/09/17 06:57:10  dave
 *   Added commons logging to FileStore.
 *   Updated logging properties in Community.
 *   Fixed bug in AGINAB deployment.
 *   Removed MySpace tests with hard coded grendel address.
 *
 *   Revision 1.2.32.1  2004/09/17 01:08:36  dave
 *   Updated debug to use commons logging API ....
 *
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.1.2.7  2004/08/04 19:48:57  dave
 *   Fixed mime type in servlet
 *
 *   Revision 1.1.2.6  2004/08/04 19:15:28  dave
 *   Refactored servlet to get reply with file contents
 *
 *   Revision 1.1.2.5  2004/08/04 17:03:33  dave
 *   Added container to servlet
 *
 *   Revision 1.1.2.4  2004/08/04 16:10:19  dave
 *   Added config to servlet
 *
 *   Revision 1.1.2.3  2004/08/04 16:06:31  dave
 *   Added config to servlet
 *
 *   Revision 1.1.2.2  2004/08/04 15:56:44  dave
 *   Added config to servlet
 *
 *   Revision 1.1.2.1  2004/08/04 06:35:02  dave
 *   Added initial stubs for servlet ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.server.servlet ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.io.IOException ;
import java.io.PrintWriter ;

import javax.servlet.ServletException ;

import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;

import org.astrogrid.filestore.common.exception.FileStoreException ;

import org.astrogrid.filestore.common.FileStoreConfig ;
import org.astrogrid.filestore.server.FileStoreConfigImpl ;

import org.astrogrid.filestore.server.repository.Repository ;
import org.astrogrid.filestore.server.repository.RepositoryImpl ;
import org.astrogrid.filestore.server.repository.RepositoryContainer ;

/**
 * A file server servlet to handle HTTP GET requests.
 *
 */
public class FileStoreServlet
    extends HttpServlet
    {
    /**
     * Our debug logger.
     *
     */
//    private static Log log = LogFactory.getLog(FileStoreServlet.class);
    private static Log log ;

    /**
     * The default content type (used if the type was not set when the file was stored).
     *
     */
    public static final String DEFAULT_CONTENT_TYPE = "text/plain" ;

    /**
     * Reference to our repository config.
     *
     */
    protected FileStoreConfig config ;

    /**
     * Reference to our local repository.
     *
     */
    protected Repository repository ;

    /**
     * Initialise our servlet.
     *
     */
    public void init()
        throws ServletException
        {
//
// Initialise our logger.
log = LogFactory.getLog(FileStoreServlet.class);
        log.debug("") ;
        log.debug("SERVLET INIT") ;
        log.debug("FileStoreServlet.init()") ;

        //
        // Initialise our config.
        config = new FileStoreConfigImpl() ;
        //
        // Initialise our repository.
        repository = new RepositoryImpl(
            config
            );
        }

    /**
     * Handle a GET request.
     *
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
        {
        log.debug("") ;
        log.debug("SERVLET GET") ;
        log.debug("FileStoreServlet.doGet()") ;
        //
        // Get the request path.
        String path = request.getPathInfo() ;
        log.debug("  Path : " + path) ;
        //
        // If the path is valid.
        if (null != path)
            {
            //
            // Try locating the container.
            try {
                //
                // Strip off the leading '/' and convert to an identifier.
                // This relies on the FileIdentifier to check that the identifier is valid (which it don't at the moment).
                FileIdentifier ident = new FileIdentifier(
                    path.substring(1)
                    ) ;
                log.debug("  Ident : " + ident.toString()) ;
                //
                // Locate the container.
                RepositoryContainer container = repository.load(
                    ident.toString()
                    ) ;
                log.debug("PASS : Got container ....") ;
                //
                // Set the response content type.
                String type = container.properties().getProperty(
                    FileProperties.MIME_TYPE_PROPERTY
                    ) ;
                if (null != type)
                    {
                    response.setContentType(
                        type
                        );
                    }
                else {
                    response.setContentType(
                        DEFAULT_CONTENT_TYPE
                        );
                    }
                //
                // Transfer the data in our response.
                container.exportData(
                    response.getOutputStream()
                    ) ;
                }
            catch (FileStoreException ouch)
                {
                log.warn("----") ;
                log.warn("Exception transferring data from repository") ;
                log.warn(ouch.toString()) ;
                log.warn("----") ;
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    ouch.getMessage()
                    ) ;
                }
            }
        }

    /**
     * Handle a PUT request.
     *
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
        {
        log.debug("") ;
        log.debug("SERVLET PUT") ;
        log.debug("FileStoreServlet.doPut()") ;
        //
        // Get the request path.
        String path = request.getPathInfo() ;
        log.debug("  Path : " + path) ;
        //
        // If the path is valid.
        if (null != path)
            {
            //
            // Try locating our container.
            try {
//
// ** Currently assumes new file, append later.
//
                //
                // Strip off the leading '/' and convert to an identifier.
                // This relies on the FileIdentifier to check that the identifier is valid (which it don't at the moment).
                FileIdentifier ident = new FileIdentifier(
                    path.substring(1)
                    ) ;
                log.debug("  Ident : " + ident.toString()) ;
                //
                // Locate the container.
                RepositoryContainer container = repository.load(
                    ident.toString()
                    ) ;
                log.debug("PASS : Got container ....") ;
                //
                // Transfer the data in our response.
	            container.importData(
					request.getInputStream()
	                ) ;
                log.debug("PASS : Transfer completed ....") ;

log.debug("") ;
log.debug("NOTIFY MANAGER ....") ;
log.debug("") ;

                }
            catch (FileStoreException ouch)
                {
                log.warn("----") ;
                log.warn("Exception transferring data into repository") ;
                log.warn(ouch.toString()) ;
                log.warn("----") ;
                response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    ouch.getMessage()
                    ) ;
                }


            }
        }
    }

