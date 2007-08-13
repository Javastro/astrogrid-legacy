/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.plastic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileProvider;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;
import org.apache.commons.vfs.provider.UriParser;
import org.apache.commons.vfs.provider.local.LocalFileSystem;
import org.astrogrid.desktop.modules.ag.vfs.VfsFileProvider;

import ca.odell.glazedlists.EventList;

/** File system view over registered plastic applications.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Aug 7, 20075:07:16 PM
 */
public class PlasticProvider extends AbstractOriginatingFileProvider implements VfsFileProvider {

    private final EventList plasticList;
    

    
    protected final static Collection caps = Collections.unmodifiableCollection(Arrays.asList(new Capability[]{
            Capability.WRITE_CONTENT
            ,Capability.VIRTUAL // dunno what this one does.
            ,Capability.LIST_CHILDREN
    }));
    
    public Collection getCapabilities() {
        return caps;
    }

    public PlasticProvider(EventList plasticList) {
        super();
        this.plasticList = plasticList;
    }


    protected FileSystem doCreateFileSystem(FileName rootName,
            FileSystemOptions fileSystemOptions) throws FileSystemException {
        return new PlasticFileSystem(rootName,fileSystemOptions,plasticList);
    }    
    
    
}
