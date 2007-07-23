/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.modules.ui.comp.UIComponentBodyguard;
import org.astrogrid.desktop.modules.ui.voexplorer.google.ResourceViewer;

/** quite a mashup class - a resoource viewer for displaying query results.
 * @fixme implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 11, 20072:05:19 AM
 */
public class ResultsResourceViewer extends JPanel implements ResourceViewer {


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


	public void addTo(UIComponentBodyguard parent, JTabbedPane t) {
		t.addTab( "Results",null/*@todo find icon*/,this,"Shows results from selected service");
	}

}
