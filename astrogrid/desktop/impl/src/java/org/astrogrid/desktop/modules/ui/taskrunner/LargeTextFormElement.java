/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;

/** Form element for large text objects.
 * indirect by default - to encourage referencing external files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20074:20:35 PM
 */
public class LargeTextFormElement extends AbstractTaskFormElement implements DocumentListener{

	/**
	 * @param pval
	 * @param pdesc
	 */
	public LargeTextFormElement(ParameterValue pval, ParameterBean pdesc,ResourceChooserInternal chooser) {
		super(pval, pdesc,chooser);
		//indirectToggle.doClick(); // causes layout to flip.
	}


	protected final JComponent createEditor() {
		text = new JTextArea();
		text.setRows(4);
		JScrollPane sp = new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		if (pdesc.getDefaultValue() != null) {
			text.setText(pdesc.getDefaultValue());
		}
		if (pval.getValue() != null && ! pval.getIndirect()) {
			text.setText(pval.getValue());
		}		
		text.getDocument().addDocumentListener(this);
		return sp;
	}

	
	protected JTextArea text;

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


	protected String getStringValue() {
		return text.getText();
	}


}
