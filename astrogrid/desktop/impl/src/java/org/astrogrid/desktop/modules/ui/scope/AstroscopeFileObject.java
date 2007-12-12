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
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.NameScope;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.impl.DefaultFileContentInfo;
import org.apache.commons.vfs.provider.AbstractFileName;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.DefaultFileContent;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.apache.commons.vfs.util.RandomAccessMode;

/** Delegate file object that allows information to be provided
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
    public AstroscopeFileObject(    FileObject file, long sz, long date, String mime) throws FileSystemException {
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

        public Object getAttribute(String attrName) throws FileSystemException {
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

        public OutputStream getOutputStream(boolean append)
                throws FileSystemException {
            belatedlyAttach();
            return delegate.getOutputStream(append);
        }

        public RandomAccessContent getRandomAccessContent(RandomAccessMode mode)
                throws FileSystemException {
            belatedlyAttach();
            return delegate.getRandomAccessContent(mode);
        }

        public boolean hasAttribute(String attrName) throws FileSystemException {
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

        public void removeAttribute(String attrName) throws FileSystemException {
            belatedlyAttach();
            delegate.removeAttribute(attrName);
        }

        public void setAttribute(String attrName, Object value)
                throws FileSystemException {
            belatedlyAttach();
            delegate.setAttribute(attrName,value);
        }

        public void setLastModifiedTime(long modTime)
                throws FileSystemException {
            belatedlyAttach();
            delegate.setLastModifiedTime(modTime);
            
        }
        public String getContentEncoding() {
            try { // awkward - can't throw at this point
            belatedlyAttach();
            return delegate.getContentInfo().getContentEncoding();
            } catch (FileSystemException e) {
                throw new RuntimeException(e);
            }
        }

    } // end of file content
    
    // fixed methods.
    // public
    public FileType getType() throws FileSystemException {
        return doGetType();
    }
    
    public FileObject resolveFile(String name, NameScope scope)
            throws FileSystemException {
        if (name.equals(".")) {
            return this;
        } else {
            return super.resolveFile(name, scope);
        }
    }

    public boolean canRenameTo(FileObject newfile) {
        return false;
    }
    public FileContent getContent() throws FileSystemException {
        return content;
    }

    // protected
    protected FileType doGetType() throws FileSystemException {
        return FileType.FILE;
    }
    protected boolean doIsReadable() throws FileSystemException {
        return true;
    }
    protected boolean doIsWriteable() throws FileSystemException {
        return false;
    }
    protected boolean doIsHidden() throws FileSystemException {
        return false;
    }

//  preset values 

    private  final long contentSize;



    private final long lastModifiedTime;


    private final String mime;
}
