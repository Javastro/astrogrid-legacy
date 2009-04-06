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
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

/** Open a file using the system browser or default helper application.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class ViewInBrowserActivity extends AbstractFileActivity {

	private final BrowserControl browser;
    private final FileSystemManager vfs;
	
	
	@Override
    protected boolean invokable(final FileObjectView f) { 
		return f.getType().hasContent();
	}


	public ViewInBrowserActivity(final BrowserControl browser,final FileSystemManager vfs) {
		super();
		this.browser = browser;
        this.vfs = vfs;
        setHelpID("activity.open");
		setText("View");
		setIcon(IconHelper.loadIcon("browser16.png"));		
		setToolTipText("View with the default application on your computer");
		setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH,UIComponentMenuBar.MENU_KEYMASK));
	}
	
	// can only handle a single selection.
	@Override
    public void manySelected(final FileObjectView[] list) {
		noneSelected();
	}
	
	@Override
    public void actionPerformed(final ActionEvent e) {
		final List<FileObjectView> l = computeInvokable();
		logger.debug(l);
		final FileObjectView fo =l.get(0);
		logger.debug(fo);
		(new BackgroundWorker<Void>(uiParent.get(),"Displaying " + fo.getBasename(),BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {

			@Override
            protected Void construct() throws Exception {
			    final FileObject f = AstroscopeFileObject.findAstroscopeOrInnermostFileObject(fo.getFileObject());
			    URL u = f.getURL();
			    if (! (u.getProtocol().equals("file") || u.getProtocol().equals("http") || u.getProtocol().equals("ftp"))) { // pass it to the browser directly.
			        // download file to temporary location, and then open it
			        reportProgress("Downloading file to temporary location");
			        final String ext = StringUtils.substringAfterLast(fo.getBasename(),".");
			        final File tmpFile = File.createTempFile("view-in-browser","." + ext);
			        tmpFile.deleteOnExit();
			        logger.debug(tmpFile);
			        final FileObject tmp = vfs.resolveFile(tmpFile.toURI().toString());
			        FileUtil.copyContent(f,tmp);
			        u = tmp.getURL();
			    }
			    reportProgress("Instructing browser to open " + u);
			    browser.openURL(u);
				return null;
			}
		}).start();
	}
	
	

}
