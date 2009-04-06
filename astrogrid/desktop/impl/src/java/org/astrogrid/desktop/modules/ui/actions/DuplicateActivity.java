/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.KeyStroke;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

/**Duplicate one or more files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 7, 20072:35:53 PM
 */
public class DuplicateActivity extends AbstractFileActivity {

    @Override
    protected boolean invokable(final FileObjectView f) {
            return (! f.isDelegate()
                    //&& f.getParent().isWritable(); //should really be testing that the parent is writable
                    );
    }
    
    private final FileSystemManager vfs;

    public DuplicateActivity(final FileSystemManager vfs) {
        super();
        setHelpID("activity.duplicate");
        this.vfs = vfs;
        setText("Duplicate");
        setToolTipText("Make a copy of the selected files");
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,UIComponentMenuBar.MENU_KEYMASK));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final List<FileObjectView> l = computeInvokable();
        // each item in the list is going to be a file object. Construct a command array...
        final CopyCommand[] commands = new CopyCommand[l.size()];
        for (int i = 0; i < l.size(); i++) {
            commands[i] = new CopyCommand(l.get(i));
        }
        // find the parent.
        (new BackgroundWorker<FileObjectView>(uiParent.get(),"Finding parent folder") {

            @Override
            protected FileObjectView construct() throws Exception {
                final FileObject pfo = l.get(0).getFileObject().getParent();
                return new FileObjectView(pfo,null);
            }
            // found parent. now set off bulk copy worker.
            @Override
            protected void doFinished(final FileObjectView fov) {                
                new BulkCopyWorker(vfs,uiParent.get(),fov,commands).start();
            }
        }).start();

    }
    
    

}
