/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.astrogrid.desktop.modules.ui.folders.ResourceFolder;

/** Abstract class for a component that edits a resource list.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 30, 20072:13:27 PM
 */
public abstract class EditingPanel extends JPanel {
	/**
	 * 
	 */
	public EditingPanel() {
		 ok = new JButton("Create");
		cancel = new JButton("Cancel");
		folderName = new JTextField();
		folderName.getDocument().addDocumentListener(new DocumentListener() {

			public void changedUpdate(final DocumentEvent e) {
				ok.setEnabled(shouldOkBeEnabled());
			}
			public void insertUpdate(final DocumentEvent e) {
				ok.setEnabled(shouldOkBeEnabled());		
			}
			public void removeUpdate(final DocumentEvent e) {
				ok.setEnabled(shouldOkBeEnabled());		
			}
		});
	}
	
	protected final JButton ok ;
	protected final JButton cancel;
	protected final JTextField folderName ;
	private ResourceFolder currentlyEditing;

	/** extend this to add in further validation - 
	 * you'll have to add suitable event listeners to trigger it too 
	 */
	protected boolean shouldOkBeEnabled() {
		return isNameNonEmpty();
	}
	
	private boolean isNameNonEmpty() {
		final String s = folderName.getText();
		return ! ( s == null || s.trim().length() == 0);
	}
	
	/** configure the panel to edit the provided folder */
	public void setCurrentlyEditing(final ResourceFolder currentlyEditing) {
		this.currentlyEditing = currentlyEditing;
		folderName.setText(currentlyEditing.getName());
	}
	
	/** access the folder / list being edited by this panel */
	public ResourceFolder getCurrentlyEditing() {
		return currentlyEditing;
	}
	
	/** load edits made by this panel into the currenlty edited folder */
	public void loadEdits() {
		currentlyEditing.setName(folderName.getText());	
	}
	
	public JButton getOkButton() {
		return ok;
	}

	public JButton getCancelButton() {
		return cancel;
	}

}
