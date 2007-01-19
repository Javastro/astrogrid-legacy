/**
 * 
 */
package org.astrogrid.desktop.protocol.httpclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Handler that extends standard http url with control of client behaviours.
 * these are passed to the client after the 'fragment' token.
 * implemented using the commons httpclient
 * url format: httpclient://www.slashdot.org#6000
 * (sets timeout to 6 seconds)
 * default for timeout is 5 second.
 * 
 * used in preference to standard http url handler which seems to hang for ages (on some platforms)
 * when a url is unreachable - which slows down hivemind startup no end.
 * @author Noel Winstanley
 * @since Jan 2, 20074:06:26 PM
 */
public final class Handler extends URLStreamHandler {
	private static final Log logger = LogFactory.getLog(Handler.class);
	  
	public Handler() {
		client = new HttpClient(new SimpleHttpConnectionManager());
	     	final HttpConnectionManagerParams params = client.getHttpConnectionManager().getParams();
			params.setSoTimeout(DEFAULT_TIMEOUT_MS); // data timeout is one second.
			params.setConnectionTimeout(DEFAULT_TIMEOUT_MS); // connection timeout is one second.
	     	  		
	}
	private final HttpClient client;
	
	protected URLConnection openConnection(URL u) throws IOException {
		GetMethod meth = null;
		try {
		meth = new GetMethod("http:" + u.toURI().getSchemeSpecificPart()); //JDT this is also JDK1.5
		HttpMethodParams params = meth.getParams();
		// set defaults.
		//params.setSoTimeout(DEFAULT_TIMEOUT_MS); // data timeout is one second.
		// check for client-specificed parameters.
		String fragment = u.getRef();
		if (fragment != null) {
			// parse it.
			try {
				int i = Integer.parseInt(fragment);
				params.setSoTimeout(i);
			} catch (NumberFormatException e) {
				logger.warn("Failed to parse fragment as a timeout - " + fragment);
			}
			// could extend this to allow more parameters later..
		}
		// try and connect.
		int code;
		if ((code = client.executeMethod(meth)) == 200) {
			return new HttpURLConnection(meth,u) {
				// override default impl, which throws a runtime exception here.
				public void connect() throws IOException {
					// does nothing - already connected.
				}
			};
		} else {
			throw new FileNotFoundException("Retrieval Error " + code);
		}
		} catch (URISyntaxException e) {
			throw new IOException("Unexpected Error" + e);
		} catch (IOException e) { // if an io exception is thrown - clean up.
			if (meth != null && meth.hasBeenUsed()) {
				meth.releaseConnection(); // note we can't do this in a finally, as want to keep it open on success
			}
			throw e;
		}
	}
	
	public static final int DEFAULT_TIMEOUT_MS = 5000;
}
