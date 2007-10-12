/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;

/** Activity to rename a single file.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class RenameActivity extends AbstractFileActivity {

    private final FileSystemManager vfs;
	
	
	protected boolean invokable(FileObject f) { 
		try {
            return (! (f instanceof DelegateFileObject)
                    && f.isWriteable());
        } catch (FileSystemException x) {
            return false;
        }
	}


	public RenameActivity(final FileSystemManager vfs) {
		super();
        this.vfs = vfs;
		setText("Rename");
		//setIcon(IconHelper.loadIcon("browser16.png"));		
		setToolTipText("rename this file");
	}
	
	// can only handle a single selection.
	public void manySelected(FileObject[] list) {
		noneSelected();
	}
	
	public void actionPerformed(ActionEvent e) {
		List l = computeInvokable();
		logger.debug(l);
		final FileObject original = (FileObject)l.get(0);
	
		
		(new BackgroundWorker(uiParent.get(),"Renaming " + original.getName().getBaseName()) {

			protected Object construct() throws Exception {
		        final FileObject parent = original.getParent();
		        FileObject f;
		        do {
		            String nuName = JOptionPane.showInputDialog(uiParent.get().getComponent(),"Enter a new name",original.getName().getBaseName());
		            if (StringUtils.isEmpty(nuName)) {
		                return null; // user pressed cancel;
		            }
		            f = parent.resolveFile(nuName);
		        } while (f.exists());
		        original.moveTo(f);
			    FileSystem fs = parent.getFileSystem();
			    if (fs instanceof AbstractFileSystem) {
			        ((AbstractFileSystem)fs).fireFileChanged(parent);
			    }
				return null;
			}
		}).start();
	}
	
	

}
