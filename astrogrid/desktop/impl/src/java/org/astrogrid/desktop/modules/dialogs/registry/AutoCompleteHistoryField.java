/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs.registry;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

/** A swing component that adds auto-completion of entered terms.
 * 
 * @todo not working at present - factord this into a separate class to keep things distinct, so they can be fixed later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20079:36:08 AM
 */
public class AutoCompleteHistoryField extends /* later JComboBox */ JTextField {
 /**
 * 
 */
public AutoCompleteHistoryField(ActionListener a) {
	this.delegate =a;
}

// sets value of the entry box
public void setValue(String s) {
	this.setText(s);
}
// gets current entered value.
public String getValue() {
	String s= this.getText();
	final String searchTerm =s.trim();
	if (! history.contains(searchTerm)) {
		history.add(searchTerm);
	}
	return s;
}

// object ot delegate button clicks to.

private final ActionListener delegate;


// stubs of the proper auto-complete solution.
private final JComboBox keywordField = new JComboBox();
private final EventList history = new BasicEventList();


// configuration - needs to happen on EDT.
public void run() { 
			AutoCompleteSupport acSupport = AutoCompleteSupport.install(keywordField,history);
			acSupport.setSelectsTextOnFocusGain(true);
			acSupport.setFilterMode(TextMatcherEditor.STARTS_WITH);
			acSupport.setStrict(false);
			final JTextField editor = ((JTextField)keywordField.getEditor().getEditorComponent());
			editor.getInputMap().put(enter,"search");
			editor.getActionMap().put("search",searchAction);
		}
	
	
	final KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
	final Action searchAction = new AbstractAction() {
		public void actionPerformed(final ActionEvent e) {
					delegate.actionPerformed(e);
			}
	};
}
