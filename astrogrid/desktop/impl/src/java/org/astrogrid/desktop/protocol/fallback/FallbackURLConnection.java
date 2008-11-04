package org.astrogrid.desktop.protocol.fallback;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * URL connection for <tt>fallback:</tt>.
 * A bit hacky - on 'connect()' cycles through the available alternatives, 
 * until it can connect to one. then data-fetching methods delegate to the 
 * actual connection object.
 * 
 * However, parameters set on main object aren't propagated through to the 
 * actuall connection - am betting that these aren't used.
 * @author Noel Winstanley
 * @since Jan 2, 20074:47:40 PM
 */
public final class FallbackURLConnection extends URLConnection {
	/**
	 * @param url
	 */
	public FallbackURLConnection(final URL url) {
		super(url);
	}

	public void connect() throws IOException {
		final StringTokenizer tok = new StringTokenizer(getURL().toString().substring(FALLBACK_PROTOCOL.length()),",");
		while (tok.hasMoreElements()) { 	// try each url in turn until something works..
			final URL candidate = new URL(tok.nextToken());
			logger.debug("Trying " + candidate);
			try {
				actual = candidate.openConnection();
				actual.connect();
				return; // if we've gotten this far, all is good.
			} catch (final IOException e) {
				logger.warn(candidate + " failed with '" + e.toString() + "' - falling back");
			}
		}
		throw new FileNotFoundException("Failed to load any resource from " + getURL());
	}
	
	public static final String FALLBACK_PROTOCOL = "fallback:";
	private static final Log logger = LogFactory.getLog(FallbackURLConnection.class);
	private URLConnection actual;
	// delegate methods..
	public Object getContent() throws IOException {
		return this.getActual().getContent();
	}

	public Object getContent(final Class[] classes) throws IOException {
		return this.getActual().getContent(classes);
	}



	public InputStream getInputStream() throws IOException {
		return this.getActual().getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return this.getActual().getOutputStream();
	}


	/** forces a connect, if not already connected.
	 * @return the actual
	 */
	private URLConnection getActual() throws IOException {
		if (actual == null) {
			connect();
		}
		return actual;
	}
	
}