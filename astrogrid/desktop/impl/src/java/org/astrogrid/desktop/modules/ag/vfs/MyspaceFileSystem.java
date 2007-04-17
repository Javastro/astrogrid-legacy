/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs;

import java.util.Collection;

import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.filemanager.client.FileManagerClient;

/** vfs filesystem for myspace.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20076:01:45 PM
 */
public class MyspaceFileSystem extends AbstractFileSystem implements FileSystem {

	/**
	 * @param rootName
	 * @param parentLayer
	 * @param fileSystemOptions
	 */
	protected MyspaceFileSystem(MyspaceFileName rootName, FileManagerClient client, FileSystemOptions fileSystemOptions) {
		super(rootName, null, fileSystemOptions);
		this.client = client;
	}
	private final FileManagerClient client;
	
	public FileManagerClient client() {		
		return client;
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

}
