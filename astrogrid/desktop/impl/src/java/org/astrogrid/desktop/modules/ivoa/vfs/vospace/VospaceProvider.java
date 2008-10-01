/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.vfs.vospace;

import java.util.Collection;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemConfigBuilder;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileProvider;
import org.astrogrid.desktop.modules.ag.vfs.VfsFileProvider;
import org.astrogrid.desktop.modules.ag.vfs.myspace.MyspaceProvider;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.desktop.modules.ivoa.RegistryInternal;

/** Stub for implementation of VOSpace VFS
 * @author Dave
 * @since Oct 1, 200810:44:43 AM
 * @see MyspaceProvider
 */
public class VospaceProvider extends AbstractFileProvider implements VfsFileProvider {

    private final CommunityInternal community;
    private final RegistryInternal registry;
    
    /**
     * @param community
     * @param registry
     */
    public VospaceProvider(final CommunityInternal community,
            final RegistryInternal registry) {
        super();
        this.community = community;
        this.registry = registry;
    }

    public FileObject createFileSystem(final String arg0, final FileObject arg1,
            final FileSystemOptions arg2) throws FileSystemException {
        return null;
    }

    public FileObject findFile(final FileObject arg0, final String arg1,
            final FileSystemOptions arg2) throws FileSystemException {
        return null;
    }

    public Collection getCapabilities() {
        return null;
    }

    public FileSystemConfigBuilder getConfigBuilder() {
        return null;
    }

    public FileName parseUri(final FileName arg0, final String arg1)
            throws FileSystemException {
        return null;
    }

    public void close() {
    }

    public void init() throws FileSystemException {
    }


}
