/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.FileManagerFactory;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

/** action to reveal a file.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 * 
 * NB: I've changed the implementation from going down one delegate step to going down all delegate steps to the innermost. seems most sensible.
 */
public class RevealFileActivity extends AbstractFileActivity {

    private final FileSystemManager vfs;
    private final FileManagerFactory mgr;
	
	
    // applies to all non-local files and folders.
	protected boolean invokable(FileObject f) { 
            return AstroscopeFileObject.isDelegateOrAstroscopeFileObject(f)
                    && ! AstroscopeFileObject.findInnermostFileObject(f).getName().getScheme().equals("tmp")
                    ;

	}


	public RevealFileActivity(final FileSystemManager vfs, FileManagerFactory mgr) {
		super();
		setHelpID("activity.reveal");
        this.vfs = vfs;
        this.mgr = mgr;
		setText("Show in File Explorer");
		setIcon(IconHelper.loadIcon("fileopen16.png"));		
		setToolTipText("Show this file in file explorer");
	}

    // can only handle a single selection.
    public void manySelected(FileObject[] list) {
        noneSelected();
    }
    
	public void actionPerformed(ActionEvent e) {
		final List l = computeInvokable();
        mgr.show(AstroscopeFileObject.findInnermostFileObject((FileObject)l.get(0)));
		
	}
	
	

}
