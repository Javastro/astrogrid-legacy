/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.impl.VirtualFileSystem;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.ag.vfs.VfsFileProvider;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.RPFileName;

/** File provider that provides access to results of cea and other remote process execution.
 * 
 * at moment just provides access to the results. However, it occurs to me
 * that if we modeled the containing folders as the processes themselves,
 * then you could enable management using 'delete', etc, refresh manualy, etc
 *  - however, leave this for now?
 * 
 * @todo see {@link VirtualFileSystem} later.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 20074:47:51 PM
 */
public class RPProvider extends
        AbstractOriginatingFileProvider implements VfsFileProvider {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(RPProvider.class);

    public RPProvider(RemoteProcessManagerInternal rpmi) {
        super();
        this.rpmi = rpmi;
        setFileNameParser(new RPParser(rpmi));
    }


    protected final static Collection caps = Collections.unmodifiableCollection(Arrays.asList(new Capability[]{
            Capability.READ_CONTENT
            ,Capability.URI
            ,Capability.LIST_CHILDREN
            ,Capability.JUNCTIONS
    }));
    
    public Collection getCapabilities() {
        return caps;
    }
    
    private final RemoteProcessManagerInternal rpmi;
    
    protected FileSystem doCreateFileSystem(FileName rootName,
            FileSystemOptions fileSystemOptions) throws FileSystemException {
        RPFileName fn = (RPFileName)rootName;
        logger.debug("creating filesystem for " + fn);
        return new RPFileSystem(fn,rpmi,fileSystemOptions);
        
    }
    
   
   
  
    
    

}
