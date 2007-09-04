/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileUtil;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

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
		logger.debug(l);
		final FileObject fo = (FileObject)l.get(0);
		logger.debug(fo);
		(new BackgroundWorker(uiParent.get(),"Displaying " + fo.getName().getBaseName()) {

			protected Object construct() throws Exception {
			    URL u = fo.getURL();
			    if (! (u.getProtocol().equals("file") || u.getProtocol().equals("http") || u.getProtocol().equals("ftp"))) { // pass it to the browser directly.
			        // download file to temporary location, and then open it
			        String name = StringUtils.substringBeforeLast(fo.getName().getBaseName(),".");
			        String ext = StringUtils.substringAfterLast(fo.getName().getBaseName(),".");
			        File f = File.createTempFile("view-in-browser-" + name,"." + ext);
			        f.deleteOnExit();
			        logger.debug(f);
			        FileObject tmp = vfs.resolveFile(f.toURI().toString());
			        FileUtil.copyContent(fo,tmp);
			        u = tmp.getURL();
			    }
			    logger.debug("instructing browser to open " + u);
			    browser.openURL(u);
				return null;
			}
		}).start();
	}
	
	

}
