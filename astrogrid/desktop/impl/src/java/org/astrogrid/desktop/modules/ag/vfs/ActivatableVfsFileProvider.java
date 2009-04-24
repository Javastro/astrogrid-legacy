/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.astrogrid.desktop.modules.ag.vfs;

/**
 *A VFS file provider that may or may not be enabled, depending on some internal logic.
 * @author noel
 */
public interface ActivatableVfsFileProvider extends VfsFileProvider{
    /** indicate whether this file provider is going to be active
     * this method is checked by ui components, and is assumed to return a constant value
     * for the runtime of the application
     * @return
     */
    public boolean isActive();
}
