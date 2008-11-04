/**
 * 
 */
package org.astrogrid.desktop.modules.ui.fileexplorer;

import javax.swing.ImageIcon;

import org.apache.commons.vfs.FileObject;

/**
 *  Maps FileObjects to suitable icons.
 * @future configure the filemap using hivemind later??
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 24, 200712:07:20 PM
 */
public interface IconFinder {
    /** find an icon for a file object */
    public ImageIcon find(FileObject fo);
    
    public ImageIcon defaultFolderIcon();

}