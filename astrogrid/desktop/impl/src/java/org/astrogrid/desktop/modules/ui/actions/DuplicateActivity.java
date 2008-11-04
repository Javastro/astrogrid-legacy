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

/**Duplicate one or more files.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 7, 20072:35:53 PM
 */
public class DuplicateActivity extends AbstractFileActivity {

    protected boolean invokable(final FileObject f) {
        try {
            return (! AstroscopeFileObject.isDelegateOrAstroscopeFileObject(f)
                    && f.getParent().isWriteable());
        } catch (final FileSystemException x) {
            return false;
        }
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
    
    public void actionPerformed(final ActionEvent e) {
        final List l = computeInvokable();
        // each item in the list is going to be a file object. Construct a command array...
        final CopyCommand[] commands = new CopyCommand[l.size()];
        for (int i = 0; i < l.size(); i++) {
            commands[i] = new CopyCommand((FileObject)l.get(i));
        }
        try {
            final FileObject parent = ((FileObject)l.get(0)).getParent();
            new BulkCopyWorker(vfs,uiParent.get(),parent,commands).start();
        } catch (final FileSystemException x) {
            uiParent.get().showTransientError("Unable to duplicate",ExceptionFormatter.formatException(x));
        }
    }
    

}
