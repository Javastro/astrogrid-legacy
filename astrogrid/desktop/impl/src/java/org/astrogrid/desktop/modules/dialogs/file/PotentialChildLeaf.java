/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.filemanager.client.FileManagerNode;

import uk.ac.starlink.connect.Leaf;

class PotentialChildLeaf extends MyspaceLeaf implements Leaf {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(PotentialChildLeaf.class);

	private final String name;

	public PotentialChildLeaf(String name,MyspaceBranch parent) {
		super(null,parent);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
protected FileManagerNode getNode() {
	synchronized(this) {
		if (super.getNode() == null) {
			try {
			setNode(((MyspaceBranch)parent).getNode().addFile(name));
			} catch (Exception e) {
				logger.error("Failed to create child node: " + name,e);
			}
		}
	}
	return super.getNode();
}

}