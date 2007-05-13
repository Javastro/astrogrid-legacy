/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import javax.swing.JComponent;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileViewDnDManager;
import org.astrogrid.desktop.modules.ui.fileexplorer.FilesList;
import org.astrogrid.desktop.modules.ui.fileexplorer.IconFinder;
import org.astrogrid.desktop.modules.ui.fileexplorer.StorageView;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventSelectionModel;

/** quite a mashup class - a resoource viewer for displaying query results.
 * @fixme implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 11, 20072:05:19 AM
 */
public class ResultsResourceViewer extends FilesList implements ResourceViewer {

	/**
	 * @param files
	 * @param currentSelection
	 * @param view
	 * @param icons
	 * @param dnd
	 */
	public ResultsResourceViewer( EventSelectionModel currentSelection, StorageView view, IconFinder icons, FileViewDnDManager dnd) {
		super(new BasicEventList() ,currentSelection, view, icons, dnd);
	}

	public void clear() {
	}

	public void display(Resource res) {
		// get files associated with this resource, 
	}

	public JComponent getComponent() {
		return null;
	}

}
