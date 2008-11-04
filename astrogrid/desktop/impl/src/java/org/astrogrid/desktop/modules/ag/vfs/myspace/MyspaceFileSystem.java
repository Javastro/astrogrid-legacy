/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URISyntaxException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.CacheStrategy;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.registry.RegistryException;

/** VFS filesystem for myspace.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 20076:01:45 PM
 */
public class MyspaceFileSystem extends AbstractFileSystem implements FileSystem {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(MyspaceFileSystem.class);


	protected MyspaceFileSystem(final MyspaceFileName rootName, final MyspaceInternal msi, final FileSystemOptions fileSystemOptions) {
		super(rootName, null, fileSystemOptions);
		this.msi = msi;
	}
	private final MyspaceInternal msi;
	
	public FileManagerClient client() throws FileSystemException {		
		try {
            return msi.getClient();
	    } catch (final CommunityException e) {
            throw new FileSystemException(e);
        } catch (final RegistryException x) {
            throw new FileSystemException(x);
        } catch (final URISyntaxException x) {
            throw new FileSystemException(x);
        }   
	}

	
	protected void doCloseCommunicationLink() {
	    // sadly nothing we can do here - as fileobjects have already attached, and so don't need the client any longer.
	    // a proper close() would require keeping refereences to all fileobjects created for tis filesystem,
	    // at least we can remove the cached entries.
	}
	
	// add the capabiltiies of this file system.
	protected void addCapabilities(final Collection caps) {
		caps.addAll(MyspaceProvider.CAPABILITIES);
	}

	protected FileObject createFile(final FileName name) throws Exception {
	    if (logger.isDebugEnabled()) {
	        logger.debug("Creating file " + name);
	    }
		return new MyspaceFileObject((MyspaceFileName)name,this);
	}
	
	// method to build a fully initialized file object.
	// used from MyspaceFileObject
	public FileObject resolveFile(final MyspaceFileName name, final FileManagerNode node) throws FileSystemException {
	    // cribbed from AbstractFileSystem.resolveFile	    
	    FileObject file = getFileFromCache(name);	     
	    if (file == null)  {
	        try {
	            synchronized (this){
	                file = new MyspaceFileObject(node,name,this);
	            }
	        } catch (final Exception e){
	            throw new FileSystemException("vfs.provider/resolve-file.error", name, e);
	        }
	        file = decorateFileObject(file);
	        putFileToCache(file);
	    }
	    /**
	     * resync the file information if requested
	     */
	    if (getFileSystemManager().getCacheStrategy().equals(CacheStrategy.ON_RESOLVE))
	    {
	        file.refresh();
	    }
	    return file;	    
	}

	public double getLastModTimeAccuracy() {
		return 1000L; // gives us some leeway.
	}

	
	public String toString() {
	    return super.toString() + "/" + getRootName();
	}



}
