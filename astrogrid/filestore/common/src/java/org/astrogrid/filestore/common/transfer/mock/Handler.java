/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/transfer/mock/Handler.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: Handler.java,v $
 *   Revision 1.2  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/10/29 12:22:07  dave
 *   Added OutputStream wrapper ...
 *
 *   Revision 1.1.2.1  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.transfer.mock ;

import java.io.InputStream ;
import java.io.OutputStream ;
import java.io.IOException ;
import java.io.FileNotFoundException ;

import java.util.Map ;
import java.util.HashMap ;

import java.net.URL ;
import java.net.URLConnection ;
import java.net.URLStreamHandler ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.apache.axis.client.Call ;

/**
 * A mock stream URL handler, so that we can create mock URLs.
 *
 */
public class Handler
	extends URLStreamHandler
	{
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(Handler.class);

	/**
	 * Our mock URL protocol.
	 *
	 */
	public static final String PROTOCOL = "mock" ;

	/**
	 * Public constructor.
	 *
	 */
	public Handler()
		{
		super();
		log.debug("");
		log.debug("Handler.Handler()");
		}

	/**
	 * Add our transport handler to the system protocol search path.
	 *
	 */
	public static void register()
		{
		log.debug("");
		log.debug("Handler.register()");
		//
		// Use the Axis Call utility.
		Call.addTransportPackage(
			"org.astrogrid.filestore.common.transfer"
			);
		}

	/**
	 * Our map of registered URLs.
	 *
	 */
	private static Map map = new HashMap() ;

	/**
	 * Register a connector for a URL.
	 *
	 */
	public static void addConnector(URL url, Connector connector)
		{
		log.debug("");
		log.debug("Handler.addConnector()");
		log.debug("  URL : " + url.toString());
		map.put(
			url.toString(),
			connector
			);
		}

	/**
	 * get a connector for a URL.
	 *
	 */
	public Connector getConnector(URL url)
		{
		log.debug("");
		log.debug("Handler.getConnector()");
		log.debug("  URL : " + url.toString());
		return (Connector) map.get(
			url.toString()
			);
		}

	/**
	 * Open a connection to a mock URL.
	 *
	 */
	public URLConnection openConnection(URL url)
		{
		log.debug("");
		log.debug("Handler.openConnection()");
		log.debug("  URL : " + url.toString());
		return new Connection(url) ;
		}

	/**
	 * Inner class to handle a URL connection.
	 *
	 */
	public class Connection
		extends URLConnection
		{

		/**
		 * Our connection URL.
		 *
		 */
		protected URL url ;

		/**
		 * Public constructor.
		 *
		 */
		public Connection(URL url)
			{
			super(url);
			this.url = url ;
			log.debug("");
			log.debug("Handler.Connection()");
			log.debug("  URL : " + url.toString());
			}

		/**
		 * Our connector.
		 *
		 */
		protected Connector connector ;

		/**
		 * Abstract connect() in URLConnection.
		 *
		 */
		public void connect()
			{
			log.debug("");
			log.debug("Handler.Connection.connect()");
			log.debug("  URL : " + url.toString());
			connector = getConnector(url) ;
			if (null != connector)
				{
				log.debug("PASS : found connector");
				}
			else {
				log.debug("FAIL : missing connector");
				}
			}

		/**
		 * Get an InputStream to the URL content.
		 *
		 */
		public InputStream getInputStream()
			throws IOException
			{
			log.debug("");
			log.debug("Handler.Connection.getInputStream()");
			log.debug("  URL : " + url.toString());
			//
			// Check if we need to connect.
			if (null == this.connector)
				{
				this.connect() ;
				}
			//
			// If we found a registered a connector.
			if (null != this.connector)
				{
				return connector.getInputStream() ;
				}
			//
			// If we didn't find a connector.
			else {
				throw new FileNotFoundException(
					"Unable to open input stream to " + url.toString()
					);
				}
			}

		/**
		 * Get an OutputStream to the URL content.
		 *
		 */
		public OutputStream getOutputStream()
			throws IOException
			{
			log.debug("");
			log.debug("Handler.Connection.getOutputStream()");
			log.debug("  URL : " + url.toString());
			//
			// Check if we need to connect.
			if (null == this.connector)
				{
				this.connect() ;
				}
			//
			// If we found a registered a connector.
			if (null != this.connector)
				{
				return connector.getOutputStream() ;
				}
			//
			// If we didn't find a connector.
			else {
				throw new FileNotFoundException(
					"Unable to open output stream to " + url.toString()
					);
				}
			}
		}
	}
