/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;

/** editor for any type with an enumeration
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20074:05:11 PM
 */
public class EnumerationFormElement extends AbstractTaskFormElement {

	/**
	 * @param pval
	 * @param pdesc
	 */
	public EnumerationFormElement(ParameterValue pval, ParameterBean pdesc, ResourceChooserInternal chooser) {
		super(pval, pdesc,chooser);
	}

	protected JComponent createEditor() {
		cb = new JComboBox(pdesc.getOptions());
		cb.setEditable(false);
		if (pdesc.getDefaultValue() != null) {
			cb.setSelectedItem(pdesc.getDefaultValue());
		}
		if (pval.getValue() != null && ! pval.getIndirect()) {
			cb.setSelectedItem(pval.getValue());
		}		
		cb.addItemListener(this);
		return cb;
	}
	
	protected JComboBox cb;
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cb) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				pval.setValue(getStringValue());
			}
		} else {
			super.itemStateChanged(e);
		}
	}

	protected String getStringValue() {
		return (String)cb.getSelectedItem();
	}

}
