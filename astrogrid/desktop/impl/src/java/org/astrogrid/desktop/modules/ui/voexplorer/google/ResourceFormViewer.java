/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.google;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.astrogrid.acr.ivoa.resource.Resource;

/**Tabular display of a resource entry.
 *@fixme implement
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 24, 200712:38:58 PM
 */
public class ResourceFormViewer extends JPanel implements ResourceViewer {

	public void clear() {
		label.setText("");		
	}

	public void display(Resource res) {

		label.setText("Will display form containing Resource metadata for\n" + res.getTitle());		
		
	}

	public JComponent getComponent() {
		return this;
	}

	/**
	 * 
	 */
	public ResourceFormViewer() {
		super(new BorderLayout());
		label = new JLabel();
		add(label,BorderLayout.CENTER);
		label.setText("Will display Resource metadata in a form layout");
	}
	private final JLabel label;
	
}
