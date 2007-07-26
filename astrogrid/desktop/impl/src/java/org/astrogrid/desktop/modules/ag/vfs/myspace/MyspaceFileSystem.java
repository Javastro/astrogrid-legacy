/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URISyntaxException;
import java.util.Collection;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.registry.RegistryException;

/** vfs filesystem for myspace.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20076:01:45 PM
 */
public class MyspaceFileSystem extends AbstractFileSystem implements FileSystem {


	protected MyspaceFileSystem(MyspaceFileName rootName, MyspaceInternal msi, FileSystemOptions fileSystemOptions) {
		super(rootName, null, fileSystemOptions);
		this.msi = msi;
	}
	private final MyspaceInternal msi;
	// lazily initialized
	private FileManagerClient _client;
	
	public FileManagerClient client() throws FileSystemException {		
		try {
            return getClient();
	    } catch (CommunityException e) {
            throw new FileSystemException(e);
        } catch (RegistryException x) {
            throw new FileSystemException(x);
        } catch (URISyntaxException x) {
            throw new FileSystemException(x);
        }   
	}

	// add the capabiltiies of this file system.
	protected void addCapabilities(Collection caps) {
		caps.addAll(MyspaceProvider.CAPABILITIES);
	}

	protected FileObject createFile(FileName name) throws Exception {
		return new MyspaceFileObject((MyspaceFileName)name,this);
	}

	public double getLastModTimeAccuracy() {
		return 1000L; // gives us some leeway.
	}
	
	public FileSystemOptions getFileSystemOptions() {
		return super.getFileSystemOptions();
	}

    /**
     * @return the client
     * @throws URISyntaxException 
     * @throws RegistryException 
     * @throws CommunityException 
     */
    private FileManagerClient getClient() throws CommunityException, RegistryException, URISyntaxException {
        synchronized(this) {
            if (_client == null) {
                _client = msi.getClient();
            }
        }
        return _client;
    }

}
