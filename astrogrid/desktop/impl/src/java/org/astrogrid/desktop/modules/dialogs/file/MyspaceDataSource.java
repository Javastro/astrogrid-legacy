/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.file;

import java.io.IOException;
import java.io.InputStream;

import org.astrogrid.filemanager.client.FileManagerNode;

import uk.ac.starlink.util.DataSource;

/**
 * @author Noel Winstanley
 * @since Nov 6, 20064:38:33 PM
 */
 class MyspaceDataSource extends DataSource {

	protected final FileManagerNode node;
	
	
	protected InputStream getRawInputStream() throws IOException {
		return node.readContent();
	}


	public MyspaceDataSource(final FileManagerNode node) {
		super();
		this.node = node;
	}

}
