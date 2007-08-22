/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemConfigBuilder;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.provider.AbstractOriginatingFileProvider;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.ag.vfs.VfsFileProvider;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.registry.RegistryException;

/** A commons VFS plugin for access to Myspace.
 * 
 * mostly cribbed from the SftpProvider
 * 
 * the underlying connection to myspace is achived by a FileManagerClient - 
 * which is a pretty high level interface to Myspace. There's a lower-level underlying 
 * client which <i>might</i> be more efficient - but not too bothered as myspace 
 * will go away soonish anyhow, and this interface has been more tested maybe.
 * 
 * The delegate we use has it's own caching mechanism - to take advantage of the 
 * pre-fetching happening from myspace. I've subclassed this to stuff the entries in
 * the vfs cache.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 200712:06:46 PM
 */
public class MyspaceProvider extends AbstractOriginatingFileProvider implements
		VfsFileProvider, UserLoginListener {

	private static final Log logger = LogFactory.getLog(MyspaceProvider.class);
	
	private final static Capability[] caps = new Capability[] {
		Capability.CREATE
		,Capability.DELETE
		,Capability.RENAME
		,Capability.READ_CONTENT
		,Capability.WRITE_CONTENT
		,Capability.APPEND_CONTENT
		
		//?, Capability.LAST_MODIFIED 
		// don't understand difference between these two
		, Capability.GET_LAST_MODIFIED // this is hte one supported by the FTP implementation 
		
		,Capability.GET_TYPE // the file tpye can be determined (I assume this means file vs folder)
		,Capability.LIST_CHILDREN // children of files can be listed.
		//URI are supported.  Files without this capability use URI that do not
		//globally and uniquely identify the file.
		,Capability.URI
		//?? possible 
		//??,Capability.RANDOM_ACCESS_READ
		//??,Capability.RANDOM_ACCESS_WRITE
		//??,Capability.FS_ATTRIBUTES //File system attributes are supported.
		,Capability.ATTRIBUTES // file attributes are supported
		
	};
	public static final Collection CAPABILITIES = Collections.unmodifiableCollection(Arrays.asList(caps));
	
	public MyspaceProvider(BundlePreferences prefs, MyspaceInternal msi) {
		super();
		this.msi = msi;
		setFileNameParser(new MyspaceNameParser());
	}
	private final MyspaceInternal msi;

	public Collection getCapabilities() {
		return CAPABILITIES; 
	}
	
	FileSystemConfigBuilder builder = new MyspaceConfigBuilder();
	public FileSystemConfigBuilder getConfigBuilder() {
		return builder;
	}

	protected FileSystem doCreateFileSystem(FileName arg0,
			FileSystemOptions arg1) throws FileSystemException {
		MyspaceFileName fn = (MyspaceFileName)arg0;

		logger.debug("Creating filesystem for " + arg0);
			FileSystem sys = new MyspaceFileSystem(fn,msi,arg1);
			filesystems.add(sys);
			return sys;
	
	}
	// hang onto the the filesystems, so can be closed on user logout.
	private final List filesystems = new ArrayList();
	
	public void userLogin(UserLoginEvent arg0) {
		// do nothing.
	}

	public void userLogout(UserLoginEvent arg0) {
		// close all the open myspace filesystems.
		for (Iterator i = filesystems.iterator(); i.hasNext();) {
			MyspaceFileSystem f = (MyspaceFileSystem) i.next();
			try {
			    getContext().getFileSystemManager().getFilesCache().clear(f);
			   
			} catch (Throwable t) {
			  logger.warn("Exception when closing filesystem",t);  
			}
		}
		filesystems.clear();
		close();
	}
	
}
