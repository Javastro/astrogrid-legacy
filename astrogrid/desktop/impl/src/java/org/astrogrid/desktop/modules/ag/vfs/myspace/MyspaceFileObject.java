/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.astrogrid.desktop.modules.ui.scope.AstroscopeFileObject;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.filemanager.common.TransferInfo;
import org.astrogrid.store.Ivorn;

/** VFS FileObject for Myspace.
 * implemented roughly using the  FileManagerClient. Would be more efficient
 * using a lower-level api, but will do for now.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 3, 200711:45:53 PM
 */
public class MyspaceFileObject extends AbstractFileObject implements FileObject {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
	.getLog(MyspaceFileObject.class);
    private final MyspaceFileSystem msFilesystem;

    /** special-case optimization where the file object to copy from is http - can pass an call to myspace to do the copy directly, rather than the client reading the data. */
    public void copyFrom(FileObject file, final FileSelector selector)
            throws FileSystemException {
        file = AstroscopeFileObject.findInnermostFileObject(file);
        if (file.getURL().getProtocol().equals("http")
                && ( selector == Selectors.SELECT_ALL  || selector == Selectors.SELECT_FILES || selector == Selectors.SELECT_SELF || selector == Selectors.SELECT_SELF_AND_CHILDREN)) {
            // unlikely any other kind of selector swould be intentionally used, but there miught be some pathological cases.
            // dunno if it\s possible to copy a subtree from http - as you can't get an 'ls'
            try {
                logger.debug("Attempting to use optimized copy URL to myspace");
                if (node == null) { // this file doesn't exist yet. need to create it
                    node = msFilesystem.client().createFile(getMsName().getIvorn());
                }
                node.copyURLToContent(file.getURL());
            } catch (final Exception e) {
                logger.warn("Failed to use optimized copy to myspace, falling back to direct copy", e);
               super.copyFrom(file,selector);
            }
        } else {
            super.copyFrom(file, selector);
        }
    }
    public FileObject resolveFile(final String path) throws FileSystemException {
        return super.resolveFile(path);
    }
    
	// can also implement doAttach and doDetach to perform lazy initializaiton.
	/**
	 * @param name
	 * @param fs
	 */
	protected MyspaceFileObject(final MyspaceFileName name, final MyspaceFileSystem fs) {
		super(name, fs);
        this.msFilesystem = fs;
	}

	protected MyspaceFileObject(final FileManagerNode node,final MyspaceFileName name,final MyspaceFileSystem fs) {
		this(name,fs);
		this.node = node;
	}

//	called before each 'do*' method - can be used to lazily initialize.
	// this impl uses attach to fetch the Myspace file object.
	private FileManagerNode node;
	protected void doAttach() throws Exception {
		logger.debug("doAttach");
		if (node == null) { //else  already attached. ignore
			logger.debug("Attaching to " + getName());
			final Ivorn ivorn = getMsName().getIvorn();
			final FileManagerClient client = msFilesystem.client();
            if (client.exists(ivorn) != null) {
				node = client.node(ivorn);
			} 
		}
	}
	protected void doDetach() throws Exception {
	    logger.debug("Detatching " + getName());
		node = null;
		super.doDetach();
	}
//	/	 access environment objects conveniently.
	protected MyspaceFileName getMsName() {
		return (MyspaceFileName)getName();
	}

	//	file operations.
	protected long doGetContentSize() throws Exception {
		logger.debug("getContentSize " + getName());
		return node.getMetadata().getSize().longValue();
	}

	protected InputStream doGetInputStream() throws Exception {
		logger.debug("getInputStream " + getName());
		return node.readContent();
	}

	protected FileType doGetType() throws Exception {
		final FileType t =  mkType(node);
		logger.debug("getType " + getName() + " : " + t);
		return t;
	}

