/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileNameParser;
import org.apache.commons.vfs.provider.UriParser;
import org.apache.commons.vfs.provider.VfsComponentContext;

/** parser for myspace names 
 * */

public class MyspaceNameParser extends AbstractFileNameParser {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(MyspaceNameParser.class);

	public FileName parseUri(VfsComponentContext context, FileName base, String filename) throws FileSystemException {
	// in all other implementations, first 2 parameters seem totally ignored
	// dunno whether I should be doing the same..
	// can I be sure that 'base' will be always non-null, for example?
		
		URI u;
		try {
			u = new URI(filename);
		} catch (URISyntaxException x) {
			throw new FileSystemException(x);
		}
		StringBuffer sb = new StringBuffer(u.getFragment() != null ? u.getFragment() : "");
		String comm = u.getAuthority();
		String user = u.getPath().substring(1); // drop leading '/'
		FileType type = UriParser.normalisePath(sb);
		return new MyspaceFileName(comm,user,sb.toString(),type);
	}
}