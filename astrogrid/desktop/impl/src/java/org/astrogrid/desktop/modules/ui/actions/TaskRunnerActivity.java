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
import org.astrogrid.desktop.modules.system.CSH;
import org.astrogrid.desktop.modules.ui.TaskRunnerInternal;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** invoke a cea task.
 * @todo initialize the form in a backgrund thread
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20075:25:43 PM
 */
public class TaskRunnerActivity extends AbstractFileOrResourceActivity {
/**
 * 
 */
public TaskRunnerActivity(TaskRunnerInternal t) {
	CSH.setHelpIDString(this, "resourceActivity.taskRunner");		
	this.t = t;
	setText("Execute Task");
	setIcon(IconHelper.loadIcon("exec16.png"));
	setToolTipText("Invoke the selected Remote Application (CEA)");
}
private final TaskRunnerInternal t;

public void actionPerformed(ActionEvent e) {
    List resources = computeInvokableResources();
    switch(resources.size()) {
        case 0:
            break;
        case 1:
            Resource r = (Resource) resources.get(0);
            t.invokeTask(r);
            break;
        default:
            //can'\t edit more than one.           
    }
    List files= computeInvokableFiles();
    switch(files.size()) {
        case 0:
            break;
        case 1:
            FileObject fo = (FileObject)files.get(0);
            //@todo - implement.
            break;
        default:
            //future - do something with a multiple selection.
    }
}


	
	public void manyFilesSelected(FileObject[] l) {
		noneSelected(); // can't operate on more than one file.
	}

	public void manyResourcesSelected(Resource[] l) {
		noneSelected(); // can't operate on more than one resource;
	}

	public boolean invokable(FileObject fo) {
	    try {
	        if (fo.getType().hasContent()) { // must be a file, not a folder.
	            String mime = fo.getContent().getContentInfo().getContentType();
	            if (mime != null && mime.equals(VoDataFlavour.MIME_CEA)) {
	                return true;
	            }
	        }
	    } catch (FileSystemException x) {
	        return false;
	    }
	    return false;
	}

	public boolean invokable(Resource resource) {
	    return resource instanceof CeaApplication;
	}


}
