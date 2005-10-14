/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreOutputStream.java,v $</cvs:source>
 * <cvs:author>$Author: nw $</cvs:author>
 * <cvs:date>$Date: 2005/10/14 12:31:02 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreOutputStream.java,v $
 *   Revision 1.6  2005/10/14 12:31:02  nw
 *   removed setChunkedStreamigMode() - not supported by present server.
 *
 *   Revision 1.5  2005/10/13 17:56:08  nw
 *   documented a quirk. no change to code.
 *
 *   Revision 1.4  2005/09/06 12:45:22  clq2
 *   dave-dev-200507251101
 *
 *   Revision 1.3.60.4  2005/08/08 11:01:45  dave
 *   Added OutOfMemory catches to OutputStream.
 *   Not idseal, as they may not catch the error in time.
 *   Needs more work on this ...
 *
 *   Revision 1.3.60.3  2005/08/08 09:19:10  dave
 *   Added catches for OutOfMemoryException ... log and re-throw.
 *
 *   Revision 1.3.60.2  2005/08/05 13:41:04  dave
 *   Changed tabs to spaces ..
 *
 *   Revision 1.3.60.1  2005/08/04 12:29:52  dave
 *   Added large data (1Gbyte) stress test.
 *   Added JDK 1.5 fix to FileStoreOutputStream.
 *
 *   Revision 1.3  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.2.10.1  2005/01/17 17:19:21  dave
 *   Fixed bug in FileManagerImpl test (missing '/' in repository path on Unix) ...
 *   Changed tabs to spaces ..
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.lang.reflect.Method ;

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
            //
            // New method added to JDK 1.5.
            // http.setChunkedStreamingMode(BUFFER_SIZE);
            //
            // JDK 1.4 patch (uses reflection to invoke the JDK 1.5 method).
            // This allows us to compile on JDK 1.4 but use the JDK 1.5 method if available.
            //NWW - 14-10-2005 commented out for now - not all servers support chunked streaming. Seems like our
            // filestore server doesn't. - means uploads fail on 1.5.
            /* temporarily removed, until supported by the server
            try {
                log.debug("Checking HttpURLConnection for JDK 1.5 method.");
                Class clazz   = this.http.getClass() ;
                Method method = clazz.getMethod(
                    "setChunkedStreamingMode",
                    new Class[] { int.class }
                    );
                method.invoke(
                    this.http,
                    new Object[] { new Integer(BUFFER_SIZE) }
                    );
                log.debug("Using HttpURLConnection.setChunkedStreamingMode for JDK 1.5.");
                }
            catch (Exception ouch)
                {
                //
                // ... Ok, we tried ...
                log.debug("HttpURLConnection does not supposrt setChunkedStreamingMode");
                }
                */
            //
            // Open the connection.
            this.http.connect();
            //
            // Use the connection stream.
            this.stream = this.http.getOutputStream() ;
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
        // If the URL points to something else.
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
                    /* NWW: this is important - although this just looks like
                     * debug code, it's the next line that actually causes the 
                     * transfer to happen. If the code doesn't query the response in some way,
                     * the transfer doesn't happen - stupid, and no hint of this in the javadoc
                     */
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
			try {
            	this.stream.flush();
				}
			catch (OutOfMemoryError ouch)
				{
		        log.warn("Output stream overflow");
	            throw new IOException(
	                "Output stream buffer overflow"
	                );
				}
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
			try {
            	this.stream.write(b);
				}
			catch (OutOfMemoryError ouch)
				{
		        log.warn("Output stream overflow");
	            throw new IOException(
	                "Output stream buffer overflow"
	                );
				}
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
			try {
            	this.stream.write(b, off, len);
				}
			catch (OutOfMemoryError ouch)
				{
		        log.warn("Output stream overflow");
	            throw new IOException(
	                "Output stream buffer overflow"
	                );
				}
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
			try {
            	this.stream.write(b);
				}
			catch (OutOfMemoryError ouch)
				{
		        log.warn("Output stream overflow");
	            throw new IOException(
	                "Output stream buffer overflow"
	                );
				}
            }
        else {
            throw new IOException(
                "Stream not open"
                );
            }
        }
    }
