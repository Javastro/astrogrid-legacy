/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.provider.local.LocalFileSystem;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;

import com.l2fprod.common.swing.JDirectoryChooser;

/** simplistic activity which just allows users to download files to a local directory.
 * may even keep around once I've debugged the save activity..
 * 
 * inspired by how downloads from a browser happen.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class SimpleDownloadActivity extends AbstractFileActivity {

    private final FileSystemManager vfs;
    private final ResourceChooserInternal chooser;
	
	
    // applies to all non-local files and folders.
	protected boolean invokable(FileObject f) { 
	    FileSystem fileSystem = f.getFileSystem();
	    return ! (fileSystem instanceof LocalFileSystem);
	}


	public SimpleDownloadActivity(final FileSystemManager vfs, ResourceChooserInternal chooser) {
		super();
        this.vfs = vfs;
        this.chooser = chooser;
		setText("Download");
		setIcon(IconHelper.loadIcon("filesave16.png"));		
		setToolTipText("Download the selected file(s) to local disk.");
	}
//	
//	private static final JDirectoryChooser chooser = new JDirectoryChooser();
//	static {
//	    chooser.setShowingCreateDirectory(true);
//	}
//	

	public void actionPerformed(ActionEvent e) {
		final List l = computeInvokable();
		logger.debug(l);
//		if (chooser.showSaveDialog(uiParent.get().getFrame()) != JFileChooser.APPROVE_OPTION) {
//		    return;
//		}
//		final File saveDir = chooser.getSelectedFile();
//		logger.debug(saveDir);
        final URI saveDir = chooser.chooseDirectoryWithParent("Select directory to download to",false,true,false,uiParent.get().getFrame());
        if (saveDir == null) {
            return;
        }
		(new BulkCopyWorker(vfs,uiParent.get(),saveDir, l)).start();
		
	}
	
	

}
