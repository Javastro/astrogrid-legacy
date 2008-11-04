/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FilesCache;
import org.apache.commons.vfs.cache.SoftRefFilesCache;

/** Extension to the standard VFS files cache that adds some logging.
 * 
 * @future later, might re-implmenet this to be backed by an eh-cache - dunno.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 8, 200712:24:18 PM
 */
public class DesktopFilesCache extends SoftRefFilesCache implements FilesCache {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(DesktopFilesCache.class);

    public FileObject getFile(final FileSystem filesystem, final FileName name) {
        final FileObject fo = super.getFile(filesystem, name);
        if (fo == null && logger.isDebugEnabled()) {
            logger.debug("Cache miss for " + name);
            MapUtils.debugPrint(System.out,"FileObjects cache for " + filesystem,getOrCreateFilesystemCache(filesystem));
        }
        return fo;
    }
    
    public void clear(final FileSystem filesystem) {
        logger.debug("Clearing filesystem " + filesystem);
        super.clear(filesystem);
    }

    public void putFile(final FileObject file) {
        if (logger.isDebugEnabled()) {
            logger.debug("Caching " + file);
        }
        super.putFile(file);
    }

    public void removeFile(final FileSystem filesystem, final FileName name) {
        if (logger.isDebugEnabled()) {
            logger.debug("Removing " + name + ", " + filesystem);
        }
        super.removeFile(filesystem, name);
    }

}
