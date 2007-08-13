/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.List;

/** interface to a component that performs VFS operations
 * understanding is that this component provides UI feedback, does tasks in background thread, etc.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 20073:20:49 PM
 */
public interface VFSOperations {
	
    /** copy this list of fileobjects (which might be subtrees of folders 
     * to the 'current location'
     * @param fileObjects
     */
	public void copyToCurrent(List fileObjects) ;
	
    /** move this list of fileobjects (which might be subtrees of folders 
     * to the 'current location'
     * @param fileObjects
     */
	public void moveToCurrent(List fileObjects);

}
