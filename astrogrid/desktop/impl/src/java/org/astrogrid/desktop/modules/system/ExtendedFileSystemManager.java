/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemManager;

/** Extension to the standard file system manager which provides
 * access to some additional functions.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 26, 200710:53:36 AM
 */
public interface ExtendedFileSystemManager extends FileSystemManager {

    /** access a virtual filesystem used for presenting results */
    public FileSystem getResultsFilesystem();

}