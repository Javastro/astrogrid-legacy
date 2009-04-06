/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.List;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.BulkCopyWorker;
import org.astrogrid.desktop.modules.ui.actions.BulkMoveWorker;
import org.astrogrid.desktop.modules.ui.actions.CopyCommand;

/** Implementation of operations on files
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 20073:24:40 PM
 */
public class VFSOperationsImpl implements VFSOperations {

    /** interface to something from which the current directory can be accessed */
	public static interface Current {
		FileObjectView get();
	}
	
	private final UIComponent parent;
	private final FileSystemManager vfs;
	private final Current current;
	
	public VFSOperationsImpl(final UIComponent parent, final Current current, final FileSystemManager vfs) {
		super();
		this.parent = parent;
		this.current = current;
		this.vfs = vfs;
	}

	public void copyToCurrent(final List fileObjects) {
		final FileObjectView target = current.get(); // take a ref before going into background. 
		final CopyCommand[] commands = new CopyCommand[fileObjects.size()];
		for (int i = 0; i < commands.length; i++) {
		    commands[i] = new CopyCommand((FileObjectView)fileObjects.get(i));
		}
		(new BulkCopyWorker(vfs,parent,target,commands)).start();

	}

	public void moveToCurrent(final List<FileObjectView> fileObjects) {
		final FileObjectView target = current.get(); // take a ref before going into background. 
		(new BulkMoveWorker(vfs,parent,target,fileObjects)).start();
	}

    public FileSystemManager getVFS() {
        return vfs;
    }

}
