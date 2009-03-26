/**
 * 
 */
package org.astrogrid.desktop.protocol.classpath;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** Handler for the 'classpath' url scheme.
 *  finds a resource on the classpath, without having to 
 * specify the jar.
 * url format - classpath:/path/to/resource
 * @author Noel Winstanley
 * @since Jan 2, 20073:56:42 PM
 */
public class Handler extends URLStreamHandler {

	@Override
    protected URLConnection openConnection(final URL arg0) throws IOException {
		final URL u = Handler.class.getResource(arg0.getPath());
		if (u == null) {
			throw new FileNotFoundException(arg0.toString());
		}
		return u.openConnection();
	}

}
