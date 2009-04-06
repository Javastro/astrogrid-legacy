/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import java.util.List;

import org.apache.commons.vfs.FileSystemManager;

/** interface to a component that performs VFS operations
 * This component provides UI feedback, does tasks in background thread, etc.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 30, 20073:20:49 PM
 */
public interface VFSOperations {
	
    /** copy this list of fileobjects / fileNmaes (which might be subtrees of folders 
     * to the 'current location'
     * @param objds a mixed list of fileObjects / uris / urls / string
     */
	public void copyToCurrent(List objs) ;
	
    /** move this list of fileobjects (which might be subtrees of folders 
     * to the 'current location'
     * @param objds a list of fileObjects 
     */
	public void moveToCurrent(List<FileObjectView> objs);
	
	/** convenince function - provide access to the underlying vfs object */
	public FileSystemManager getVFS();

}
