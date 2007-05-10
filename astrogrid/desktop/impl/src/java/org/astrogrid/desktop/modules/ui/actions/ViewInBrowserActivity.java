/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.collections.Transformer;
import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class ViewInBrowserActivity extends AbstractFileActivity {

	private final BrowserControl browser;
	
	
	protected boolean invokable(FileObject f) {
		return true;
	}


	public ViewInBrowserActivity(final BrowserControl browser) {
		super();
		this.browser = browser;
		setText("View in browser");
		setIcon(IconHelper.loadIcon("browser16.png"));		
		setToolTipText("display the selection in the system webbrowser");
	}
	
	// can only handle a single selection.
	public void manySelected(FileObject[] list) {
		noneSelected();
	}
	
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		final FileObject fo = (FileObject)l.get(0);
		(new BackgroundWorker(uiParent.get(),"Displaying " + fo.getName().getBaseName()) {

			protected Object construct() throws Exception {
				browser.openURL(fo.getURL());
				return null;
			}
		}).start();
	}
	
	

}
