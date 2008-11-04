/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import org.apache.commons.vfs.provider.FileProvider;
import org.apache.commons.vfs.provider.VfsComponent;

/** Union of two interfaces that the service needs to export.
 * necessary to make it work in hivemind.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 4, 20072:07:27 AM
 */
public interface VfsFileProvider extends FileProvider, VfsComponent {

}
