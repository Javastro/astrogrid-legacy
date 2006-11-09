/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.net.URI;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/** Abstract class that captures commonality of andy implementation of a resource chooser dialogue.
 * not an interface, as want to extends from JDialog too.
 * @author Noel Winstanley
 * @since Nov 6, 20063:31:07 PM
 */
public abstract class AbstractResourceChooserDialog extends JDialog {

	/**
	 * bog-standard dialogue constructors.
	 */
	public AbstractResourceChooserDialog() {
		super();
	}

	public AbstractResourceChooserDialog(Dialog owner, boolean modal) throws HeadlessException {
		super(owner, modal);
	}

	public AbstractResourceChooserDialog(Dialog owner, String title, boolean modal, GraphicsConfiguration gc) throws HeadlessException {
		super(owner, title, modal, gc);
	}

	public AbstractResourceChooserDialog(Dialog owner, String title, boolean modal) throws HeadlessException {
		super(owner, title, modal);
	}

	public AbstractResourceChooserDialog(Dialog owner, String title) throws HeadlessException {
		super(owner, title);
	}

	public AbstractResourceChooserDialog(Dialog owner) throws HeadlessException {
		super(owner);
	}

	public AbstractResourceChooserDialog(Frame owner, boolean modal) throws HeadlessException {
		super(owner, modal);
	}

	public AbstractResourceChooserDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
	}

	public AbstractResourceChooserDialog(Frame owner, String title, boolean modal) throws HeadlessException {
		super(owner, title, modal);
	}

	public AbstractResourceChooserDialog(Frame owner, String title) throws HeadlessException {
		super(owner, title);
	}

	public AbstractResourceChooserDialog(Frame owner) throws HeadlessException {
		super(owner);
	}

	public abstract URI getUri() ;

	public abstract void setEnableLocalFilePanel(boolean enableLocalFilePanel) ;

	public abstract  void setEnableMySpacePanel(boolean enableMySpacePanel);
	public abstract void setEnableURIPanel(boolean enableURIPanel) ;

	public abstract void setEnabledDirectorySelection(boolean enableDirectorySelection);

	/** resets the dialog, then hides it. */
	public abstract void resetAndHide();
	public abstract void setUri(URI uri) ;

}