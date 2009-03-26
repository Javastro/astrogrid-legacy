/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;

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
		indirectToggle.setToolTipText("Toggle whether to store this output in cache or write it to your VO Workspace");
		chooserButton.setText("Choose Location..");
		chooserButton.setToolTipText("Select the location in VO Workspace that this output parameter will be saved to");
	      CSH.setHelpIDString(getEditor(),"task.form.output");
	}

	@Override
    protected JComponent createEditor() {
		return new JLabel("Store in cache"); 
	}

	@Override
    protected String getStringValue() {
		return "";
	}


}
