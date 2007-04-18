/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.storage;

import java.util.List;

/** interface to a component that performs VFS operations
 * understanding is that this component provides UI feedback, does tasks in background thread, etc.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 20073:20:49 PM
 */
public interface VFSOperations {
	
	public void copyToCurrent(List fileObjects) ;
	
	public void moveToCurrent(List fileObjects);
	
	public void copyOrMoveToCurrent(List fileObjects);

}