	private FileType mkType(final FileManagerNode n) {
		if (n == null) {// attach() will have created a node, if this file exists already.
			return FileType.IMAGINARY;
		} else if (n.isFile()) {
			return FileType.FILE;
		} else  {
			return FileType.FOLDER;
		}
	}

	protected String[] doListChildren() throws Exception {
		logger.debug("listChildren " + getName());
		final String[] result = new String[node.getChildCount()];
		final NodeIterator i = node.iterator();
		for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
			final FileManagerNode child = i.nextNode();
			result[ix] = child.getName();
		}
		if (logger.isDebugEnabled()) {
			logger.debug(getName() + " : " + result.length + "children");
			logger.debug(ArrayUtils.toString(result));
		}
		return result;		
	}
	// if this is implemented, it's called in preference to dListChildren
	// as it's more efficient
	protected FileObject[] doListChildrenResolved() throws Exception {
		logger.debug("listChildrenResolved " + getName());
		doAttach();
		final FileObject[] result = new FileObject[node.getChildCount()];
		final NodeIterator i = node.iterator();
		final MyspaceFileName msName = getMsName();
        final String parentPath = msName.getPath();
		final String prefix = parentPath  +( parentPath.endsWith("/") ? "" : "/");
		for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
			final FileManagerNode child = i.nextNode();
			final MyspaceFileName fn = (MyspaceFileName)msName.createName(prefix + child.getName(),mkType(child));
			result[ix] = msFilesystem.resolveFile(fn,child);
		}
		return result;		
	}
	protected void doCreateFolder() throws Exception {
		logger.debug("createFolder " + getName());
		msFilesystem.client().createFolder(getMsName().getIvorn());
	}

	protected void doDelete() throws Exception {
		logger.debug("delete " + getName());
		node.delete();
		node = null;
	}

	// modified to get the readContentURL too - as there's no other way to 
	// get to this in the VFS api.
	protected Map doGetAttributes() throws Exception {
		logger.debug("getAttributes " + getName());
		final TransferInfo info = node.getNodeDelegate().readContent(node.getMetadata().getNodeIvorn());
		final Map attr =  node.getMetadata().getAttributes();
		final HashMap hm = new HashMap(attr);
		hm.put("ContentURL",info.getUri().toString());
		hm.put("ContentMethod",info.getMethod());
		return hm;
		

	}

	protected long doGetLastModifiedTime() throws Exception {
		logger.debug("getLastModifiedTime " + getName());
		return node.getMetadata().getModifyDate().getTimeInMillis();
	}

	protected OutputStream doGetOutputStream(final boolean bAppend) throws Exception {
	    logger.debug("getOutputStream " + getName());
	    if (node == null) { // file doesn't yet exist - else we'd be attached by now.
	        //@todo should I be doing this file creation elsewhere - doAttach for examp[le?
	        node = msFilesystem.client().createFile(getMsName().getIvorn());
	    }
		if (bAppend) {
			return node.appendContent();
		} else {
			return node.writeContent();
		}
	}

	protected void doRename(final FileObject newfile) throws Exception {
		logger.debug("rename " + getName() + " to " + newfile);
		final MyspaceFileObject p = (MyspaceFileObject)newfile.getParent();
		node.move(newfile.getName().getBaseName(),p.node,null);
	}


	protected void onChange() throws Exception {
	    if (node != null) {
	        node.refresh();
	    }
	}
	

	protected void onChildrenChanged(final FileName child, final FileType newType) throws Exception {
		logger.debug("childChanged " + getName());
		if (node != null) {
		    node.refresh(); // ? fair guess.
		}
	}

	//overridden to return the accessURL
	public URL getURL() throws FileSystemException {
	    try {
	    if (!isAttached()) {
	        doAttach();
	    }
            final URL content =  node.contentURL();
            if (content == null) {
                throw new FileSystemException("node has no content");
            }
            return content;
        } catch (final Exception x) {
            throw new FileSystemException(x);
        } 
	}
	
}
