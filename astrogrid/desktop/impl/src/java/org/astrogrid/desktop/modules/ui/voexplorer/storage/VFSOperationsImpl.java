/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.storage;

import java.util.List;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.desktop.modules.ui.UIComponent;

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

	public void copyOrMoveToCurrent(List fileObjects) {
		//@todo prompt user for the action to take.
	//	copyToCurrent(fileObjects);
	}

	//@fixme - not correct - overwrites current file, instead of copying into.
	public void copyToCurrent(final List fileObjects) {
	/*	final FileObject base = current.get(); // take a ref before going into background. 
		(new BackgroundWorker(parent,"Copying") {

			protected Object construct() throws Exception {
				for (Iterator i = fileObjects.iterator(); i.hasNext();) {
					FileObject target = (FileObject) i.next();
					base.copyFrom(target,Selectors.SELECT_ALL);
				}
				return null;
			}
		}).start();*/
	}

	public void moveToCurrent(final List fileObjects) {
		final FileObject base = current.get(); // take a ref before going into background. 
		//@todo
	}

}
