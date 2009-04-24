/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.astrogrid.desktop.modules.system;

import java.util.Map;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.provider.FileProvider;

/**
 * Exgtended interface to VFS.
 * @author noel
 */
public interface HivemindFileSystemManager extends FileSystemManager{
    Map<String,FileProvider> getProvidermap();
}
