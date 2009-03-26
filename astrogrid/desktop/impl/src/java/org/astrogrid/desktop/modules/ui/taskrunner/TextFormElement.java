/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;

/** Form element for a simple text element.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20073:58:58 PM
 */
public class TextFormElement extends AbstractTaskFormElement implements DocumentListener {

	/**
	 * @param pval
	 * @param pdesc
	 */
	public TextFormElement(ParameterValue pval, ParameterBean pdesc, ResourceChooserInternal chooser) {
		super(pval, pdesc,chooser);
	      CSH.setHelpIDString(getEditor(),"task.form.text");
	}

	@Override
    protected final JComponent createEditor() {
		text = new JTextField();
		if (!pval.getIndirect()) {
		    if (pdesc.getDefaultValue() != null) {
		        text.setText(pdesc.getDefaultValue());
		    }
		    if (pval.getValue() != null) {
		        text.setText(pval.getValue());
		    }				
		    pval.setValue(getStringValue());
		}
		text.getDocument().addDocumentListener(this);
		return text;
	}

	protected JTextComponent text;

	// listen to all changes to the dociment, and map this instantly back to the tool document.
	public void changedUpdate(DocumentEvent e) {
		pval.setValue(getStringValue());
	}

	public void insertUpdate(DocumentEvent e) {
		pval.setValue(getStringValue());
	}

	public void removeUpdate(DocumentEvent e) {
		pval.setValue(getStringValue());
	}


	@Override
    protected String getStringValue() {
		return text.getText();
	}

}
