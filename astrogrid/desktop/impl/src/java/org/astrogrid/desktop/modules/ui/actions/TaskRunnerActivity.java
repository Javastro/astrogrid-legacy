/**
 * 
 */
package org.astrogrid.desktop.modules.ui.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.TaskRunnerInternal;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** Open TaskRunner for a resource.
 * @todo initialize the form in a backgrund thread
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20075:25:43 PM
 */
public class TaskRunnerActivity extends AbstractFileOrResourceActivity {
/**
 * 
 */
public TaskRunnerActivity(final TaskRunnerInternal t) {
    setHelpID("activity.taskRunner");		
	this.t = t;
	setText("Execute Task");
	setIcon(IconHelper.loadIcon("exec16.png"));
	setToolTipText("Invoke the selected Remote Application (CEA)");
    //setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,UIComponentMenuBar.MENU_KEYMASK));	
}
private final TaskRunnerInternal t;

@Override
public void actionPerformed(final ActionEvent e) {
    final List resources = computeInvokableResources();
    switch(resources.size()) {
        case 0:
            break;
        case 1:
            final Resource r = (Resource) resources.get(0);
            t.invokeTask(r);
            break;
        default:
            //can'\t edit more than one.           
    }
    final List files= computeInvokableFiles();
    switch(files.size()) {
        case 0:
            break;
        case 1:
            final FileObject fo = (FileObject)files.get(0);
            t.edit(fo);
            break;
        default:
            //future - do something with a multiple selection.
    }
}


	
	@Override
    public void manySelected(final FileObject[] l) {
		noneSelected(); // can't operate on more than one file.
	}

	@Override
    public void someSelected(final Resource[] l) {
		noneSelected(); // can't operate on more than one resource;
	}

	@Override
    public boolean invokable(final FileObject fo) {
	    try {
	        if (fo.getType().hasContent()) { // must be a file, not a folder.
	            final String mime = fo.getContent().getContentInfo().getContentType();
	            if (mime != null && mime.equals(VoDataFlavour.MIME_CEA)) {
	                return true;
	            }
	        }
	    } catch (final FileSystemException x) {
	        return false;
	    }
	    return false;
	}

	@Override
    public boolean invokable(final Resource resource) {
	    // invokable if there's a non-ADQL interface to this app
	    return resource instanceof CeaApplication
	        && BuildQueryActivity.whatKindOfInterfaces((CeaApplication)resource) < 1;
	}


}
