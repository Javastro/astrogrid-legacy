/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;

/** form editor for binary objects. all we can do here is just show the 
 * indirection dialogue.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20074:26:59 PM
 */
public class BinaryFormElement extends AbstractTaskFormElement {

	/**
	 * @param pval
	 * @param pdesc
	 */
	public BinaryFormElement(ParameterValue pval, ParameterBean pdesc, ResourceChooserInternal chooser) {
		super(pval, pdesc, chooser);
		editor.show(INDIRECT);
		pval.setIndirect(true);
		indirectToggle.setEnabled(false); // flip to indirect mode, and stay there.
		indirectToggle.setVisible(false);
	}

	protected JComponent createEditor() {
		return new JLabel("Choose a file"); // placeholder - shouldn't be displayed.
	}

	// not possible with a binary editor.
	protected String getStringValue() {
		return "";
	}



}
