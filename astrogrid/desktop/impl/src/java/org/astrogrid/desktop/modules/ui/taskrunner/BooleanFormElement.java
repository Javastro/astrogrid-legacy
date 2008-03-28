/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.event.ItemEvent;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;

/** editor for a boolean element.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20073:54:08 PM
 */
public class BooleanFormElement extends AbstractTaskFormElement {

	
	/**
	 * @param pval
	 * @param pdesc
	 */
	public BooleanFormElement(ParameterValue pval, ParameterBean pdesc, ResourceChooserInternal fileChooser) {
		super(pval, pdesc,fileChooser);
		disableIndirect();
        CSH.setHelpIDString(getEditor(),"task.form.boolean");		
	}

	
	protected JComponent createEditor() {
	    cb = new JCheckBox("true");
	    if (! pval.getIndirect()) {
	        if (pdesc.getDefaultValue() != null) {
	            cb.setSelected(Boolean.valueOf(pdesc.getDefaultValue()).booleanValue());
	        } 
	        if (pval.getValue() != null) {
	            cb.setSelected(Boolean.valueOf(pval.getValue()).booleanValue());
	        }
	        pval.setValue(getStringValue());
	    }
	    cb.addItemListener(this);
	    return cb;
	}
	
	protected JCheckBox cb;
	
	// override parents item listener method.
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cb) {
			pval.setValue(getStringValue());
		} else {
			super.itemStateChanged(e);
		}
	}


	protected String getStringValue() {
		return Boolean.toString(cb.isSelected());
	}



}

	