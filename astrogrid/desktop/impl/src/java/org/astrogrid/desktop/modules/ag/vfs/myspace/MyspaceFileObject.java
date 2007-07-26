/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.myspace;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.NodeIterator;
import org.astrogrid.store.Ivorn;

/** fileobject for myspace.
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

	// can also implement doAttach and doDetach to perform lazy initializaiton.
	/**
	 * @param name
	 * @param fs
	 */
	protected MyspaceFileObject(MyspaceFileName name, MyspaceFileSystem fs) {
		super(name, fs);
	}

	protected MyspaceFileObject(FileManagerNode node,MyspaceFileName name,MyspaceFileSystem fs) {
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
			Ivorn ivorn = getMsName().getIvorn();
			if (client().exists(ivorn) != null) {
				node = client().node(ivorn);
			}
		}
	}
	protected void doDetach() throws Exception {
		node = null;
		super.doDetach();
	}
//	/	 access environment objects conveniently.
	protected MyspaceFileName getMsName() {
		return (MyspaceFileName)getName();
	}

	protected MyspaceFileSystem getMsFileSystem() {
		return (MyspaceFileSystem)getFileSystem();
	}

	protected FileManagerClient client() throws FileSystemException {
		return getMsFileSystem().client();
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
		logger.debug("getType " + getName());
		FileType t =  mkType(node);
		logger.debug(t);
		return t;
	}

	private FileType mkType(FileManagerNode n) {
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
		String[] result = new String[node.getChildCount()];
		NodeIterator i = node.iterator();
		for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
			FileManagerNode child = i.nextNode();
			result[ix] = child.getName();
		}
		if (logger.isDebugEnabled()) {
			logger.debug(getName() + " : " + result.length + "children");
			logger.debug(ArrayUtils.toString(result));
		}
		return result;		
	}
	protected FileObject[] doListChildrenResolved() throws Exception {
		logger.debug("listChildrenResolved " + getName());
		FileObject[] result = new FileObject[node.getChildCount()];
		NodeIterator i = node.iterator();
		for (int ix = 0; ix < result.length &&  i.hasNext(); ix++) {
			FileManagerNode child = i.nextNode();
			String parentPath = getMsName().getPath();
			String path = parentPath 
			+( parentPath.endsWith("/") ? "" : "/")
			+ child.getName();
			MyspaceFileName fn = (MyspaceFileName)getMsName().createName(path,mkType(child));
			result[ix] = new MyspaceFileObject(child,fn,getMsFileSystem());
		}
		return result;		
	}
	protected void doCreateFolder() throws Exception {
		logger.debug("createFolder " + getName());
		client().createFolder(getMsName().getIvorn());
	}

	protected void doDelete() throws Exception {
		logger.debug("delete " + getName());
		node.delete();
	}


	protected Map doGetAttributes() throws Exception {
		logger.debug("getAttributes " + getName());
		return node.getMetadata().getAttributes();

	}

	protected long doGetLastModifiedTime() throws Exception {
		logger.debug("getLastModifiedTime " + getName());
		return node.getMetadata().getModifyDate().getTimeInMillis();
	}

	protected OutputStream doGetOutputStream(boolean bAppend) throws Exception {
		logger.debug("getOutputStream " + getName());
		if (bAppend) {
			return node.appendContent();
		} else {
			return node.writeContent();
		}
	}

	protected void doRename(FileObject newfile) throws Exception {
		logger.debug("rename " + getName());
		// more of a move than a rename, it seems.
		//node.move(newfile.getName().getURI());
		//@FIXME
	}

	protected void doSetAttribute(String atttrName, Object value) throws Exception {
		logger.debug("setAttribiute " + getName());
		//@FIXME
	}

	protected void onChange() throws Exception {
		logger.debug("change " + getName());
		node.refresh(); // just to be safe.
	}

	protected void onChildrenChanged(FileName child, FileType newType) throws Exception {
		logger.debug("childChanged " + getName());
		node.refresh(); // ? fair guess.
	}



}
