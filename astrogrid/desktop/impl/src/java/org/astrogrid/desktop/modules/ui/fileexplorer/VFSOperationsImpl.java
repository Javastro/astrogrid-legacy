/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.actions.BulkCopyWorker;
import org.astrogrid.desktop.modules.ui.actions.BulkMoveWorker;

/** Implementation of operations on files - liable to change.
 * @todo hook this into tasks infrastryctyre, when it comes along - use to 
 * display a dialogue with a progress bar.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 20073:24:40 PM
 */
public class VFSOperationsImpl implements VFSOperations {

	public static interface Current {
		FileObject get();
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
		final FileObject target = current.get(); // take a ref before going into background. 
		(new BulkCopyWorker(vfs,parent,target,fileObjects)).start();

	}

	public void moveToCurrent(final List fileObjects) {
		final FileObject target = current.get(); // take a ref before going into background. 
		(new BulkMoveWorker(vfs,parent,target,fileObjects)).start();
	}

}
