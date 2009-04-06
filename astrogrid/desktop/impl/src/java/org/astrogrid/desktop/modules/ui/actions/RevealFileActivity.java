/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.FileManagerFactory;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

/**Reveal a file in fileexplorer.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 10, 20071:03:46 PM
 * 
 * NB: I've changed the implementation from going down one delegate step to going down all delegate steps to the innermost. seems most sensible.
 */
public class RevealFileActivity extends AbstractFileActivity {

    private final FileSystemManager vfs;
    private final FileManagerFactory mgr;
	
	
    // applies to all non-local files and folders.
	@Override
    protected boolean invokable(final FileObjectView f) { 
            return f.isDelegate() 
                    && !("tmp".equals(f.getInnermostScheme()) || "http".equals(f.getInnermostScheme()) )
                    ;
	}


	public RevealFileActivity(final FileSystemManager vfs, final FileManagerFactory mgr) {
		super();
		setHelpID("activity.reveal");
        this.vfs = vfs;
        this.mgr = mgr;
		setText("Show in File Explorer");
		setIcon(IconHelper.loadIcon("fileopen16.png"));		
		setToolTipText("Show this file in file explorer");
	}

    // can only handle a single selection.
    @Override
    public void manySelected(final FileObjectView[] list) {
        noneSelected();
    }
    
	@Override
    public void actionPerformed(final ActionEvent e) {
		final List<FileObjectView> l = computeInvokable();
		(new BackgroundWorker<FileObjectView>(uiParent.get(),"Finding target file object") {

            @Override
            protected FileObjectView construct() throws Exception {
                final FileObject fileObject = AstroscopeFileObject.findInnermostFileObject(l.get(0).getFileObject());
                return new FileObjectView(fileObject,null);
            }
            @Override
            protected void doFinished(final FileObjectView result) {
                mgr.show(result);
            }
		}).start();		
	}
	
	

}
