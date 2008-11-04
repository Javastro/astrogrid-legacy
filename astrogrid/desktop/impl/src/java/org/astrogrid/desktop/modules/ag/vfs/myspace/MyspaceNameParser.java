/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileNameParser;
import org.apache.commons.vfs.provider.UriParser;
import org.apache.commons.vfs.provider.VfsComponentContext;

/** Parser for myspace names. 
 * */

public class MyspaceNameParser extends AbstractFileNameParser {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(MyspaceNameParser.class);

	public FileName parseUri(final VfsComponentContext context, final FileName base, String filename) throws FileSystemException {
	// in all other implementations, first 2 parameters seem totally ignored
	// dunno whether I should be doing the same..
	// can I be sure that 'base' will be always non-null, for example?
	    
	    // filter out nonsense created by hyper link handler.
		if (StringUtils.isEmpty(filename) || "ivo:null".equals(filename) ) {
		    throw new FileSystemException("Null filename");
		}
		
		filename = StringUtils.replace(filename," ","_");
		URI u;
		try {
			u = new URI(filename);
		} catch (final URISyntaxException x) {
			throw new FileSystemException(x);
		}
		final StringBuffer sb = new StringBuffer(u.getFragment() != null ? u.getFragment() : "");
		final String comm = u.getAuthority();
		final String user = u.getPath().substring(1); // drop leading '/'
		
		if (user.indexOf('/') != -1) {
	        // work-around for registry ivorns which are faultily passed in here by the hyperlink handler (as is same scheme).
	        // if we detect a file name with more than 2 bits before the hash, just throw an exception to abort it.
	        // as this can't be a community/user - and must be a registry ivorn instead.
	        // won't catch all, but will eradicate some problems.
	        throw new FileSystemException("Registry ivorn: " + filename);		    
		}
		
		final FileType type = UriParser.normalisePath(sb);
		return new MyspaceFileName(comm,user,sb.toString(),type);
	}
}