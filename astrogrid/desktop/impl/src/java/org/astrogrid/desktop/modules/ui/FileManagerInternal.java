/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.apache.commons.vfs.FileObject;
import org.astrogrid.acr.ui.FileManager;

/** internal interface to a file manager component.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 10, 20073:49:24 PM
 */
public interface FileManagerInternal extends FileManager {
    /** show this file object
     * 
     * @param fo if a directory, will show the contents of that directory.
     *  if a file, will show the containing directory, and select this file.
     */
        public void show(FileObject fo);

}
