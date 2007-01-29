/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.net.URI;

import org.astrogrid.filemanager.client.FileManagerNode;

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

	/** returns the name of the node, or null of the node is not set */
	public String getName() {
		return getNode() == null ? null : getNode().getName();
	}

	public Branch getParent() {
		return parent;
	}
	
	protected FileManagerNode getNode() {
		return this.node;
	}
	
	protected void setNode(FileManagerNode fn) {
		this.node = fn;
	}
	
	/** returns the URI of getNode(), or null if this node is not set */
	public final URI getURI()  throws Exception {
		return getNode() == null ? null : new URI(getNode().getIvorn().toString());
	}
	
	public String toString() {
		if (getNode() == null) {
			return "no filemanager node set";
		}
		try {
			return getURI().toString();
		} catch (Exception x) {
			return getNode().getMetadata().getNodeIvorn().toString();
		}
	}

}
