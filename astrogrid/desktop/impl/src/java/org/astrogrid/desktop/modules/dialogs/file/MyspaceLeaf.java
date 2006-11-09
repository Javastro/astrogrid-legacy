/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.io.IOException;
import java.io.OutputStream;

import org.astrogrid.filemanager.client.FileManagerNode;

import uk.ac.starlink.connect.Branch;
import uk.ac.starlink.connect.Leaf;
import uk.ac.starlink.util.DataSource;

/**
 * @author Noel Winstanley
 * @since Nov 6, 20064:37:00 PM
 */
class MyspaceLeaf extends MyspaceNode implements Leaf {

	/**
	 * @param node
	 * @param parent
	 */
	public MyspaceLeaf(FileManagerNode node, Branch parent) {
		super(node, parent);
	}

	public DataSource getDataSource() throws IOException {
		return new MyspaceDataSource(getNode());
	}

	public OutputStream getOutputStream() throws IOException {
		return getNode().writeContent();
	}

}
