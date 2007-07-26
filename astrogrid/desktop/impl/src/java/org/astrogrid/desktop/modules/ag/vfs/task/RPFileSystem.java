/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.task;

import java.util.Collection;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.RPFileName;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.ResultFileName;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.RootFileName;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.TaskFileName;

/** vfs filtsystem for remote tasks.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 20076:42:26 PM
 */
public class RPFileSystem extends AbstractFileSystem implements
        FileSystem {

    private final RemoteProcessManagerInternal rpmi;

    /**
     * @param rootName
     * @param parentLayer
     * @param fileSystemOptions
     */
    protected RPFileSystem(FileName rootName,
            RemoteProcessManagerInternal rpmi,FileSystemOptions fileSystemOptions) {
        super(rootName, null, fileSystemOptions);
        this.rpmi = rpmi;
        
    }

    protected void addCapabilities(Collection caps) {
        caps.addAll(RPProvider.caps);
    }

    protected FileObject createFile(FileName name) throws Exception {
      if (name instanceof RPFileName) {
          return ((RPFileName)name).createFileObject(this);
      } else {
          throw new Exception("Unknown kind of process file");
      }
    
    }

    /**
     * @return the rpmi
     */
    public final RemoteProcessManagerInternal getRpmi() {
        return this.rpmi;
    }
    
    //@todo implement add and remobe junction fuinctions.

}
