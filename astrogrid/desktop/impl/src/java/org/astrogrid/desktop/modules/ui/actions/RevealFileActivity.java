/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.FileManagerFactory;

/** action to reveal a file.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 */
public class RevealFileActivity extends AbstractFileActivity {

    private final FileSystemManager vfs;
    private final FileManagerFactory mgr;
	
	
    // applies to all non-local files and folders.
	protected boolean invokable(FileObject f) { 
            return f instanceof DelegateFileObject
                    && ! ((DelegateFileObject)f).getDelegateFile().getName().getScheme().equals("tmp")
                    ;

	}


	public RevealFileActivity(final FileSystemManager vfs, FileManagerFactory mgr) {
		super();
        this.vfs = vfs;
        this.mgr = mgr;
		setText("Show file");
		setIcon(IconHelper.loadIcon("fileopen16.png"));		
		setToolTipText("Show this file in file explorer");
	}

    // can only handle a single selection.
    public void manySelected(FileObject[] list) {
        noneSelected();
    }
    
	public void actionPerformed(ActionEvent e) {
		final List l = computeInvokable();
		logger.debug(l);
        final DelegateFileObject del = (DelegateFileObject)l.get(0);
        FileObject fo = del.getDelegateFile();
        logger.debug(fo);		
	
        mgr.show(fo);
		
	}
	
	

}
