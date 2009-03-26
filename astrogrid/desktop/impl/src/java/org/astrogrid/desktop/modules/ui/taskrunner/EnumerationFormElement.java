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
import org.astrogrid.desktop.modules.system.CSH;

/** Form element for an enumerated parameter.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20074:05:11 PM
 */
public class EnumerationFormElement extends AbstractTaskFormElement {

	/**
	 * @param pval
	 * @param pdesc
	 */
	public EnumerationFormElement(final ParameterValue pval, final ParameterBean pdesc, final ResourceChooserInternal chooser) {
		super(pval, pdesc,chooser);
		disableIndirect();
        CSH.setHelpIDString(getEditor(),"task.form.enumeration");		
	}

	@Override
    protected JComponent createEditor() {
	    cb = new JComboBox(pdesc.getOptions());
	    cb.setEditable(false);
	    if (! pval.getIndirect()) {
	        if (pdesc.getDefaultValue() != null) {
	            cb.setSelectedItem(pdesc.getDefaultValue());
	        }
	        if (pval.getValue() != null ) {
	            cb.setSelectedItem(pval.getValue());
	        }
	        pval.setValue(getStringValue());
	    }
	    cb.addItemListener(this);
	    return cb;
	}
	
	protected JComboBox cb;
	@Override
    public void itemStateChanged(final ItemEvent e) {
		if (e.getSource() == cb) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				pval.setValue(getStringValue());
			}
		} else {
			super.itemStateChanged(e);
		}
	}

	@Override
    protected String getStringValue() {
		return (String)cb.getSelectedItem();
	}

}
