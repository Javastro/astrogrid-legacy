/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;

/** Form element for an output parameter.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20074:02:39 PM
 */
public class OutputFormElement extends AbstractTaskFormElement {

	/**
	 * @param pval
	 * @param pdesc
	 */
	public OutputFormElement(ParameterValue pval, ParameterBean pdesc,ResourceChooserInternal chooser) {
		super(pval, pdesc,chooser);
		super.localFileEnabled=false;
		indirectToggle.setToolTipText("Save this result to VO Workspace");
	}

	protected JComponent createEditor() {
		return new JLabel("Store in cache"); 
	}

	protected String getStringValue() {
		return "";
	}


}
