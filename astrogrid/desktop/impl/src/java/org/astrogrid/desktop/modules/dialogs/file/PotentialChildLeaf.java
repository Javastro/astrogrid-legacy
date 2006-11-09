/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.astrogrid.filemanager.client.FileManagerNode;

import uk.ac.starlink.connect.Branch;
import uk.ac.starlink.connect.Leaf;
import uk.ac.starlink.util.DataSource;

class PotentialChildLeaf extends MyspaceLeaf implements Leaf {

	private final String name;
	private final MyspaceBranch parent;
	private FileManagerNode childNode;

	public PotentialChildLeaf(String name,MyspaceBranch parent) {
		super(null,parent);
		this.name = name;
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
protected FileManagerNode getNode() {
	synchronized(this) {
		if (super.getNode() == null) {
			try {
			setNode(parent.getNode().addFile(name));
			} catch (Exception e) {
				return null;
			}
		}
	}
	return super.getNode();
}

}