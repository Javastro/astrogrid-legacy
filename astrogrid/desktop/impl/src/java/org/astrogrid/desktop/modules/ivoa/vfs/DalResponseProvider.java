/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa.vfs;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs.Capability;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileSystemOptions;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileProvider;
import org.apache.commons.vfs.provider.AbstractLayeredFileProvider;
import org.apache.commons.vfs.provider.LayeredFileName;
import org.astrogrid.desktop.modules.ag.vfs.VfsFileProvider;

/** A filesystem that mounts over a dal response document - be that 
 * SSAP, SIAP or STAP - makes the dal response appear as a folder, containing
 * result files (the resources pointed to by URL).
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 9, 20078:08:59 PM
 */
public class DalResponseProvider extends AbstractFileProvider implements
		VfsFileProvider {

    protected final static Collection capabilities = Collections.unmodifiableCollection(Arrays.asList(new Capability[]
                                                                                                                     {
                                                                                                                         Capability.GET_TYPE,
                                                                                                                         Capability.LIST_CHILDREN,
                                                                                                                         Capability.READ_CONTENT,
                                                                                                                         Capability.URI,
                                                                                                                         Capability.VIRTUAL,
                                                                                                                         Capability.JUNCTIONS
                                                                                                                     }));
 
   public FileObject createFileSystem(String scheme, FileObject file, FileSystemOptions properties) throws FileSystemException {
	   
	   FileSystemManager m = getContext().getFileSystemManager();
	   FileObject fo = m.createVirtualFileSystem("/tmp");
	   fo.getFileSystem().addJunction("fred",m.resolveFile("http://www.slashdot.org"));
	   fo.getFileSystem().addJunction("barney",m.resolveFile("http://www.google.com"));
	   return fo;
}
	

	public Collection getCapabilities() {
		return capabilities;
	}

	public FileObject findFile(FileObject baseFile, String uri, FileSystemOptions fileSystemOptions) throws FileSystemException {
		return createFileSystem("dal",baseFile,fileSystemOptions);
	}



}
