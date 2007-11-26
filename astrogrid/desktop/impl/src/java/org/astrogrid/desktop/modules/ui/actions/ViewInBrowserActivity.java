/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.swing.KeyStroke;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileUtil;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class ViewInBrowserActivity extends AbstractFileActivity {

	private final BrowserControl browser;
    private final FileSystemManager vfs;
	
	
	protected boolean invokable(FileObject f) { 
		return true;
	}


	public ViewInBrowserActivity(final BrowserControl browser,final FileSystemManager vfs) {
		super();
		this.browser = browser;
        this.vfs = vfs;
		setText("View");
		setIcon(IconHelper.loadIcon("browser16.png"));		
		setToolTipText("View with the default application on your computer");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH,UIComponentMenuBar.MENU_KEYMASK));
	}
	
	// can only handle a single selection.
	public void manySelected(FileObject[] list) {
		noneSelected();
	}
	
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		logger.debug(l);
		final FileObject fo = (FileObject)l.get(0);
		logger.debug(fo);
		(new BackgroundWorker(uiParent.get(),"Displaying " + fo.getName().getBaseName(),BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {

			protected Object construct() throws Exception {
			    URL u = fo.getURL();
			    if (! (u.getProtocol().equals("file") || u.getProtocol().equals("http") || u.getProtocol().equals("ftp"))) { // pass it to the browser directly.
			        // download file to temporary location, and then open it
			        reportProgress("Downloading file to temporary location");
			        String name = StringUtils.substringBeforeLast(fo.getName().getBaseName(),".");
			        String ext = StringUtils.substringAfterLast(fo.getName().getBaseName(),".");
			        File f = File.createTempFile("view-in-browser-" + name,"." + ext);
			        f.deleteOnExit();
			        logger.debug(f);
			        FileObject tmp = vfs.resolveFile(f.toURI().toString());
			        FileUtil.copyContent(fo,tmp);
			        u = tmp.getURL();
			    }
			    reportProgress("Instructing browser to open " + u);
			    browser.openURL(u);
				return null;
			}
		}).start();
	}
	
	

}
