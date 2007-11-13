/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.KeyStroke;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 7, 20072:35:53 PM
 */
public class DuplicateActivity extends AbstractFileActivity {

    protected boolean invokable(FileObject f) {
        try {
            return (! (f instanceof DelegateFileObject)
                    && f.getParent().isWriteable());
        } catch (FileSystemException x) {
            return false;
        }
    }
    
    private final FileSystemManager vfs;

    public DuplicateActivity(FileSystemManager vfs) {
        super();
        this.vfs = vfs;
        setText("Duplicate");
        setToolTipText("Make a copy of the selected files");
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,UIComponentMenuBar.MENU_KEYMASK));
    }
    
    public void actionPerformed(ActionEvent e) {
        final List l = computeInvokable();
        try {
            FileObject parent = ((FileObject)l.get(0)).getParent();
            new BulkCopyWorker(vfs,uiParent.get(),parent,l).start();
        } catch (FileSystemException x) {
            uiParent.get().showTransientError("Unable to duplicate",ExceptionFormatter.formatException(x));
        }
    }
    

}
