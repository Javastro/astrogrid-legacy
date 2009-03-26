/**
 * 
 */
package org.astrogrid.desktop.protocol.fallback;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.StringTokenizer;


/**Handler for the 'fallback' url scheme.
 * takes a list of sub-urls, and tries each in turn.
 * 
 * A bit picky - doesn't accept http: sub-urls - use http client instead.
 * furthermore, has not been tested with file:, ftp:, jar: etc - prefer only httpclient: and classpath:
 * @author Noel Winstanley
 * @since Jan 2, 20074:04:22 PM
 */
public class Handler extends URLStreamHandler {
	@Override
    protected URLConnection openConnection(URL u) throws IOException {
		
		// validate the url first.
		StringTokenizer tok = new StringTokenizer(u.toString().substring(FallbackURLConnection.FALLBACK_PROTOCOL.length()),",");
		while(tok.hasMoreElements()) {
			URL url = new URL(tok.nextToken());
			if (url.getProtocol().equals("http")) {
				throw new MalformedURLException("http: unsupported in fallback: use equivalent httpclient: instead");
			}
		}
		FallbackURLConnection conn =  new FallbackURLConnection(u);
		//conn.connect(); // breaks contract a little.
		return conn;
	}
		

}
