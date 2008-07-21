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
import org.astrogrid.desktop.modules.ui.UIComponentMenuBar;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 7, 20072:35:53 PM
 */
public class DuplicateActivity extends AbstractFileActivity {

    protected boolean invokable(FileObject f) {
        try {
            return (! AstroscopeFileObject.isDelegateOrAstroscopeFileObject(f)
                    && f.getParent().isWriteable());
        } catch (FileSystemException x) {
            return false;
        }
    }
    
    private final FileSystemManager vfs;

    public DuplicateActivity(FileSystemManager vfs) {
        super();
        setHelpID("activity.duplicate");
        this.vfs = vfs;
        setText("Duplicate");
        setToolTipText("Make a copy of the selected files");
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,UIComponentMenuBar.MENU_KEYMASK));
    }
    
    public void actionPerformed(ActionEvent e) {
        final List l = computeInvokable();
        // each item in the list is going to be a file object. Construct a command array...
        CopyCommand[] commands = new CopyCommand[l.size()];
        for (int i = 0; i < l.size(); i++) {
            commands[i] = new CopyCommand((FileObject)l.get(i));
        }
        try {
            FileObject parent = ((FileObject)l.get(0)).getParent();
            new BulkCopyWorker(vfs,uiParent.get(),parent,commands).start();
        } catch (FileSystemException x) {
            uiParent.get().showTransientError("Unable to duplicate",ExceptionFormatter.formatException(x));
        }
    }
    

}
