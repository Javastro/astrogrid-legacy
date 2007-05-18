/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
public class ResultsResourceViewer extends JPanel implements ResourceViewer {

	/**
	 * @param files
	 * @param currentSelection
	 * @param view
	 * @param icons
	 * @param dnd
	 */
	public ResultsResourceViewer( ) {
		label = new JLabel();
		add(label);
		label.setText("Will display results returned from the selected service\nand enable selection, actions and Drag-n-drop");
	}
	private final JLabel label;

	public void clear() {
		label.setText("");
	}

	public void display(Resource res) {
		label.setText("Will display results returned from service\n" + res.getTitle());
		
	}

	public JComponent getComponent() {
		return this;
	}

}
