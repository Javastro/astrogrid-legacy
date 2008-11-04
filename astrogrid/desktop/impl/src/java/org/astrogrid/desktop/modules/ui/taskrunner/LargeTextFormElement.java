/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;

/** Form element for large text parameter.
 * indirect by default - to encourage referencing external files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 4, 20074:20:35 PM
 */
public class LargeTextFormElement extends AbstractTaskFormElement implements DocumentListener{

	/**
	 * @param pval
	 * @param pdesc
	 */
	public LargeTextFormElement(final ParameterValue pval, final ParameterBean pdesc,final ResourceChooserInternal chooser) {
		super(pval, pdesc,chooser);
		if (pdesc.getType().equalsIgnoreCase("votable") || pdesc.getType().equalsIgnoreCase("anyxml")){
		    indirectToggle.doClick(); // causes layout to flip.
		}
	      CSH.setHelpIDString(getEditor(),"task.form.largeText");
	}


	protected final JComponent createEditor() {
		text = new JTextArea();
		text.setRows(4);
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		final JScrollPane sp = new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		if (! pval.getIndirect()) {
		    if (pdesc.getDefaultValue() != null) {
		        text.setText(pdesc.getDefaultValue());
		    }
		    if (pval.getValue() != null) {
		        text.setText(pval.getValue());
		    }		
		    pval.setValue(getStringValue());
		}
		text.getDocument().addDocumentListener(this);
		return sp;
	}


    protected JTextArea text;

	// listen to all changes to the dociment, and map this instantly back to the tool document.
	public void changedUpdate(final DocumentEvent e) {
		pval.setValue(getStringValue());
	}

	public void insertUpdate(final DocumentEvent e) {
		pval.setValue(getStringValue());
	}

	public void removeUpdate(final DocumentEvent e) {
		pval.setValue(getStringValue());
	}


	protected String getStringValue() {
	    if (text == null) {
	        return "";
	    } else {
	        return text.getText();
	    }
	}


}
