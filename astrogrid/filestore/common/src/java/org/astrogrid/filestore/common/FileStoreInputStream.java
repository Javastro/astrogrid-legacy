/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/FileStoreInputStream.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreInputStream.java,v $
 *   Revision 1.2  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.1.2.1  2004/10/29 13:21:58  dave
 *   Added InputStream wrapper ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common ;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.HttpURLConnection ;
import java.net.MalformedURLException ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.filestore.common.transfer.mock.Handler ;

/**
 * A wrapper for GET transfer streams.
 *
 */
public class FileStoreInputStream
	extends InputStream
	{

    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreInputStream.class);

	/**
	 * Create a FileStoreOutputStream from a string URL.
	 * @param string A string representation of the target URL.
	 * @throws MalformedURLException
	 *
	 */
	public FileStoreInputStream(String string)
		throws MalformedURLException
		{
		this(
			new URL(
				string
				)
			);
		}

	/**
	 * Create a FileStoreInputStream from a URL.
	 * @param url The target URL to connect to.
	 *
	 */
	public FileStoreInputStream(URL url)
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
	private InputStream stream ;

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
		log.debug("FileStoreInputStream.open()");
		log.debug("  URL  : " + url);
		//
		// If our URL is a http:// URL.
		if ("http".equals(url.getProtocol()))
			{
			log.debug("  Handling http URL");
			this.http = (HttpURLConnection) url.openConnection() ;
            this.http.setAllowUserInteraction(false);
            this.http.setDoInput(true);
//			this.http.setDoOutput(true) ;
            this.http.setUseCaches(false);
			this.http.setRequestMethod("GET");
            this.http.setRequestProperty("User-Agent", this.getClass().getName());
//			connection.setRequestProperty("Content-Type",   "application/octet-stream");
//			connection.setRequestProperty("Content-Length", "" + contentLength);
            this.http.connect();
			//
			// Create our input stream.
			this.stream = http.getInputStream() ;
			}
		//
		// If the URL is a file:// URL.
		else if ("file".equals(url.getProtocol()))
			{
			log.debug("  Handling file URL");
			log.debug("  Path : " + url.getPath());
			//
			// Create our file output stream
			this.stream = new FileInputStream(
				url.getPath()
				);
			}
		//
		// If the URL points to a live server.
		else {
			log.debug("  Handling generic URL");
			//
			// Create our input stream.
			this.conn = url.openConnection() ;
			this.stream = this.conn.getInputStream() ;
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
		log.debug("FileStoreInputStream.close()");
		log.debug("  URL  : " + url);
		if (null != this.stream)
			{
			//
			// Close our stream.
			this.stream.close();
			log.debug("  PASS : Stream closed");
			}
		else {
			throw new IOException(
				"Stream not open"
				);
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public int available()
		throws IOException
		{
		if (null != this.stream)
			{
			return this.stream.available();
			}
		else {
			throw new IOException(
				"Stream not open"
				);
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public void mark(int limit)
		{
		if (null != this.stream)
			{
			this.stream.mark(limit);
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public boolean markSupported()
		{
		if (null != this.stream)
			{
			return this.stream.markSupported();
			}
		else {
			return false ;
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public int read()
		throws IOException
		{
		if (null != this.stream)
			{
			return this.stream.read();
			}
		else {
			throw new IOException(
				"Stream not open"
				);
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public int read(byte[] b)
		throws IOException
		{
		if (null != this.stream)
			{
			return this.stream.read(b);
			}
		else {
			throw new IOException(
				"Stream not open"
				);
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public int read(byte[] b, int off, int len)
		throws IOException
		{
		if (null != this.stream)
			{
			return this.stream.read(b, off, len);
			}
		else {
			throw new IOException(
				"Stream not open"
				);
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public void reset()
		throws IOException
		{
		if (null != this.stream)
			{
			this.stream.reset();
			}
		else {
			throw new IOException(
				"Stream not open"
				);
			}
		}

	/**
	 * InputStream method ...
	 *
	 */
	public long skip(long n)
		throws IOException
		{
		if (null != this.stream)
			{
			return this.stream.skip(n);
			}
		else {
			throw new IOException(
				"Stream not open"
				);
			}
		}

	}
