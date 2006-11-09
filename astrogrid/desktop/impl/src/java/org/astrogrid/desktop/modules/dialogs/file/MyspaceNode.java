/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.net.URI;
import java.rmi.RemoteException;

import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.NodeNotFoundFault;

import uk.ac.starlink.connect.Branch;
import uk.ac.starlink.connect.Node;

/**
 * @author Noel Winstanley
 * @since Nov 6, 20064:19:50 PM
 */
class MyspaceNode implements Node {

	private FileManagerNode node;
	protected final Branch parent;
	
	public MyspaceNode(final FileManagerNode node, Branch parent) {
		super();
		this.node = node;
		this.parent = parent;
	}

	public String getName() {
		return getNode().getName();
	}

	public Branch getParent() {
		return parent;
	}
	
	protected FileManagerNode getNode() {
		return this.node;
	}
	
	protected final void setNode(FileManagerNode fn) {
		this.node = fn;
	}
	
	public final URI getURI()  throws Exception {
		return new URI(getNode().getIvorn().toString());
	}
	
	public String toString() {
		try {
			return getURI().toString();
		} catch (Exception x) {
			return node.getMetadata().getNodeIvorn().toString();
		}
	}

}
