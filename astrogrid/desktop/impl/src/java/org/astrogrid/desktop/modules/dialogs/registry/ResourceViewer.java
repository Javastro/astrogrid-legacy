/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

import javax.swing.JComponent;

import org.astrogrid.acr.ivoa.resource.Resource;

/** Inteface to a component that views resources.
 * 
 * would prefer this to be an asbtract class, but this then fixes the base class.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 13, 20072:31:44 PM
 */
public interface ResourceViewer {
	/** clear the display */
	public void clear();
	/** show this resource in the display */
	public void display(Resource res) ;
	
	/** return the swing component - expected to return 'this'
	 * 
	 */
	public JComponent getComponent();
}
