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
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.TaskInvoker;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/** invoke a cea task.
 * @todo initialize the form in a backgrund thread
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 26, 20075:25:43 PM
 */
public class CeaActivity extends AbstractFileOrResourceActivity {
/**
 * 
 */
public CeaActivity(TaskInvoker t) {
	CSH.setHelpIDString(this, "resourceTask.cea");		
	this.t = t;
	setText("Execute Task");
	setIcon(IconHelper.loadIcon("exec16.png"));
	setToolTipText("Invoke the selected long running task");
}
private final TaskInvoker t;
private Resource currentResource;
private FileObject currentFile;

public void noneSelected() {
	currentResource = null;
	currentFile = null;
	super.noneSelected();
}
	public void actionPerformed(ActionEvent e) {
		if (currentResource != null) {
		(new BackgroundWorker(uiParent.get(),"Opening task launcher") {

			protected Object construct() throws Exception {
				t.invokeTask(currentResource);
				return null;
			}
		}).start();
		} else if (currentFile != null) {
			//@todo implement.
		}
	}
	
	public void manyFilesSelected(FileObject[] l) {
		noneSelected(); // can't operate on more than one file.
	}

	public void manyResourcesSelected(Resource[] l) {
		noneSelected(); // can't operate on more than one resource;
	}

	public void oneSelected(FileObject fo) {
		String mime;
		currentResource = null;
		try {
			mime = fo.getContent().getContentInfo().getContentType();
			if (mime != null && mime.equals(VoDataFlavour.MIME_CEA)) {
				setEnabled(true);
				currentFile = fo;
			} else {
				setEnabled(false);
				currentFile = null;
			} 
		} catch (FileSystemException x) {
			logger.error("FileSystemException",x);
		}
	}

	public void oneSelected(Resource resource) {
		currentFile = null;
		if (resource instanceof CeaApplication) {
			setEnabled(true);
			currentResource = resource;
		} else {
			setEnabled(false);
			currentResource = null;
		}
	}

	public void somethingElseSelected() {
		noneSelected();
	}

}
