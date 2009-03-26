/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.Map;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileContentInfo;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.NameScope;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.apache.commons.vfs.util.RandomAccessMode;

import edu.berkeley.guir.prefuse.graph.TreeNode;

/** Delegate file object  used to model a result in astroscope.
 * allows metadata information to be provided
 * rather than detected.
 * lots of special case shortcuts to make it more performant.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 4, 20074:35:38 PM
 */
public class AstroscopeFileObject extends DelegateFileObject {
    public static final long UNKNOWN_SIZE = -2;
/**
 * construct a new astroscope file object
 * @param file actual object to wrap
 * @param sz number of bytes this file contains (must be correct, else excess will not get read )
 * @param date last modification date
 * @param mime mimetype of the data.
 * @throws FileSystemException
 */
    public AstroscopeFileObject(    final FileObject file, final long sz, final long date, final String mime) throws FileSystemException {
        super(file.getName(),(AbstractFileSystem)file.getFileSystem(), file);
        //NB:  type is also storedin thye filename - may need to override this too.
        content = new LazyFileContent();
        this.lastModifiedTime = date;
        this.contentSize = sz;
        this.mime = mime;
    }
    private final LazyFileContent content;
    
    /** lazy wrapper around file content */
    private class LazyFileContent implements FileContent, FileContentInfo {
        private FileContent delegate;
        private void belatedlyAttach() throws FileSystemException {
            synchronized(getFileSystem()) {
                if (delegate == null) {
                    delegate = doCreateFileContent();
                }
            }
        }
// methods that we have pre-canned responses to.
        public FileObject getFile() {
            return AstroscopeFileObject.this;
        }        

        public long getLastModifiedTime() throws FileSystemException {
                return lastModifiedTime;
          
        }
        
        public long getSize() throws FileSystemException {
                return contentSize;
         
        }        
        public String getContentType() {
            return mime;
        }
        public FileContentInfo getContentInfo() throws FileSystemException {
                return this;                     
        }        
        
// methods that delegate to real file content        
        public void close() throws FileSystemException {
            belatedlyAttach();
            delegate.close();
        }

        public Object getAttribute(final String attrName) throws FileSystemException {
            belatedlyAttach();
            return delegate.getAttribute(attrName);
        }

        public String[] getAttributeNames() throws FileSystemException {
            belatedlyAttach();
            return delegate.getAttributeNames();
        }

        public Map getAttributes() throws FileSystemException {
            belatedlyAttach();
            return delegate.getAttributes();
        }

        public Certificate[] getCertificates() throws FileSystemException {
            belatedlyAttach();
            return delegate.getCertificates();
        }

        public InputStream getInputStream() throws FileSystemException {
            belatedlyAttach();
            return delegate.getInputStream();
        }

        public OutputStream getOutputStream() throws FileSystemException {
            belatedlyAttach();
            return delegate.getOutputStream();
        }

        public OutputStream getOutputStream(final boolean append)
                throws FileSystemException {
            belatedlyAttach();
            return delegate.getOutputStream(append);
        }

        public RandomAccessContent getRandomAccessContent(final RandomAccessMode mode)
                throws FileSystemException {
            belatedlyAttach();
            return delegate.getRandomAccessContent(mode);
        }

        public boolean hasAttribute(final String attrName) throws FileSystemException {
            belatedlyAttach();
            return delegate.hasAttribute(attrName);
        }

        public boolean isOpen() {
            // can be a bit clever with this one
            if (delegate == null) {
                return false;
            } else {
                return delegate.isOpen();
            }
        }

        public void removeAttribute(final String attrName) throws FileSystemException {
            belatedlyAttach();
            delegate.removeAttribute(attrName);
        }

        public void setAttribute(final String attrName, final Object value)
                throws FileSystemException {
            belatedlyAttach();
            delegate.setAttribute(attrName,value);
        }

        public void setLastModifiedTime(final long modTime)
                throws FileSystemException {
            belatedlyAttach();
            delegate.setLastModifiedTime(modTime);
            
        }
        public String getContentEncoding() {
            try { // awkward - can't throw at this point
            belatedlyAttach();
            return delegate.getContentInfo().getContentEncoding();
            } catch (final FileSystemException e) {
                throw new RuntimeException(e);
            }
        }

    } // end of file content
    
    // fixed methods.
    // public
    @Override
    public FileType getType() throws FileSystemException {
        return doGetType();
    }
    
    @Override
    public FileObject resolveFile(final String name, final NameScope scope)
            throws FileSystemException {
        if (name.equals(".")) {
            return this;
        } else {
            return super.resolveFile(name, scope);
        }
    }

    @Override
    public boolean canRenameTo(final FileObject newfile) {
        return false;
    }
    @Override
    public FileContent getContent() throws FileSystemException {
        return content;
    }

    // protected
    @Override
    protected FileType doGetType() throws FileSystemException {
        return FileType.FILE;
    }
    @Override
    protected boolean doIsReadable() throws FileSystemException {
        return true;
    }
    @Override
    protected boolean doIsWriteable() throws FileSystemException {
        return false;
    }
    @Override
    protected boolean doIsHidden() throws FileSystemException {
        return false;
    }

//  preset values 

    private  final long contentSize;



    private final long lastModifiedTime;


    private final String mime;


    /** the node inthe result tree that this result is associated with */
    private TreeNode node;
    /**
     * @param node
     */
    public final void setNode(final TreeNode node) {
        this.node = node;
    }
    /** access the result-tree-node that this result is associated with */
    public final TreeNode getNode() {
        return this.node;
    }
    
    // helper methods for working with delegte file objects.
    /** determine if fo is a delegate, but NOT an astroscopeFileObject */
    public static final boolean isOnlyDelegateFileObject(final FileObject fo) {
        return (fo instanceof DelegateFileObject && ! (fo instanceof AstroscopeFileObject));
    }
   
    /** determin if fo is a delegate of any kind( including an astroscope file object) */
    public static final boolean isDelegateOrAstroscopeFileObject(final FileObject fo) {
        return fo instanceof DelegateFileObject;
    }
    
    /** return the Astroscope fileObject that this delegates to
     * 
     * @param fo a file object
     * @return the AstroscopeFIleObject that this fiileObject dlegates to, or null if there is no astroscopefileobject in the delegattion chain..
     */
    public static final AstroscopeFileObject findAstroscopeFileObject(FileObject fo) {
        fo = findAstroscopeOrInnermostFileObject(fo);
        return fo instanceof AstroscopeFileObject ? (AstroscopeFileObject)fo : null;
    }
  
    /** return the innermost file object that this delegates to
     * @return the innermost file - never null */
    public static final FileObject findInnermostFileObject(FileObject fo) {
        while (isDelegateOrAstroscopeFileObject(fo)) {
            fo = ((DelegateFileObject)fo).getDelegateFile();
        }
        return fo;
    }

    
    /** return AstroscopeFileObject that this file objct delegates to, or if none present, the innermost file object that this delegates to
     * @return the file object - never null */
    public static final FileObject findAstroscopeOrInnermostFileObject(FileObject fo) {
        while (isOnlyDelegateFileObject(fo)) {
            fo = ((DelegateFileObject)fo).getDelegateFile();
        }
        return fo;
    }
    
}
