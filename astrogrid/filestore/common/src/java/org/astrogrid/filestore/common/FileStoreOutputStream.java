/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreOutputStream.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreOutputStream.java,v $
 *   Revision 1.3  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.2.10.1  2005/01/17 17:19:21  dave
 *   Fixed bug in FileManagerImpl test (missing '/' in repository path on Unix) ...
 *   Changed tabs to spaces ..
 *
 *   Revision 1.2  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.4  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.1.2.3  2004/10/29 13:21:58  dave
 *   Added InputStream wrapper ...
 *
 *   Revision 1.1.2.2  2004/10/29 12:36:41  dave
 *   Fixed http connection bug ...
 *
 *   Revision 1.1.2.1  2004/10/29 12:22:07  dave
 *   Added OutputStream wrapper ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.HttpURLConnection ;
import java.net.MalformedURLException ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.filestore.common.transfer.mock.Handler ;

/**
 * A wrapper for PUT transfer streams.
 *
 */
public class FileStoreOutputStream
    extends OutputStream
    {

    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreOutputStream.class);

    /**
     * Our default buffer size.
     *
     */
    public static final int BUFFER_SIZE = 1024 ;

    /**
     * Create a FileStoreOutputStream from a string URL.
     * @param string A string representation of the target URL.
     * @throws MalformedURLException
     *
     */
    public FileStoreOutputStream(String string)
        throws MalformedURLException
        {
        this(
            new URL(
                string
                )
            );
        }

    /**
     * Create a FileStoreOutputStream from a URL.
     * @param url The target URL to connect to.
     *
     */
    public FileStoreOutputStream(URL url)
        {
        if (null == url)
            {
            throw new IllegalArgumentException(
                "Null url"
                );
            }
        this.url = url ;
        }

    /**
     * Our url.
     *
     */
    private URL url ;

    /**
     * Our URL connection.
     *
     */
    private URLConnection conn ;

    /**
     * Our HTTP connection.
     *
     */
    private HttpURLConnection http ;

    /**
     * Our underlying stream.
     *
     */
    private OutputStream stream ;

    /**
     * Open our connection.
     * @todo Better exception handling ....
     * @todo Add support for other protocols, ftp etc ...
     *
     */
    public void open()
        throws IOException
        {
        log.debug("");
        log.debug("FileStoreOutputStream.open()");
        log.debug("  URL  : " + url);
        //
        // If our URL is a http:// URL.
        if ("http".equals(url.getProtocol()))
            {
            log.debug("  Handling http URL");
            this.http = (HttpURLConnection) url.openConnection() ;
            this.http.setAllowUserInteraction(false);
            this.http.setDoInput(true);
            this.http.setDoOutput(true) ;
            this.http.setUseCaches(false);
            this.http.setRequestMethod("PUT");
            this.http.setRequestProperty("User-Agent", this.getClass().getName());
//            connection.setRequestProperty("Content-Type",   "application/octet-stream");
//            connection.setRequestProperty("Content-Length", "" + contentLength);
            this.http.connect();
            //
            // Create our stream buffer.
            this.stream = new BufferedOutputStream(
                this.http.getOutputStream(),
                BUFFER_SIZE
                );
            }
        //
        // If the URL is a file:// URL.
        else if ("file".equals(url.getProtocol()))
            {
            log.debug("  Handling file URL");
            log.debug("  Path : " + url.getPath());
            //
            // Create our file output stream
            this.stream = new FileOutputStream(
                url.getPath()
                );
            }
        //
        // If the URL points to a live server.
        else {
            log.debug("  Handling generic URL");
            this.conn = url.openConnection() ;
            //
            // Create our stream.
            this.stream = this.conn.getOutputStream() ;
            }
        log.debug("  PASS : Stream open");
        }

    /**
     * Close our connection.
     * @todo Better exception handling ....
     *
     */
    public void close()
        throws IOException
        {
        log.debug("");
        log.debug("FileStoreOutputStream.close()");
        log.debug("  URL  : " + url);
        if (null != this.stream)
            {
            //
            // Flush our output stream.
            this.stream.flush();
            //
            // Close our stream.
            this.stream.close();
            //
            // If our URL is a mock
            if (Handler.PROTOCOL.equals(url.getProtocol()))
                {
                log.debug("  PASS : Closing mock URL");
                }
            //
            // If the URL points to a live server.
            else {
                log.debug("  PASS : Closing live URL");
                //
                // Get the server response.
                if (null != this.http)
                    {
                    int code   = this.http.getResponseCode() ;
                    String msg = this.http.getResponseMessage() ;
                    log.debug("  Response code : " + code) ;
                    log.debug("  Response msg  : " + msg) ;
                    }
                }
            log.debug("  PASS : Stream closed");
            }
        else {
            throw new IOException(
                "Stream not open"
                );
            }
        }

    /**
     * OutputStream method ...
     *
     */
    public void flush()
        throws IOException
        {
        if (null != this.stream)
            {
            this.stream.flush();
            }
        else {
            throw new IOException(
                "Stream not open"
                );
            }
        }

    /**
     * OutputStream method ...
     *
     */
    public void write(byte[] b)
        throws IOException
        {
        if (null != this.stream)
            {
            this.stream.write(b);
            }
        else {
            throw new IOException(
                "Stream not open"
                );
            }
        }

    /**
     * OutputStream method ...
     *
     */
    public void write(byte[] b, int off, int len)
        throws IOException
        {
        if (null != this.stream)
            {
            this.stream.write(b, off, len);
            }
        else {
            throw new IOException(
                "Stream not open"
                );
            }
        }

    /**
     * OutputStream method ...
     *
     */
    public void write(int b)
        throws IOException
        {
        if (null != this.stream)
            {
            this.stream.write(b);
            }
        else {
            throw new IOException(
                "Stream not open"
                );
            }
        }

    }
