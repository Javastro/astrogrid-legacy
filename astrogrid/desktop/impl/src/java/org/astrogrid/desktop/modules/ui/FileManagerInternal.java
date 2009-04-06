/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import org.astrogrid.acr.ui.FileManager;
import org.astrogrid.desktop.modules.ui.fileexplorer.FileObjectView;

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
        public void show(FileObjectView fo);
        public void show(java.net.URI uriLocation);

}
