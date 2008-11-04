/**
 * 
 */
package org.astrogrid.desktop.modules.ui.taskrunner;

import java.awt.Color;
import java.text.Format;
import java.text.ParsePosition;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.system.CSH;

/** Abstract form element for parameters which have some formatting constrains.
 * Note that this is only a hint, not a validation - as the 
 * resource descriptions are so poor that we can't reject invalid input 
 * confidently.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 5, 20072:12:19 PM
 */
public class LooselyFormattedFormElement extends AbstractTaskFormElement implements DocumentListener {

	/** used to control the correct format of the input */
	private final Format format;
	
	public LooselyFormattedFormElement(final ParameterValue pval, final ParameterBean pdesc,final Format format,final ResourceChooserInternal chooser) {
		super(pval, pdesc,chooser);
		this.format = format;
		disableIndirect(); // never allow indirectionn for these primitive types.
		CSH.setHelpIDString(getEditor(),"task.form.formatted");
	}

	protected final JComponent createEditor() {
	    text = new JTextField();
	    this.original = text.getBorder();
	    this.warn = BorderFactory.createLineBorder(Color.RED);
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
	    return text;
	}

	protected JTextComponent text;
	protected Border original;
	protected Border warn;
	
	// listen to all changes to the dociment, and map this instantly back to the tool document.
	public void changedUpdate(final DocumentEvent e) {
		update();
	}

	private void update() {
		final String s= getStringValue();
		pval.setValue(s);
		if (format.parseObject(s,new ParsePosition(0)) != null) {
			text.setBorder(original);
		} else {
			text.setBorder(warn);
		}
	}

	public void insertUpdate(final DocumentEvent e) {
		update();
	}

	public void removeUpdate(final DocumentEvent e) {
		update();
	}

	protected String getStringValue() {
		return text.getText();
	}


}
