/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.datatransfer.Transferable;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileContentInfo;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.NameScope;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.impl.DecoratedFileObject;
import org.apache.commons.vfs.operations.FileOperations;
import org.apache.commons.vfs.util.RandomAccessMode;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectListTransferable;
import org.astrogrid.desktop.modules.ui.dnd.FileObjectTransferable;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**factory that builds a  transferable object that represents a set of nodes selected in astroscope.
 * @author Noel.Winstanley@manchester.ac.uk
 * 
 * @future - would it be more efficient to have a single transferable object,
 * and constantly repopulate it? (or even have the transferable object listen
 * to the FocusSet, and repopulate itself (so it becomes a view on the selection).
 * @since May 10, 20074:17:45 PM
 */
public class ScopeTransferableFactory{

	private static final Log logger = LogFactory
			.getLog(ScopeTransferableFactory.class);

	private final FileSystemManager vfs;
	
	
	/** additional interface that clever nodes should implement - 
	 * as they know better than us how to produce a meaningful 
	 * fileObject.
	 * also provides a convinient place to cache these for next time.
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since May 10, 20076:23:51 PM
	 */
	public interface HasFileObject {
		/** access the file object associated with this node.
		 * 
		 * @param factory factory to use to create the object
		 * @return a file object (may be cached from previous)
		 */
		FileObject fileObject(ScopeTransferableFactory factory);
	}
	
	
	public ScopeTransferableFactory(final FileSystemManager vfs) {
		super();
		this.vfs = vfs;
	}


	/** factory method - builds fileObjects from the selection, then
	 * creates a FileObjectLisTransferable.
	 */
	public Transferable create(FocusSet selection) {
		List l = new ArrayList();
		// scan through the selection, looking for nodes who 
		// offer a FileObject.
		for (Iterator i = selection.iterator(); i.hasNext();) {
			TreeNode tn = (TreeNode) i.next();
			if (tn instanceof HasFileObject) {
				FileObject fo = ((HasFileObject)tn).fileObject(this);
				if (fo != null) {
					l.add(fo);
				}
			}
		}
		switch (l.size()) {
		case 0:
			return null; 
		case 1:
			return new FileObjectTransferable((FileObject)l.get(0));
		default:
			return new FileObjectListTransferable(l);
		}
	}

	public FileSystemManager getVFS() {
		return vfs;
	}

	/** delegate / wrapper class that delays creation of real file object
	 *  as long as possible 
	 *  
	 *  @todo re-implemnt based on DelegateFileObject
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since May 10, 20077:23:57 PM
	 */
	public class AstroscopeFileObject implements FileObject {

		public boolean canRenameTo(FileObject newfile) {
			return false;
		}

		public void close() throws FileSystemException {
			this.getFo().close();
		}

		public void copyFrom(FileObject srcFile, FileSelector selector) throws FileSystemException {
		    throw new FileSystemException("not supported");
		}

		public void createFile() throws FileSystemException {
            throw new FileSystemException("not supported");
		}

		public void createFolder() throws FileSystemException {
            throw new FileSystemException("not supported");
		}

		public boolean delete() throws FileSystemException {
            throw new FileSystemException("not supported");
		}

		public int delete(FileSelector selector) throws FileSystemException {
            throw new FileSystemException("not supported");
		}

		public boolean exists() throws FileSystemException {
		    return true;
		}

		public void findFiles(FileSelector selector, boolean depthwise, List selected) throws FileSystemException {
			this.getFo().findFiles(selector, depthwise, selected);
		}

		public FileObject[] findFiles(FileSelector selector) throws FileSystemException {
			return this.getFo().findFiles(selector);
		}

		public FileObject getChild(String name) throws FileSystemException {
			return this.getFo().getChild(name);
		}

		public FileObject[] getChildren() throws FileSystemException {
			return this.getFo().getChildren();
		}

		public FileOperations getFileOperations() throws FileSystemException {
			return this.getFo().getFileOperations();
		}

		public FileSystem getFileSystem() {
			return this.getFo().getFileSystem();
		}

		public FileName getName() {
			return this.getFo().getName();
		}

		public FileObject getParent() throws FileSystemException {
			return this.getFo().getParent();
		}

		public URL getURL() throws FileSystemException {
			try {
				return new URL(url);
			} catch (MalformedURLException x) {
				throw new FileSystemException(x);
			}
		}

		public boolean isAttached() {
			return this.getFo().isAttached();
		}

		public boolean isContentOpen() {
			return this.getFo().isContentOpen();
		}

		public boolean isHidden() throws FileSystemException {
		    return false;
		}

		public boolean isReadable() throws FileSystemException {
		    return true;
		}

		public boolean isWriteable() throws FileSystemException {
		    return false;
		}

		public void moveTo(FileObject destFile) throws FileSystemException {
            throw new FileSystemException("not supported");
		}

		public void refresh() throws FileSystemException {
			this.getFo().refresh();
		}

		public FileObject resolveFile(String name, NameScope scope) throws FileSystemException {
			return this.getFo().resolveFile(name, scope);
		}

		public FileObject resolveFile(String path) throws FileSystemException {
			return this.getFo().resolveFile(path);
		}

		/**
		 * 
		 * @param mime mime type
		 * @param tn associated tree node
		 * @param url url of the real file.
		 */
		public AstroscopeFileObject(String mime,TreeNode tn,String url) {
			this.url = url;
			this.tn = tn;
			this.mime = mime;
		}
		private final String url;
		private final TreeNode tn;
		private final String mime;
		
		private FileObject _fo;
		protected FileObject getFo() {
			if (_fo == null) {
				try {
					_fo = vfs.resolveFile(url);
				} catch (FileSystemException x) {
					logger.warn("Failed to get inner file object ",x);
					_fo = null; // dunno what else to do here.
				}
			}
			return _fo;
		}
		
		public FileType getType() throws FileSystemException {
			return FileType.FILE;
		}
		// would be nice to provide additional filename info too...
		
		
		public FileContent getContent() throws FileSystemException {
			return new FileContent() {
				private FileContent _inner;
				private FileContent getInner() throws FileSystemException {
					if (_inner == null) {
						_inner = getFo().getContent();
					}
					return _inner;
				}
				public void close() throws FileSystemException {
					getInner().close();
				}

				public Object getAttribute(String attrName) throws FileSystemException {
					return tn.getAttribute(attrName);
				}

				public String[] getAttributeNames() throws FileSystemException {
					// not sure this will work..
					return (String[])tn.getAttributes().keySet().toArray(new String[]{});
				}

				public Map getAttributes() throws FileSystemException {
					return tn.getAttributes();
				}

				public Certificate[] getCertificates() throws FileSystemException {
					return getInner().getCertificates();
				}

				public FileContentInfo getContentInfo() throws FileSystemException {
					return new FileContentInfo() {

						public String getContentEncoding() {
							try {
								return getInner().getContentInfo().getContentEncoding();
							} catch (FileSystemException x) {
								return null;
							}
						}

						public String getContentType() {
							return mime;
						}
					};
				}

				public FileObject getFile() {
					return AstroscopeFileObject.this;
				}

				public InputStream getInputStream() throws FileSystemException {
					return getInner().getInputStream();
				}

				public long getLastModifiedTime() throws FileSystemException {
					return getInner().getLastModifiedTime();
				}

				public OutputStream getOutputStream() throws FileSystemException {
					return getInner().getOutputStream();
				}

				public OutputStream getOutputStream(boolean bAppend) throws FileSystemException {
					return getInner().getOutputStream(bAppend);
				}

				public RandomAccessContent getRandomAccessContent(RandomAccessMode mode) throws FileSystemException {
					return getInner().getRandomAccessContent(mode);
				}

				public long getSize() throws FileSystemException {
					return getInner().getSize(); // @future can I provide this from astroscope data too?
				}

				public boolean isOpen() {
					try {
						return getInner().isOpen();
					} catch (FileSystemException x) {
						logger.error("FileSystemException",x);
						return false;
					}
				}

				public void setAttribute(String attrName, Object value) throws FileSystemException {
					// unimplemented
				}

				public void setLastModifiedTime(long modTime) throws FileSystemException {
					// unimplemented
				}
                public boolean hasAttribute(String attrName)
                        throws FileSystemException {
                    return false;
                }
                public void removeAttribute(String attrName)
                        throws FileSystemException {
                }
			};

			// from this accessed ContentInfo.contentType
		}
	}


}
