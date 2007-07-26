/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URISyntaxException;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileName;
import org.astrogrid.store.Ivorn;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20071:55:10 PM
 */
public class MyspaceFileName extends AbstractFileName {


	public MyspaceFileName(String community, String user, String path, FileType type) {
		super("ivo", path, type);
		this.community = community;
		this.user = user;
	}
	private final String community;
	private final String user;
	
	protected void appendRootUri(StringBuffer buffer, boolean addPassword) {
		buffer.append("ivo://");
		buffer.append(community);
		buffer.append("/");
		buffer.append(user);
		buffer.append("#");
	}

	public FileName createName(String absPath, FileType type) {
		return new MyspaceFileName(community, user,absPath,type);
	}
	
	public Ivorn getIvorn() throws URISyntaxException {
		return new Ivorn(getURI());
	}

}
