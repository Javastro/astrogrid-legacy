/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileName;
import org.astrogrid.store.Ivorn;

/** VFS FileName for the Myspace file system.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20071:55:10 PM
 */
public class MyspaceFileName extends AbstractFileName {


	public MyspaceFileName(final String community, final String user, final String path, final FileType type) {
		super("ivo", path, type);
		this.community = community;
		this.user = user;
	}
	private final String community;
	private final String user;
	
	protected void appendRootUri(final StringBuffer buffer, final boolean addPassword) {
		buffer.append("ivo://");
		buffer.append(community);
		buffer.append("/");
		buffer.append(user);
		buffer.append("#");
	}

	public FileName createName(final String absPath, final FileType type) {
		return new MyspaceFileName(community, user,absPath,type);
	}
	
	public Ivorn getIvorn() throws URISyntaxException {
		return new Ivorn(getURI());
	}
	
	public String getURI() {
	    return StringUtils.replace(super.getURI()," ","%20"); // bugfix override.
	}

}
