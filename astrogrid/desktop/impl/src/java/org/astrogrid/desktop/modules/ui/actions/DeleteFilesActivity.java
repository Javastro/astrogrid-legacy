/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.apache.commons.vfs.provider.local.LocalFileSystem;
import org.astrogrid.acr.system.BrowserControl;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

import com.l2fprod.common.swing.JDirectoryChooser;

/** delete one or more files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class DeleteFilesActivity extends AbstractFileActivity {

    private final FileSystemManager vfs;
	
	
    // applies to all non-local files and folders.
	protected boolean invokable(FileObject f) { 
	    try {
            return f.isWriteable();
        } catch (FileSystemException x) {
            logger.error("FileSystemException",x);
            return false;
        }
	}


	public DeleteFilesActivity(final FileSystemManager vfs) {
		super();
        this.vfs = vfs;
		setText("Delete");
		setIcon(IconHelper.loadIcon("editdelete16.png"));		
		setToolTipText("Deleted this files");
	}


	public void actionPerformed(ActionEvent e) {
		final List l = computeInvokable();
		logger.debug(l);
		if (JOptionPane.showConfirmDialog(uiParent.get().getFrame(),"Delete these " + l.size() + " files?")
		    != JOptionPane.OK_OPTION) {
		}

		(new BackgroundWorker(uiParent.get(),"Deleting files") {

            protected Object construct() throws Exception {
                for (Iterator i = l.iterator(); i.hasNext();) {
                    FileObject f = (FileObject) i.next();
                    f.delete(Selectors.SELECT_ALL);
                }
                return null;
            }
		}).start();
		
	}
	
	

}
